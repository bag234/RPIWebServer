package org.bag.WebServer.Test;

import java.util.Map;

import org.bag.WebServer.Annotation.EndPoint;
import org.bag.WebServer.Interfaces.IEndPoint;
import org.bag.WebServer.Reqwest.HTTPReqwest;
import org.bag.WebServer.Response.ContetTypes;
import org.bag.WebServer.Response.HTTPResponse;
import org.bag.WebServer.Response.StatusCode;

@EndPoint(path = "/test")
public class TestEndpoint implements IEndPoint {

	@Override
	public void mount(Map<String, String> parameters) {

	}

	@Override
	public HTTPResponse doit(HTTPReqwest req) {
		return HTTPResponse.New().setBody(req).setCode(StatusCode.OK).setType(ContetTypes.PLAIN);
	}

}
