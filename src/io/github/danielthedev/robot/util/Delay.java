package io.github.danielthedev.robot.util;

import com.pi4j.library.pigpio.internal.PIGPIO;

public class Delay {

	public static void miliseconds(long mili) {
		try {
			Thread.sleep(mili);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public static void microseconds(int microseconds) {
		PIGPIO.gpioDelay(microseconds);
	}
}
