package io.github.danielthedev.robot;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pi4j.context.Context;

import io.github.danielthedev.robot.controllers.ArmController;
import io.github.danielthedev.robot.controllers.BeltController;
import io.github.danielthedev.robot.raspberry.library.motor.MotorController;

public class Robot {

	public static Logger LOGGER = LogManager.getLogger(Robot.class);

	private final ArmController armController;
	private final BeltController beltController;

	public Robot(Context context) {
		MotorController motorController = new MotorController(context);
		this.armController = new ArmController(context, motorController);
		this.beltController = new BeltController(context, motorController);
	}

	public void start() {
		this.preinit();
	}

	public void preinit() {
		this.armController.preinit();
	}
}
