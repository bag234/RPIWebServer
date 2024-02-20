package org.bag.WebServer.WebSocket;

import java.util.ArrayList;
import java.util.stream.Stream;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

enum OpCODE {
	
	CONT(0), TEXT(1), BINA(2), CLOSE(8), PING(0x9), PONG(0xA);
	
	int count;
	
	private OpCODE(int count) {
		this.count = count;
	}
	
	public int getCount() {
		return count;
	}
}
// Simple Bytes 
public class WSFrame {
	
	//FIRST BYTE
	boolean isFine;
	
	boolean RSV1 = false;
	
	boolean RSV2 = false;
	
	boolean RSV3 = false;
	
	OpCODE opCode;
	
	//Second Byte
	
	boolean isMask = false;
	
	int size;

	// Triple Byte - Foure byte
	
	int[] Mask = new int[4];
	
	// Fived byte;
	
	int[] payLoad;
	

	public WSFrame(int[] message) {
//		if (message.length < 4) {
//			throw new Exception("SO Litle");
//		}
		// load First Bytes
		isFine = ((message[0] & 128) != 0);
		RSV1 = ((message[0] & 64) != 0);
		RSV2 = ((message[0] & 32) != 0);
		RSV3 = ((message[0] & 16) != 0);
	
		for (OpCODE code : OpCODE.values()) {
			if((message[0] & code.getCount()) != 0) {
				opCode = code;
			}
		}
		//load Second Byte
		isMask =  ((message[1] & 128) != 0);
		size = (message[1] - 128);
		
		//load 3-7 Mask
		System.arraycopy(message, 2, Mask, 0, 4);
		//load 8-All Payload 
		payLoad = new int[size];
		System.arraycopy(message, 6, payLoad, 0, size);
	}
	
	public int[] getUnMasked() {
		int[] unMasked = new int[payLoad.length];
		
		for(int i = 0; i < payLoad.length; i++) {
			unMasked[i] = (payLoad[i] ^ Mask[i % 4]);
		}
		
		return unMasked;
	}
	
	public static WSFrame getPing() {
		WSFrame frame = new WSFrame();
		int[] arr = {100, 100, 100, 100, 100, 100};
		frame.isFine = true;
		frame.opCode = OpCODE.PING;
		frame.size =  6;
		frame.payLoad = arr;
		
		return frame;
	}
	
	public byte[] getBytes() {
		byte[] bs = new byte[6 + size];

		
		int b1 = 0;
		int b2 = 0;
		//firstByte
		if (isFine)
			b1 = 128;
		if (RSV1)
			b1 |= 64;
		if (RSV2)
			b1 |= 32;
		if (RSV3)
			b1 |= 16;
		b1 |= opCode.getCount();
		//Second Byte;
		if (isMask)
			b2 |= 128;
		b2 |= size;
		
		bs[0] = (byte) b1;
		bs[1] = (byte) b2;
		for (int i = 0; i < Mask.length; i++) {
			bs[2 + i] = (byte) Mask[i];
		}
		for (int i = 0; i < size; i++) {
			bs[6 + i] = (byte) payLoad[i];
		}
		return bs;
 	}
	
	public WSFrame() {
	}
	
	public static void main(String[] args) throws DataFormatException {
		WSFrame frame = new WSFrame();
		frame.isFine = true;
		frame.opCode = OpCODE.PING;
		frame.isMask = true;
		frame.size = 6;
		
		int b1 = 0;
		int b2 = 0;
		// first byte
		if (frame.isFine)
			b1 = 128;
		if (frame.RSV1)
			b1 |= 64;
		if (frame.RSV2)
			b1 |= 32;
		if (frame.RSV3)
			b1 |= 16;
		b1 |= frame.opCode.getCount();
		//Second Byte;
		if (frame.isMask)
			b2 |= 128;
		b2 |= frame.size;
		
		int[] bw = {b1, b2, 0xf7, 0x1a, 0x92, 0xa3, 0x3d, 0xd2, 0x5a, 0x6b, 0xf7, 0x1a, 0xC8};
		WSFrame frame2 = new WSFrame(bw);
		
		for (int i = 0; i < bw.length; i++) {
			System.out.print(Integer.toHexString(bw[i]) + ", ");
		}
		System.out.println();
		
		int[] sh = frame2.getUnMasked();
		
		for (int i = 0; i < sh.length; i++) {
			System.out.print(sh[i]  + ", ");
		}
		System.out.println();
		
		
		System.out.println(Integer.toBinaryString(bw[2]));
		System.out.println(b2);
	}
	
}
