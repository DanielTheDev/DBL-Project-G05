package io.github.danielthedev.robot.controllers;

import java.util.stream.Stream;

import com.pi4j.context.Context;

import io.github.danielthedev.robot.raspberry.Button;
import io.github.danielthedev.robot.raspberry.ButtonListener;
import io.github.danielthedev.robot.raspberry.Pin;
import io.github.danielthedev.robot.raspberry.PinRegistry;
import io.github.danielthedev.robot.raspberry.library.motor.Motor;
import io.github.danielthedev.robot.raspberry.library.motor.MotorController;
import io.github.danielthedev.robot.raspberry.library.motor.MotorState;
import io.github.danielthedev.robot.raspberry.library.motor.MotorType;
import io.github.danielthedev.robot.util.Timeout;

public class ArmController implements ButtonListener { 
    
	private final Motor motor;
	private final Button button;
	
	private boolean isRetracted;
	
	public ArmController(Context context, MotorController motorController) {
		this.motor = new Motor(context, motorController, MotorType.MOTOR_2, PinRegistry.PIN_MOTOR_2);
		this.button = new Button(context, PinRegistry.PIN_ARM_BUTTON, this);
	}
	
	public void preinit() {
		this.retractArm();
	}

	public void extendArm() {
		this.motor.setState(MotorState.FORWARD);
	}
	
	public void retractArm() {
		//this.motor.setState(MotorState.BACKWARD);
		Timeout.createTimeoutPredicate(2000, v->!this.isRetracted, ()->{
			System.out.println("timeout");
		});		
		

	}
	
	
	@Override
	public void onButtonClick(Pin pin) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onButtonPress(Pin pin) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onButtonRelease(Pin pin) {
		// TODO Auto-generated method stub
		
	}
	
	
}
