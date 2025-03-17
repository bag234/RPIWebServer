package org.bag.WebServer.Interfaces;

import java.util.Map;

import org.bag.WebServer.Reqwest.HTTPReqwest;
import org.bag.WebServer.Response.HTTPResponse;

public interface IEndPoint {
	
	public void mount(Map<String, String> parameters);

	public HTTPResponse doit(HTTPReqwest req);
	
}
