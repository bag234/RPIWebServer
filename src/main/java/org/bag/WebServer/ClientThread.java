package org.bag.WebServer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Map;

import org.apache.log4j.Logger;
import org.bag.WebServer.Reqwest.HTTPReqwest;
import org.bag.WebServer.Response.HTTPResponse;
import org.bag.WebServer.Response.IResponse;
import org.bag.WebServer.Response.Response.FileSimpleResponse;
import org.bag.WebServer.Storage.FileStorage;
import org.bag.WebServer.WebSocket.SimpleWebSocket;
import org.bag.WebServer.WebSocket.WSFrame;

public class ClientThread extends Thread {

	BufferedInputStream bufIn;
	
	BufferedOutputStream bufOut;
	
	final Logger log = Logger.getLogger(getClass()); 
	
	final FileStorage storage = new FileStorage();
	
	Map<String, IResponse> paths;
	
	public String decoding() throws IOException {
		String str = "";
		log.debug("Start Decoding message");
		while (bufIn.available() > 0) {
			str += (char) bufIn.read();
		}
		log.debug("End Decoding message");
		return str;
	}
	
	public ClientThread(Socket sock, Map<String, IResponse> paths) {
		this.paths = paths;
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
			if(req.isWS()) {
				SimpleWebSocket resWs = new SimpleWebSocket();
				resWs.sendResponse(bufOut, new HTTPResponse(), req);
				log.debug("Start WebSocket decoding message");
				
				WSFrame frame = new WSFrame();
				byte[] buf;
				int[] intBuf;
				
				while (true) {
					 if (bufIn.available() > 0) {
						buf = new byte[bufIn.available()];
						intBuf = new int[bufIn.available()];
						
						bufIn.readNBytes(buf, 0, bufIn.available());
											
						frame = new WSFrame(buf);
						if(frame.isColseFrame()) {
							log.debug("Close WebSocket frame");
							break;
						}
						frame.getUnMasked();
						
						bufOut.write(WSFrame.getBinary(frame.getUnMasked()).getBytesOutput());
						bufOut.flush();
						
					}
				}
			}
			else
			if(req.isHTTP()) {
				if (req.isRFile())
					new HTTPResponse().sendFileRespons(bufOut, storage.getFile(req.getPath()));
				else {
					IResponse res  = paths.get(req.getPath().toLowerCase());
//					res.sendResponse(bufOut);
					paths.getOrDefault(req.getPath().toLowerCase(), new FileSimpleResponse("/error"))
						.sendResponse(bufOut, new HTTPResponse(), req);
				}
//					new HTTPResponse().getSimpelResponse(bufOut, "<h1> INDEX PAGE SIMPLE ANSWER </h1>\n");
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
