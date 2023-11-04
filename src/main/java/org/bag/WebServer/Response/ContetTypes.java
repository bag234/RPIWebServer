package org.bag.WebServer.Response;

public enum ContetTypes {
	
	PLAIN("text/plain"), HTML("text/html"), JS("code/js"), OTHER("text/plain");
	
	String type;
	
	private ContetTypes(String type) {
		this.type = type;
	}
	
}
