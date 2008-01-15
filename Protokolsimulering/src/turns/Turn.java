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
		this.sensors = new TreeSet<Sensor>(new SensorComparator(SensorComparator.SORT_BY_ID));
		this.sensors.addAll(sensors.descendingSet());
	}
	
	public class RunnableTurn extends Turn {

		public RunnableTurn(TreeSet<Sensor> sensors) {
			super(sensors);
		}
		
	}
	
}
