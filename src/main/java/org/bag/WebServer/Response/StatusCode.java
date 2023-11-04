package org.bag.WebServer.Response;

public enum StatusCode {

	INFO("100 Continue"), OK("200 OK"), WARNING("400 Bad Request"), ERROR("500 Internal Server Error");
	
	String message;
	
	private StatusCode(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	
}
