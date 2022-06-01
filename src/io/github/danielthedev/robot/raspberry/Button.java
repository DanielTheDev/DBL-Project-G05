package io.github.danielthedev.robot.raspberry;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalState;
import com.pi4j.io.gpio.digital.DigitalStateChangeEvent;
import com.pi4j.io.gpio.digital.DigitalStateChangeListener;
import com.pi4j.io.gpio.digital.PullResistance;

import io.github.danielthedev.robot.Robot;
import io.github.danielthedev.robot.sync.SyncVariable;

public class Button implements DigitalStateChangeListener {

	private final DigitalInput button;
	private final SyncVariable<Boolean> state;
	
	public Button(Context context, Pin pin) {
		this.button = PinFactory.createInputPin(context, pin, "btn", PullResistance.PULL_UP);
		this.state = new SyncVariable<Boolean>(this.button.isHigh());
		this.button.addListener(this);
	}
	
	public boolean isPressedSync() {
		Robot.verifyMainThread();
		return this.state.getSync();
	}
	
	public boolean isPressedAsync() {
		return this.button.isHigh();
	}

	@Override
	public void onDigitalStateChange(DigitalStateChangeEvent e) {
		this.state.putSync(e.state() == DigitalState.HIGH);
	}

	public SyncVariable<Boolean> getState() {
		return state;
	}
}
