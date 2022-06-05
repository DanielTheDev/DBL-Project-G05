package io.github.danielthedev.robot.sequence;

import java.util.Optional;
import java.util.function.Consumer;

import io.github.danielthedev.robot.Robot;

public class SequenceFunction {	
	
	private final SequenceType type;
	private final String name;
	private Optional<Consumer<Robot>> consumer = Optional.empty();
	private Optional<Runnable> runnable = Optional.empty();
	
	public SequenceFunction(SequenceType type, String name, Consumer<Robot> consumer) {
		this.type = type;
		this.name = name;
		this.consumer = Optional.of(consumer);
	}
	
	public SequenceFunction(SequenceType type, String name, Runnable runnable) {
		this.type = type;
		this.name = name;
		this.runnable = Optional.of(runnable);
	}

	public SequenceType getType() {
		return type;
	}

	public void callFunction(Robot robot) {
		this.consumer.ifPresent(s->s.accept(robot));
		this.runnable.ifPresent(Runnable::run);
	}

	public String getName() {
		return name;
	}
	
}
