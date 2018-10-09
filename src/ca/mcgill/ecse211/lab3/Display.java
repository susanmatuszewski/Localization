package ca.mcgill.ecse211.lab3;

import lejos.hardware.lcd.TextLCD;

/**
 * This class is used to display the content of the odometer variables (x, y, Theta)
 */
public class Display {
	
	private TextLCD lcd;
	private final long DISPLAY_PERIOD = 25;
	private long timeout = Long.MAX_VALUE;
	
	/**	   
	 * This is the class constructor
     * 
	 * @param odoData
     * @throws OdometerExceptions 
	 */
	public Display (TextLCD lcd, long timeout) {
		this.timeout = timeout;
		this.lcd = lcd;
	}
	
	public void run() {
		lcd.clear();
		
		long updateStart, updateEnd;
		
		long tStart = System.currentTimeMillis();
		do {
			updateStart = System.currentTimeMillis();
			
			//whatever needs to be printed
			
			updateEnd = System.currentTimeMillis();
			if (updateEnd - updateStart < DISPLAY_PERIOD) {
				try {
					Thread.sleep(DISPLAY_PERIOD - (updateEnd - updateStart));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} while ((updateEnd - tStart) <= timeout);
		
	}
	

}
