package io.github.danielthedev.robot.arduino;

import io.github.danielthedev.robot.DiskType;

public interface ArduinoListener {

	void onItemDetect();
	
	void onFailure();
	
	void onItemRead(DiskType type);
	
}
