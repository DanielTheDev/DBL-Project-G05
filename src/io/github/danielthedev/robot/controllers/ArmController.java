package io.github.danielthedev.robot.controllers;

import com.pi4j.context.Context;

import io.github.danielthedev.robot.ExceptionType;
import io.github.danielthedev.robot.Robot;
import io.github.danielthedev.robot.raspberry.Button;
import io.github.danielthedev.robot.raspberry.PinRegistry;
import io.github.danielthedev.robot.raspberry.library.motor.Motor;
import io.github.danielthedev.robot.raspberry.library.motor.MotorController;
import io.github.danielthedev.robot.raspberry.library.motor.MotorState;
import io.github.danielthedev.robot.raspberry.library.motor.MotorType;
import io.github.danielthedev.robot.util.Delay;

public class ArmController { 
    
	private final Motor motor;
	private final Button button;
	
	public ArmController(Context context, MotorController motorController) {
		this.motor = new Motor(context, motorController, MotorType.MOTOR_2, PinRegistry.PIN_MOTOR_2);
		this.button = new Button(context, PinRegistry.PIN_ARM_BUTTON);
		this.motor.setSpeed(100);
	}
	
	public void preinit() {
		this.retractArm();
	}
	
	public void stop() {
		this.motor.setState(MotorState.RELEASE);
	}

	public void extendArm() {
		this.motor.setState(MotorState.FORWARD);
	}
	
	public void retractArm() {
		this.motor.setState(MotorState.BACKWARD);
	}
	
	public void shutdown() {
		if(!this.button.isPressedSync()) {
			this.retractArm();
			if(!this.button.getState().waitForChange(2000)) {
				Robot.throwError(ExceptionType.BELT_STUCK);
			}
			this.stop();
		}
	}
	
	public void grabDisk() {
		this.extendArm();
		boolean success = this.getButton().getState().waitForChange(300);
		if(!success || this.getButton().isPressedSync()) {
			Robot.throwError(ExceptionType.FAILED_ARM_EXTEND);
		}
		Delay.miliseconds(500);
		this.stop();
		Delay.miliseconds(5000);
		this.retractArm();
		boolean success1 = this.getButton().getState().waitForChange(1000);
		if(!(success1 && this.getButton().isPressedSync())) {
			this.stop();
			Robot.throwError(ExceptionType.FAILED_ARM_RETRACT);
		} else {
			Delay.miliseconds(60);
			this.stop();
		}
	}

	public Button getButton() {
		return button;
	}
	
}
