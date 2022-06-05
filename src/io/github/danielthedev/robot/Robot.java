package io.github.danielthedev.robot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pi4j.context.Context;

import io.github.danielthedev.robot.controllers.ArmController;
import io.github.danielthedev.robot.controllers.BeltController;
import io.github.danielthedev.robot.raspberry.Beeper;
import io.github.danielthedev.robot.raspberry.library.arduino.Arduino;
import io.github.danielthedev.robot.raspberry.library.lcd.LCDScreen;
import io.github.danielthedev.robot.raspberry.library.motor.MotorController;
import io.github.danielthedev.robot.sequence.SequenceList;
import io.github.danielthedev.robot.sequence.SequenceType;

public class Robot {

	public static final Logger LOGGER = LogManager.getLogger(Robot.class);
	public static final String MAIN_THREAD = "robot-thread";
		
	private final Beeper beeper;
	private final LCDScreen lcdScreen;
	private final ArmController armController;
	private final BeltController beltController;
	private final Arduino arduino;
	private final RobotExceptionHandler exceptionHandler;
	private final MotorController motorController;
	private final SequenceList sequenceList = new SequenceList();
	
	private SequenceType sequenceType; 
	
	public Robot(Context context) {
		Thread.currentThread().setName(MAIN_THREAD);
		this.motorController = new MotorController(context);
		this.armController = new ArmController(context, motorController);
		this.beltController = new BeltController(context, motorController);
		this.arduino = new Arduino(context);
		this.exceptionHandler = new RobotExceptionHandler(this);
		this.lcdScreen = new LCDScreen(context);
		this.sequenceList.registerSequences(this);
		this.beeper = new Beeper(context);
	}
	
	public void start() {
		this.exceptionHandler.startListening();
		this.preinit();
		this.init();
	}
	
	public void stop() {
		Robot.LOGGER.info("Shutting down robot");
		this.runSequence(SequenceType.SHUTDOWN);
		this.sequenceList.reset();
		this.arduino.stop();
		this.lcdScreen.clear();
		this.motorController.disable();
	}
	
	public void preinit() {
		Robot.LOGGER.info("Pre-initializing robot");
		this.runSequence(SequenceType.STARTUP);
	}
	
	public void init() {
		Robot.LOGGER.info("Initializing robot");
		this.runSequence(SequenceType.RUNTIME);
	}
	
	public void continueExecution() {
		switch (this.sequenceType) {
		case RUNTIME:
			this.init();
			break;
		case SHUTDOWN:
			this.stop();
			break;
		case STARTUP:
			this.preinit();
			this.init();
			break;
		}
	}
	
	public void runSequence(SequenceType type) {
		this.sequenceType = type;
		while(this.sequenceList.executeSequence(sequenceType, this)) {}
	}
	
	public void registerExceptionHandler() {
		if(isMainThread()) {
			Thread.currentThread().setUncaughtExceptionHandler(this.exceptionHandler);
		}
	}
	
	public LCDScreen getLCDScreen() {
		return lcdScreen;
	}

	public ArmController getArmController() {
		return armController;
	}

	public BeltController getBeltController() {
		return beltController;
	}

	public Arduino getArduino() {
		return arduino;
	}

	public SequenceType getSequenceType() {
		return sequenceType;
	}
	
	public Beeper getBeeper() {
		return beeper;
	}

	public RobotExceptionHandler getExceptionHandler() {
		return exceptionHandler;
	}

	public static void throwError(ExceptionType type) {
		throw type.createException();
	}
	
	public static boolean isMainThread() {
		return Thread.currentThread().getName().equals(MAIN_THREAD);
	}
	
	public static boolean isMainThread(Thread thread) {
		return thread.getName().equals(MAIN_THREAD);
	}
	
	public static void verifyMainThread() {
		if(!isMainThread()) {
			throwError(ExceptionType.NOT_MAIN_THREAD);
		}
	}
}
