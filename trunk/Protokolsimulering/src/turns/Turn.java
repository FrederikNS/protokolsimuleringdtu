package turns;

import java.util.TreeSet;

import nodes.Sensor;

/**
 * Turn contains data about each turn (between every step).
 * Turns should only be constructed directly after a full step() finishes
 * @author Niels Thykier
 */
public class Turn {
	public final TreeSet<? extends Sensor> sensors; 
	public Turn(TreeSet<? extends Sensor> sensors) {
		this.sensors = sensors;
	}
}
