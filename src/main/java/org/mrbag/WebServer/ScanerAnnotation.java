package org.mrbag.WebServer;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import org.mrbag.WebServer.Annotation.EndPoint;
import org.mrbag.WebServer.Annotation.WSEndPoint;
import org.mrbag.WebServer.Interfaces.IEndPoint;
import org.mrbag.WebServer.Response.IResponse;
import org.mrbag.WebServer.WebSocket.AbstractWSEndPoint;
import org.mrbag.WebServer.WebSocket.IWebSocketMethod;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ConfigurationBuilder;

class ScanerAnnotation {

	final static String pathScaner = "org.bag";
	
	final static Logger log = Logger.getLogger(ScanerAnnotation.class);
	
	public static Set<Class<?>> scanAnnotation(Class<? extends Annotation> annotation){
		Reflections r = new Reflections(
				new ConfigurationBuilder().forPackage(pathScaner).setScanners(Scanners.values())
		); 
		return r.getTypesAnnotatedWith(annotation);
	}
	
	public static Set<IEndPoint> initEnds(Map<String, String> param, Set<Class<?>> clazz) {
		
		return clazz.stream()
			.filter(cl ->  Arrays.asList(cl.getInterfaces()).contains(IEndPoint.class))
			.map(cl -> {try {
				return ((IEndPoint) cl.getDeclaredConstructor().newInstance(null)).mount(param);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				log.warn(String.format("[%s] %s", cl.getName(), e));
				return null;
			}})
			.filter(ins -> ins != null)
			.collect(Collectors.toSet());
	}
	
	public static Set<AbstractWSEndPoint> initWSEnds(Map<String, String> param, Set<Class<?>> clazz) {
		return clazz.stream() //.forEach(s -> System.out.println(s.getSuperclass()));
			.filter(cl -> cl.getSuperclass().equals(AbstractWSEndPoint.class))
			.map(cl -> {
				try {
					return ((AbstractWSEndPoint) cl.getDeclaredConstructor().newInstance(null));
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e) {
					log.warn(String.format("[%s] %s", cl.getName(), e));
					return null;
				}
			}).filter(i -> i != null).collect(Collectors.toSet());
//		return null;
	}
	
	public static String[] getPathsEnd(Class<?> end) {
		EndPoint e = end.getAnnotation(EndPoint.class);
		WSEndPoint wse = end.getAnnotation(WSEndPoint.class);
		
		if (e != null)
			return e.path();
		if (wse != null)
			return wse.path();
		
		return null;
	}
	
	public static void main(String[] args) {
//		Set<Class<?>> clazzs = ScanerAnnotation.scanAnnotation(WSEndPoint.class);
		Reflections r = new Reflections(
//				new ConfigurationBuilder().forPackage(pathScaner).setScanners(Scanners.values())
				pathScaner
		); 
		
//		r.get(null)
		
		initWSEnds(null, scanAnnotation(WSEndPoint.class)).forEach(s -> System.out.println(s));
//		scan.initWSEnds(null, clazzs).forEach(i -> System.out.println(i));
	}
}
