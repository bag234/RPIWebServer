package org.bag.WebServer.Reqwest;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class HTTPReqwest {

	String path;
	
	Method method;
	
	Map<String, String> parametrs;
	
	boolean isHTTP = false;
	
	boolean isRFile = false;
	
	Logger log = Logger.getLogger(getClass());
	
	public HTTPReqwest(String mes) {
		parametrs = new HashMap<String, String>();
		log.debug("Start Decoding");
		log.debug(mes);
		decode(mes);
		log.debug("Stop Decoding");
		if(!isHTTP)
			log.warn("NOT HTTP ");
	}
	
	private void decode(String mes) {
		String[] mSlice = mes.split("\n");
		if(mSlice.length < 1)
			return;
		String[] headSlice = mSlice[0].split(" ");
		if(headSlice.length < 2)
			return;
		method = Method.getMethod(headSlice[0]);
		path = headSlice[1];
		isRFile = path.contains(".");
		isHTTP = true;
		String[] mmSlice;
		for (int i = 1; i < mSlice.length; i++) {
			mmSlice = mSlice[i].split(":");
			if(mmSlice.length > 1)
				parametrs.put(mmSlice[0], mmSlice[1]);
		}
		
	}
	
	public Method getMethod() {
		return method;
	}
	
	public String getPath() {
		return path;
	}
	
	public Map<String, String> getParametrs() {
		return parametrs;
	}
	
	public boolean isRFile() {
		return isRFile;
	}
	
	public boolean isHTTP() {
		return isHTTP;
	}
	
	public String getParametrs(String name) {
		return parametrs.getOrDefault(name, "None");
	}
	
}
