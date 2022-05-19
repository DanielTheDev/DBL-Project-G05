package io.github.danielthedev.robot.raspberry;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalState;
import com.pi4j.io.gpio.digital.DigitalStateChangeEvent;
import com.pi4j.io.gpio.digital.DigitalStateChangeListener;
import com.pi4j.io.gpio.digital.PullResistance;

public class Button implements DigitalStateChangeListener {

	private final DigitalInput button;
	private final Pin pin;
	private final ButtonListener listener;
	
	public Button(Context context, Pin pin, ButtonListener listener) {
		this.button = PinFactory.createInputPin(context, pin, "btn", PullResistance.PULL_UP);
		this.pin = pin;
		this.listener = listener;
		this.button.addListener(this);
	}
	
	public boolean isPressed() {
		return this.button.isHigh();
	}

	public boolean isReleased() {
		return this.button.isLow();
	}
	
	@Override
	public void onDigitalStateChange(DigitalStateChangeEvent e) {
		if(e.state() == DigitalState.HIGH) {
			this.listener.onButtonPress(this.pin);
		} else {
			this.listener.onButtonRelease(this.pin);
			this.listener.onButtonClick(this.pin);
		}
	}
}
