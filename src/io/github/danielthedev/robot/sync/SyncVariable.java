package io.github.danielthedev.robot.sync;


public class SyncVariable<T> {
		
		private final Object lock = new Object();
		
		private T object;
		
		public SyncVariable(T obj) {
			this.object = obj;
		}
		
		public synchronized void putSync(T obj) {
			this.object = obj;
			synchronized (lock) {
				this.lock.notifyAll();
			}
		}
		
		public boolean waitForChange(int miliseconds) {
			T obj = this.getSync();
			synchronized (lock) {
				try {
					this.lock.wait(miliseconds);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return obj != getSync();
		}
		
		public synchronized T getSync() {
			return this.object;
		}
}
