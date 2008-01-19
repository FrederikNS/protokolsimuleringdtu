package turns;

import java.awt.Graphics;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

import nodes.Sensor;
import nodes.Sensor.SensorComparator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import shape.Drawable;
import xml.Saveable;

/**
 * Turn contains data about each turn (between every step).
 * Turns should only be constructed directly after a full step() finishes
 * @author Niels Thykier
 */
public class Turn implements Saveable, Drawable{
	final TreeSet<Sensor> sensors;
	public final int turn;
	
	/*public Turn(Collection<Sensor> sensors) {
		this(sensors, SensorComparator.SORT_BY_ID, turnsCreated++, false);

	}*/
	Turn(Collection<Sensor> sensor, int sortBy, int turn, boolean roll) {
		if(roll) {
			Sensor.rollTurnOrder();
		}
		this.sensors = new TreeSet<Sensor>(new SensorComparator(sortBy));
		for(Sensor sen : sensor) {
			this.sensors.add(sen.copyRealSensor());
		}		
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
	

	public void draw(Graphics g) {
		for(Sensor sen : sensors) {
			sen.draw(g);
		}
	}
	
	public class RunnableTurn extends Turn implements Prepareable, EndSteppable {

		public final static short PHASE_NOT_STARTED = 0;
		public final static short PHASE_PREPARE = 1;
		public final static short PHASE_STEP = 2;
		public final static short PHASE_END_STEP = 3;
		public final static short PHASE_FINISHED = 4;
		private short phase = PHASE_NOT_STARTED;
		boolean isRunning = false;
		protected Sensor current;
		private Iterator<Sensor> iter;
		
		private RunnableTurn(TreeSet<Sensor> sensors, int turn) {
			super(sensors, SensorComparator.SORT_BY_TURNS, turn, true);
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
			else if(!iter.hasNext()) {
				return;
			}
			if(current != null) {
				current.setHasTurn(false);
			}
			current = iter.next();
			current.setHasTurn(true);
			switch(phase) {
			case PHASE_NOT_STARTED:
				phase = PHASE_PREPARE;
			case PHASE_PREPARE:
				current.prepare();
				break;
			case PHASE_STEP:
				current.step();
				break;
			case PHASE_END_STEP:
				current.endStep();
				break;
			case PHASE_FINISHED:
			default:
				break;
			}
			if(!iter.hasNext()){
				phase++;
				iter = sensors.descendingIterator();
				Sensor.endOfPhase();
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
