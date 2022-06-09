package io.github.danielthedev.robot.controllers;

import com.pi4j.context.Context;

import io.github.danielthedev.robot.DiskType;
import io.github.danielthedev.robot.ExceptionType;
import io.github.danielthedev.robot.Robot;
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
	
	public void moveItem(Robot robot) {
		switch (robot.getArduino().getDetectedDisk()) {
		case WHITE:
			this.moveLeft();
			break;
		case BLACK:
			this.moveRight();
			break;
		}
	}
	
	public void moveLeft() {
		this.motor.setState(MotorState.BACKWARD);
		this.dynamicDelay(Delay.WHITE_DISK_MOVE_DELAY);
		this.stop();
	}
	
	public void moveRight() {
		this.motor.setState(MotorState.FORWARD);
		this.dynamicDelay(Delay.BLACK_DISK_MOVE_DELAY);
		this.stop();
	}
	
	public void testBelt() {
		this.motor.setState(MotorState.BACKWARD);
		Robot.throwErrorIfNot(this.getButton().getState().waitForChange(Delay.CONVEYER_BELT_TIMEOUT), ExceptionType.BELT_STUCK);
		Delay.miliseconds(1000);
		this.motor.setState(MotorState.FORWARD);
		Robot.throwErrorIfNot(this.getButton().getState().waitForChange(Delay.CONVEYER_BELT_TIMEOUT), ExceptionType.BELT_STUCK);
		this.stop();
	}
	
	private void dynamicDelay(int delay) {
		while(delay > Delay.CONVEYER_BELT_TIMEOUT) {
			boolean success = this.getButton().getState().waitForChange(Delay.CONVEYER_BELT_TIMEOUT);
			Robot.throwErrorIf(!success, ExceptionType.BELT_STUCK);
			delay -= Delay.CONVEYER_BELT_TIMEOUT;
		}
		Delay.miliseconds(delay);
	}
	
	public void stop() {
		this.motor.setState(MotorState.RELEASE);
	}


	public Button getButton() {
		return button;
	}
	
}
