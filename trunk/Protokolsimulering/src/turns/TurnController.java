package turns;

import java.awt.Graphics;
import java.util.HashMap;

import exceptions.LabelNotRecognizedException;

import graphics.Drawable;
import nodes.Sensor;

public abstract class TurnController implements Drawable{
	
	/**
	 * Handles labelling.
	 */
	private HashMap<String,Integer> sensorLabel;
	
	/**
	 * Play the next unit's prepare or step phase (depending on what turn 
	 */
	public abstract void playTick();
	/**
	 * Play out the complete prepare phase of all units.
	 */
	public abstract void playPrepareFase();
	/**
	 * Play the whole turn.
	 * If the prepare phase has not been played, it will be played.
	 */
	public abstract void playTurn();
	/**
	 * Adds a Sensor to the calculations
	 * @param toAdd The sensor
	 * @return true if could be added.
	 */
	public abstract boolean addSensor(Sensor toAdd);
	/**
	 * Fetches a sensor by ID.
	 * @param id The id of the sensor.
	 * @return The sensor
	 */
	public abstract Sensor getSensor(int id);
	
	/**
	 * Fetches a sensor's ID by its label. 
	 * Sensors do not have a label unless one has been assigned to them!
	 * @param label The label.
	 * @return The id of the sensor.
	 * @throws LabelNotRecognizedException If no sensor has been assigned that label.
	 */
	public int getSensorID(String label) throws LabelNotRecognizedException {
		Integer id = sensorLabel.get(label);
		if(id != null) {
			return id;
		}
		throw new LabelNotRecognizedException("The label " + label + " is not recognized.");
	}

	/**
	 * Fetches a sensors by its label. 
	 * Sensors do not have a label unless one has been assigned to them!
	 * @param label The label.
	 * @return The Sensor
	 * @throws LabelNotRecognizedException If no sensor has been assigned that label.
	 */
	public Sensor getSensor(String label) throws LabelNotRecognizedException {
		return getSensor(getSensorID(label));
	}
	
	public abstract boolean assignLabel(String labal, int id);
	
	public abstract void draw(Graphics g);
	public abstract void goToTurn(int turn);
	
}
