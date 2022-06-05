package io.github.danielthedev.robot.enums;

import io.github.danielthedev.robot.util.DiskColor;

public enum DiskType {

	BLACK(DiskColor.of(97, 138, 153)),
	GREEN(DiskColor.of(206, 409, 290)),
	WHITE(DiskColor.of(520, 765, 827));

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
