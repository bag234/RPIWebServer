package org.mrbag.WebServer.Response.Response;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;

import org.mrbag.WebServer.Reqwest.HTTPReqwest;
import org.mrbag.WebServer.Response.HTTPResponse;
import org.mrbag.WebServer.Response.IResponse;
import org.mrbag.WebServer.Storage.FileStorage;

public class FileSimpleResponse implements IResponse {
	
	final FileStorage storage = new FileStorage();
	
	File file;
	
	public FileSimpleResponse(String name) {
		file = storage.getFile(name);
	}

	@Override
	public void sendResponse(BufferedOutputStream bufOut) throws IOException {
		HTTPResponse.New().sendFileRespons(bufOut, file);		
	}
	
	@Override
	public void sendResponse(BufferedOutputStream bufOut, HTTPResponse res) throws IOException {
		HTTPResponse.New().sendFileRespons(bufOut, file);		
	}

	@Override
	public void sendResponse(BufferedOutputStream bufOut, HTTPResponse res, HTTPReqwest req) throws IOException {
		HTTPResponse.New().sendFileRespons(bufOut, file);
	}
	
}
