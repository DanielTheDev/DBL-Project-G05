package io.github.danielthedev.robot.motor;

import com.pi4j.context.Context;
import com.pi4j.io.pwm.Pwm;
import com.pi4j.io.pwm.PwmType;

import io.github.danielthedev.robot.Pin;

public class Motor {

	private static final int FREQUENCEY = 39000;
	
	private final MotorType type;
	private final MotorController controller; 
	private final Pwm pwmPin;
	private MotorState state;
	private int speed;
	
	public Motor(Context context, MotorController controller, MotorType type, Pin pwnPin) {
		this.type = type;
		this.controller = controller;
		this.pwmPin = createPin(context, pwnPin);
		this.controller.enable();
	}
	
	public void setState(MotorState state) {
		int latchState = this.controller.getLatchState();
		
		if(state.isForward()) {
			latchState |= (0b00000001 << this.type.getMotorBitPosForward());
		} else {
			latchState &= ~(0b00000001 << this.type.getMotorBitPosBackward());
		}
		
		if(state.isBackward()) {
			latchState |= (0b00000001 << this.type.getMotorBitPosBackward());
		} else {
			latchState &= ~(0b00000001 << this.type.getMotorBitPosBackward());
		}
		
		this.controller.setLatchState(latchState);
		this.state = state;
	}
	
	public void setSpeed(int speed) {
		this.speed = speed;
		if(speed <= 0 && speed > 100) {
			this.pwmPin.off();
		} else {
			this.pwmPin.on(250, FREQUENCEY);
		}
	}

	public void start() {
		this.setSpeed(100);
	}
	
	public void stop() {
		this.setSpeed(0);
	}

	public int getSpeed() {
		return speed;
	}

	public MotorType getType() {
		return type;
	}

	public MotorState getState() {
		return state;
	}
	
	private static Pwm createPin(Context context, Pin pin) {
		Pwm pwmPin = context.create(Pwm.newConfigBuilder(context)
	                .id("Motor-"+pin.getName())
	                .name(pin.getName())
	                .address(pin.getBCMAddress())
	                .pwmType(PwmType.SOFTWARE)
	                .initial(0)
	                .shutdown(0)
	                .provider("pigpio-pwm")
	                .build());
		pwmPin.setFrequency(FREQUENCEY);
		return pwmPin;
	}
}
