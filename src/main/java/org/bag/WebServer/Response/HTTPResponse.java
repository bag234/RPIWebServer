package org.bag.WebServer.Response;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.bag.WebServer.Storage.FileStorage;

public class HTTPResponse {

	
	StatusCode code = StatusCode.OK;
	
	ContetTypes type = ContetTypes.HTML;
	
	final String name = "Server: BAG-RPI LITE BETA";
	
	Logger log = Logger.getLogger(getClass());
	
	
	
	public HTTPResponse(StatusCode code, ContetTypes type) {
		this.code = code;
		this.type = type;
	}
	
	public HTTPResponse() {
	}
	
	private String getHead() {
		return "HTTP/1.0 " + code.getMessage() +
				"\n " + name + 
				"\n Content-Type: " + type + "\n\n";
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
	
}
