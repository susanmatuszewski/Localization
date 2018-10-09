package ca.mcgill.ecse211.lab4;

import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.robotics.SampleProvider;

//use the us sensor to poll data and process it by detecting
//either the rising or falling edge to figure out where it is
//see Localization tutorial
public class UltrasonicLocalizer extends Thread implements UltrasonicController {
	
	private int distance;
	private Odometer odometer;
	private EV3LargeRegulatedMotor leftMotor;
	private EV3LargeRegulatedMotor rightMotor;
	private SampleProvider us;
	private float[] usData;

	public UltrasonicLocalizer(EV3LargeRegulatedMotor leftmotor, EV3LargeRegulatedMotor rightmotor, SampleProvider us, float[] usData, Odometer odometer) {
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
		this.us = us;
		this.usData = usData;
		this.odometer = odometer;
	}
	
	public void run() {
		while (true) {
		us.fetchSample(usData, 0);
		distance = (int) (usData[0] * 100.0); // get data
		LCD.drawString(Integer.toString(distance), 0, 5);

		if (Lab4.edge.equals("rising")) {
			risingEdge(distance);
		} else {
			fallingEdge(distance);
		}
		}
		
	}
	
	public void fallingEdge(int distance) {
		leftMotor.setSpeed(Lab4.ROTATE_SPEED);
		rightMotor.setSpeed(Lab4.ROTATE_SPEED);
		
		leftMotor.rotate(-convertAngle(Lab4.WHEEL_RAD, Lab4.TRACK, 360), true); // Turn left
		rightMotor.rotate(convertAngle(Lab4.WHEEL_RAD, Lab4.TRACK, 360), true);
		
	}
	
	private static int convertDistance(double radius, double distance) {
	    return (int) ((180.0 * distance) / (Math.PI * radius));
	  }

	  private static int convertAngle(double radius, double width, double angle) {
	    return convertDistance(radius, Math.PI * width * angle / 360.0);
	  }
	  
	public void risingEdge(int distance) {
		
	}

	@Override
	public void processUSData(int distance) {
		// TODO Auto-generated method stub
	}
	

}
