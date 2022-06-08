package io.github.danielthedev.robot;


import com.pi4j.Pi4J;
import com.pi4j.context.Context;

public class Main {
	
	public static void main(String[] args) throws Exception {
    	Context pi4j = null;
    	try {
    		pi4j = Pi4J.newAutoContext();
    		Robot robot = new Robot(pi4j);
    		robot.start();
		} finally {
			/*if(pi4j != null) {
				System.out.println("here");
				pi4j.shutdown();
			}*/
		}       
    }

}
