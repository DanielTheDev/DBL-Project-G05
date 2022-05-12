package io.github.danielthedev.robot;

public class Pin {
	
	private final int bcm;
	private final String name;
	
	public Pin(String name, int bcm) {
		this.name = name;
		this.bcm = bcm;
	}

	public int getBCMAddress() {
		return bcm;
	}

	public String getName() {
		return name;
	}
	
	
}
