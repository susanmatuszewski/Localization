package ca.mcgill.ecse211.lab3;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;

public class Lab4 {
	
	//initialize motor objects
	private static final EV3LargeRegulatedMotor leftMotor = 
			new EV3LargeRegulatedMotor(LocalEV3.get().getPort("A"));
	private static final EV3LargeRegulatedMotor rightMotor = 
			new EV3LargeRegulatedMotor(LocalEV3.get().getPort("B"));
	private static final TextLCD lcd = LocalEV3.get().getTextLCD();
	
	public static final double WHEEL_RAD = 2.2;
	public static final double TRACK = 12.075 ;
	
	public static void main (String[] args) {
		int buttonChoice;
		
		do {
			//clear display
			lcd.clear();
			
			lcd.drawString("< Left | Right >", 0, 0);
			lcd.drawString("                ", 0, 1);
			lcd.drawString("Rising | Falling", 0, 2);
			lcd.drawString("  Edge | Edge   ", 0, 3);
			
			buttonChoice = Button.waitForAnyPress();
		} while (buttonChoice != Button.ID_LEFT && buttonChoice != Button.ID_RIGHT);
		
		//Rising edge
		if (buttonChoice == Button.ID_LEFT) {
			
		}
		//Falling edge
		else {
			
		}
	}
	
	

}
