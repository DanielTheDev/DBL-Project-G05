package io.github.danielthedev.robot.arduino;

public interface ArduinoListener {

	void onItemDetect();
	
	void onFailure();
	
	void onItemRead(DiskType type);
	
}
