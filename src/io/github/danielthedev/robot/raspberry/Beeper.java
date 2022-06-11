package io.github.danielthedev.robot.raspberry;

import com.pi4j.context.Context;
import com.pi4j.io.pwm.Pwm;

import io.github.danielthedev.robot.util.Delay;

public class Beeper {

	private final Pwm beeper;

	public Beeper(Context context) {
		this.beeper = PinFactory.createPWMPin(context, PinRegistry.PIN_BEEPER, "beeper", 490);
	}
	
	public void playResetSound() {
		this.beep(250);
		Delay.miliseconds(50);
		this.beep(100);
		Delay.miliseconds(50);
		this.beep(100);
	}
	
	public void playErrorSound() {
		this.beep(150);
		Delay.miliseconds(50);
		this.beep(150);
	}

	public void beep(int miliseconds) {
		this.beeper.on(40);
		Delay.miliseconds(miliseconds);
		this.beeper.off();
	}

	public void beeplow(int miliseconds) {
		this.beeper.on(25);
		Delay.miliseconds(miliseconds);
		this.beeper.off();
	}
}
