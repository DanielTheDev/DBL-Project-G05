package io.github.danielthedev.robot.sequence;

import java.util.ArrayList;
import java.util.List;

import io.github.danielthedev.robot.Robot;

public class SequenceList {

	private final List<SequenceFunction> sequenceList = new ArrayList<SequenceFunction>();
	private int sequenceIndex = 0;
	
	public void init() {}
	
	public boolean executeSequence(SequenceType sequenceType, Robot robot) {
		Robot.verifyMainThread();
		int index = this.findNextSequence(sequenceType);
		if(index == -1) return false;
		this.sequenceIndex = index;
		SequenceFunction sequenceItem = this.sequenceList.get(this.sequenceIndex);
		robot.getLCDScreen().printState(sequenceType, sequenceItem.getName());
		sequenceItem.callFunction(robot);
		return true;
	}
	
	public int findNextSequence(SequenceType type) {
		boolean duplicate = false;
		
		for(int x = sequenceIndex+1; x < this.sequenceList.size(); x++) {
			if(this.sequenceList.get(x).getType() == type) return x;
			if(type == SequenceType.RUNTIME) {
				if(x >= this.sequenceIndex-1) {
					if(!duplicate) break;
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
		this.sequenceList.add(new SequenceFunction(SequenceType.STARTUP, "Calibrate Sensor", r.getArduino()::calibrate));
		this.sequenceList.add(new SequenceFunction(SequenceType.RUNTIME, "Detecting Disk", r.getArduino()::detectDisk));
		this.sequenceList.add(new SequenceFunction(SequenceType.RUNTIME, "Grabbing Disk", r.getArmController()::grabDisk));
		this.sequenceList.add(new SequenceFunction(SequenceType.RUNTIME, "Moving item", r.getBeltController()::moveBelt));
		this.sequenceList.add(new SequenceFunction(SequenceType.SHUTDOWN, "Stopping belt", r.getBeltController()::stop));
		this.sequenceList.add(new SequenceFunction(SequenceType.SHUTDOWN, "Retracting arm", r.getArmController()::shutdown));
	}
}
