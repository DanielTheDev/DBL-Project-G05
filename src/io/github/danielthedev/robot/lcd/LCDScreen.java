package io.github.danielthedev.robot.lcd;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalState;
import com.pi4j.library.pigpio.impl.PiGpioBase;
import com.pi4j.library.pigpio.internal.PIGPIO;

import io.github.danielthedev.robot.Pin;
import io.github.danielthedev.robot.PinFactory;

public class LCDScreen {

	private static final int LOW = 0;
	private static final int HIGH = 1;
	// commands
	private static final int LCD_CLEARDISPLAY = 0x01;
	private static final int LCD_RETURNHOME = 0x02;
	private static final int LCD_ENTRYMODESET = 0x04;
	private static final int LCD_DISPLAYCONTROL = 0x08;
	private static final int LCD_CURSORSHIFT = 0x10;
	private static final int LCD_FUNCTIONSET = 0x20;
	private static final int LCD_SETCGRAMADDR = 0x40;
	private static final int LCD_SETDDRAMADDR = 0x80;

	// flags for display entry mode
	private static final int LCD_ENTRYRIGHT = 0x00;
	private static final int LCD_ENTRYLEFT = 0x02;
	private static final int LCD_ENTRYSHIFTINCREMENT = 0x01;
	private static final int LCD_ENTRYSHIFTDECREMENT = 0x00;

	// flags for display on/off control
	private static final int LCD_DISPLAYON = 0x04;
	private static final int LCD_DISPLAYOFF = 0x00;
	private static final int LCD_CURSORON = 0x02;
	private static final int LCD_CURSOROFF = 0x00;
	private static final int LCD_BLINKON = 0x01;
	private static final int LCD_BLINKOFF = 0x00;

	// flags for display/cursor shift
	private static final int LCD_DISPLAYMOVE = 0x08;
	private static final int LCD_CURSORMOVE = 0x00;
	private static final int LCD_MOVERIGHT = 0x04;
	private static final int LCD_MOVELEFT = 0x00;

	// flags for function set
	private static final int LCD_8BITMODE = 0x10;
	private static final int LCD_4BITMODE = 0x00;
	private static final int LCD_2LINE = 0x08;
	private static final int LCD_1LINE = 0x00;
	private static final int LCD_5x10DOTS = 0x04;
	private static final int LCD_5x8DOTS = 0x00;

	private DigitalOutput rs_pin; // LOW: command. HIGH: character.
	private DigitalOutput enable_pin; // activated by a HIGH pulse.
	private DigitalOutput[] data_pins = new DigitalOutput[4];

	private int displayfunction;
	private int displaycontrol;
	private int displaymode;

	private int numlines;
	private int[] row_offsets = new int[4];

	public LCDScreen(Context context, Pin rs, Pin enable, Pin d0, Pin d1, Pin d2, Pin d3) {
		this.rs_pin = PinFactory.createOutputPin(context, rs, "LCD");
		this.enable_pin = PinFactory.createOutputPin(context, enable, "LCD");
		this.data_pins[0] = PinFactory.createOutputPin(context, d0, "LCD");
		this.data_pins[1] = PinFactory.createOutputPin(context, d1, "LCD");
		this.data_pins[2] = PinFactory.createOutputPin(context, d2, "LCD");
		this.data_pins[3] = PinFactory.createOutputPin(context, d3, "LCD");

		this.displayfunction = LCD_4BITMODE | LCD_1LINE | LCD_5x8DOTS;

		this.begin(16, 1, 0);
	}

	public void begin(int cols, int lines, int dotsize) {
		if (lines > 1) {
			this.displayfunction |= LCD_2LINE;
		}
		this.numlines = lines;

		this.setRowOffsets(0x00, 0x40, 0x00 + cols, 0x40 + cols);

		// for some 1 line displays you can select a 10 pixel high font
		if ((dotsize != LCD_5x8DOTS) && (lines == 1)) {
			displayfunction |= LCD_5x10DOTS;
		}

		// SEE PAGE 45/46 FOR INITIALIZATION SPECIFICATION!
		// according to datasheet, we need at least 40 ms after power rises above 2.7 V
		// before sending commands. Arduino can turn on way before 4.5 V so we'll wait
		// 50
		this.delayMicroseconds(50000);
		// Now we pull both RS and R/W low to begin commands
		this.rs_pin.low();
		this.enable_pin.low();
		// put the LCD into 4 bit mode
		// this is according to the Hitachi HD44780 datasheet
		// figure 24, pg 46

		// we start in 8bit mode, try to set 4 bit mode
		this.write4bits(0x03);
		this.delayMicroseconds(4500); // wait min 4.1ms

		// second try
		this.write4bits(0x03);
		this.delayMicroseconds(4500); // wait min 4.1ms

		// third go!
		this.write4bits(0x03);
		this.delayMicroseconds(150);

		// finally, set to 4-bit interface
		this.write4bits(0x02);

		// finally, set # lines, font size, etc.
		this.command(LCD_FUNCTIONSET | displayfunction);

		// turn the display on with no cursor or blinking default
		this.displaycontrol = LCD_DISPLAYON | LCD_CURSOROFF | LCD_BLINKOFF;
		this.display();

		// clear it off
		this.clear();

		// Initialize to default text direction (for romance languages)
		this.displaymode = LCD_ENTRYLEFT | LCD_ENTRYSHIFTDECREMENT;
		// set the entry mode
		this.command(LCD_ENTRYMODESET | displaymode);

	}
	
	public void setCursor(int col, int row)
	{
	  int max_lines = row_offsets.length;
	  if ( row >= max_lines ) {
	    row = max_lines - 1;    // we count rows starting w/ 0
	  }
	  if ( row >= numlines ) {
	    row = numlines - 1;    // we count rows starting w/ 0
	  }
	  
	  this.command(LCD_SETDDRAMADDR | (col + this.row_offsets[row]));
	}

	public void setRowOffsets(int row0, int row1, int row2, int row3) {
		this.row_offsets[0] = row0;
		this.row_offsets[1] = row1;
		this.row_offsets[2] = row2;
		this.row_offsets[3] = row3;
	}

	public void clear() {
		this.command(LCD_CLEARDISPLAY); // clear display, set cursor position to zero
		this.delayMicroseconds(2000); // this command takes a long time!
	}

	private void delayMicroseconds(int microseconds) {
		PIGPIO.gpioDelay(microseconds);
	}

	public void home() {
		this.command(LCD_RETURNHOME); // set cursor position to zero
		this.delayMicroseconds(2000); // this command takes a long time!
	}

	public boolean write(int value) {
		this.send(value, HIGH);
		return true;
	}
	
	public void print(String s) {
		for(int x = 0; x < s.length(); x++) {
			if(x == 16) {
				this.setCursor(0, 1);
			}
			if(x == 32) {
				break;
			} 
			this.write(s.charAt(x));
		}
		
	}

	// Turns the underline cursor on/off
	public void noCursor() {
		this.displaycontrol &= ~LCD_CURSORON;
		this.command(LCD_DISPLAYCONTROL | this.displaycontrol);
	}

	public void cursor() {
		this.displaycontrol |= LCD_CURSORON;
		this.command(LCD_DISPLAYCONTROL | this.displaycontrol);
	}

	// Turn on and off the blinking cursor
	public void noBlink() {
		this.displaycontrol &= ~LCD_BLINKON;
		this.command(LCD_DISPLAYCONTROL | this.displaycontrol);
	}

	public void blink() {
		this.displaycontrol |= LCD_BLINKON;
		this.command(LCD_DISPLAYCONTROL | this.displaycontrol);
	}

	// These this.commands scroll the display without changing the RAM
	public void scrollDisplayLeft() {
		this.command(LCD_CURSORSHIFT | LCD_DISPLAYMOVE | LCD_MOVELEFT);
	}

	public void scrollDisplayRight() {
		this.command(LCD_CURSORSHIFT | LCD_DISPLAYMOVE | LCD_MOVERIGHT);
	}

	// This is for text that flows Left to Right
	public void leftToRight() {
		this.displaymode |= LCD_ENTRYLEFT;
		this.command(LCD_ENTRYMODESET | this.displaymode);
	}

	// This is for text that flows Right to Left
	public void rightToLeft() {
		this.displaymode &= ~LCD_ENTRYLEFT;
		this.command(LCD_ENTRYMODESET | this.displaymode);
	}

	// This will 'right justify' text from the cursor
	public void autoscroll() {
		this.displaymode |= LCD_ENTRYSHIFTINCREMENT;
		this.command(LCD_ENTRYMODESET | this.displaymode);
	}

	// This will 'left justify' text from the cursor
	public void noAutoscroll() {
		this.displaymode &= ~LCD_ENTRYSHIFTINCREMENT;
		this.command(LCD_ENTRYMODESET | this.displaymode);
	}

	// Allows us to fill the first 8 CGRAM locations
	// with custom characters
	public void createChar(int location, int charmap[]) {
		location &= 0x7; // we only have 8 locations 0-7
		this.command(LCD_SETCGRAMADDR | (location << 3));
		for (int i = 0; i < 8; i++) {
			write(charmap[i]);
		}
	}

	private void command(int value) {
		this.send(value, LOW);
	}

	private void send(int value, int mode) {
		if (mode == HIGH)
			this.rs_pin.high();
		else
			this.rs_pin.low();
		this.write4bits(value >> 4);
		this.write4bits(value);
	}

	public void write4bits(int value) {
		for (int i = 0; i < 4; i++) {
			if (((value >> i) & 0x01) == 1) {
				this.data_pins[i].high();
			} else {
				this.data_pins[i].low();
			}
		}

		this.pulseEnable();
	}

	public void pulseEnable() {
		this.enable_pin.low();
		this.delayMicroseconds(1);
		this.enable_pin.high();
		this.delayMicroseconds(1); // enable pulse must be >450 ns
		this.enable_pin.low();
		this.delayMicroseconds(100); // commands need >37 us to settle
	}

	public void noDisplay() {
		this.displaycontrol &= ~LCD_DISPLAYON;
		this.command(LCD_DISPLAYCONTROL | this.displaycontrol);
	}

	public void display() {
		this.displaycontrol |= LCD_DISPLAYON;
		this.command(LCD_DISPLAYCONTROL | this.displaycontrol);
	}
}
