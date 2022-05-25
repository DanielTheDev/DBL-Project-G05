package io.github.danielthedev.robot.raspberry.library.arduino;

public class ArduinoPacket {

	public static final int PACKET_SIZE = 4;
	
	private final int uniquePacketId;
	private final int red;
	private final int green;
	private final int blue;

	public ArduinoPacket(int uniquePacketId, int red, int green, int blue) {
		this.uniquePacketId = uniquePacketId;
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	
	public static ArduinoPacket deseserialize(short[] rawpacket) {
		return new ArduinoPacket(rawpacket[0], rawpacket[1], rawpacket[2], rawpacket[3]);
	}

	public int getRed() {
		return red;
	}

	public int getGreen() {
		return green;
	}

	public int getBlue() {
		return blue;
	}
	
	public int getUniquePacketId() {
		return uniquePacketId;
	}

	@Override
	public String toString() {
		return "ArduinoPacket [uniquePacketId=" + uniquePacketId + ", red=" + red + ", green=" + green + ", blue="
				+ blue + "]";
	}

	
	
}
