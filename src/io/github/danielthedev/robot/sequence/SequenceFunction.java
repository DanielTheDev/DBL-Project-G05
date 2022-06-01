package io.github.danielthedev.robot.sequence;

import java.util.function.Consumer;

import io.github.danielthedev.robot.Robot;

public class SequenceFunction {	
	
	private final SequenceType type;
	private final String name;
	private final Consumer<Robot> consumer;
	private final Runnable runnable;
	
	public SequenceFunction(SequenceType type, String name, Consumer<Robot> consumer) {
		this.type = type;
		this.name = name;
		this.consumer = consumer;
		this.runnable = null;
	}
	
	public SequenceFunction(SequenceType type, String name, Runnable runnable) {
		this.type = type;
		this.name = name;
		this.runnable = runnable;
		this.consumer = null;
	}

	public SequenceType getType() {
		return type;
	}

	public void callFunction(Robot robot) {
		if(this.runnable != null) {
			this.runnable.run();
		}
		if(this.consumer != null) {
			this.consumer.accept(robot);
		}
	}

	public String getName() {
		return name;
	}
	
}
