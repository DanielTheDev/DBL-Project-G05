package io.github.danielthedev.robot.raspberry.library.arduino;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalState;
import com.pi4j.io.gpio.digital.DigitalStateChangeEvent;
import com.pi4j.io.gpio.digital.DigitalStateChangeListener;

import io.github.danielthedev.robot.Robot;
import io.github.danielthedev.robot.enums.DiskType;
import io.github.danielthedev.robot.raspberry.Pin;
import io.github.danielthedev.robot.raspberry.PinFactory;

public class Arduino implements DigitalStateChangeListener {
	
	private final ArduinoListener listener;
	private final DigitalInput clockPin;
	private final DigitalInput dataPin;
	
	private int buffer;
	private int bufferIndex = 0;
	
	public Arduino(Context context, Pin clockPin, Pin dataPin, ArduinoListener listener) {
		this.listener = listener;
		this.clockPin = PinFactory.createInputPin(context, clockPin, "ARD");
		this.dataPin = PinFactory.createInputPin(context, dataPin, "ARD");
	}
	
	public void enable() {
		this.clockPin.addListener(this);
	}
	
	public void disable() {
		this.clockPin.removeListener(this);
	}
	
	@Override
	public void onDigitalStateChange(DigitalStateChangeEvent e) {
		int dat = this.dataPin.isHigh() ? 1 : 0;
		if(e.state() == DigitalState.HIGH) {
			System.out.println(dat);
			this.buffer <<= 1;
			this.buffer |= dat;
			if(this.bufferIndex == 1) {
				this.bufferIndex = -1;
				this.executeCommand();
			}
			this.bufferIndex++;
		}
	}

	public void executeCommand() {
		int opcode = this.buffer & 0b00000011;
		ArduinoCommandType command = ArduinoCommandType.getCommandType(opcode);
		switch (command) {
		case DETECT_BLACK_DISK:
			Robot.LOGGER.debug("Detected black disk");
			this.listener.onItemRead(DiskType.BLACK);
			break;
		case DETECT_WHITE_DISK:
			Robot.LOGGER.debug("Detected white disk");
			this.listener.onItemRead(DiskType.WHITE);
			break;
		case DETECT_DISK:
			Robot.LOGGER.debug("Detected a disk");
			this.listener.onItemDetect();
			break;
		case DETECT_FAILURE:
			Robot.LOGGER.debug("Detected failure");
			this.listener.onFailure();
			break;
		}
		
	}
}
