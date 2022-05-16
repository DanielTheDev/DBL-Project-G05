package io.github.danielthedev.robot;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;

import io.github.danielthedev.robot.lcd.LCDScreen;
import io.github.danielthedev.robot.motor.Motor;
import io.github.danielthedev.robot.motor.MotorController;
import io.github.danielthedev.robot.motor.MotorState;
import io.github.danielthedev.robot.motor.MotorType;

import static io.github.danielthedev.robot.Pin.*;

public class Main {
	
	
	//PINS 4, 17, 27, 22, 5, 6, 13, 19, 26, 21, 20, 16, 12, 25, 24, 23, 18

    private static final Pin PIN_MC_CLOCK = new Pin("clock", GPIO_24, "D4");  
    private static final Pin PIN_MC_ENABLE = new Pin("enable", GPIO_25, "D7");  
    private static final Pin PIN_MC_DATA = new Pin("data", GPIO_15, "D8");  
    private static final Pin PIN_MC_LATCH = new Pin("latch", GPIO_14, "D12");  
    
    private static final Pin PIN_MOTOR_1 = new Pin("motor-1", GPIO_8, "D11");
    private static final Pin PIN_MOTOR_2 = new Pin("motor-2", GPIO_7, "D3");  
    
    
    private static final Pin PIN_LCD_RS = new Pin("rs", GPIO_5);
    private static final Pin PIN_LCD_ENABLE = new Pin("enable", GPIO_6);
    private static final Pin PIN_LCD_D4 = new Pin("d4", GPIO_13);
    private static final Pin PIN_LCD_D5 = new Pin("d5", GPIO_19);
    private static final Pin PIN_LCD_D6 = new Pin("d6", GPIO_26);
    private static final Pin PIN_LCD_D7 = new Pin("d7", GPIO_21);
    
    
    public static void main(String[] args) throws Exception {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "DEBUG");   
        Context pi4j = Pi4J.newAutoContext();
        
        //MotorController motorController = new MotorController(pi4j, PIN_MC_LATCH, PIN_MC_DATA, PIN_MC_CLOCK, PIN_MC_ENABLE);
        //Motor motor1 = new Motor(pi4j, motorController, MotorType.MOTOR_1, PIN_MOTOR_1);
        //Motor motor2 = new Motor(pi4j, motorController, MotorType.MOTOR_2, PIN_MOTOR_2);
        
        LCDScreen lcd = new LCDScreen(pi4j, PIN_LCD_RS, PIN_LCD_ENABLE, PIN_LCD_D4, PIN_LCD_D5, PIN_LCD_D6, PIN_LCD_D7);
        lcd.begin(16, 2, 0);
        
        String s =  "Welcome to the strawberry pi    ";
        lcd.print(s);
        
        
        
        Thread.sleep(5000);
        
        
        pi4j.shutdown();
    }
}
