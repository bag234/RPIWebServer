package org.mrbag.WebServer.WebSocket;

import java.io.IOException;
import java.io.OutputStream;

public interface IWebSocketMethod {

	//IF WSFRAME == NULL frame don't send;
	public void onOpen(OutputStream out) throws IOException ;
	
	public void onMessage(WSFrame ws,OutputStream out) throws IOException ;
	
	public void onClose(OutputStream out) throws IOException ;
	
	public void onPing(OutputStream out) throws IOException ;
}
