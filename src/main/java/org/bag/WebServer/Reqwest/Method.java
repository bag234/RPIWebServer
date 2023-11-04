package org.bag.WebServer.Reqwest;

public enum Method {

	GET, POST, OTHER;
	
	public static Method getMethod(String mes) {
		switch (mes) {
		case "GET":
			return GET;
		case "POST":
			return POST;

		default:
			return OTHER;
		}
	}
	
}
