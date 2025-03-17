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
import org.bag.WebServer.WebSocket.IWebSocketMethod;
import org.bag.WebServer.WebSocket.SimpleWebSocket;
import org.bag.WebServer.WebSocket.WSFrame;

public class ClientThread extends Thread {

	static long idThread = 0;
	
	BufferedInputStream bufIn;
	
	BufferedOutputStream bufOut;
	
	final Logger log = Logger.getLogger(getClass()); 
	
	final FileStorage storage = new FileStorage();
	
	Map<String, IResponse> paths;
	
	Map<String, IResponse> WSpaths;
	
	public String decoding() throws IOException {
		String str = "";
		log.debug(idThread + "| Start Decoding message");
		while (bufIn.available() > 0) {
			str += (char) bufIn.read();
		}
		log.debug(idThread + "| End Decoding message");
		return str;
	}
	
	public ClientThread(Socket sock, Map<String, IResponse> paths, Map<String, IResponse> WSpaths) {
		idThread++;
		this.paths = paths;
		this.WSpaths = WSpaths;
		log.info(idThread + "| START SOCKET LISSEN: " + sock);
		try {
			bufIn = new BufferedInputStream(sock.getInputStream());
			bufOut = new BufferedOutputStream(sock.getOutputStream());
		} catch (IOException e) {
			log.error(idThread + "| Error get stream's");
			log.error(e);
		}
		
	}
	
	@Override
	public void run() {
		try {
			
			HTTPReqwest req = new HTTPReqwest(decoding());
			
			if(req.isWS()) {
				IResponse res = WSpaths.get(req.getPath().toLowerCase());
				if(res != null) {
					res.sendResponse(bufOut, HTTPResponse.New(), req);
					IWebSocketMethod wsmes = (IWebSocketMethod) res;
					log.debug(idThread + "| Start WebSocket decoding message");
					wsmes.onOpen(bufOut);
					
					WSFrame frame = new WSFrame();
					byte[] buf;
					
					while (true) {
						 if (bufIn.available() > 0) {
							buf = new byte[bufIn.available()];
							
							bufIn.readNBytes(buf, 0, bufIn.available());
												
							frame = new WSFrame(buf);
							if(frame.isColseFrame()) {
								wsmes.onClose(bufOut);
								log.debug(idThread + "| Close WebSocket frame");
								break;
							}
							if(frame.isPingFrame()) {
								wsmes.onPing(bufOut);
								bufOut.write(WSFrame.getPong().getBytesOutput());
								bufOut.flush();
								continue;
							}
							if(frame.isPongFrame()) {
								log.debug(idThread + "| Client send pong frame");
							}
							
							wsmes.onMessage(frame, bufOut);
						}
					}
				}
				else {
					new FileSimpleResponse("/error").sendResponse(bufOut, HTTPResponse.New(), req);
				}
			}
			else
			if(req.isHTTP()) {
				if (req.isRFile())
					HTTPResponse.New().sendFileRespons(bufOut, storage.getFile(req.getPath()));
				else {
//					IResponse res  = paths.get(req.getPath().toLowerCase());
//					res.sendResponse(bufOut);
					paths.getOrDefault(req.getPath().toLowerCase(), new FileSimpleResponse("/error"))
						.sendResponse(bufOut, HTTPResponse.New(), req);
				}
			}
			
			bufOut.flush();
			bufIn.close();
			bufOut.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error(idThread + "| Error", e);
			
		} 
		
		log.info(idThread + "| END SOCKET LISSEN");
		super.run();
	}
}
