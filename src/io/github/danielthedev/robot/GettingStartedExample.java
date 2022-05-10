package io.github.danielthedev.robot;

import com.pi4j.Pi4J;
import com.pi4j.io.gpio.analog.AnalogInput;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalState;
import com.pi4j.io.gpio.digital.PullResistance;
import com.pi4j.util.Console;

class GettingStartedExample {

    private static final int PIN_LED = 22; // PIN 15 = BCM 22

    public static void main(String[] args) throws Exception {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "DEBUG");

        final var console = new Console();
        
        var pi4j = Pi4J.newAutoContext();
        
        var ledConfig = DigitalOutput.newConfigBuilder(pi4j)
                .id("led")
                .name("LED Flasher")
                .address(PIN_LED)
                .shutdown(DigitalState.LOW)
                .initial(DigitalState.LOW)
                .provider("pigpio-digital-output");
        var led = pi4j.create(ledConfig);

        
        for(int x = 0; x < 10; x++){
            if (led.equals(DigitalState.HIGH)) {
                console.println("LED low");
                led.low();
            } else {
                console.println("LED high");
                led.high();
            }
            Thread.sleep(500);
        }
        
        pi4j.shutdown();
    }
}
