package org.mrbag.WebServer.Test;

import org.mrbag.WebServer.Annotation.WSEndPoint;
import org.mrbag.WebServer.WebSocket.AbstractWSEndPoint;
import org.mrbag.WebServer.WebSocket.WSFrame;

@WSEndPoint(path = "/ws")
public class TestWSFrame extends AbstractWSEndPoint {

	@Override
	public void open() {
		System.out.println("On Open");
	}

	@Override
	public WSFrame message(WSFrame frame) {
		System.out.println("On mess: " + new String(frame.getUnMasked()));
		return WSFrame.getText(new String(frame.getUnMasked()));
	}

	@Override
	public void close() {
		System.out.println("On close");
	}

}
