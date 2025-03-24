package org.mrbag.WebServer.Response.Response;

import java.io.BufferedOutputStream;
import java.io.IOException;

import org.mrbag.WebServer.Interfaces.IEndPoint;
import org.mrbag.WebServer.Reqwest.HTTPReqwest;
import org.mrbag.WebServer.Response.ContetTypes;
import org.mrbag.WebServer.Response.HTTPResponse;
import org.mrbag.WebServer.Response.IResponse;
import org.mrbag.WebServer.Response.StatusCode;

public class WarpedResponse implements IResponse{

	IEndPoint end;
	
	public WarpedResponse(IEndPoint end) {
		this.end = end;
	}
	
	@Override
	public void sendResponse(BufferedOutputStream bufOut) throws IOException {
		new HTTPResponse(StatusCode.ERROR, ContetTypes.PLAIN)
		.getSimpelResponse(bufOut, "Error method`s call back!\n");
		
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
