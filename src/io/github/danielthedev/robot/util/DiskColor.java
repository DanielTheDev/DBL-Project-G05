package io.github.danielthedev.robot.util;

public class DiskColor {

	private final short red;
	private final short blue;
	private final short green;
	
	public DiskColor(short red, short blue, short green) {
		this.red = red;
		this.blue = blue;
		this.green = green;
	}
	
	public static DiskColor of(int red, int blue, int green) {
		return new DiskColor((short)red, (short)blue, (short)green);
	} 

	public short getRed() {
		return red;
	}

	public short getBlue() {
		return blue;
	}

	public short getGreen() {
		return green;
	}
	
	public int getColorDifferenceIndex(DiskColor disk) {
		return (Math.abs(red - disk.red) +
				Math.abs(green - disk.green) + 
				Math.abs(blue - disk.blue))/3;
	}

	public boolean isNull() {
		return (this.red == 0 && this.green == 0 && this.blue == 0) || (this.red < 0 || this.green < 0 || this.blue < 0);
	}
}
