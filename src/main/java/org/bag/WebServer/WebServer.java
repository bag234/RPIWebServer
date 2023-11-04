package org.bag.WebServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.bag.WebServer.Response.IResponse;
import org.bag.WebServer.Response.Response.FileSimpleResponse;

public class WebServer {

	final Logger log = Logger.getLogger(this.getClass());
	
	int port;
	
	Map<String, IResponse> paths;
	
	public void initPath() {
		log.debug("Initialization paths");
		paths.put("/", new FileSimpleResponse("/Index.html"));
		paths.put("/index", new FileSimpleResponse("/Index.html"));
		paths.put("/main", new FileSimpleResponse("/Index.html"));
	}
	
	public WebServer(int port) {
		log.info("Init server");
		paths = new HashMap<String, IResponse>();
		initPath();
		try {
			ServerSocket servSock = new ServerSocket(port);
			while(servSock.isBound()) {
				Socket sock = servSock.accept();
				new Thread(new ClientThread(sock, paths)).run();
			}
		} catch (IOException e) {
			log.fatal("FATAL ERROR");
			log.fatal(e);
		}
		
	}
	
}
