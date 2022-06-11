package io.github.danielthedev.robot.raspberry;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalState;
import com.pi4j.io.gpio.digital.PullResistance;
import com.pi4j.io.i2c.I2C;
import com.pi4j.io.i2c.I2CProvider;
import com.pi4j.io.pwm.Pwm;
import com.pi4j.io.pwm.PwmType;

public class PinFactory {

	public static Pwm createPWMPin(Context context, Pin pin, String type, int frequency) {
		Pwm pwmPin = context.create(Pwm.newConfigBuilder(context).id(type + "-" + pin.getName()).name(pin.getName())
				.address(pin.getBCMAddress()).pwmType(PwmType.SOFTWARE).initial(0).shutdown(0).provider("pigpio-pwm")
				.build());
		pwmPin.setFrequency(frequency);
		return pwmPin;
	}

	public static DigitalOutput createOutputPin(Context context, Pin pin, String type) {
		return context.create(DigitalOutput.newConfigBuilder(context).id(type + "-" + pin.getName()).name(pin.getName())
				.address(pin.getBCMAddress()).shutdown(DigitalState.LOW).initial(DigitalState.LOW)
				.provider("pigpio-digital-output"));
	}

	public static DigitalInput createInputPin(Context context, Pin pin, String type) {
		return context.create(DigitalInput.newConfigBuilder(context).id(type + "-" + pin.getName()).name(pin.getName())
				.address(pin.getBCMAddress()).provider("pigpio-digital-input"));
	}

	public static DigitalInput createInputPin(Context context, Pin pin, String type, PullResistance resistance) {
		return context.create(DigitalInput.newConfigBuilder(context).id(type + "-" + pin.getName()).name(pin.getName())
				.address(pin.getBCMAddress()).pull(resistance).provider("pigpio-digital-input"));
	}

	public static I2C createI2CChannel(Context context, int bus, int device) {
		return ((I2CProvider) context.provider("pigpio-i2c")).create(I2C.newConfigBuilder(context).id("i2c-" + bus)
				.name("device-" + device).bus(bus).device(device).build());
	}

}
