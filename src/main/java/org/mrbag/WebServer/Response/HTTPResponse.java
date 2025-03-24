package org.mrbag.WebServer.Response;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.apache.log4j.Logger;
import org.mrbag.WebServer.Reqwest.HTTPReqwest;
import org.mrbag.WebServer.Storage.FileStorage;

public class HTTPResponse {

	final static Logger log = Logger.getLogger(HTTPResponse.class);
	
	final String nameLine = "Server: %s";
	
	static String name = "RPI_LITE";
	
	Object body = null;
		
	StatusCode code = StatusCode.OK;
	
	ContetTypes type = ContetTypes.HTML;
	
	boolean isFile;
	
	static MessageDigest md;
	
	public static void setName(String name) {
		HTTPResponse.name = name;
	}
	
	public HTTPResponse(StatusCode code, ContetTypes type) {
		this.code = code;
		this.type = type;
		try {
			md = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {}
	}
	
	public HTTPResponse setCode(StatusCode code) {
		this.code = code;
		return this;
	}
	
	public HTTPResponse setType(ContetTypes type) {
		this.type = type;
		return this;
	}
	
	public HTTPResponse setBody(Object body) {
		isFile = false;
		this.body = body;
		return this;
	}
	/**
	 * Set to send file to response file
	 * @param file - path to file response;
	 * @return
	 */
	public HTTPResponse setBodyFile(String file) {
		isFile = true;
		this.body = file;
		return this;
	}
	
	public static HTTPResponse New() {
		return new HTTPResponse();
	}
	
	protected HTTPResponse() {
	}
	
	private String getHead() {
		return "HTTP/1.0 " + code.getMessage() +
				"\n " + String.format(nameLine, name) + 
				"\n Content-Type: " + type + "\n\n";
	}
	
	private String getAnswerWebSocket(String str) {
		String Sec = getCode(str);
		return "HTTP/1.1 " + code.getMessage() +
				"\r\n " + String.format(nameLine, name) + 
				"\r\nUpgrade: websocket" + 
				"\r\nConnection: upgrade" + 
				"\r\nSec-WebSocket-Accept: " + Sec +
				"\r\n\r\n";
	}
	
	public void getResponse(BufferedOutputStream bufOut) throws IOException {
		if(isFile && body != null) {
			sendFileRespons(bufOut, FileStorage.getFile((String)body));
			return;
		}
			
		bufOut.write(getHead().getBytes());
		if(body != null) {
			if(body instanceof String)
				bufOut.write(((String) body).getBytes());
			else
				bufOut.write(body.toString().getBytes());
		}
		bufOut.flush();
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
		@SuppressWarnings("resource")
		
		InputStream inF = new FileInputStream(file);
		bufOut.write(inF.readAllBytes());
		
		bufOut.flush();
	}
	
	/* WEB SOCKET */
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
