package io.github.danielthedev.robot.util;

import com.pi4j.library.pigpio.internal.PIGPIO;

import io.github.danielthedev.robot.sequence.SequenceFunction;
import io.github.danielthedev.robot.sequence.SequenceType;

public class Delay extends SequenceFunction {

	public static final int CONVEYER_BELT_TIMEOUT = 1600;
	public static final int ARM_RETRACT_TIMEOUT = 1000;
	public static final int ARM_EXTEND_TIMEOUT = 300;
	public static final int ARM_HOLD_DELAY = 5000;
	public static final int ARM_RETRACT_POST_DELAY = 50;
	public static final int ARM_EXTEND_POST_DELAY = 350;
	public static final int ARM_GRAB_DELAY = 4000;
	public static final int CONVEYER_BELT_MOVE_DELAY = 1000;
	
	public static final int BLACK_DISK_MOVE_DELAY = 2000;
	public static final int WHITE_DISK_MOVE_DELAY = 1000;

	
	
	public Delay(SequenceType type, long miliseconds) {
		super(type, "Delay", (r)->miliseconds(miliseconds));
	}

	public static void miliseconds(long miliseconds) {
		if(miliseconds <= 0) return;
		try {
			Thread.sleep(miliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	

	public static void microseconds(long microseconds) {
		PIGPIO.gpioDelay(microseconds);
	}
	
}
