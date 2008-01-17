package turns;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import nodes.Sensor;
import nodes.Sensor.SensorComparator;

/**
 * Turn contains data about each turn (between every step).
 * Turns should only be constructed directly after a full step() finishes
 * @author Niels Thykier
 */
public class Turn {
	public final TreeSet<Sensor> sensors;
	public final int turn;
	private static int turnsCreated = 0;
	
	public Turn(Collection<Sensor> sensors) {
		this(sensors, SensorComparator.SORT_BY_ID, turnsCreated++);

	}
	Turn(Collection<Sensor> sensor, int sortBy, int turn) {
		this.sensors = new TreeSet<Sensor>(new SensorComparator(sortBy));
		this.sensors.addAll(sensors.descendingSet());		
		this.turn = turn;
	}
	
	public RunnableTurn getRunnableTurn() {
		return new RunnableTurn(this.sensors, this.turn);
	}
	
	public Element generateXMLElement(Document doc) {
		Element turnElement = doc.createElement("turn");
		turnElement.setAttribute("turn", String.valueOf(turn));
		turnElement.setIdAttribute("turn", true);

		return generateInnerXMLElement(turnElement, doc);
	}
	
	protected Element generateInnerXMLElement(Element outerElement, Document doc) {
		for(Sensor sen : sensors) {
			outerElement.appendChild(sen.generateXMLTurnElement(doc));
		}
		return outerElement;
	}
	
	
	public class RunnableTurn extends Turn implements Prepareable, EndSteppable {

		public final static short PHASE_NOT_STARTED = 0;
		public final static short PHASE_PREPARE = 1;
		public final static short PHASE_STEP = 2;
		public final static short PHASE_END_STEP = 3;
		public final static short PHASE_FINISHED = 4;
		private short phase = PHASE_NOT_STARTED;
		boolean isRunning = false;
		private Iterator<Sensor> iter;
		
		private RunnableTurn(TreeSet<Sensor> sensors, int turn) {
			super(sensors, SensorComparator.SORT_BY_TURNS, turn);
		}
		
		public short getPhase() {
			return phase;
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
				break;
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
		
		@Override
		public Element generateXMLElement(Document doc) {
			Element turnElement = doc.createElement("runningTurn");
			turnElement.setAttribute("turn", String.valueOf(turn));
			turnElement.setIdAttribute("turn", true);
			
			return generateInnerXMLElement(turnElement, doc);
		}
	}
	
	
	
}
