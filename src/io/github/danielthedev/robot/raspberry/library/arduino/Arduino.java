package io.github.danielthedev.robot.raspberry.library.arduino;

import com.pi4j.context.Context;
import com.pi4j.io.i2c.I2C;

import io.github.danielthedev.robot.ExceptionType;
import io.github.danielthedev.robot.Robot;
import io.github.danielthedev.robot.enums.DiskType;
import io.github.danielthedev.robot.raspberry.PinFactory;
import io.github.danielthedev.robot.raspberry.PinRegistry;
import io.github.danielthedev.robot.util.Delay;
import io.github.danielthedev.robot.util.DiskColor;

public class Arduino {

	private DiskType detectedDisk;
	private DiskColor defaultColor;
	private final I2C channel;

	public Arduino(Context context) {
		this.channel = PinFactory.createI2CChannel(context, PinRegistry.I2C_BUS, PinRegistry.I2C_DEVICE);
	}
	
	public void stop() {
		try {
			if(this.channel != null) {
				this.channel.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	
	public void detectDisk(Robot robot) {
		ArduinoPacket packet;
		do {
			packet = this.readNextPacket();
			if(packet.getColor().isNull()) Robot.throwError(ExceptionType.FAILING_ARDUINO_SENSOR);
			this.detectedDisk = DiskType.getDisk(packet.getColor(), this.defaultColor);
			
			//REMOVE
			this.detectedDisk = DiskType.BLACK;
			break;
		} while(this.detectedDisk == null);
		robot.getLCDScreen().clear();
		robot.getLCDScreen().printLine("Disk Detected", 0);
		robot.getLCDScreen().printLine(String.format("Match %d%% %s", 
				this.detectedDisk.getColor().getMatchPercentage(packet.getColor()),
				this.detectedDisk.name()), 1);
		Delay.miliseconds(1000); //CHANGE
	}
	
	public ArduinoPacket readNextPacket() {
		ArduinoPacket lastPacket = this.readPacket();
		while(true) {
			Delay.miliseconds(200);
			ArduinoPacket packet = this.readPacket();
			if(packet.isChanged(lastPacket)) return packet;
		}
	}
	
	public void calibrate() {
		Robot.verifyMainThread();
		int calibratedCount = 8;
		int red = 0;
		int green = 0;
		int blue = 0;
		ArduinoPacket packet = this.readPacket();
		ArduinoPacket nextPacket;
		if(packet.getColor().isNull()) Robot.throwError(ExceptionType.FAILING_ARDUINO_SENSOR);
		for(int x = 0; x < calibratedCount; x++) {
			Delay.miliseconds(200);
			do {
				nextPacket = this.readPacket();
				if(packet.getColor().isNull()) Robot.throwError(ExceptionType.FAILING_ARDUINO_SENSOR);
				Delay.miliseconds(50);
			} while(!nextPacket.isChanged(packet));
			red += nextPacket.getColor().getRed();
			green += nextPacket.getColor().getGreen();
			blue += nextPacket.getColor().getBlue();
		}
		this.defaultColor = DiskColor.of(red/calibratedCount, green/calibratedCount, blue/calibratedCount);
		Robot.LOGGER.info("Calibrated robot on " + this.defaultColor);

	}
	
	public ArduinoPacket readPacket() {		
		int size = Short.BYTES;
		short[] arr = new short[ArduinoPacket.PACKET_SIZE];
		byte[] bytes = new byte[size*arr.length];
		int s = this.channel.read(bytes);
		if(s == -83) {
			return null;
		}
		for(int t = 0; t < arr.length; t++) {
			short val = 0;
			for(int x = 0; x < 2; x++) {
				val |= (bytes[bytes.length-x-1-(size*t)] & 0xFF) << (8 * x);
			}
			arr[arr.length-1-t] = val;
		}
		return ArduinoPacket.deseserialize(arr);
	}

	public DiskType getDetectedDisk() {
		return this.detectedDisk;
	}


}
