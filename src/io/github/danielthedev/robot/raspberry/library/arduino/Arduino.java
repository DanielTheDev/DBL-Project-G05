package io.github.danielthedev.robot.raspberry.library.arduino;

import java.util.Timer;
import java.util.TimerTask;

import com.pi4j.context.Context;
import com.pi4j.io.i2c.I2C;

import io.github.danielthedev.robot.raspberry.PinFactory;
import io.github.danielthedev.robot.raspberry.PinRegistry;

public class Arduino extends TimerTask implements AutoCloseable {
	
	private final static int READ_INTERVAL = 250;	
	
	private final Timer timer = new Timer();
	private final ArduinoListener listener;
	private final I2C channel;

	public Arduino(Context context, ArduinoListener listener) {
		this.listener = listener;
		this.channel = PinFactory.createI2CChannel(context, PinRegistry.I2C_BUS, PinRegistry.I2C_DEVICE);
	}
	
	public void start() {
		this.timer.schedule(this, 0, READ_INTERVAL);
	}
	
	public void stop() {
		try {
			this.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private ArduinoPacket readPacket() {
		int size = Short.BYTES;
		short[] arr = new short[ArduinoPacket.PACKET_SIZE];
		byte[] bytes = new byte[size*arr.length];
		this.channel.read(bytes);
		for(int t = 0; t < arr.length; t++) {
			short val = 0;
			for(int x = 0; x < 2; x++) {
				val |= (bytes[bytes.length-x-1-(size*t)] & 0xFF) << (8 * x);
			}
			arr[arr.length-1-t] = val;
		}
		return ArduinoPacket.deseserialize(arr);
	}

	@Override
	public void close() throws Exception {
		this.timer.cancel();
		if(this.channel != null) {
			this.channel.close();
		}
	}

	@Override
	public void run() {
		ArduinoPacket packet = this.readPacket();
		System.out.println(packet);
	}
}
