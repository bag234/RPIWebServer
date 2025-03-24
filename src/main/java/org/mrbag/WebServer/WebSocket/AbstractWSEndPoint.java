package org.mrbag.WebServer.WebSocket;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.mrbag.WebServer.Reqwest.HTTPReqwest;
import org.mrbag.WebServer.Response.HTTPResponse;
import org.mrbag.WebServer.Response.IResponse;

public abstract class AbstractWSEndPoint implements IResponse, IWebSocketMethod{

	OutputStream out;
	
	public abstract void open();
	
	/**
	 * if return null frame not send
	 * @param frame
	 * @return
	 */
	public abstract WSFrame message(WSFrame frame);
	
	public abstract void close();
	
	public void sendMessage(WSFrame frame) throws IOException {
		out.write(frame.getBytesOutput());
		out.flush();
	}
	
	@Override
	public void sendResponse(BufferedOutputStream bufOut) throws IOException {
		 throw new IOException("NOT implementing");
	}
	
	@Override
	public void sendResponse(BufferedOutputStream bufOut, HTTPResponse res) throws IOException {
		sendResponse(bufOut);
	}
	
	@Override
	public void sendResponse(BufferedOutputStream bufOut, HTTPResponse res, HTTPReqwest req) throws IOException {
		res.sendWSResponse(bufOut,req);
	}
	
	@Override
	public void onOpen(OutputStream out) throws IOException {
		open();
		this.out = out;
	}
	
	public void onMessage(WSFrame ws,OutputStream out) throws IOException {
		WSFrame frame = message(ws);
		if (frame != null) {
			out.write(frame.getBytesOutput());
			out.flush();
		}
	}
	
	@Override
	public void onClose(OutputStream out) {
		close();
	}
	
	@Override
	public void onPing(OutputStream out) throws IOException {
		out.write(WSFrame.getPong().getBytesOutput());
		out.flush();
	}
}
