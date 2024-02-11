package org.bag.WebServer;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import io.netty.handler.codec.base64.Base64Decoder;

public class Test {

	public static void main(String[] args) throws NoSuchAlgorithmException {
		String text = "dGhlIHNhbXBsZSBub25jZQ==258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		byte[] buf = md.digest(text.getBytes());
		String based = Base64.getEncoder().encodeToString(buf);
		System.out.println(based);

	}

}
