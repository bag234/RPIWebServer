package org.bag.RPIControl;

import uk.pigpioj.*;

public class PigFactory {

	PigpioInterface pgio;
	
	
	
	public PigFactory() {
		pgio = PigpioJ.autoDetectedImplementation();
	}
	
	public void setSimpleOutPin(int pin) {
		pgio.setMode(pin, PigpioConstants.MODE_PI_OUTPUT);
	}
	
	public void setSimplePinValue(int pin, boolean value) {
		pgio.write(pin, value);
	}
	
	public boolean getSimplePinValue(int pin) {
		return pgio.read(pin) == 1;
	}
}
