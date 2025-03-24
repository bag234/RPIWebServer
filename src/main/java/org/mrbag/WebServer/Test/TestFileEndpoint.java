package org.mrbag.WebServer.Test;

import java.util.Map;

import org.mrbag.WebServer.Annotation.EndPoint;
import org.mrbag.WebServer.Interfaces.IEndPoint;
import org.mrbag.WebServer.Reqwest.HTTPReqwest;
import org.mrbag.WebServer.Response.HTTPResponse;

@EndPoint(path = {"/", "/index", "/main", ""})
public class TestFileEndpoint implements IEndPoint {

	@Override
	public IEndPoint mount(Map<String, String> parameters) {
		return this;
	}

	@Override
	public HTTPResponse doit(HTTPReqwest req) {
		return HTTPResponse.New().setBodyFile("/Index.html");
	}

}
