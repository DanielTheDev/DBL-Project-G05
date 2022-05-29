package io.github.danielthedev.robot.handler;

import io.github.danielthedev.robot.Robot;

public class ErrorIdentifier {

	private final RecoveryManager recoveryManager;
	
	public ErrorIdentifier(Robot robot) {
		this.recoveryManager = new RecoveryManager(robot);
	}
	
}
