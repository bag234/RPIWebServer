package org.bag.RPIControl;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Map;

import org.bag.WebServer.Reqwest.HTTPReqwest;
import org.bag.WebServer.Response.HTTPResponse;
import org.bag.WebServer.Response.IResponse;

public class RPIListPin implements IResponse {
	
	PigFactory pig;
	
	public RPIListPin(PigFactory pig) {
		this.pig = pig;
	}

	@Override
	public void sendResponse(BufferedOutputStream bufOut) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendResponse(BufferedOutputStream bufOut, HTTPResponse res) throws IOException {
		
	}

	@Override
	public void sendResponse(BufferedOutputStream bufOut, HTTPResponse res, HTTPReqwest req) throws IOException {
		String mes = "";
		for (Map.Entry<Integer, String> entry : RPIResponse.list.entrySet()) {
			Integer pin = entry.getKey();
			String name = entry.getValue();
			mes += name + ":" +  pig.getSimplePinValue(pin) + "\n";
		}
		res.getSimpelResponse(bufOut, mes);
	}

}
