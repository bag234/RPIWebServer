package org.mrbag.WebServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.mrbag.WebServer.Response.IResponse;
import org.mrbag.WebServer.Response.Response.FileSimpleResponse;
import org.mrbag.WebServer.WebSocket.SimpleWebSocket;

public class WebServer {

	final Logger log = Logger.getLogger(this.getClass());
	
	int port = 9099; 
	
	Map<String, IResponse> paths;
	
	Map<String, IResponse> WSpaths;
	
//	PigFactory pig = new PigFactory();
	
	public WebServer addPath(String path, IResponse response) {
		paths.put(path, response);
		return this;
	}
	
	public WebServer addWebSocket(String path, IResponse response) {
		WSpaths.put(path, response);
		return this;
	}

	public WebServer setPort(int port) {
		this.port = port;
		return this;
	}

	public WebServer() {
		paths = new HashMap<String, IResponse>();
		WSpaths = new HashMap<String, IResponse>();
	}
	
	public void run() {
		printLogHead();
		
		try (ServerSocket servSock = new ServerSocket(port)) {
			while(servSock.isBound()) {
				Socket sock = servSock.accept();
				new Thread(new ClientThread(sock, paths, WSpaths)).start();
			}
		} catch (IOException e) {
			log.fatal("FATAL ERROR");
			log.fatal(e);
		}
	}

	private void printLogHead() {
		log.info("SERVER START WHTIS PORT: " + port);
	}
	
}
