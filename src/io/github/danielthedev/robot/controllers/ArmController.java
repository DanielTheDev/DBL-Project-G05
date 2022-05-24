package io.github.danielthedev.robot.controllers;

import com.pi4j.context.Context;

import io.github.danielthedev.robot.Robot;
import io.github.danielthedev.robot.raspberry.Button;
import io.github.danielthedev.robot.raspberry.ButtonListener;
import io.github.danielthedev.robot.raspberry.Pin;
import io.github.danielthedev.robot.raspberry.PinRegistry;
import io.github.danielthedev.robot.raspberry.library.motor.Motor;
import io.github.danielthedev.robot.raspberry.library.motor.MotorController;
import io.github.danielthedev.robot.raspberry.library.motor.MotorState;
import io.github.danielthedev.robot.raspberry.library.motor.MotorType;
import io.github.danielthedev.robot.util.Delay;

public class ArmController implements ButtonListener { 
    
	private final Motor motor;
	private final Button button;
	
	private boolean isRetracted;
	
	public ArmController(Context context, MotorController motorController) {
		this.motor = new Motor(context, motorController, MotorType.MOTOR_1, PinRegistry.PIN_MOTOR_1);
		this.button = new Button(context, PinRegistry.PIN_ARM_BUTTON, this);
	}
	
	public void preinit() {
		this.retractArm();
	}

	public void extendArm() {
		this.motor.setState(MotorState.FORWARD);
		Delay.miliseconds(500);
	}
	
	public void retractArm() {
		this.motor.setState(MotorState.BACKWARD);
	}
	
	
	@Override
	public void onButtonClick(Pin pin) {}


	@Override
	public void onButtonPress(Pin pin) {
		this.isRetracted = true;
		Robot.LOGGER.debug("Arm is retracted");
	}


	@Override
	public void onButtonRelease(Pin pin) {
		this.isRetracted = false;
		Robot.LOGGER.debug("Arm is no longer retracted");
	}
}
