package io.github.danielthedev.robot.raspberry.library.arduino;

import com.pi4j.context.Context;
import com.pi4j.io.i2c.I2C;

import io.github.danielthedev.robot.DiskType;
import io.github.danielthedev.robot.ExceptionType;
import io.github.danielthedev.robot.Robot;
import io.github.danielthedev.robot.raspberry.PinFactory;
import io.github.danielthedev.robot.raspberry.PinRegistry;
import io.github.danielthedev.robot.util.Delay;
import io.github.danielthedev.robot.util.DiskColor;

public class Arduino {

	private DiskType detectedDisk;
	private final I2C channel;

	public Arduino(Context context) {
		this.channel = PinFactory.createI2CChannel(context, PinRegistry.I2C_BUS, PinRegistry.I2C_DEVICE);
	}

	public void stop() {
		try {
			if (this.channel != null) {
				this.channel.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void detectDisk(Robot robot) {
		ArduinoPacket packet;
		this.detectedDisk = null;
		do {
			packet = this.readNextPacket();
			if (packet.getColor().isNull())
				Robot.throwError(ExceptionType.FAILING_ARDUINO_SENSOR);
			if (packet.getDistance() < 40) {
				robot.getLCDScreen().print("Disk detected");
				Delay.miliseconds(Delay.DISK_READ_DELAY);
				int iterations = 4;
				int red = 0;
				int green = 0;
				int blue = 0;
				for (int x = 0; x < iterations; x++) {
					DiskColor color = packet.getColor();
					red += color.getRed();
					green += color.getGreen();
					blue += color.getBlue();
					packet = this.readNextPacket();
					if (packet.getColor().isNull())
						Robot.throwError(ExceptionType.FAILING_ARDUINO_SENSOR);
				}
				DiskColor color = DiskColor.of(red / iterations, blue / iterations, green / iterations);
				this.detectedDisk = DiskType.getDisk(color);
			}
		} while (this.detectedDisk == null);
		robot.getLCDScreen().print("Disk Detected", String.format("Match %d%% %s",
				this.detectedDisk.getColor().getMatchPercentage(packet.getColor()), this.detectedDisk.name()));
		if(!(robot.getArduino().getDetectedDisk() == DiskType.BLACK || robot.getArduino().getDetectedDisk() == DiskType.WHITE)) {
			Delay.miliseconds(Delay.DISK_PASS_DELAY);
		}
	}

	public ArduinoPacket readNextPacket() {
		ArduinoPacket lastPacket = this.readPacket();
		while (true) {
			Delay.miliseconds(200);
			ArduinoPacket packet = this.readPacket();
			if (packet.isChanged(lastPacket))
				return packet;
		}
	}

	public ArduinoPacket readPacket() {
		int size = Short.BYTES;
		short[] arr = new short[ArduinoPacket.PACKET_SIZE];
		byte[] bytes = new byte[size * arr.length];
		int s = this.channel.read(bytes);
		if (s == -83) {
			return ArduinoPacket.deseserialize(new short[ArduinoPacket.PACKET_SIZE]);
		}
		for (int t = 0; t < arr.length; t++) {
			short val = 0;
			for (int x = 0; x < 2; x++) {
				val |= (bytes[bytes.length - x - 1 - (size * t)] & 0xFF) << (8 * x);
			}
			arr[arr.length - 1 - t] = val;
		}
		return ArduinoPacket.deseserialize(arr);
	}

	public DiskType getDetectedDisk() {
		return this.detectedDisk;
	}
}
