package org.bag.RPIControl;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.bag.WebServer.Reqwest.HTTPReqwest;
import org.bag.WebServer.Reqwest.Method;
import org.bag.WebServer.Response.HTTPResponse;
import org.bag.WebServer.Response.IResponse;

public class RPIResponse implements IResponse {

	PigFactory pig = new PigFactory();
	
	final Logger log = Logger.getLogger(getClass());
	
	int pin;
	
	public static Map<Integer, String> list = new HashMap<Integer, String>();
	
	String name;
	
	public RPIResponse(int pin, String name, PigFactory pig) {
		log.debug("Make GPIO Response: " + pin +"=" + name);
		this.pin = pin;
		this.name = name;
		this.pig = pig;
		list.put(pin,name);
		pig.setSimpleOutPin(pin);
	}
	
	@Override
	public void sendResponse(BufferedOutputStream bufOut) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendResponse(BufferedOutputStream bufOut, HTTPResponse res) throws IOException {
		log.info("Get GPIO HTTP STATUS: " + pin);
		res.getSimpelResponse(bufOut, name + ":" + pig.getSimplePinValue(pin) + "\n");		
	}

	@Override
	public void sendResponse(BufferedOutputStream bufOut, HTTPResponse res, HTTPReqwest req) throws IOException {
		if(req.getMethod() == Method.GET) {
			pig.setSimplePinValue(pin, ! pig.getSimplePinValue(pin));
		}
		if(req.getMethod() == Method.POST) {
			pig.setSimplePinValue(pin, ! pig.getSimplePinValue(pin));
		}
		res.getSimpelResponse(bufOut, "<h1>" + name + ":" + pig.getSimplePinValue(pin) + "\n");		
	}

}
