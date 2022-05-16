package io.github.danielthedev.robot.motor;

public enum MotorState {

	FORWARD(true, false),
	BACKWARD(false, true),
	RELEASE(false, false);
	
	private final boolean forward;
	private final boolean backward;
	
	private MotorState(boolean forward, boolean backward) {
		this.forward = forward;
		this.backward = backward;
	}

	public boolean isForward() {
		return forward;
	}

	public boolean isBackward() {
		return backward;
	}
}
