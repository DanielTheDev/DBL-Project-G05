package io.github.danielthedev.robot.raspberry;

public interface ButtonListener {

	void onButtonClick(Pin pin);
	
	void onButtonPress(Pin pin);
	
	void onButtonRelease(Pin pin);
	
}
