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
			//Robot.forceShutdown();
		}
	}
	
	@Override
	public void uncaughtException(Thread thread, Throwable e) {
		this.robot.getBeeper().beep(150);
		Delay.miliseconds(50);
		this.robot.getBeeper().beep(150);
		try {
			if(e instanceof RobotException) {
				RobotException exception = (RobotException) e;
				ExceptionType type = ExceptionType.getById(exception.getId());
				this.robot.getLCDScreen().printError(type);
				switch (ExceptionType.getById(exception.getId())) {
				case BELT_STUCK:
					break;
				case FAILED_ARM_EXTEND:
					break;
				case FAILED_ARM_RETRACT:
					
					break;
				case FAILING_ARDUINO_SENSOR:
					break;
				case NOT_MAIN_THREAD:
					break;
				default:
					break;
				}
			} else {
				
			}
		} catch (Exception ee) {
			ee.printStackTrace();
		}
		Delay.miliseconds(2000);
		System.exit(0);
	}

}
