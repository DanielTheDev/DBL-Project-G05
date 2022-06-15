package io.github.danielthedev.robot;

import io.github.danielthedev.robot.util.DiskColor;

public enum DiskType {

	BLACK(DiskColor.of(200, 200, 200)), WHITE(DiskColor.of(800, 800, 800)), RED(DiskColor.of(500, 300, 300)), GREEN(DiskColor.of(300, 500, 300));

	private final DiskColor color;

	private DiskType(DiskColor color) {
		this.color = color;
	}

	public static DiskType getDisk(DiskColor color) {
		DiskType result = null;
		int differenceFactor = 100000;
		for (DiskType disk : values()) {
			int tempDifferenceFactor = disk.getColor().getColorDifferenceIndex(color);
			if (tempDifferenceFactor < differenceFactor) {
				result = disk;
				differenceFactor = tempDifferenceFactor;
			}
		}
		return result;

	}

	public DiskColor getColor() {
		return color;
	}
}
