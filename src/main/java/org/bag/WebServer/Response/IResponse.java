package org.bag.WebServer.Response;

import java.io.BufferedOutputStream;
import java.io.IOException;

public interface IResponse {

	public void sendResponse(BufferedOutputStream bufOut) throws IOException ;
	
	public void sendResponse(BufferedOutputStream bufOut, HTTPResponse res) throws IOException ;
	
	//HTTPResponse res
	
}
