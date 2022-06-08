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
		this.robot.stop();
		this.robot.getBeeper().beep(150);
		Delay.miliseconds(50);
		this.robot.getBeeper().beep(150);
		try {
			if(e instanceof RobotException) {
				RobotException exception = (RobotException) e;
				ExceptionType type = ExceptionType.getById(exception.getId());
				this.robot.getLCDScreen().printError(type);
				boolean recovered = false;
				switch (ExceptionType.getById(exception.getId())) {
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
				case FAILED_ARM_EXTEND:
					break;
				case FAILED_ARM_RETRACT:
					break;
				case FAILING_ARDUINO_SENSOR:
					this.robot.shutdown();
					break;
				case NOT_MAIN_THREAD:
					this.robot.shutdown();
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
			} else {
				this.robot.getLCDScreen().printError(ExceptionType.UNKNOWN_EXCEPTION);
				this.robot.shutdown();
				e.printStackTrace();
			}
		} catch (Exception ee) {
			ee.printStackTrace();
		}
	}

}
