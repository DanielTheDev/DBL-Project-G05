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
		this.motor.setSpeed(100);
	}
	
	public void preinit() {
		this.retractArm();
	}

	public void extendArm() {
		Robot.LOGGER.debug("Extending Arm");
		this.motor.setState(MotorState.FORWARD);
		Delay.miliseconds(450);
		this.motor.setState(MotorState.RELEASE);
	}
	
	public void retractArm() {
		if(this.button.isReleased()) {
			this.motor.setState(MotorState.BACKWARD);
			Robot.threadCommunication.waitButtonPress(3000, ()->{
				this.motor.setState(MotorState.RELEASE);
				Robot.LOGGER.debug("ERROR ARM DOES NOT RETRACT");
			}, ()->{
				Delay.miliseconds(60);
				this.motor.setState(MotorState.RELEASE);
				Robot.LOGGER.debug("Retracted");
			});
		}
	}
	
	
	@Override
	public void onButtonClick(Pin pin) {}


	@Override
	public void onButtonPress(Pin pin) {
		this.isRetracted = true;
		Robot.threadCommunication.fireButtonPress();
		Robot.LOGGER.debug("Arm is retracted");
	}


	@Override
	public void onButtonRelease(Pin pin) {
		this.isRetracted = false;
		Robot.LOGGER.debug("Arm is no longer retracted");
	}
}
