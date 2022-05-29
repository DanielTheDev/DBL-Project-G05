package io.github.danielthedev.robot.enums;

public enum DiskType {

	BLACK(100, 100, 100),
	WHITE(100, 100, 100);
	
	private final int red;
	private final int green;
	private final int blue;
	
	private DiskType(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	
	public static DiskType getDisk(short red, short green, short blue) {
		DiskType result = null;
		int differenceFactor = 10000;
		for(DiskType disk : values()) {
			int tempDifferenceFactor = 
					(Math.abs(red - disk.red) +
					Math.abs(green - disk.green) + 
					Math.abs(blue - disk.blue))/3;
			if(tempDifferenceFactor < differenceFactor) {
				result = disk;
			}
		}
		return result;
		
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
}
