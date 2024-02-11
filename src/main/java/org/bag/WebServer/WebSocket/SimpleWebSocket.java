package org.bag.WebServer.WebSocket;

import java.io.BufferedOutputStream;
import java.io.IOException;

import org.bag.WebServer.Reqwest.HTTPReqwest;
import org.bag.WebServer.Response.HTTPResponse;
import org.bag.WebServer.Response.IResponse;

public class SimpleWebSocket implements IResponse {

	@Override
	public void sendResponse(BufferedOutputStream bufOut) throws IOException {	
		throw new IOException("NOT implementing");
	}

	@Override
	public void sendResponse(BufferedOutputStream bufOut, HTTPResponse res) throws IOException {
		throw new IOException("NOT implementing");
	}

	@Override
	public void sendResponse(BufferedOutputStream bufOut, HTTPResponse res, HTTPReqwest req) throws IOException {
		res.sendWSResponse(bufOut,req);
	}
}
