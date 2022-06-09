package io.github.danielthedev.robot;


import com.pi4j.Pi4J;
import com.pi4j.context.Context;

public class Main {
	
	public static void main(String[] args) throws Exception {
		while(true) {
	    	Context pi4j = Pi4J.newAutoContext();
	    	Robot robot = new Robot(pi4j, ()->{
				if(pi4j != null) {
				pi4j.shutdown();
				System.gc();
			}});
	    	robot.getLCDScreen().print("Press reset button to continue");
	    	robot.getResetButton().waitForSyncClick();
			robot.start();
		}
    }

}
