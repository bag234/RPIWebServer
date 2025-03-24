package org.mrbag.WebServer;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.apache.log4j.Logger;
import org.mrbag.WebServer.Annotation.EndPoint;
import org.mrbag.WebServer.Annotation.WSEndPoint;
import org.mrbag.WebServer.Annotation.WebServerConfigFile;
import org.mrbag.WebServer.Interfaces.IEndPoint;
import org.mrbag.WebServer.Response.HTTPResponse;
import org.mrbag.WebServer.Response.Response.WarpedResponse;
import org.mrbag.WebServer.Storage.FileStorage;
import org.mrbag.WebServer.WebSocket.AbstractWSEndPoint;

public final class ServerFactory {

	final static Logger log = Logger.getLogger(ServerFactory.class);
	
	private static void parserConfig(URL path, Map<String, String> pars) throws FileNotFoundException, URISyntaxException {
		@SuppressWarnings("resource")
		Scanner bin = new Scanner( new File(path.toURI()));
		String line;
		while(bin.hasNext()) {
			line = bin.nextLine().trim();
			if (line.startsWith("#"))
				continue;
			if (line.contains("=")) {
				pars.put(
						line.substring(0, line.indexOf('=')), 
						line.substring(line.indexOf('=') + 1)
						);
			}
		}
	}
	
	static private Map<String, String> loadConfig(Class<?> main_class){
		Map<String, String> pars = new HashMap<String, String>();
		
		URL path = ServerFactory.class.getResource(
				((WebServerConfigFile) main_class.getAnnotation(WebServerConfigFile.class)).value()
				);
		try {
			parserConfig(path, pars);
		} catch (FileNotFoundException e) {
			log.warn("Create file: serv.conf in resouse or configure annation!");
		} catch (URISyntaxException e) {
			log.fatal("Unnable configure anatation!: " + e);
			return null;
		}
		
		return pars; 
	}
	
	static private void configWebServerEndPoint(WebServer serv, Map<String, String> pars) {
		Set<Class<?>> clazzs = ScanerAnnotation.scanAnnotation(EndPoint.class);
		Set<IEndPoint> ends = ScanerAnnotation.initEnds(pars, clazzs);
		clazzs = ScanerAnnotation.scanAnnotation(WSEndPoint.class);
		Set<AbstractWSEndPoint> wsEnds = ScanerAnnotation.initWSEnds(pars, clazzs);
		
		String[] paths = null;
		WarpedResponse wr = null;
		
		log.debug(String.format("Found IEndPoint implement`s: %d", ends.size()));
		for(IEndPoint e: ends) {
			paths = ScanerAnnotation.getPathsEnd(e.getClass());
			if (paths != null) {
				wr = new WarpedResponse(e);
				for(String path: paths) {
					log.debug(String.format("EndPoint mount on %s and class %s", path, e));
					serv.addPath(path, wr);
				}
			}
		}
		
		log.debug(String.format("Found WSEndPoint implement`s: %d", wsEnds.size()));
		for(AbstractWSEndPoint e: wsEnds) {
			paths = ScanerAnnotation.getPathsEnd(e.getClass());
			if (paths != null) {
				for(String path: paths) {
					log.debug(String.format("WsEndPoint mount on %s and class %s", path, e));
					serv.addWebSocket(path, e);
				}
			}
		}
	}
	
	static public WebServer runWebServer(Class<?> main_class) throws Exception {
		Map<String, String> pars = loadConfig(main_class); 
		if (pars == null)
			throw new Exception("List Parametrs is null");
		
		WebServer serv = new WebServer();
		// rewrite to annotion style link;
		if(pars.containsKey("port"))
			serv.setPort(Integer.parseInt(pars.get("port")));
		if(pars.containsKey("name"))
			HTTPResponse.setName(pars.get("name"));
		if(pars.containsKey("dir"))
			FileStorage.setmFile(pars.get("dir"));
	
		configWebServerEndPoint(serv, pars);
		
		return serv;
	}
	
}
