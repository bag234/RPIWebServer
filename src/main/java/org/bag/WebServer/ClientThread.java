package org.bag.WebServer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;


import org.apache.log4j.Logger;
import org.bag.WebServer.Reqwest.HTTPReqwest;
import org.bag.WebServer.Response.HTTPResponse;
import org.bag.WebServer.Storage.FileStorage;

public class ClientThread extends Thread {

	BufferedInputStream bufIn;
	
	BufferedOutputStream bufOut;
	
	final Logger log = Logger.getLogger(getClass()); 
	
	final FileStorage storage = new FileStorage();
	
	public String decoding() throws IOException {
		String str = "";
		log.debug("Start Decoding message");
		while (bufIn.available() > 0) {
			str += (char) bufIn.read();
		}
		log.debug("End Decoding message");
		return str;
	}
	
	public ClientThread(Socket sock) {
		log.info("START SOCKET LISSEN: " + sock);
		try {
			bufIn = new BufferedInputStream(sock.getInputStream());
			bufOut = new BufferedOutputStream(sock.getOutputStream());
		} catch (IOException e) {
			log.error("Error get stream's");
			log.error(e);
		}
		
	}
	
	@Override
	public void run() {
		try {
			HTTPReqwest req = new HTTPReqwest(decoding());
			if(req.isHTTP()) {
				if (req.isRFile())
					new HTTPResponse().sendFileRespons(bufOut, storage.getFile(req.getPath()));
				else
					new HTTPResponse().getSimpelResponse(bufOut, "<h1> INDEX PAGE SIMPLE ANSWER </h1>\n");
			}
			bufOut.flush();
			bufIn.close();
			bufOut.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
			
		}
		log.info("END SOCKET LISSEN");
		super.run();
	}
}
