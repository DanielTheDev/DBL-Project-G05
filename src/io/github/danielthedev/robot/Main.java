package io.github.danielthedev.robot;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;

public class Main {

	public static void main(String[] args) {
		new Main().startRobot();
	}

	public void startRobot() {
		Robot.LOGGER.info("Startup library");
		Context pi4j = Pi4J.newAutoContext();
		Robot robot = new Robot(pi4j, () -> {
			Robot.LOGGER.info("Shutdown listener called");
			if (pi4j != null) {
				Robot.LOGGER.info("Shutdown library");
				pi4j.shutdown();
			}
			new Thread(this::startRobot).start();
		});
		robot.start();
	}

}
