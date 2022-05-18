package io.github.danielthedev.robot.motor;

import com.pi4j.context.Context;
import com.pi4j.io.pwm.Pwm;
import com.pi4j.io.pwm.PwmType;

import io.github.danielthedev.robot.Pin;
import io.github.danielthedev.robot.PinFactory;

public class Motor {

	public static final int FREQUENCEY = 39000;
	
	public final MotorType type;
	public final MotorController controller; 
	public final Pwm pwmPin;
	public MotorState state;
	public int speed;
	
	public Motor(Context context, MotorController controller, MotorType type, Pin pwnPin) {
		this.type = type;
		this.controller = controller;
		this.pwmPin = PinFactory.createPWMPin(context, pwnPin, "Motor", FREQUENCEY);
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
			System.out.println("on");
			this.pwmPin.on(speed, FREQUENCEY);
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
}
