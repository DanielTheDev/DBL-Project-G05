package io.github.danielthedev.robot.arduino;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalState;
import com.pi4j.io.gpio.digital.DigitalStateChangeEvent;
import com.pi4j.io.gpio.digital.DigitalStateChangeListener;

import io.github.danielthedev.robot.Pin;

public class Arduino implements DigitalStateChangeListener {
	
	private final ArduinoListener listener;
	private final DigitalInput clockPin;
	private final DigitalInput dataPin;
	
	private int buffer;
	private int bufferIndex = 0;
	
	public Arduino(Context context, Pin clockPin, Pin dataPin, ArduinoListener listener) {
		this.listener = listener;
		this.clockPin = createPin(context, clockPin);
		this.dataPin = createPin(context, dataPin);
	}
	
	public void enable() {
		this.clockPin.addListener(this);
	}
	
	public void disable() {
		this.clockPin.removeListener(this);
	}
	
	@Override
	public void onDigitalStateChange(DigitalStateChangeEvent e) {
		boolean dat = this.dataPin.isHigh();
		if(e.state() == DigitalState.HIGH) {
			this.buffer <<= 1;
			this.buffer |= dat ? 1 : 0;
			if(this.bufferIndex == 1) {
				this.bufferIndex = -1;
				this.executeCommand();
			}
			this.bufferIndex++;
		}
	}

	private void executeCommand() {
		int opcode = this.buffer & 0x03;
		ArduinoCommandType command = ArduinoCommandType.getCommandType(opcode);
		switch (command) {
		case DETECT_BLACK_DISK:
			this.listener.onItemRead(DiskType.BLACK);
			break;
		case DETECT_WHITE_DISK:
			this.listener.onItemRead(DiskType.WHITE);
			break;
		case DETECT_DISK:
			this.listener.onItemDetect();
			break;
		case DETECT_FAILURE:
			this.listener.onFailure();
			break;
		}
		
	}

	private static DigitalInput createPin(Context context, Pin pin) {
		return context.create(DigitalInput.newConfigBuilder(context).id("Arduino-" + pin.getName()).name(pin.getName())
				.address(pin.getBCMAddress())
				.provider("pigpio-digital-input"));
	}

}
