package io.github.danielthedev.robot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalState;

import io.github.danielthedev.robot.controllers.ArmController;
import io.github.danielthedev.robot.controllers.BeltController;
import io.github.danielthedev.robot.raspberry.Beeper;
import io.github.danielthedev.robot.raspberry.Button;
import io.github.danielthedev.robot.raspberry.PinRegistry;
import io.github.danielthedev.robot.raspberry.library.arduino.Arduino;
import io.github.danielthedev.robot.raspberry.library.lcd.LCDScreen;
import io.github.danielthedev.robot.raspberry.library.motor.MotorController;
import io.github.danielthedev.robot.sequence.SequenceList;
import io.github.danielthedev.robot.sequence.SequenceType;
import io.github.danielthedev.robot.util.Delay;

public class Robot {

	public static final Logger LOGGER = LogManager.getLogger(Robot.class);
	public static final String MAIN_THREAD = "robot-thread";

	private final Button resetButton;
	private final Beeper beeper;
	private final LCDScreen lcdScreen;
	private final ArmController armController;
	private final BeltController beltController;
	private final Arduino arduino;
	private final RobotExceptionHandler exceptionHandler;
	private final MotorController motorController;
	private final SequenceList sequenceList = new SequenceList();
	private final Runnable shutdownListener;

	private SequenceType sequenceType;

	public Robot(Context context, Runnable shutdownListener) {
		Thread.currentThread().setName(MAIN_THREAD);
		this.motorController = new MotorController(context);
		this.armController = new ArmController(context, motorController);
		this.beltController = new BeltController(context, motorController);
		this.arduino = new Arduino(context);
		this.exceptionHandler = new RobotExceptionHandler(this);
		this.lcdScreen = new LCDScreen(context);
		this.sequenceList.registerSequences(this);
		this.beeper = new Beeper(context);
		this.shutdownListener = shutdownListener;
		this.resetButton = new Button(context, PinRegistry.RESET_BUTTON);
	}

	public void start() {
		this.lcdScreen.print("Hey, I am Henry the biased robot");
		Delay.miliseconds(3000);
		this.lcdScreen.print("Press reset", "to continue");
		this.resetButton.waitForSyncClick();
		this.exceptionHandler.startListening();
		
		this.resetButton.setASyncButtonListener(s -> {
			if (s == DigitalState.HIGH) {
				this.resetButton.removeListener();
				this.reset();
			}
		});
		this.preinit();
		this.init();
	}

	public void reset() {
		if (!Robot.isMainThread()) {
			Delay.intercept();
		}
	}

	public void stop() {
		this.armController.stop();
		this.beltController.stop();
	}

	public void shutdown(boolean force) {
		Robot.LOGGER.info("Shutting down robot");
		if(!force) this.runSequence(SequenceType.SHUTDOWN);
		for (int x = 0; x < 3; x++) {
			this.lcdScreen.print(String.format("Robot shutdown in %s second(s)", 3 - x));
		}
		this.sequenceList.reset();
		this.arduino.stop();
		this.lcdScreen.clear();
		this.motorController.disable();
		this.shutdownListener.run();
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
		while (this.sequenceList.executeSequence(sequenceType, this)) {
		}
	}

	public void registerExceptionHandler() {
		if (isMainThread()) {
			Thread.currentThread().setUncaughtExceptionHandler(this.exceptionHandler);
		}
	}

	public void safeRestartThread(Runnable runnable) {
		Thread thread = new Thread(runnable, MAIN_THREAD);
		thread.setUncaughtExceptionHandler(exceptionHandler);
		thread.start();
	}

	public Button getResetButton() {
		return resetButton;
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

	public static void throwErrorIf(boolean predicate, ExceptionType type) {
		if (predicate)
			throwError(type);
	}

	public static void throwErrorIfNot(boolean predicate, ExceptionType type) {
		throwErrorIf(!predicate, type);
	}

	public static boolean isMainThread() {
		return Thread.currentThread().getName().equals(MAIN_THREAD);
	}

	public static boolean isMainThread(Thread thread) {
		return thread.getName().equals(MAIN_THREAD);
	}

	public static boolean executeSafe(Runnable... methods) {
		try {
			for (Runnable method : methods) {
				method.run();
			}
			return true;
		} catch (Exception ee) {
			return false;
		}
	}

	public static void verifyMainThread() {
		if (!isMainThread()) {
			throwError(ExceptionType.NOT_MAIN_THREAD);
		}
	}
}
