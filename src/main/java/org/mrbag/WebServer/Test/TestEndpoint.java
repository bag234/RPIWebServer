package org.mrbag.WebServer.Test;

import java.util.Map;

import org.mrbag.WebServer.Annotation.EndPoint;
import org.mrbag.WebServer.Interfaces.IEndPoint;
import org.mrbag.WebServer.Reqwest.HTTPReqwest;
import org.mrbag.WebServer.Response.ContetTypes;
import org.mrbag.WebServer.Response.HTTPResponse;
import org.mrbag.WebServer.Response.StatusCode;

@EndPoint(path = "/test")
public class TestEndpoint implements IEndPoint {

	@Override
	public HTTPResponse doit(HTTPReqwest req) {
		return HTTPResponse.New()
				.setBody(req)
				.setCode(StatusCode.OK)
				.setType(ContetTypes.PLAIN);
	}

	@Override
	public IEndPoint mount(Map<String, String> parameters) {
		return this;
	}

}
