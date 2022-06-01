package io.github.danielthedev.robot.util;

import com.pi4j.library.pigpio.internal.PIGPIO;

import io.github.danielthedev.robot.Robot;

public class Delay {

	public static void miliseconds(long mili) {
		try {
			Robot.LOGGER.debug("Waiting a delay of " + mili + " miliseconds");
			Thread.sleep(mili);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	

	public static void microseconds(int microseconds) {
		PIGPIO.gpioDelay(microseconds);
	}
}
