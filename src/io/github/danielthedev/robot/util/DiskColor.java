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
	
	public short getMatchPercentage(DiskColor color) { 
		int max = 600;
		return (short) (100*(1 - ((double)this.getColorDifferenceIndex(color)/max)));
	}
	
	public int getColorDifferenceIndex(DiskColor color) {
		return (Math.abs(red - color.red) +
				Math.abs(green - color.green) + 
				Math.abs(blue - color.blue))/3;
	}

	public boolean isNull() {
		return (this.red == 0 && this.green == 0 && this.blue == 0) || (this.red < 0 || this.green < 0 || this.blue < 0);
	}

	@Override
	public String toString() {
		return "(red=" + red + ", blue=" + blue + ", green=" + green + ")";
	}
	
	
}
