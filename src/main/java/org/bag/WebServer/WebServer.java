package org.bag.WebServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.bag.RPIControl.PigFactory;
import org.bag.RPIControl.RPIListPin;
import org.bag.RPIControl.RPIResponse;
import org.bag.WebServer.Response.IResponse;
import org.bag.WebServer.Response.Response.FileSimpleResponse;
import org.bag.WebServer.WebSocket.SimpleWebSocket;

public class WebServer {

	final Logger log = Logger.getLogger(this.getClass());
	
	int port;
	
//	PigFactory pig = new PigFactory();
	
	Map<String, IResponse> paths;
	
	Map<String, IResponse> WSpaths;
	
	public void initPath() {
		
		log.debug("Initialization paths");
		paths.put("/", new FileSimpleResponse("/Index.html"));
		paths.put("/index", new FileSimpleResponse("/Index.html"));
		paths.put("/main", new FileSimpleResponse("/Index.html"));
//		paths.put("/gpio", new RPIListPin(pig));
//		paths.put("/gpio/rel1", new RPIResponse(14, "rel1", pig));
//		paths.put("/gpio/rel2", new RPIResponse(15, "rel2", pig));
//		paths.put("/gpio/rel3", new RPIResponse(18, "rel3", pig));
		
		WSpaths.put("/ws", new SimpleWebSocket());
	}
	
	public WebServer(int port) {
		log.info("Init server");
		paths = new HashMap<String, IResponse>();
		WSpaths = new HashMap<String, IResponse>();
		initPath();
		try {
			ServerSocket servSock = new ServerSocket(port);
			while(servSock.isBound()) {
				Socket sock = servSock.accept();
				new Thread(new ClientThread(sock, paths, WSpaths)).run();
			}
		} catch (IOException e) {
			log.fatal("FATAL ERROR");
			log.fatal(e);
		}
		
	}
	
}
