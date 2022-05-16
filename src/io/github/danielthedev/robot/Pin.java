package io.github.danielthedev.robot;

public class Pin {

	public static final int GPIO_4 = 4;
	public static final int GPIO_5 = 5;
	public static final int GPIO_6 = 6;
	
	public static final int GPIO_7 = 5;
	public static final int GPIO_8 = 6;
	
	public static final int GPIO_12 = 12;
	public static final int GPIO_13 = 13;
	
	public static final int GPIO_14 = 14;
	public static final int GPIO_15 = 15;
	
	public static final int GPIO_16 = 16;
	public static final int GPIO_17 = 17;
	public static final int GPIO_18 = 18;
	public static final int GPIO_19 = 19;
	
	public static final int GPIO_20 = 20;
	public static final int GPIO_21 = 21;
	public static final int GPIO_22 = 22;
	public static final int GPIO_23 = 23;
	public static final int GPIO_24 = 24;
	public static final int GPIO_25 = 25;
	public static final int GPIO_26 = 26;
	public static final int GPIO_27 = 27;
	
	private final int bcm;
	private final String name;
	private final String endpoint;
	
	public Pin(String name, int bcm) {
		this(name, bcm, "");
	}

	
	public Pin(String name, int bcm, String endpoint) {
		this.name = name;
		this.bcm = bcm;
		this.endpoint = endpoint;
	}

	public int getBCMAddress() {
		return bcm;
	}

	public String getName() {
		return name;
	}
	
	public String getEndpoint() {
		return endpoint;
	}
}
