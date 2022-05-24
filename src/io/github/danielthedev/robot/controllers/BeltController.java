package io.github.danielthedev.robot.controllers;

import com.pi4j.context.Context;

import io.github.danielthedev.robot.raspberry.PinRegistry;
import io.github.danielthedev.robot.raspberry.library.motor.Motor;
import io.github.danielthedev.robot.raspberry.library.motor.MotorController;
import io.github.danielthedev.robot.raspberry.library.motor.MotorState;
import io.github.danielthedev.robot.raspberry.library.motor.MotorType;
import io.github.danielthedev.robot.util.Delay;

public class BeltController {

	private final int rotationDuration = 1000;
	private final Motor motor;
	
	public BeltController(Context context, MotorController motorController) {
		this.motor = new Motor(context, motorController, MotorType.MOTOR_1, PinRegistry.PIN_MOTOR_1);
		this.motor.setSpeed(100);
	}
	
	public void moveLeft() {
		this.motor.setState(MotorState.BACKWARD);
		Delay.miliseconds((100*this.rotationDuration)/this.motor.getSpeed());
		this.stop();
	}
	
	public void moveRight() {
		this.motor.setState(MotorState.FORWARD);
		Delay.miliseconds((100*this.rotationDuration)/this.motor.getSpeed());
		this.stop();
	}
	
	public void stop() {
		this.motor.setState(MotorState.RELEASE);
	}
	
	
}
