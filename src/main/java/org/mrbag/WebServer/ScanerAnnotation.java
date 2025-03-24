package org.bag.WebServer;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

import org.bag.WebServer.Annotation.EndPoint;
import org.bag.WebServer.Interfaces.IEndPoint;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

public class ScanerAnnotation {

	final static String pathScaner = "org.bag";
	
	public Set<Class<?>> scanAnnotation(Class<? extends Annotation> annotation){
		Reflections r = new Reflections(
				new ConfigurationBuilder().forPackage(pathScaner).setScanners(Scanners.values())
		); 
		return r.getTypesAnnotatedWith(annotation);
	}
	
	public Set<IEndPoint> initEnds(Map<String, String> param, Set<Class<?>> clazz) {
		
		return null; 
	}
	
	public static void main(String[] args) {
		ScanerAnnotation scan = new ScanerAnnotation();
		scan.scanAnnotation(EndPoint.class).forEach(e -> System.out.println(e.getSimpleName()));
	}
}
