package io.github.danielthedev.robot.raspberry.library.arduino;

import io.github.danielthedev.robot.enums.DiskType;

public interface ArduinoListener {

	void onItemDetect();
	
	void onFailure();
	
	void onItemRead(DiskType type);
	
}
