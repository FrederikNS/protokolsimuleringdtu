package turns;

import nodes.Sensor;
import nodes.Sensor.SensorComparator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import turns.Turn.RunnableTurn;
import xml.Saveable;
import exceptions.XMLParseException;

/**
 * TurnController is class to handle Turn advancements conviently.
 * @author Niels Thykier
 */
public abstract class TurnController implements Saveable{

	/**
	 * The current active TurnController.
	 */
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
	
	/**
	 * Gets the current turn of the simulation.
	 * @return The current turn.
	 */
	public abstract Turn getCurrentTurn();
	
	/**
	 * Clears all turns from the controller as if they had never been there.
	 * @return The TurnController (for convience)
	 */
	public static TurnController clearAll() {
		return (instance = new TurnControllerImplementation());
	}
	
	/**
	 * Loads a turn controller from a XML node
	 * @param turnControllerNode The node.
	 * @return The newly loaded TurnController.
	 * @throws XMLParseException Thrown if the XML node was malformatted.
	 */
	public abstract TurnController loadFromXMLElement(Node turnControllerNode) throws XMLParseException;

	/* (non-Javadoc)
	 * @see xml.Saveable#generateXMLElement(org.w3c.dom.Document)
	 */
	public abstract Element generateXMLElement(Document doc);
	
	/**
	 * Static method for acquiring an instance of the TurnController class.
	 * @return A TurnController.
	 */
	public static TurnController getInstance() {
		if(instance == null) {
			instance = new TurnControllerImplementation();
		}
		return instance;
	}
	
	/**
	 * This class implements TurnController.
	 * @author Niels Thykier
	 */
	private static class TurnControllerImplementation extends TurnController {

		/**
		 * A list of turns.
		 */
		private Turn[] turnList = new Turn[10];
		/**
		 * Where it is in the array.
		 */
		private short currentEntry = -1;
		/**
		 * The current turn.
		 */
		private int currentTurn = -1;
		/**
		 * The current running turn (if any).
		 */
		private RunnableTurn run;
		/**
		 * Used to find out, if field has been altered.
		 */
		private boolean notReady = true;
		
		/**
		 * Internal command, to be use of there always is a runable turn.
		 * @return the current turn
		 */
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

		/**
		 * Internal command to handle the end of a turn.
		 */
		private void endOfTurn() {
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
				turnListNode.appendChild(run.generateXMLElement(doc));
			}
			else if(currentEntry > -1) {
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
					if(current.getNodeName().equals("runnableTurn")) {
						controller.notReady = false;
						try {
							controller.run = (RunnableTurn) Turn.loadFromXMLElement(current);
						} catch(ClassCastException e) {
							throw new XMLParseException("runnableTurn did not contain a running turn!");
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
			controller.currentTurn = controller.turnList[0].turn;
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
		
	}
}
