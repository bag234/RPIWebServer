package org.bag.WebServer;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.log4j.Logger;

import org.bag.WebServer.Annotation.WebServerConfigFile;
import org.bag.WebServer.Response.HTTPResponse;
import org.bag.WebServer.Storage.FileStorage;

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
		
		return serv;
	}
	
}
