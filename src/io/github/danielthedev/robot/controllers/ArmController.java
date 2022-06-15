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

public class ArmController {

	private final Motor motor;
	private final Button button;

	public ArmController(Context context, MotorController motorController) {
		this.motor = new Motor(context, motorController, MotorType.MOTOR_2, PinRegistry.PIN_MOTOR_2);
		this.button = new Button(context, PinRegistry.PIN_ARM_BUTTON);
		this.motor.setSpeed(100);
	}

	public void start() {
		this.retractArm();
	}

	public void stop() {
		this.motor.setState(MotorState.RELEASE);
	}

	public void moveBackward() {
		this.motor.setState(MotorState.BACKWARD);
	}

	public void moveForward() {
		this.motor.setState(MotorState.FORWARD);
	}

	public void retractArm() {
		if (!this.getButton().isPressedSync()) {
			this.moveBackward();
			boolean success = this.getButton().getState().waitForChange(Delay.ARM_RETRACT_TIMEOUT);
			Robot.throwErrorIfNot(success, ExceptionType.FAILED_ARM_RETRACT);
			Delay.miliseconds(Delay.ARM_RETRACT_POST_DELAY);
			this.stop();
		}
	}

	public void extendArm() {
		if (!this.getButton().isPressedAsync()) {
			this.retractArm();
		}
		this.moveForward();
		boolean success = this.getButton().getState().waitForChange(Delay.ARM_EXTEND_TIMEOUT);
		Robot.throwErrorIf(!success || this.getButton().isPressedSync(), ExceptionType.FAILED_ARM_EXTEND);
		Delay.miliseconds(Delay.ARM_EXTEND_POST_DELAY);
		this.stop();
	}

	public void grabDisk(Robot robot) {
		if(robot.getArduino().getDetectedDisk() == DiskType.BLACK || robot.getArduino().getDetectedDisk() == DiskType.WHITE) {
			this.extendArm();
			Delay.miliseconds(Delay.ARM_HOLD_DELAY);
			this.retractArm();
		}
	}

	public void testArm() {
		this.extendArm();
		this.retractArm();
	}

	public Button getButton() {
		return button;
	}

}
