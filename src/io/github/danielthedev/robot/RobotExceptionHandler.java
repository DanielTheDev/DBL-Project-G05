package io.github.danielthedev.robot;

import java.lang.Thread.UncaughtExceptionHandler;

import io.github.danielthedev.robot.util.Delay;

public class RobotExceptionHandler implements UncaughtExceptionHandler {

	private final Robot robot;

	public RobotExceptionHandler(Robot robot) {
		this.robot = robot;
	}

	public void startListening() {
		if (Robot.isMainThread()) {
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
		if (e instanceof RobotException) {
			RobotException exception = (RobotException) e;
			type = ExceptionType.getById(exception.getId());
		} else {
			type = ExceptionType.UNKNOWN_EXCEPTION;
			e.printStackTrace();
			Robot.LOGGER.catching(e);
		}
		Robot.LOGGER.info(type);

		if (type == ExceptionType.INTERUPT_RESET_EXCEPTION) {
			this.robot.getBeeper().playResetSound();
			this.robot.getLCDScreen().print("Reset has been", "initialized");
			Delay.miliseconds(2000);
			this.robot.shutdown(false);
		} else {
			this.robot.getBeeper().playErrorSound();
			this.robot.getLCDScreen().printError(type);
			boolean recovered = false;
			switch (type) {
				case BELT_STUCK:
					for (int x = 0; x < 3; x++) {
						if (Robot.executeSafe(this.robot.getBeltController()::moveLeft,
								this.robot.getBeltController()::moveRight)) {
							recovered = true;
							break;
						}
					}
					break;
				case FAILED_ARM_EXTEND:
				case FAILED_ARM_RETRACT:
					for (int x = 0; x < 3; x++) {
						if (Robot.executeSafe(this.robot.getArmController()::extendArm,
								this.robot.getArmController()::retractArm)) {
							recovered = true;
							break;
						}
					}
					break;
				default:
					break;
			}
			this.robot.getLCDScreen().print(recovered ? new String[] {"Successfully", "recovered robot"} : new String[] {"Failed to", "recover robot"});
			Delay.miliseconds(2000);

			if (recovered) {
				this.robot.safeRestartThread(this.robot::continueExecution);
			} else {
				this.robot.shutdown(true);
			}
		}
	}

}
