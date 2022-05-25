package io.github.danielthedev.robot.raspberry.library.arduino;

public class ArduinoPacket {


	public static final int PACKET_SIZE = 4;
	
	private final int randomPacketId;
	
	private final int red;
	private final int green;
	private final int blue;
	
	private final int intensity;

	public ArduinoPacket(int randomPacketId, int red, int green, int blue, int intensity) {
		this.randomPacketId = randomPacketId;
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.intensity = intensity;
	}
	
	public static ArduinoPacket deseserialize(short[] rawpacket) {
		return new ArduinoPacket(rawpacket[0], rawpacket[1], rawpacket[2], rawpacket[3], rawpacket[4]);
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

	public int getIntensity() {
		return intensity;
	}

	public int getRandomPacketId() {
		return randomPacketId;
	}
	
}
