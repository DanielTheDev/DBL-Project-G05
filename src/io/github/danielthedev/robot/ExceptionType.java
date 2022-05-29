package io.github.danielthedev.robot;

public enum ExceptionType {

	BELT_STUCK(0x01, "Belt is stuck");
	
	private final int id;
	private final String description;
	private final boolean fatal;
	
	ExceptionType(int id, String description) {
		this(id, description, false);
	}
	
	ExceptionType(int id, String description, boolean fatal) {
		this.id = id;
		this.fatal = fatal;
		this.description = description;
	}
	
	public RobotException createException() {
		return new RobotException(this.id, this.description, this.fatal);
	}
	
}
