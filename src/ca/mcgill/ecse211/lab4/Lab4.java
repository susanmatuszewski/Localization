package ca.mcgill.ecse211.lab4;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;

public class Lab4 {
	
	//initialize motor objects
	private static final EV3LargeRegulatedMotor leftMotor = 
			new EV3LargeRegulatedMotor(LocalEV3.get().getPort("A"));
	private static final EV3LargeRegulatedMotor rightMotor = 
			new EV3LargeRegulatedMotor(LocalEV3.get().getPort("B"));
	private static final TextLCD lcd = LocalEV3.get().getTextLCD();
	public static final EV3ColorSensor colorPort = new EV3ColorSensor(LocalEV3.get().getPort("S1"));
	private static final Port usPort = LocalEV3.get().getPort("S2");
	
	public static final double WHEEL_RAD = 2.2;
	public static final double TRACK = 12.075 ;
	public static final int FORWARD_SPEED = 175;
	public static final int ROTATE_SPEED = 75;
	
	public static String edge;
	
	public static void main (String[] args) throws OdometerExceptions, InterruptedException {
		int buttonChoice;
		
		SensorModes usSensor = new EV3UltrasonicSensor(usPort);
		SampleProvider us = usSensor.getMode("Distance");
		float[] usData = new float[us.sampleSize()];
		
		Odometer odometer = Odometer.getOdometer(leftMotor, rightMotor, TRACK, WHEEL_RAD);
		Display odometryDisplay = new Display(lcd);
		
		UltrasonicLocalizer UltrasonicLocalizer = new UltrasonicLocalizer(leftMotor, rightMotor, us, usData, odometer);
		LightLocalizer ColorSensor = new LightLocalizer(leftMotor, rightMotor, odometer, colorPort);
		
		
		Thread odometryThread = new Thread(odometer);
		Thread odometryDisplayThread = new Thread(odometryDisplay);
		Thread UltrasonicLocalizerThread = new Thread(UltrasonicLocalizer);
		UltrasonicPoller usPoller = null;

		Thread ColorSensorThread = new Thread(ColorSensor);
		
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
			lcd.clear();
			
			edge = "rising";
			
			usPoller = new UltrasonicPoller(us, usData, UltrasonicLocalizer);

			odometryThread.start();
			odometryDisplayThread.start();
			
			// Start Us Localizer
			UltrasonicLocalizer.start();
			
			// Stop before Light Sensor Localization
			Button.waitForAnyPress();
			
			// kill us localizer thread
			UltrasonicLocalizer.join();
			
			// Start light sensor localization
			ColorSensorThread.start();
		}
		//Falling edge
		else {
			lcd.clear();
			
			edge = "falling";
			
			usPoller = new UltrasonicPoller(us, usData, UltrasonicLocalizer);

			odometryThread.start();
			odometryDisplayThread.start();
			
			// Start Us Localizer
			UltrasonicLocalizer.start();
			
			// Stop before Light Sensor Localization
			Button.waitForAnyPress();
			
			// kill uslocalizer thread
			UltrasonicLocalizer.join(); // kill
			
			// Start light sensor localization
			ColorSensorThread.start();
		}
		
		while (Button.waitForAnyPress() != Button.ID_ESCAPE);
		
		System.exit(0);
	}
	
	

}
