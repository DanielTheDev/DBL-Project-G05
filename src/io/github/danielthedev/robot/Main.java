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
import io.github.danielthedev.robot.motor.Motor;
import io.github.danielthedev.robot.motor.MotorController;
import io.github.danielthedev.robot.motor.MotorState;
import io.github.danielthedev.robot.motor.MotorType;

import static io.github.danielthedev.robot.Pin.*;

public class Main {
	
	
	//PINS 4, 17, 27, 22, 5, 6, 13, 19, 26, 21, 20, 16, 12, 25, 24, 23, 18

    private static final Pin PIN_MC_CLOCK = new Pin("clock", GPIO_26, "D4");  
    private static final Pin PIN_MC_ENABLE = new Pin("enable", GPIO_19, "D7");  
    private static final Pin PIN_MC_DATA = new Pin("data", GPIO_13, "D8");  
    private static final Pin PIN_MC_LATCH = new Pin("latch", GPIO_5, "D12");  
    
    private static final Pin PIN_MOTOR_1 = new Pin("motor-1", GPIO_6, "D11");
    //private static final Pin PIN_MOTOR_2 = new Pin("motor-2", GPIO_7, "D3");  
    
    private static final Pin PIN_ARDUINO_CLOCK = new Pin("arduino-clock", GPIO_27, "3");
    private static final Pin PIN_ARDUINO_DATA = new Pin("arduino-data", GPIO_17, "2");
    
    private static final Pin PIN_LCD_RS = new Pin("rs", GPIO_5);
    private static final Pin PIN_LCD_ENABLE = new Pin("enable", GPIO_6);
    private static final Pin PIN_LCD_D4 = new Pin("d4", GPIO_13);
    private static final Pin PIN_LCD_D5 = new Pin("d5", GPIO_19);
    private static final Pin PIN_LCD_D6 = new Pin("d6", GPIO_26);
    private static final Pin PIN_LCD_D7 = new Pin("d7", GPIO_21);
    
    private static final Pin BTN_INPUT = new Pin("btn", GPIO_4);
    
    public static void main(String[] args) throws Exception {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "DEBUG");   
        Context pi4j = Pi4J.newAutoContext();
        
        //MotorController motorController = new MotorController(pi4j, PIN_MC_LATCH, PIN_MC_DATA, PIN_MC_CLOCK, PIN_MC_ENABLE);
        //Motor motor1 = new Motor(pi4j, motorController, MotorType.MOTOR_1, PIN_MOTOR_1);
        
        
                
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
    
	private static Pwm createPin(Context context, Pin pin, int freq) {
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
