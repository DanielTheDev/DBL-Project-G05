package io.github.danielthedev.robot.sequence;

import java.util.ArrayList;
import java.util.List;

import io.github.danielthedev.robot.Robot;
import io.github.danielthedev.robot.util.Delay;

public class SequenceList {

	private final List<SequenceFunction> sequenceList = new ArrayList<SequenceFunction>();
	private int sequenceIndex = 0;
	
	public boolean executeSequence(SequenceType sequenceType, Robot robot) {
		Robot.verifyMainThread();
		int index = this.findNextSequence(sequenceType);
		if(index == -1) return false;
		this.sequenceIndex = index;
		SequenceFunction sequenceItem = this.sequenceList.get(this.sequenceIndex);
		robot.getLCDScreen().printState(sequenceType, sequenceItem.getName());
		Robot.LOGGER.debug("Starting sequence " + sequenceItem.getName());
		sequenceItem.callFunction(robot);
		this.sequenceIndex++;
		return true;
	}
	
	public int findNextSequence(SequenceType type) {
		boolean duplicate = false;
		for(int x = sequenceIndex; x < this.sequenceList.size(); x++) {
			if(this.sequenceList.get(x).getType() == type) return x;
			if(type == SequenceType.RUNTIME) {
				if(x >= this.sequenceIndex-1) {
					if(duplicate) break;
					x = 0;
					duplicate = true;
				}
			}
		}
		return -1;
	}

	public void reset() {
		Robot.verifyMainThread();
		this.sequenceIndex = 0;
		this.sequenceList.clear();
	}
	
	public void registerSequences(Robot r) {
		this.reset();
		this.sequenceList.add(new SequenceFunction(SequenceType.STARTUP, "Testing Arm", r.getArmController()::testArm));
		this.sequenceList.add(new SequenceFunction(SequenceType.STARTUP, "Testing Conveyer", r.getBeltController()::testBelt));
		this.sequenceList.add(new SequenceFunction(SequenceType.RUNTIME, "Detecting Disk", r.getArduino()::detectDisk));
		this.sequenceList.add(new Delay(SequenceType.RUNTIME, Delay.ARM_GRAB_DELAY));
		this.sequenceList.add(new SequenceFunction(SequenceType.RUNTIME, "Grabbing Disk", r.getArmController()::grabDisk));
		this.sequenceList.add(new Delay(SequenceType.RUNTIME, Delay.CONVEYER_BELT_MOVE_DELAY));
		this.sequenceList.add(new SequenceFunction(SequenceType.RUNTIME, "Moving item", r.getBeltController()::moveItem));
		this.sequenceList.add(new SequenceFunction(SequenceType.SHUTDOWN, "Stopping belt", r.getBeltController()::stop));
		this.sequenceList.add(new SequenceFunction(SequenceType.SHUTDOWN, "Retracting arm", r.getArmController()::retractArm));
	}
}
