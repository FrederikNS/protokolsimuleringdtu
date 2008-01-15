package turns;

import java.util.TreeSet;

import nodes.Sensor;
import nodes.Sensor.SensorComparator;

/**
 * Turn contains data about each turn (between every step).
 * Turns should only be constructed directly after a full step() finishes
 * @author Niels Thykier
 */
public class Turn {
	public final TreeSet<Sensor> sensors;
	boolean isRunning = false;
	
	public Turn(TreeSet<Sensor> sensors) {
		this(sensors ,SensorComparator.SORT_BY_ID);

	}
	private Turn(TreeSet<Sensor> sensor, int sortBy) {
		this.sensors = new TreeSet<Sensor>(new SensorComparator(sortBy));
		this.sensors.addAll(sensors.descendingSet());		
	}
	
	public RunnableTurn getRunnableTurn() {
		return new RunnableTurn(this.sensors);
	}
	
	private class RunnableTurn extends Turn {

		private RunnableTurn(TreeSet<Sensor> sensors) {
			super(sensors, SensorComparator.SORT_BY_TURNS);
		}
		
		
	}
	
}
