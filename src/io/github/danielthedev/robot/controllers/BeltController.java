package io.github.danielthedev.robot.controllers;

import com.pi4j.context.Context;

import io.github.danielthedev.robot.ExceptionType;
import io.github.danielthedev.robot.Robot;
import io.github.danielthedev.robot.enums.DiskType;
import io.github.danielthedev.robot.raspberry.Button;
import io.github.danielthedev.robot.raspberry.PinRegistry;
import io.github.danielthedev.robot.raspberry.library.motor.Motor;
import io.github.danielthedev.robot.raspberry.library.motor.MotorController;
import io.github.danielthedev.robot.raspberry.library.motor.MotorState;
import io.github.danielthedev.robot.raspberry.library.motor.MotorType;
import io.github.danielthedev.robot.util.Delay;

public class BeltController {

	private final Button button;
	private final Motor motor;
	
	public BeltController(Context context, MotorController motorController) {
		this.motor = new Motor(context, motorController, MotorType.MOTOR_1, PinRegistry.PIN_MOTOR_1);
		this.button = new Button(context, PinRegistry.PIN_BELT_BUTTON);
		this.motor.setSpeed(100);
	}
	
	public void moveLeft() {
		this.motor.setState(MotorState.BACKWARD);
	}
	
	public void moveRight() {
		this.motor.setState(MotorState.FORWARD);
	}
	
	public void moveBelt(Robot robot) {
		Delay.miliseconds(1000);
		DiskType type = robot.getArduino().getDetectedDisk();
		int delay = type == DiskType.BLACK ? 2000 : 1000;
		int buttonInterval = 1000;
		Runnable belt = type == DiskType.BLACK ? this::moveLeft : this::moveRight;
		while(delay > 0) {
			belt.run();
			if(delay > buttonInterval) {
				long time = System.currentTimeMillis();
				boolean success = this.getButton().getState().waitForChange(buttonInterval);
				delay -= System.currentTimeMillis() - time;
				if(!success) {
					Robot.throwError(ExceptionType.BELT_STUCK);
				}
			} else {
				Delay.miliseconds(delay);
				delay = 0;
			}
		}
		this.stop();
	}

	public void stop() {
		this.motor.setState(MotorState.RELEASE);
	}

	public Button getButton() {
		return button;
	}
	
	
	
}
