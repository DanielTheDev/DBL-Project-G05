package io.github.danielthedev.robot;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalStateChangeEvent;
import com.pi4j.io.gpio.digital.DigitalStateChangeListener;
import com.pi4j.io.gpio.digital.PullResistance;
import com.pi4j.io.pwm.Pwm;
import com.pi4j.io.pwm.PwmType;
import com.pi4j.library.pigpio.PiGpio;
import com.pi4j.library.pigpio.PiGpio_GPIO;
import com.pi4j.library.pigpio.internal.PIGPIO;

import io.github.danielthedev.robot.arduino.Arduino;
import io.github.danielthedev.robot.arduino.ArduinoListener;
import io.github.danielthedev.robot.lcd.LCDScreen;
import io.github.danielthedev.robot.logging.Log;
import io.github.danielthedev.robot.motor.Motor;
import io.github.danielthedev.robot.motor.MotorController;
import io.github.danielthedev.robot.motor.MotorState;
import io.github.danielthedev.robot.motor.MotorType;

import static io.github.danielthedev.robot.Pin.*;

public class Main {
	
	
	//PINS 4, 17, 27, 22, 5, 6, 13, 19, 26, 21, 20, 16, 12, 25, 24, 23, 18



    
    public static void main(String[] args) throws Exception {
        //Log.logger.info("hey");
    	Log.logger.info("hey");
        Context pi4j = Pi4J.newAutoContext();
        

        
        //LCDScreen lcd = new LCDScreen(pi4j, PIN_LCD_RS, PIN_LCD_ENABLE, PIN_LCD_D4, PIN_LCD_D5, PIN_LCD_D6, PIN_LCD_D7);
        //lcd.begin(16, 2, 0);
        //String s =  "Welcome to the strawberry pi    ";
        //lcd.print(s);
        
        /*Arduino arduino = new Arduino(pi4j, PIN_ARDUINO_CLOCK, PIN_ARDUINO_DATA, new ArduinoListener() {
			
			@Override
			public void onItemRead(DiskType type) {
				System.out.println("Read: " + type);	
			}
			
			@Override
			public void onItemDetect() {
				System.out.println("Detect");
			}
			
			@Override
			public void onFailure() {
				System.out.println("Error");				
			}
		});
        arduino.enable();
        */
        Thread.sleep(20000);
        
        
        pi4j.shutdown();
    }
    
	public static Pwm createPin(Context context, Pin pin, int freq) {
		Pwm pwmPin = context.create(Pwm.newConfigBuilder(context)
	                .id("Motor-"+pin.getName())
	                .name(pin.getName())
	                .address(pin.getBCMAddress())
	                .pwmType(PwmType.SOFTWARE)
	                .initial(0)
	                .shutdown(0)
	                .provider("pigpio-pwm")
	                .build());
		pwmPin.setFrequency(freq);
		return pwmPin;
	}
}
