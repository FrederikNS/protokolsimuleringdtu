package nodes;

import nodes.Sensor;

/**
 * This class keeps track of where a sensor is placed, so it's easier to find it (if the user clicks on the screen and want to select the closest sensor).
 * To be able to do that easier (instead of using Phytagoras on every sensor and then select the closest), it splits the screen up in smaller blocks.
 * Every time (in this case) 10 sensors is placed in the same block, it splits in four equal sized blocks, and the registrering of sensors goes on.
 * When 10 sensors is detected in one of the new smaller blocks, that one will split in four new blocks, and so on.
 * In that way, when the user clicks somewhere on the screen, only che closests blocks will be searched for the closest sensor.
 * @author Morten Soerensen
 */
public class SplitField {

	/**
	 * Sensor array containing a list over sensors.
	 */
	private Sensor[] sensorList;
	/**
	 * Integer containing the number of sensors.
	 */
	private int size;
	/**
	 * An instance of this class, to house all the minions.
	 */
	private SplitField[] splitField;
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
	 * Constant for the add-command.
	 */
	private static final int ADD = 1;
	/**
	 * Constant for the remove-command.
	 */
	private static final int REMOVE = 2;


	/**
	 * The constructor the this class.
	 * When this class is initialized, it will make the integer array and set it's size to 10 (slot 0 to 9).
	 * @param xMin the minimum x coordinate
	 * @param xMax the maximum x coordinate
	 * @param yMin the minimum y coordinate
	 * @param yMax the maximum y coordinate
	 */
	public SplitField(int xMin, int xMax, int yMin, int yMax) {
		sensorList = new Sensor[10];
		size = 0;
		splitField = null;
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;
	}

	/**
	 * A private constructor for this class. The only time this can be called, is when the window splits.
	 * @param splitMe the parent class
	 * @param block the block number. This will be used to know, how to calculate the new block's coordinates
	 */
	private SplitField(SplitField splitMe, int block) {
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
		this.size = 0;
		sensorList = new Sensor[10];
	}

	/**
	 * Adds a sensor to the sensor array.
	 * @param sensor to be added
	 */
	public void addSensor(Sensor sensor) {
		executeCommand(sensor, ADD);
	}

	/**
	 * Removed a sensor from the sensor array.
	 * @param sensor to be removed
	 */
	public void removeSensor(Sensor sensor) {
		executeCommand(sensor, REMOVE);
	}

	/**
	 * This method go through the correct blocks to find the sensor closest to where the user clicked.
	 * @param loc the location of where the user clicked (scaled so it fits the window)
	 * @param dist the maximal distance from where the sensor is, to where it can be selected
	 * @return the closest sensor (null if none is close enought)
	 */
	public Sensor selectSensor(Location loc, int dist) {
		Sensor toReturn = null;
		if(splitField != null) {
			Sensor[] sensorArray = new Sensor[4];
			int[] sectorCheckList = {-1,-1,-1,-1};
			int amountToAsk = -1;
			if(((loc.getX()-dist) < this.xMax/2) && ((loc.getY()-dist) < this.yMax/2)) {
				sectorCheckList[++amountToAsk] = 0;
			}
			if(((loc.getX()+dist) > this.xMax/2) && ((loc.getY()-dist) < this.yMax/2)) {
				sectorCheckList[++amountToAsk] = 1;
			}
			if(((loc.getX()-dist) < this.xMax/2) && ((loc.getY()+dist) > this.yMax/2)) {
				sectorCheckList[++amountToAsk] = 2;
			}
			if(((loc.getX()+dist) > this.xMax/2) && ((loc.getY()+dist) > this.yMax/2)) {
				sectorCheckList[++amountToAsk] = 3;
			}
			/*if(loc.getX()+dist < this.xMax/2) {
				if(loc.getY()+dist < this.yMax/2) {
					sectorCheckList[++amountToAsk] = 0;
				} else {
					sectorCheckList[++amountToAsk] = 2;
				}
			} else {
				if(loc.getY()+dist < this.yMax/2) {
					sectorCheckList[++amountToAsk] = 1;
				} else {
					sectorCheckList[++amountToAsk] = 3;
				}
			}*/
			Sensor test = null;
			int k = 0;
			for(int j = 0 ; j < sectorCheckList.length ; j++) {
				if(sectorCheckList[j] == -1) {

				} else {
					test = splitField[sectorCheckList[j]].selectSensor(loc, dist);
					if (test != null) {
						sensorArray[k++]=test;
					}
				}
			}
			toReturn = returnSensor(sensorArray, k, dist, loc);
		} else {
			toReturn = returnSensor(sensorList, size, dist, loc);
		}
		return toReturn;
	}

	/**
	 * This method calculates the distance from the selected sensors, and returns the closest sensor (if the distance is less than max).
	 * @param list the array in which it should search
	 * @param length the length of the array
	 * @param dist the maximal distance from where the sensor is, to where it can be selected
	 * @param loc the location of where the user clicked (scaled so it fits the window)
	 * @return the closest sensor or null, if no sensor was within dist
	 */
	private Sensor returnSensor(Sensor[] list, int length, int dist, Location loc) {
		Sensor toReturn = null;
		int currentDistance = 0;
		int lowestDistance = (int) Math.pow(dist, 2);
		for(int i = 0 ; i < length ; i++) {
			currentDistance = list[i].internalDistanceCheck(loc);
			if(currentDistance < lowestDistance) {
				lowestDistance = currentDistance;
				toReturn = sensorList[i];
			}
		}
		return toReturn;
	}

	/**
	 * This method is for adding or removing a sensor from the field.
	 * @param sensor the sensor in question
	 * @param flag the flag used to decide what to do
	 */
	private void executeCommand(Sensor sensor, int flag) {
		if(splitField != null) {
			int i = getBlock(sensor);
			if(flag == REMOVE) {
				splitField[i].removeSensor(sensor);
			} else {
				splitField[i].addSensor(sensor);
			}
		} else if((size == sensorList.length) && (flag == ADD)) {
			splitField = new SplitField[4];
			for(int i = 0 ; i < 4 ; i++){
				splitField[i] = new SplitField(this,i);
			}
			for(int j = 0 ; j < sensorList.length ; j++) {
				int s = getBlock(sensorList[j]);
				splitField[s].addSensor(sensorList[j]);
			}
			size++;
		} /*else if( TODO - check if the number of sensors is == 10, then kill the children) {

		}*/	else {
			if(flag == REMOVE) {
				sensorList[--size] = null;
			} else {
				sensorList[size] = sensor;
				size++;
			}
		}
	}

	/**
	 * This method checks which block it should send the sensor to.
	 * @param sensor the sensor in question
	 * @return the integer value of the correct block
	 */
	private int getBlock(Sensor sensor) {
		int i;
		if(sensor.getX() < this.xMax/2) {
			if(sensor.getY() < this.yMax/2) {
				i = 0;
			} else {
				i = 2;
			}
		} else {
			if(sensor.getY() < this.yMax/2) {
				i = 1;
			} else {
				i = 3;
			}
		}
		return i;
	}
}