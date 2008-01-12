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
	 * Play the next unit's prepare or step phase (depending on what part of the turn it is).
	 * If a turn was finished, a new one will be started. 
	 */
	public abstract boolean playTick();
	/**
	 * Play out the complete prepare phase of all units. This will never advance the time by a turn (or step).
	 * @return false, if the prepare phase could not be played (e.g. it was already over)
	 */
	public abstract boolean playPreparePhase();
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
	
	/**
	 * Assigns a label to a sensor using the sensor's ID.
	 * @param label The new label of the sensor. If null or "", the sensor's current label will be removed.
	 * @param id The id of the sensor.
	 * @return true, if the label was updated.
	 */
	public abstract boolean assignLabel(String label, int id);
	
	public abstract void draw(Graphics g);
	/**
	 * The TurnController will attempt to go to the given turn. If it jumps forward it may have to 
	 * calculate all the turns inbetween. If it goes back it must keep a history and may have to 
	 * reload that turn.
	 * @param turn The turn number.
	 * @return true, if it could jump to that turn successfully.
	 */
	public abstract boolean goToTurn(int turn);
	
}
