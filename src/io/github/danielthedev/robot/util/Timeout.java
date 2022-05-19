package io.github.danielthedev.robot.util;

import java.util.function.Predicate;

public class Timeout {
	
	private final Runnable timeout;
	
	public Timeout(Runnable timeout) {
		this.timeout = timeout;
	}

	public static void createTimeoutPredicate(long miliseconds, Predicate<Void> p, Runnable timeout) {
		long time = System.currentTimeMillis() + miliseconds;
		while((time - System.currentTimeMillis()) < 0) {
			if(p.test(null)) return;
		}
		timeout.run();
	}

}
