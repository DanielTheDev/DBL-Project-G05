package io.github.danielthedev.robot.logging_feature;

import java.util.concurrent.TimeUnit;
import java.util.Random;
import java.text.SimpleDateFormat;  
import java.util.Date;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class LogFile {
	
	//RANDOMLY GENERATING INPUTS
	void timer() throws InterruptedException {
		// timer variables
		boolean x=true;
	    long starttime=System.currentTimeMillis();
	    while(x)
	    {
	    	//timer process
	        TimeUnit.SECONDS.sleep(1);
	        long timepassed=System.currentTimeMillis()-starttime;
	        long secondspassed=timepassed/1000;

	        //actions
	        if(secondspassed % 5 == 0) {
	    	    actions();
	        }
	        if(secondspassed == 60) { //killing the timer after 1 minute
	        	System.out.println("Timer Stopped");
	        	x = false;
	        }
	    }
	}
	
	//getting inputs from the sensors and motors
	void get_robot_inputs(boolean arm_motor, boolean conveyer_motor, boolean disk_sensor, boolean light_sensor) {
		
	}
	
	int robot_states() {
		boolean arm_motor = false;
		boolean conveyer_motor = false;
		boolean disk_sensor = false;
		boolean light_sensor = false;
		
		//randomly giving inputs
		Random rand = new Random();
		int input_decider = rand.nextInt(6);
		switch(input_decider) {
		  case 1:
			  arm_motor = true;
		    break;
		  case 2:
			  conveyer_motor = true;
		    break;
		  case 3:
			  disk_sensor = true;
		    break;
		  case 4:
			  light_sensor = true;
			break;
		  default:
			  //induce error
		}		
		//deciding the state based on inputs
		int state = 0;
		if(!arm_motor && !conveyer_motor && !disk_sensor && !light_sensor) state = 1;
		else if(!arm_motor && !conveyer_motor && disk_sensor && !light_sensor) state = 2; //put arm motor true after demonstration
		else if(arm_motor && !conveyer_motor && !disk_sensor && !light_sensor) state = 3;
		else if(!arm_motor && !conveyer_motor && !disk_sensor && light_sensor) state = 4;
		else if(!arm_motor && conveyer_motor && !disk_sensor && !light_sensor) state = 5;
		
		return state;
	}
	
	void robot_output() throws InterruptedException {
		timer();
	}
	
	//logging functions dsfdneonfdoiesnf
	
	void LCD_display() {
		switch(robot_states()) {
		  case 1:
			  System.out.println("Idle");
		    break;
		  case 2:
			  System.out.println("Extending Arm");
		    break;
		  case 3:
			  System.out.println("Retrieving Disk");
		    break;
		  case 4:
			  System.out.println("Checking Color");
		    break;
		  case 5:
			  System.out.println("Storing Black Disk");
			break;
		  case 6:
			  System.out.println("Storing White Disk");
			break;
		  default:
			  System.out.println("ERROR");
		}
	}
	
	void creating_files() {
		try {
		      FileWriter myWriter = new FileWriter("event_file.txt");
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
		File myWriter = new File("log_file.txt");
	}
	
	void event_file() {
		try (FileWriter f = new FileWriter("event_file.txt", true);
                BufferedWriter b = new BufferedWriter(f);
                PrintWriter p = new PrintWriter(b);) {
			
			//printing the date and time
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
		    Date date = new Date();  
		    p.print(formatter.format(date) + ": "); 
		    
		    //printing the state
			switch(robot_states()) {
			  case 1:
				  p.println("Idle");
			    break;
			  case 2:
				  p.println("Extending Arm");
			    break;
			  case 3:
				  p.println("Retrieving Disk");
			    break;
			  case 4:
				  p.println("Checking Color");
			    break;
			  case 5:
				  p.println("Storing Black Disk");
				break;
			  case 6:
				  p.println("Storing White Disk");
				break;
			  default:
				  p.println("ERROR");
			}

        } catch (IOException i) {
            i.printStackTrace();
        }
	}
	
	void log_file() {
		try (FileWriter f = new FileWriter("log_file.txt", true);
                BufferedWriter b = new BufferedWriter(f);
                PrintWriter p = new PrintWriter(b);) {
			
			//printing the date and time
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
		    Date date = new Date();  
		    p.print(formatter.format(date) + ": "); 
		    
		    //printing the state
			switch(robot_states()) {
			  case 1:
				  p.println("Idle");
			    break;
			  case 2:
				  p.println("Extending Arm");
			    break;
			  case 3:
				  p.println("Retrieving Disk");
			    break;
			  case 4:
				  p.println("Checking Color");
			    break;
			  case 5:
				  p.println("Storing Black Disk");
				break;
			  case 6:
				  p.println("Storing White Disk");
				break;
			  default:
				  p.println("ERROR");
			}

        } catch (IOException i) {
            i.printStackTrace();
        }
	}
	
	void actions() {
		LCD_display();
		event_file();
		log_file();
	}
	
	public static void main(String args[]) throws InterruptedException {
		
		(new LogFile()).creating_files();
		System.out.println("LCD Screen:");
		(new LogFile()).robot_output();
	}
}
