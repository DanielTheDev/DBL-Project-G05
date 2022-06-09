package io.github.danielthedev.robot;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;

public class Main {
	
	public static void main(String[] args) {
		Robot.LOGGER.info("Startup library");
    	Context pi4j = Pi4J.newAutoContext();
    	Robot robot = new Robot(pi4j, ()->{
			if(pi4j != null) {
				Robot.LOGGER.info("Shutdown library");
				pi4j.shutdown();
				System.gc();
				main(null);
			}
		});
    	robot.getLCDScreen().print("Press reset button to continue");
    	robot.getResetButton().waitForSyncClick();
		robot.start();
    }

}
