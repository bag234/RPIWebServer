package org.mrbag.WebServer.Response;

import java.io.BufferedOutputStream;
import java.io.IOException;

import org.mrbag.WebServer.Reqwest.HTTPReqwest;

public interface IResponse {

	public void sendResponse(BufferedOutputStream bufOut) throws IOException ;
	
	public void sendResponse(BufferedOutputStream bufOut, HTTPResponse res) throws IOException ;
	
	public void sendResponse(BufferedOutputStream bufOut, HTTPResponse res, HTTPReqwest req) throws IOException;
	
}
