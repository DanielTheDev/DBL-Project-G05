package io.github.danielthedev.robot;

import io.github.danielthedev.robot.util.DiskColor;

public enum DiskType {

	BLACK(DiskColor.of(270, 290, 280)), WHITE(DiskColor.of(550, 800, 850));

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
		System.out.println(result);
		return result;

	}

	public DiskColor getColor() {
		return color;
	}
}
