package io.github.danielthedev.robot.raspberry;

import static io.github.danielthedev.robot.raspberry.Pin.GPIO_13;
import static io.github.danielthedev.robot.raspberry.Pin.GPIO_17;
import static io.github.danielthedev.robot.raspberry.Pin.GPIO_19;
import static io.github.danielthedev.robot.raspberry.Pin.GPIO_21;
import static io.github.danielthedev.robot.raspberry.Pin.GPIO_26;
import static io.github.danielthedev.robot.raspberry.Pin.GPIO_27;
import static io.github.danielthedev.robot.raspberry.Pin.GPIO_4;
import static io.github.danielthedev.robot.raspberry.Pin.GPIO_5;
import static io.github.danielthedev.robot.raspberry.Pin.GPIO_6;

public class PinRegistry {

    public static final Pin PIN_MC_CLOCK = new Pin("clock", GPIO_26, "D4");  
    public static final Pin PIN_MC_ENABLE = new Pin("enable", GPIO_19, "D7");  
    public static final Pin PIN_MC_DATA = new Pin("data", GPIO_13, "D8");  
    public static final Pin PIN_MC_LATCH = new Pin("latch", GPIO_5, "D12"); 
    
    public static final Pin PIN_MOTOR_1 = new Pin("motor-1", GPIO_6, "D11");
    public static final Pin PIN_MOTOR_2 = new Pin("motor-2", GPIO_26, "D3");  
    
    public static final Pin PIN_ARDUINO_CLOCK = new Pin("arduino-clock", GPIO_27, "3");
    public static final Pin PIN_ARDUINO_DATA = new Pin("arduino-data", GPIO_17, "2");
    
    public static final Pin PIN_LCD_RS = new Pin("rs", GPIO_5);
    public static final Pin PIN_LCD_ENABLE = new Pin("enable", GPIO_6);
    public static final Pin PIN_LCD_D4 = new Pin("d4", GPIO_13);
    public static final Pin PIN_LCD_D5 = new Pin("d5", GPIO_19);
    public static final Pin PIN_LCD_D6 = new Pin("d6", GPIO_26);
    public static final Pin PIN_LCD_D7 = new Pin("d7", GPIO_21);
    
    public static final Pin PIN_ARM_BUTTON = new Pin("btn", GPIO_4);
}
