package turns;

import java.awt.Graphics;

import graphics.Drawable;
import nodes.Sensor;

public abstract class TurnController implements Drawable{
	
	/**
	 * Play the next unit's prepare or step phase (depending on what part of the turn it is).
	 * If a turn was finished, a new one will be started. 
	 */
	public abstract void playTick();
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
