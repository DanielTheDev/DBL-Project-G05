package io.github.danielthedev.robot;

import java.lang.Thread.UncaughtExceptionHandler;

public class RobotExceptionHandler implements UncaughtExceptionHandler {

	private final Robot robot;
	
	public RobotExceptionHandler(Robot robot) {
		this.robot = robot;
	}
	
	public void startListening() {
		if(Robot.isMainThread()) {
			Thread.currentThread().setUncaughtExceptionHandler(this);
		} else {
			//Robot.forceShutdown();
		}
	}
	
	@Override
	public void uncaughtException(Thread thread, Throwable e) {
		try {
			if(e instanceof RobotException) {
				RobotException exception = (RobotException) e;
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
			System.exit(0);
		}
	}

}
