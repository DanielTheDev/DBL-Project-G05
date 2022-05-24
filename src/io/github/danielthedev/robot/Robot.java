package io.github.danielthedev.robot;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pi4j.context.Context;

import io.github.danielthedev.robot.controllers.ArmController;
import io.github.danielthedev.robot.controllers.BeltController;
import io.github.danielthedev.robot.raspberry.library.motor.MotorController;
import io.github.danielthedev.robot.util.Delay;

public class Robot {

	public static Logger LOGGER = LogManager.getLogger(Robot.class);

	private final ArmController armController;
	private final BeltController beltController;
	private final MotorController motorController;

	public Robot(Context context) {
		this.motorController = new MotorController(context);
		this.armController = new ArmController(context, motorController);
		this.beltController = new BeltController(context, motorController);
	}

	public void start() {
		Robot.LOGGER.debug("Robot should start HERE");
		this.preinit();
		
		beltController.moveLeft();
		Delay.miliseconds(3000);
		
		beltController.moveRight();
		
		Delay.miliseconds(1000);
	}
	
	public void stop() {
		this.motorController.disable();
	}

	public void preinit() {
		//this.armController.preinit();
	}
}
