package io.github.danielthedev.robot;

public class RobotException extends RuntimeException {

	private final int id;
	private final boolean fatal;
	
	public RobotException(int id, String description, boolean fatal) {
		super(description);
		this.id = id;
		this.fatal = fatal;
	}

	public int getId() {
		return id;
	}

	public boolean isFatal() {
		return fatal;
	}
}
