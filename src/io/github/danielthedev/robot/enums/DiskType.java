package io.github.danielthedev.robot.enums;

import io.github.danielthedev.robot.util.DiskColor;

public enum DiskType {

	BLACK(DiskColor.of(500, 500, 500)),
	WHITE(DiskColor.of(1500, 1500, 1500));
	
	private final DiskColor color;
	
	private DiskType(DiskColor color) {
		this.color = color;
	}
	
	public static DiskType getDisk(DiskColor color, DiskColor defaultColor) {
		DiskType result = null;
		int differenceFactor = color.getColorDifferenceIndex(defaultColor);
		for(DiskType disk : values()) {
			int tempDifferenceFactor = disk.getColor().getColorDifferenceIndex(color);
			if(tempDifferenceFactor < differenceFactor) {
				result = disk;
			}
		}
		return result;
		
	}

	public DiskColor getColor() {
		return color;
	}
}
