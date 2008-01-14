package gui;

import nodes.Sensor;

/**
 * This class keeps track of where a sensor is placed, so it's easier to find it (if the user clicks on the screen and want to select the closest sensor).
 * To be able to do that easier (instead of using Phytagoras on every sensor and then select the closest), it splits the screen up in smaller blocks.
 * Every time (in this case) 10 sensors is placed in the same block, it splits in four equal sized blocks, and the registrering of sensors goes on.
 * When 10 sensors is detected in one of the new smaller blocks, that one will split in four new blocks, and so on.
 * In taht way, when the user clicks somewhere on the screen, only che closests blocks will be searched for the closest sensor.
 * @author Morten Soerensen
 */
public class SplitScreen {
	
	/**
	 * Integer array containing a list over sensors.
	 */
	private int[] SL;
	/**
	 * Integer containing the number of sensors.
	 */
	private int size;
	/**
	 * An instance of this class, to house all the minions.
	 */
	private SplitScreen[] ss;
	/**
	 * The minimum x coordinate.
	 */
	private int xMin;
	/**
	 * The maximum x coordinate.
	 */
	private int xMax;
	/**
	 * The minimum y coordinate.
	 */
	private int yMin;
	/**
	 * The maximum y coordinate.
	 */
	private int yMax;
	
	
	/**
	 * The constructor the this class.
	 * When this class is initialized, it will make the integer array and set it's size to 10 (slot 0 to 9).
	 * @param xMin the minimum x coordinate
	 * @param xMax the maximum x coordinate
	 * @param yMin the minimum y coordinate
	 * @param yMax the maximum y coordinate
	 */
	public SplitScreen(int xMin, int xMax, int yMin, int yMax) {
		SL = new int[10];
		size = 0;
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;
	}

	/**
	 * A private constructor for this class. The only time this can be called, is when the window splits.
	 * @param splitMe the parent class
	 * @param block the block number. This will be used to know, how to calculate the new block's coordinates
	 * @param sensor the sensor number that caused the array SL to exceed its limit
	 */
	private SplitScreen(SplitScreen splitMe, int block, int sensor) {
		switch(block) {
		case 0:
			//upper left block
			this.xMin = splitMe.xMin;
			this.xMax = splitMe.xMax/2;
			this.yMin = splitMe.yMin;
			this.yMax = splitMe.yMax/2;
			break;
		case 1:
			//upper right block
			this.xMin = (splitMe.xMax/2)+1;
			this.xMax = splitMe.xMax;
			this.yMin = splitMe.yMin;
			this.yMax = splitMe.yMax/2;
			break;
		case 2:
			//lower left block
			this.xMin = splitMe.xMin;
			this.xMax = splitMe.xMax/2;
			this.yMin = (splitMe.yMax/2)+1;
			this.yMax = splitMe.yMax;
			break;
		case 3:
			//lower right block
			this.xMin = (splitMe.xMax/2)+1;
			this.xMax = splitMe.xMax;
			this.yMin = (splitMe.yMax/2)+1;
			this.yMax = splitMe.yMax;
			break;
		}
		this.SL[0] = sensor;
		this.size = 1;
	}
	
	/**
	 * Adds a sensor to the integer array.
	 * @param sensor to be added
	 */
	public void addSensor(Sensor sensor) {
		if(ss != null) {
			
		} else if(size == SL.length) {
			ss = new SplitScreen[4];
			for(int i = 0 ; i < 4 ; i++){
				ss[i] = new SplitScreen(this,i, sensor.id);
			}
			//TODO
		} else {
			SL[size] = sensor.id;
			size++;
		}
	}
	
	/**
	 * Removed a sensor from the integer array.
	 * @param sensor to be removed
	 */
	public void removeSensor(int sensor) {
		
	}
}
