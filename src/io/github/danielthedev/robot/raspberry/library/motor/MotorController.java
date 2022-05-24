package io.github.danielthedev.robot.raspberry.library.motor;

import static io.github.danielthedev.robot.raspberry.PinRegistry.PIN_MC_CLOCK;
import static io.github.danielthedev.robot.raspberry.PinRegistry.PIN_MC_DATA;
import static io.github.danielthedev.robot.raspberry.PinRegistry.PIN_MC_ENABLE;
import static io.github.danielthedev.robot.raspberry.PinRegistry.PIN_MC_LATCH;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.library.pigpio.internal.PIGPIO;

import io.github.danielthedev.robot.raspberry.PinFactory;

public class MotorController {
	
	
	private final DigitalOutput latchPin;
	private final DigitalOutput dataPin;
	private final DigitalOutput clockPin;
	private final DigitalOutput enablePin;
	
	//bitmask of the latches values
	public int latchState = 0;
	
	public MotorController(Context context) {
		this.latchPin = PinFactory.createOutputPin(context, PIN_MC_LATCH, "MC");
		this.dataPin = PinFactory.createOutputPin(context, PIN_MC_DATA, "MC");
		this.clockPin = PinFactory.createOutputPin(context, PIN_MC_CLOCK, "MC");
		this.enablePin = PinFactory.createOutputPin(context, PIN_MC_ENABLE, "MC");
	}
	
	private void latch() {
		this.latchPin.low();
		this.dataPin.low();
		
		for(int t = 0; t < 8; t++) {
			this.delayMicroseconds(10);
			this.clockPin.low();
			
			if(((latchState >> (7-t)) & 0b00000001) == 1) {
				this.dataPin.high();
				System.out.print("1");
			} else {
				this.dataPin.low();
				System.out.print("0");
			}
			this.delayMicroseconds(10);
			this.clockPin.high();
		}
		
		this.latchPin.high();
		System.out.println();
	}
	

	
	public void delayMicroseconds(int microseconds) {
		PIGPIO.gpioDelay(microseconds);
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


