package org.mrbag.WebServer.Interfaces;

import java.util.Map;

import org.mrbag.WebServer.Reqwest.HTTPReqwest;
import org.mrbag.WebServer.Response.HTTPResponse;

public interface IEndPoint {
	
	public IEndPoint mount(Map<String, String> parameters);

	public HTTPResponse doit(HTTPReqwest req);
	
}
