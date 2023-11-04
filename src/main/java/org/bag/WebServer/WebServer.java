package org.bag.WebServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

public class WebServer {

	final Logger log = Logger.getLogger(this.getClass());
	
	int port;
	
	public WebServer(int port) {
		log.debug("Init server");
		try {
			ServerSocket servSock = new ServerSocket(port);
			while(servSock.isBound()) {
				Socket sock = servSock.accept();
				new Thread(new ClientThread(sock)).run();
			}
		} catch (IOException e) {
			log.fatal("FATAL ERROR");
			log.fatal(e);
		}
		
	}
	
}
