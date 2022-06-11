package io.github.danielthedev.robot.raspberry.library.arduino;

public enum ArduinoCommandType {

	DETECT_DISK(0x00), DETECT_BLACK_DISK(0x02), DETECT_WHITE_DISK(0x03), DETECT_FAILURE(0x01);

	private final int opcode;

	ArduinoCommandType(int opcode) {
		this.opcode = opcode;
	}

	public int getOpcode() {
		return opcode;
	}

	public static ArduinoCommandType getCommandType(int opcode) {
		for (ArduinoCommandType type : values()) {
			if (type.opcode == opcode)
				return type;
		}
		return null;
	}

}
