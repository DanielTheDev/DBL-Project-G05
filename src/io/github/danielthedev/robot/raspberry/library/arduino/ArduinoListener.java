package io.github.danielthedev.robot.raspberry.library.arduino;

import io.github.danielthedev.robot.DiskType;

public interface ArduinoListener {

	void onItemDetect();

	void onArduinoFailure();

	void onItemRead(DiskType type);

}
