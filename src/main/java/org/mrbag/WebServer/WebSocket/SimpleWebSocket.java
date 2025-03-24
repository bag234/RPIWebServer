package org.bag.WebServer.WebSocket;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.bag.WebServer.Reqwest.HTTPReqwest;
import org.bag.WebServer.Response.HTTPResponse;
import org.bag.WebServer.Response.IResponse;

public class SimpleWebSocket implements IResponse, IWebSocketMethod {

	@Override
	public void sendResponse(BufferedOutputStream bufOut) throws IOException {	
		throw new IOException("NOT implementing");
	}

	@Override
	public void sendResponse(BufferedOutputStream bufOut, HTTPResponse res) throws IOException {
		throw new IOException("NOT implementing");
	}
	//unated method onOpen and sendResponse
	@Override
	public void sendResponse(BufferedOutputStream bufOut, HTTPResponse res, HTTPReqwest req) throws IOException {
		res.sendWSResponse(bufOut,req);
	}

	@Override
	public void onOpen(OutputStream out) {
		System.out.println("{ON OPEN}");
	}

	@Override
	public void onClose(OutputStream out) {
		System.out.println("{ON CLOSE}");
	}

	@Override
	public void onMessage(WSFrame ws,OutputStream out) throws IOException {
		String mess = new String(ws.getUnMasked());
		
		if(ws.isBinarFrame()) {
			System.out.println("{ON MESSAGE} binar " + mess);
		}
		if(ws.isTextFrame()) {
			System.out.println("{ON MESSAGE} text " + mess);
		}
		if(mess.equals("ping")) {
			out.write(WSFrame.getPing().getBytesOutput());
		}
		else
			out.write(WSFrame.getText(mess).getBytesOutput());
		out.flush();
	}

	@Override
	public void onPing(OutputStream out) throws IOException {
		// TODO Auto-generated method stub
		
	}
}
