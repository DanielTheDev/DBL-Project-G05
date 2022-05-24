package io.github.danielthedev.robot.util;

public class SyncTimeout {
	
	private final long time;
	
	private SyncTimeout(long delay) {
		this.time = System.currentTimeMillis() + delay;
	}

	public static SyncTimeout createTimeoutLoop(long miliseconds) {
		return new SyncTimeout(miliseconds);
	}

	public boolean isTimeout() {
		return (this.time - System.currentTimeMillis()) <= 0;
	}
}
