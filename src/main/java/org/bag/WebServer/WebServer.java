package org.bag.WebServer;

import org.apache.log4j.Logger;

public class WebServer {

	final Logger log = Logger.getLogger(this.getClass());
	
	int port;
	
	public WebServer(int port) {
		log.debug("Simple mess");
	}
	
}
