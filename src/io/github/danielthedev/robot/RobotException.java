package io.github.danielthedev.robot;

public class RobotException extends RuntimeException {

	private final int id;
	
	public RobotException(int id, String description) {
		super(description);
		this.id = id;
	}

	public int getId() {
		return id;
	}

}
