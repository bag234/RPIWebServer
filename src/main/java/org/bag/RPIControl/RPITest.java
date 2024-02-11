package org.bag.RPIControl;

public class RPITest {
	
	final PigFactory pig = new PigFactory();
	
	public RPITest() throws InterruptedException {
		pig.setSimpleOutPin(14);
		for (int i = 0; i < 100; i++) {
			pig.setSimplePinValue(14, false);
			Thread.sleep(1000);
			pig.setSimplePinValue(14, true);
			System.out.println(pig.getSimplePinValue(14));
			Thread.sleep(1000);
		}
	}

	
	public static void main(String[] args) throws InterruptedException {
		new RPITest();
	}
	
}
