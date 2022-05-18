package io.github.danielthedev.robot.controllers;

import com.pi4j.context.Context;

import static io.github.danielthedev.robot.PinRegistry.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.github.danielthedev.robot.logging.Log;
import io.github.danielthedev.robot.motor.MotorController;

public class Robot {
    
	public static Logger LOGGER = LogManager.getLogger(Robot.class);

	private final ArmController armController;
	private final BeltController beltController;
	private final MotorController motorController;
	
	public Robot(Context context) {
		this.motorController = new MotorController(context, PIN_MC_LATCH, PIN_MC_DATA, PIN_MC_CLOCK, PIN_MC_ENABLE);
		this.armController = new ArmController(context, this.motorController);
		this.beltController = new BeltController(context, this.motorController);
	}
	
	public void start() {
		
	}
	
	public void preinit() {
		
	}
}
