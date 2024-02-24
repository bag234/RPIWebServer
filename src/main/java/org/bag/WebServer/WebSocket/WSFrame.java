package org.bag.WebServer.WebSocket;

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
	
	byte[] Mask = new byte[4];
	
	// Fived byte;
	
	byte[] payLoad;
	
	
	public WSFrame(byte[] message) {
//		if (message.length < 4) {
//			throw new Exception("SO Litle");
//		}
		// load First Bytes
		isFine = ((message[0] & 128) != 0);
		RSV1 = ((message[0] & 64) != 0);
		RSV2 = ((message[0] & 32) != 0);
		RSV3 = ((message[0] & 16) != 0);
		opCode = OpCODE.CONT;
		
		for (OpCODE code : OpCODE.values()) {
			if((message[0] | code.getCount()) == message[0]) {
				opCode = code;
			}
		}
		//load Second Byte
		isMask =  ((message[1] & 128) != 0);
		if(isMask)
			size = (message[1] - (byte)128);
		
		//load 3-7 Mask
		System.arraycopy(message, 2, Mask, 0, 4);
		//load 8-All Payload 
		payLoad = new byte[size];
		System.arraycopy(message, 6, payLoad, 0, size);
	}
	
	public byte[] getUnMasked() {
		byte[] unMasked = new byte[payLoad.length];
		
		for(int i = 0; i < payLoad.length; i++) {
			unMasked[i] = (byte)(payLoad[i] ^ Mask[i % 4]);
		}
		
		return unMasked;
	}
	
	public static WSFrame getPing() {
		WSFrame frame = new WSFrame();
		String mes = "Is Ping Sever Message";
		frame.isFine = true;
		frame.opCode = OpCODE.PING;
		frame.size =  mes.length();
		frame.payLoad = mes.getBytes();
		
		return frame;
	}
	
	public static WSFrame getPong() {
		WSFrame frame = new WSFrame();
		String mes = "Is Pong Sever Message";
		frame.isFine = true;
		frame.opCode = OpCODE.PONG;
		frame.size =  mes.length();
		frame.payLoad = mes.getBytes();
		
		return frame;
	}
	
	public static WSFrame getBinary(byte[] mess) {
		WSFrame frame = new WSFrame();
		frame.isFine = true;
		frame.opCode = OpCODE.BINA;
		frame.size =  mess.length;
		frame.payLoad = mess;
	
		return frame;
	}
	
	public static WSFrame getText(String mess) {
		WSFrame frame = new WSFrame();
		frame.isFine = true;
		frame.opCode = OpCODE.TEXT;
		frame.size =  mess.getBytes().length;
		frame.payLoad = mess.getBytes();
	
		return frame;
	}
	
	public byte[] getBytesOutput() {
		byte[] bs = new byte[2 + size];

		
		byte b1 = 0;
		byte b2 = 0;
		//firstByte
		if (isFine)
			b1 = (byte)128;
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
//		for (int i = 0; i < Mask.length; i++) {
//			bs[2 + i] = (byte) Mask[i];
//		}
		for (int i = 0; i < size; i++) {
			bs[2 + i] = (byte) payLoad[i];
		}
		return bs;
 	}
	
	public WSFrame() {
	}
	
	public boolean isColseFrame() {
		return (opCode == OpCODE.CLOSE);
	}
	
	public boolean isPingFrame() {
		return (opCode == OpCODE.PING);
	}
	
	public boolean isPongFrame() {
		return (opCode == OpCODE.PONG);
	}
	
	public boolean isTextFrame() {
		return (opCode == OpCODE.TEXT);
	}
	
	public boolean isBinarFrame() {
		return (opCode == OpCODE.BINA);
	}
	
	public boolean isNoneFrame() {
		return (opCode == OpCODE.CONT);
	}
}
