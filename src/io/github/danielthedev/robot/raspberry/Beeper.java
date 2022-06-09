package io.github.danielthedev.robot.raspberry;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.pwm.Pwm;

import io.github.danielthedev.robot.util.Delay;

public class Beeper {

	
	private final Pwm beeper;
	
	public Beeper(Context context) {
		this.beeper = PinFactory.createPWMPin(context, PinRegistry.PIN_BEEPER, "beeper", 490);
	}
	
	public void beep(int miliseconds) {
		this.beeper.on(40);
		Delay.miliseconds(miliseconds);
		this.beeper.off();
	}
}
