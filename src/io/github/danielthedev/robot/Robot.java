package io.github.danielthedev.robot;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.pi4j.context.Context;

import io.github.danielthedev.robot.controllers.ArmController;
import io.github.danielthedev.robot.controllers.BeltController;
import io.github.danielthedev.robot.raspberry.Button;
import io.github.danielthedev.robot.raspberry.ButtonListener;
import io.github.danielthedev.robot.raspberry.Pin;
import io.github.danielthedev.robot.raspberry.library.motor.MotorController;

public class Robot implements ButtonListener {

	public static Logger LOGGER = LogManager.getLogger(Robot.class);

	private final ArmController armController;
	private final BeltController beltController;
	private final MotorController motorController;

	public Robot(Context context) {
		this.motorController = new MotorController(context);
		this.armController = new ArmController(context, this.motorController);
		this.beltController = new BeltController(context, this.motorController);
	}

	public void start() {
		this.preinit();
	}

	public void preinit() {
		this.armController.preinit();
	}

	@Override
	public void onButtonClick(Pin pin) {

	}

	@Override
	public void onButtonPress(Pin pin) {
		
	}

	@Override
	public void onButtonRelease(Pin pin) {
		
	}
}
