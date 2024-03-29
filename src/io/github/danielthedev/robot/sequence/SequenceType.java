package io.github.danielthedev.robot.sequence;

public enum SequenceType {

	STARTUP("Starting"), RUNTIME("Running"), SHUTDOWN("Stopping");

	private final String status;

	private SequenceType(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

}
