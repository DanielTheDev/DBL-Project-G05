package io.github.danielthedev.robot;

public enum ExceptionType {

	BELT_STUCK(0x01, "Belt is stuck"),
	NOT_MAIN_THREAD(0x02, "Function not called on main thread"),
	FAILING_ARDUINO_SENSOR(0x03, "Arduino sensor failed"),
	FAILED_ARM_EXTEND(0x04, "Arm could not extend"),
	FAILED_ARM_RETRACT(0x05, "Arm could not retract"),
	UNKNOWN_EXCEPTION(0x05, "Robot cannot continue");
	
	private final int id;
	private final String description;
	
	ExceptionType(int id, String description) {
		this.id = id;
		this.description = description;
	}

	public RobotException createException() {
		return new RobotException(this.id, this.description);
	}

	public int getId() {
		return id;
	}
	
	public String getDescription() {
		return description;
	}
	
	public static ExceptionType getById(int id) {
		for(ExceptionType type : values()) {
			if(type.id == id) return type;
		}
		return null;
	}
}
