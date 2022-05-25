package io.github.danielthedev.robot.util;


public class ThreadCommunication {

	private final Object lock = new Object();
	
	public void waitButtonPress(int timeout, Runnable timeouthandler, Runnable successhandler) {
		synchronized (this.lock) {
			long time = System.currentTimeMillis() + timeout;
			try {
				this.lock.wait(timeout);
				if(time - System.currentTimeMillis() <= 0) {
					timeouthandler.run();
					return;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
		}
		successhandler.run();
	}
	
	public void fireButtonPress() {
		synchronized (this.lock) {
			this.lock.notifyAll();	
		}
	}
	
}
