package io.github.danielthedev.robot;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;

import io.github.danielthedev.robot.motor.Motor;
import io.github.danielthedev.robot.motor.MotorController;
import io.github.danielthedev.robot.motor.MotorType;

public class Main {

    private static final Pin PIN_MC_CLOCK = new Pin("clock", 26);  
    private static final Pin PIN_MC_ENABLE = new Pin("enable", 19);  
    private static final Pin PIN_MC_DATA = new Pin("data", 13);  
    private static final Pin PIN_MC_LATCH = new Pin("latch", 6);  
    
    private static final Pin PIN_MOTOR_1 = new Pin("motor-1", -9999999);  
    
    public static void main(String[] args) throws Exception {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "DEBUG");   
        Context pi4j = Pi4J.newAutoContext();
        
        MotorController motorController = new MotorController(pi4j, PIN_MC_LATCH, PIN_MC_DATA, PIN_MC_CLOCK, PIN_MC_ENABLE);
        Motor motor = new Motor(pi4j, motorController, MotorType.MOTOR_1, PIN_MOTOR_1);
        
        Thread.sleep(5000);
        
        pi4j.shutdown();
    }

}
