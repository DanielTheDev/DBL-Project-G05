package io.github.danielthedev.robot.raspberry.library.motor;

public enum MotorState {

	FORWARD(true, false), BACKWARD(false, true), RELEASE(false, false);

	private final boolean forward;
	private final boolean backward;

	MotorState(boolean forward, boolean backward) {
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
