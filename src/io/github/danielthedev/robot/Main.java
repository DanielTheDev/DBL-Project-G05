package io.github.danielthedev.robot;

public class Main {

	public static void main(String[] args) {
		if(!System.getProperty("java.version").startsWith("11")) {
			System.err.println("Incorrect java version (Required 11).");
			System.exit(0);
		}

	}

}
