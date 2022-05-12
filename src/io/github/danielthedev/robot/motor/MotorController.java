package io.github.danielthedev.robot.motor;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalState;

import io.github.danielthedev.robot.Pin;

public class MotorController {
	
	private final DigitalOutput latchPin;
	private final DigitalOutput dataPin;
	private final DigitalOutput clockPin;
	private final DigitalOutput enablePin;
	
	//bitmask of the latches values
	private int latchState = 0;
	
	public MotorController(Context context, Pin latchPin, Pin dataPin, Pin clockPin, Pin enablePin) {
		this.latchPin = createPin(context, latchPin);
		this.dataPin = createPin(context, dataPin);
		this.clockPin = createPin(context, clockPin);
		this.enablePin = createPin(context, enablePin);
	}
	
	private void latch() {
		this.latchPin.low();
		this.dataPin.low();
		for(int t = 0; t < 2; t++) {
			for(MotorType motor : MotorType.values()) {
				this.clockPin.low();
				if(((latchState >> (t == 0 ? motor.getMotorBitPosForward() : motor.getMotorBitPosBackward())) & 0b00000001) == 1) {
					this.dataPin.high();
				} else {
					this.dataPin.low();
				}
				this.clockPin.high();
			} 
			this.latchPin.high();	
		}
	}
	
	private static DigitalOutput createPin(Context context, Pin pin) {
		return context.create(
				DigitalOutput.newConfigBuilder(context)
	                .id("MC-"+pin.getName())
	                .name(pin.getName())
	                .address(pin.getBCMAddress())
	                .shutdown(DigitalState.LOW)
	                .initial(DigitalState.LOW)
	                .provider("pigpio-digital-output"));
	}
	
	public void enable() {
		this.setLatchState(0);
		this.enablePin.low();
	}

	public int getLatchState() {
		return latchState;
	}
	
	public void setLatchState(int latchState) {
		this.latchState = latchState;
		this.latch();
	}

	public DigitalOutput getLatchPin() {
		return latchPin;
	}

	public DigitalOutput getDataPin() {
		return dataPin;
	}

	public DigitalOutput getClockPin() {
		return clockPin;
	}

	public DigitalOutput getEnablePin() {
		return enablePin;
	}
}


