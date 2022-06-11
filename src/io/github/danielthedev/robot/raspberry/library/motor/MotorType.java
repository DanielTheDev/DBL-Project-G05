package io.github.danielthedev.robot.raspberry.library.motor;

public enum MotorType {

	MOTOR_1(1, 2, 3), MOTOR_2(2, 1, 4), MOTOR_3(3, 5, 7), MOTOR_4(4, 0, 6);

	private final int motorBitPosForward;
	private final int motorBitPosBackward;
	private final int motorID;

	MotorType(int motorID, int motorBitPosForward, int motorBitPosBackward) {
		this.motorID = motorID;
		this.motorBitPosForward = motorBitPosForward;
		this.motorBitPosBackward = motorBitPosBackward;
	}

	public int getMotorBitPosForward() {
		return motorBitPosForward;
	}

	public int getMotorBitPosBackward() {
		return motorBitPosBackward;
	}

	public int getMotorID() {
		return motorID;
	}
}
