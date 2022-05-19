package io.github.danielthedev.robot.controllers;

import com.pi4j.context.Context;

import io.github.danielthedev.robot.raspberry.PinRegistry;
import io.github.danielthedev.robot.raspberry.library.motor.Motor;
import io.github.danielthedev.robot.raspberry.library.motor.MotorController;
import io.github.danielthedev.robot.raspberry.library.motor.MotorType;

public class BeltController {

	private final Motor motor;
	
	public BeltController(Context context, MotorController motorController) {
		this.motor = new Motor(context, motorController, MotorType.MOTOR_1, PinRegistry.PIN_MOTOR_1);
	}
	
	
	
}
