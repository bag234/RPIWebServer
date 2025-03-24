package org.mrbag.WebServer.Reqwest;

public enum Method {

	GET, POST, OTHER;
	
	public static Method getMethod(String mes) {
		try {
			return Method.valueOf(mes.toUpperCase());
		}
		catch (Exception e) {
			return Method.OTHER;
		}
		
	}
	
}
