package turns;

import java.util.Iterator;
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
	
	private class RunnableTurn extends Turn implements Prepareable, EndSteppable {

		private final static int PHASE_NOT_STARTED = 0;
		private final static int PHASE_PREPARE = 1;
		private final static int PHASE_STEP = 2;
		private final static int PHASE_END_STEP = 3;
		private final static int PHASE_FINISHED = 4;
		private int phase = PHASE_NOT_STARTED;
		private Iterator<Sensor> iter;
		
		private RunnableTurn(TreeSet<Sensor> sensors) {
			super(sensors, SensorComparator.SORT_BY_TURNS);
		}
		
		public void tick() {
			if(phase == PHASE_FINISHED) {
				return;
			}
			if(iter == null) {
				iter = sensors.descendingIterator();
			}
			if(!iter.hasNext()) {
				return;
			}
			Sensor sen = iter.next();
			
			switch(phase) {
			case PHASE_NOT_STARTED:
				phase = PHASE_PREPARE;
			case PHASE_PREPARE:
				sen.prepare();
				break;
			case PHASE_STEP:
				sen.step();
				break;
			case PHASE_END_STEP:
				sen.endStep();
				break;
			case PHASE_FINISHED:
			default:
				break;
			}
		}

		public void prepare() {
			switch(phase) {
			case PHASE_NOT_STARTED:
				phase = PHASE_PREPARE;
			case PHASE_PREPARE:
				doLoop();
				break;
			default:
				return;
			}
		}

		public void step() {
			switch(phase) {
			case PHASE_NOT_STARTED:
				phase = PHASE_PREPARE;
			case PHASE_PREPARE:
				prepare();
			case PHASE_STEP:
				doLoop();
				break;
			default:
				return;
			}
		}

		public void endStep() {
			switch(phase) {
			case PHASE_NOT_STARTED:
			case PHASE_PREPARE:
				prepare();
			case PHASE_STEP:
				step();
			case PHASE_END_STEP:
				doLoop();
				break;
			default:
				return;
			}
		}
		
		private void doLoop() {
			if(iter == null) {
				iter = sensors.descendingIterator();
			}
			while(iter.hasNext()) {
				tick();
			}
			phase++;
			iter = sensors.descendingIterator();
		}
	}
	
}
