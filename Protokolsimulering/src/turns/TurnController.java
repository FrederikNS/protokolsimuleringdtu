package turns;

import nodes.Sensor;
import nodes.Sensor.SensorComparator;
import notification.Note;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import exceptions.XMLParseException;

import turns.Turn.RunnableTurn;
import xml.Saveable;

public abstract class TurnController implements Saveable{

	protected static TurnController instance;
	
	/**
	 * Call when the field has been altered.
	 */
	public abstract void fieldHasBeenAltered();
	
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
	
	public abstract void playTickBackwards();
	
	public abstract Turn getCurrentTurn();
	
	public abstract TurnController loadFromXMLElement(Node turnControllerNode) throws XMLParseException;
	
	/**
	 * The TurnController will attempt to go to the given turn. If it jumps forward it may have to 
	 * calculate all the turns inbetween. If it goes back it must keep a history and may have to 
	 * reload that turn.
	 * @param turn The turn number.
	 * @return true, if it could jump to that turn successfully.
	 */
	public abstract boolean goToTurn(int turn);

	
	/* (non-Javadoc)
	 * @see xml.Saveable#generateXMLElement(org.w3c.dom.Document)
	 */
	public abstract Element generateXMLElement(Document doc);
	
	public static TurnController getInstance() {
		if(instance == null) {
			instance = new TurnControllerImplementation();
		}
		return instance;
	}
	
	private static class TurnControllerImplementation extends TurnController {

		private Turn[] turnList = new Turn[10];
		private short currentEntry = -1;
		private int currentTurn = -1;
		private RunnableTurn run;
		private boolean notReady = true;
		
		private RunnableTurn getCurrentRunnableTurn() {
			if(notReady) {
				fieldHasBeenAltered();
			}
			if(run == null) {
				run = turnList[currentEntry].getRunnableTurn();
			}
			return run;
		}

		@Override
		public boolean goToTurn(int turn) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean playPreparePhase() {
			RunnableTurn turn = getCurrentRunnableTurn();
			if(turn.getPhase() > RunnableTurn.PHASE_PREPARE){
				return false;
			}
			turn.prepare();
			return true;
		}

		@Override
		public void playTick() {
			RunnableTurn turn = getCurrentRunnableTurn();
			turn.tick();
			if(turn.getPhase() == RunnableTurn.PHASE_FINISHED) {
				endOfTurn();
			}
		}

		private void endOfTurn() {
			Note.sendNote("End of Turn: " + currentTurn);
			currentEntry++;
			if(currentEntry == turnList.length) {
				Turn[] temp = new Turn[turnList.length];
				for(int i = 5; i < turnList.length ; i++) {
					temp[i-5] = turnList[i];
				}
				turnList = temp;
				currentEntry = 5;
			} 
			currentTurn++;
			turnList[currentEntry] = new Turn(run.sensors, SensorComparator.SORT_BY_TURNS, currentTurn);
			run = null;
		}
		
		@Override
		public void fieldHasBeenAltered() {
			if(this.currentTurn < 0) {
				currentTurn = 0;
				currentEntry = 0;
				turnList[currentEntry] = new Turn(Sensor.idToSensor.values(), SensorComparator.SORT_BY_TURNS, currentTurn);
			}
			notReady = false;
		}
		
		@Override
		public Element generateXMLElement(Document doc) {
			Element turnListNode = doc.createElement("turnController");
			if(run != null) {
				turnListNode.appendChild(run.generateRunnableTurnXMLElement(doc));
			}
			if(currentEntry > -1) {
				turnListNode.appendChild(turnList[currentEntry].generateXMLElement(doc));
			}
			return turnListNode;
		}
		
		@Override
		public TurnController loadFromXMLElement(Node turnControllerNode) throws XMLParseException {
			NodeList list = turnControllerNode.getChildNodes();
			int length = list.getLength();
			Node current ; 
			TurnControllerImplementation controller = new TurnControllerImplementation();
			tagLoop:
			for(int i = 0 ; i < length ; i++) {
				current = list.item(i);
				switch(current.getNodeName().charAt(0)) {
				case 'r':
					if(current.getNodeName().equals("runningTurn")) {
						controller.notReady = false;
						try {
							controller.run = (RunnableTurn) Turn.loadFromXMLElement(current);
						} catch(ClassCastException e) {
							throw new XMLParseException("runningTurn did not contain a running turn!");
						}
						controller.currentEntry = 0;
						controller.turnList[0] = controller.run;
						break tagLoop;
					}
					break;
				case 't':
					if(current.getNodeName().equals("turn")) {
						controller.currentEntry = 0;
						controller.turnList[0] = Turn.loadFromXMLElement(current);
						break tagLoop;
					}
					break;
				}
			}
			controller.currentTurn = turnList[0].turn;
			instance = controller;
			return instance;
		}

		@Override
		public Turn getCurrentTurn() {
			if(currentEntry < 0){
				return null;
			}
			return turnList[currentEntry];
		}

		@Override
		public void playTickBackwards() {
			// Do nothing - not going to be implemented
		}



		
	}
}
