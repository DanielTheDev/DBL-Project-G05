package io.github.danielthedev.robot;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pi4j.context.Context;

import io.github.danielthedev.robot.controllers.ArmController;
import io.github.danielthedev.robot.controllers.BeltController;
import io.github.danielthedev.robot.enums.DiskType;
import io.github.danielthedev.robot.raspberry.library.arduino.Arduino;
import io.github.danielthedev.robot.raspberry.library.arduino.ArduinoListener;
import io.github.danielthedev.robot.raspberry.library.motor.MotorController;
import io.github.danielthedev.robot.util.Delay;
import io.github.danielthedev.robot.util.ThreadCommunication;

public class Robot implements ArduinoListener {

	public static Logger LOGGER = LogManager.getLogger(Robot.class);
	public static ThreadCommunication threadCommunication = new ThreadCommunication();

	private final ArmController armController;
	private final BeltController beltController;
	private final MotorController motorController;
	private final Arduino arduinoListener;
	
	
	public Robot(Context context) {
		this.motorController = new MotorController(context);
		this.armController = new ArmController(context, motorController);
		this.beltController = new BeltController(context, motorController);
		this.arduinoListener = new Arduino(context, this);
	}

	public void start() {
		Robot.LOGGER.debug("Robot should start HERE");
		//this.preinit();
		
		//beltController.moveLeft();
		//Delay.miliseconds(3000);
		
		//beltController.moveRight();
			
		Delay.miliseconds(10000);
	}
	
	public void stop() {
		this.motorController.disable();
	}

	public void preinit() {
		this.armController.preinit();
		this.beltController.preinit();
	}

	@Override
	public void onItemDetect() {
		LOGGER.info("on detect");
	}

	@Override
	public void onArduinoFailure() {
		LOGGER.info("on failure");	
	}

	@Override
	public void onItemRead(DiskType type) {
		LOGGER.info("item read " + type);
	}
}
