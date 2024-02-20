package org.bag.WebServer.Response;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.apache.log4j.Logger;
import org.bag.WebServer.Reqwest.HTTPReqwest;
import org.bag.WebServer.Storage.FileStorage;
import org.bag.WebServer.WebSocket.WSFrame;

public class HTTPResponse {

	
	StatusCode code = StatusCode.OK;
	
	ContetTypes type = ContetTypes.HTML;
	
	final String name = "Server: BAG-RPI LITE BETA";
	
	Logger log = Logger.getLogger(getClass());
	
	static MessageDigest md;
	
	public HTTPResponse(StatusCode code, ContetTypes type) {
		this.code = code;
		this.type = type;
		try {
			md = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public HTTPResponse() {
	}
	
	private String getHead() {
		return "HTTP/1.0 " + code.getMessage() +
				"\n " + name + 
				"\n Content-Type: " + type + "\n\n";
	}
	
	private String getAnswerWebSocket(String str) {
		String Sec = getCode(str);
		return "HTTP/1.1 " + code.getMessage() +
				"\r\n " + name + 
				"\r\nUpgrade: websocket" + 
				"\r\nConnection: upgrade" + 
//				"\r\n WebSocket-Origin: http://localhost:9099" + 
//				"\r\n WebSocket-Location: ws://localhost:9099/ws" + 
				"\r\nSec-WebSocket-Accept: " + Sec +
				"\r\n\r\n";
	}
	
	public void getSimpelResponse(BufferedOutputStream bufOut, String message) throws IOException {
		log.debug("send simple Response: " + message);
		bufOut.write(getHead().getBytes());
		bufOut.write(message.getBytes());
		bufOut.flush();
	}
	
	public void sendFileRespons(BufferedOutputStream bufOut, File file) throws IOException {
		log.debug("send simple File Response: " + file.getAbsolutePath());
		if(!file.canRead())
			code = StatusCode.ERROR;
		type = FileStorage.getTypesFile(file);
		bufOut.write(getHead().getBytes());
		InputStream inF = new FileInputStream(file);
		bufOut.write(inF.readAllBytes());
		bufOut.flush();
	}
	
	static public String getCode(String str) { 
		String text = str + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
		byte[] buf = md.digest(text.getBytes());
		String based = Base64.getEncoder().encodeToString(buf);
		return based;
	}
	
	public void sendWSResponse(BufferedOutputStream bufOut, HTTPReqwest req) throws IOException {
		log.debug("send WebSocket Response");
		HTTPResponse res = new HTTPResponse(StatusCode.SWITCH, ContetTypes.HTML);
		String wsScode = req.getParametrs().get("Sec-WebSocket-Key");
		String mess = res.getAnswerWebSocket(wsScode);
		bufOut.write(mess.getBytes());
		bufOut.flush();
	}
	
}
