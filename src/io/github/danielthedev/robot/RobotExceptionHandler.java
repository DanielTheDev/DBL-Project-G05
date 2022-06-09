package io.github.danielthedev.robot;

import java.lang.Thread.UncaughtExceptionHandler;

import io.github.danielthedev.robot.util.Delay;

public class RobotExceptionHandler implements UncaughtExceptionHandler {

	private final Robot robot;
	
	public RobotExceptionHandler(Robot robot) {
		this.robot = robot;
	}
	
	public void startListening() {
		if(Robot.isMainThread()) {
			Robot.LOGGER.info("Started exception listener");
			Thread.currentThread().setUncaughtExceptionHandler(this);
		} else {
			Robot.LOGGER.fatal("Robot could not set exceptionListener");
			System.exit(1);
		}
	}
	
	@Override
	public void uncaughtException(Thread thread, Throwable e) {
		this.robot.stop();
		ExceptionType type;
		if(e instanceof RobotException) {
			RobotException exception = (RobotException) e;
			type = ExceptionType.getById(exception.getId());
		} else {
			type = ExceptionType.UNKNOWN_EXCEPTION;
			Robot.LOGGER.catching(e);
		}
		if(type != ExceptionType.INTERUPT_RESET_EXCEPTION) {
			this.robot.getBeeper().beep(150);
			Delay.miliseconds(50);
			this.robot.getBeeper().beep(150);
		} else {
			this.robot.getBeeper().beep(250);
			Delay.miliseconds(50);
			this.robot.getBeeper().beep(100);
			Delay.miliseconds(50);
			this.robot.getBeeper().beep(100);
			this.robot.getLCDScreen().print("Reset initialized");
			Delay.miliseconds(2000);
			this.robot.shutdown();
			return;
		}
		
		
		this.robot.getLCDScreen().printError(type);
		boolean recovered = false;
		switch (type) {
		case BELT_STUCK:
			for(int x = 0; x < 3; x++) {
				boolean success = true;
				try {
					this.robot.getBeltController().moveLeft();
					this.robot.getBeltController().moveRight();
				} catch (Exception ee) {
					success = false;
				}
				if(success) {
					recovered = true;
					break;
				}
			}
			break;
		case FAILED_ARM_EXTEND: case FAILED_ARM_RETRACT:
			for(int x = 0; x < 3; x++) {
				boolean success = true;
				try {
					this.robot.getArmController().extendArm();
					this.robot.getArmController().retractArm();
				} catch (Exception ee) {
					success = false;
				}
				if(success) {
					recovered = true;
					break;
				}
			}
			break;
		case NOT_MAIN_THREAD: case UNKNOWN_EXCEPTION: case FAILING_ARDUINO_SENSOR:
			Robot.LOGGER.catching(e);
			break;
		case INTERUPT_RESET_EXCEPTION:
			break;
		default:
			break;
		}
		
		if(recovered) {
			this.robot.getLCDScreen().print("Successfully recovered robot");
			Delay.miliseconds(2000);
			this.robot.continueExecution();
		} else {
			this.robot.getLCDScreen().print("Failed to recover robot");
			Delay.miliseconds(2000);
			this.robot.shutdown();
		}
	}

}
