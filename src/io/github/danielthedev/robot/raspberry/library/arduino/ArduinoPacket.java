package io.github.danielthedev.robot.raspberry.library.arduino;

import io.github.danielthedev.robot.util.DiskColor;

public class ArduinoPacket {

	public static final int PACKET_SIZE = 5;

	private final int uniquePacketId;
	private final DiskColor color;
	private final int distance;

	public ArduinoPacket(int uniquePacketId, short red, short green, short blue, short distance) {
		this.uniquePacketId = uniquePacketId;
		this.distance = distance;
		this.color = new DiskColor(red, green, blue);
	}

	public static ArduinoPacket deseserialize(short[] rawpacket) {
		return new ArduinoPacket(rawpacket[0], rawpacket[1], rawpacket[2], rawpacket[3], rawpacket[4]);
	}

	public DiskColor getColor() {
		return color;
	}

	public int getDistance() {
		return distance;
	}

	public int getUniquePacketId() {
		return uniquePacketId;
	}

	public boolean isChanged(ArduinoPacket t) {
		return this.uniquePacketId != t.uniquePacketId;
	}

	@Override
	public String toString() {
		return "ArduinoPacket [uniquePacketId=" + uniquePacketId + ", color=" + color + ", distance=" + distance + "]";
	}

}
