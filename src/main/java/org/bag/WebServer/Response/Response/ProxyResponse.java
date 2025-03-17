package org.bag.WebServer.Response.Response;

import java.io.BufferedOutputStream;
import java.io.IOException;

import org.bag.WebServer.Interfaces.IEndPoint;
import org.bag.WebServer.Reqwest.HTTPReqwest;
import org.bag.WebServer.Response.ContetTypes;
import org.bag.WebServer.Response.HTTPResponse;
import org.bag.WebServer.Response.IResponse;
import org.bag.WebServer.Response.StatusCode;

public class ProxyResponse implements IResponse{

	IEndPoint end;
	
	public ProxyResponse(IEndPoint end) {
		this.end = end;
	}
	
	@Override
	public void sendResponse(BufferedOutputStream bufOut) throws IOException {
		new HTTPResponse(StatusCode.ERROR, ContetTypes.PLAIN).getSimpelResponse(bufOut, "Error method`s call back!\n");
		
	}

	@Override
	public void sendResponse(BufferedOutputStream bufOut, HTTPResponse res) throws IOException {
		sendResponse(bufOut);
	}

	@Override
	public void sendResponse(BufferedOutputStream bufOut, HTTPResponse res, HTTPReqwest req) throws IOException {
		end.doit(req).getResponse(bufOut);
		
	}

}
