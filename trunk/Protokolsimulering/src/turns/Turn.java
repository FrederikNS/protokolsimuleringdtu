package turns;

import java.awt.Graphics;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

import nodes.Sensor;
import nodes.Sensor.SensorComparator;
import nodes.Sensor.SensorImplementation;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import exceptions.XMLParseException;

import shape.Drawable;
import xml.DOMxmlParser;
import xml.Saveable;

/**
 * Turn contains data about each turn (between every step).
 * Turns should only be constructed directly after a full step() finishes
 * @author Niels Thykier
 */
public class Turn implements Saveable, Drawable{
	final TreeSet<SensorImplementation> sensors;
	public final int turn;
	
	protected Turn(Collection<? extends Sensor> sensors, int sortBy, int turn, boolean roll) {
		if(roll) {
			Sensor.rollTurnOrder();
		}
		this.sensors = new TreeSet<SensorImplementation>(new SensorComparator(sortBy));
		for(Sensor sen : sensors) {
			this.sensors.add(sen.getReal());
		}
		this.turn = turn;
	}

	Turn(Collection<? extends Sensor> sensor, int sortBy, int turn) {
		this.sensors = new TreeSet<SensorImplementation>(new SensorComparator(sortBy));
		for(Sensor sen : sensor) {
			this.sensors.add((SensorImplementation)sen.getReal().clone());
		}		
		this.turn = turn;
	}
	
	
	public RunnableTurn getRunnableTurn() {
		return new RunnableTurn(Sensor.idToSensor.values(), this.turn);
	}
	
	public static Turn loadFromXMLElement(Node turnNode) throws XMLParseException {
		boolean runTurn = turnNode.getNodeName().equals("runnableTurn");
		if(turnNode.getNodeType() != Node.ELEMENT_NODE || (!runTurn && !turnNode.getNodeName().equals("turn"))) {
			throw new IllegalArgumentException("Node was not a turn nor a runnableTurn tag");
		}
		NodeList list = turnNode.getChildNodes();
		Node current;
		int size = list.getLength();
		Integer turnNo;
		Short phase = null;
		Integer currentSensorID = null;
		try {
			turnNo = Integer.parseInt(((Element) turnNode).getAttribute("turn"));
			if(turnNo == null) {
				throw new XMLParseException("Invalid or missing turn attribute.");
			}
		} catch(Exception e) {
			throw new XMLParseException("Invalid or missing turn attribute.");
		}
		TreeSet<Sensor> sensorTree = new TreeSet<Sensor>();
		Turn toReturn = null;
		for(int i = 0; i < size ; i++) {
			current = list.item(i);
			switch(current.getNodeName().charAt(0)) {
			case 's':
				sensorTree.add(SensorImplementation.loadFromXMLElement(current));
				break;
			case 'c':
				if(current.getNodeName().equals("currentSensor")) {
					try {
						currentSensorID = Integer.parseInt(DOMxmlParser.getTextNodeValue(current).trim());
					} catch(Exception e) {
						throw new XMLParseException("phase tag was malformatted.");
					}
				}
				break;
			case 'p':
				if(current.getNodeName().equals("phase")) {
					try {
						phase = Short.parseShort(DOMxmlParser.getTextNodeValue(current).trim());
					} catch(Exception e) {
						throw new XMLParseException("phase tag was malformatted.");
					}
				}
				break;
			}
		}
		if(sensorTree.size() == 0) {
			throw new XMLParseException("turn contained no sensors?");
		}
		toReturn = new Turn(sensorTree, SensorComparator.SORT_BY_TURNS, turnNo,false);
		if(runTurn) {
			RunnableTurn runner = toReturn.getRunnableTurn();
			runner.phase = phase;
			if(currentSensorID != null) {
				runner.isRunning = true;
				runner.iter = runner.sensors.descendingIterator();
				SensorImplementation sen;
				while(runner.iter.hasNext()) {
					sen = runner.iter.next();
					if(sen.id == currentSensorID) {
						runner.current = sen;
						break;
					}
				}
			} else {
				runner.isRunning = false;
			}
			toReturn = runner;
		} 
		return toReturn;
	}
	
	public Element generateXMLElement(Document doc) {
		Element turnElement;
		System.err.println("Saving..." + this);
		if(this instanceof RunnableTurn) {
			turnElement = doc.createElement("runnableTurn");
			RunnableTurn run = (RunnableTurn) this;
			turnElement.setAttribute("turn", String.valueOf(turn));
			turnElement.setIdAttribute("turn", true);
			Element phaseElement = doc.createElement("phase");
			phaseElement.appendChild(doc.createTextNode(String.valueOf(run.phase)));
			turnElement.appendChild(phaseElement);
			if(run.current!= null) {
				Element currentSensorElement = doc.createElement("currentSensor");
				currentSensorElement.appendChild(doc.createTextNode(String.valueOf(run.current.id)));
				turnElement.appendChild(currentSensorElement);
			}
		}else {
			turnElement = doc.createElement("turn");
		}
		turnElement.setAttribute("turn", String.valueOf(turn));
		turnElement.setIdAttribute("turn", true);

		return generateInnerXMLElement(turnElement, doc);
	}
	
	protected Element generateInnerXMLElement(Element outerElement, Document doc) {
		Element sensorElement;
		int i = sensors.size();
		for(Sensor sen : sensors) {
			sensorElement = sen.generateXMLElement(doc);
			sensorElement.setAttribute("initiative", String.valueOf(i--));
			outerElement.appendChild(sensorElement);
		}
		return outerElement;
	}
	

	public void draw(Graphics g) {
		for(Sensor sen : sensors) {
			sen.draw(g);
		}
	}
	
	public static class RunnableTurn extends Turn implements Prepareable, EndSteppable {

		public final static short PHASE_NOT_STARTED = 0;
		public final static short PHASE_PREPARE = 1;
		public final static short PHASE_STEP = 2;
		public final static short PHASE_END_STEP = 3;
		public final static short PHASE_FINISHED = 4;
		private short phase = PHASE_NOT_STARTED;
		boolean isRunning = false;
		protected SensorImplementation current;
		private Iterator<SensorImplementation> iter;
		
		private RunnableTurn(Collection<? extends Sensor> sensors, int turn) {
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
	}
	
}
