package io.github.danielthedev.robot.raspberry;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalOutput;

import io.github.danielthedev.robot.util.Delay;

public class Beeper {

	private final DigitalOutput beeper;
	
	public Beeper(Context context) {
		this.beeper = PinFactory.createOutputPin(context, PinRegistry.PIN_BEEPER, "beeper");
	}
	
	public void beep(int miliseconds) {
		this.beeper.high();
		Delay.miliseconds(miliseconds);
		this.beeper.low();
	}
}
