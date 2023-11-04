package org.bag.WebServer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.bag.WebServer.Reqwest.HTTPReqwest;

public class ClientThread extends Thread {

	BufferedInputStream bufIn;
	
	BufferedOutputStream bufOut;
	
	final Logger log = Logger.getLogger(getClass()); 
	
	public String decoding() throws IOException {
		String str = "";
		log.debug("Start Decoding message");
		while (bufIn.available() > 0) {
			str += (char) bufIn.read();
		}
		log.debug("End Decoding message");
		return str;
	}
	
	public ClientThread() {
		
	}
	
	@Override
	public void run() {
		try {
			HTTPReqwest req = new HTTPReqwest(decoding());
			if(req.isHTTP());
		} catch (Exception e) {
			log.error(e);
		}
		super.run();
	}
}
