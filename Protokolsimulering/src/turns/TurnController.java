package turns;

import java.awt.Graphics;

import nodes.Sensor;
import nodes.Sensor.SensorComparator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import shape.Drawable;
import turns.Turn.RunnableTurn;
import xml.Saveable;

public abstract class TurnController implements Saveable, Drawable{
	
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
	 * Play the whole turn.
	 * If the prepare phase has not been played, it will be played.
	 */
	public abstract void playTurn();
	
	public abstract void playTickBackwards();
	
	public abstract void playTurnBackwards();
	
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
	
	public static TurnController newInstance() {
		return new TurnControllerImplementation();
	}
	
	private static class TurnControllerImplementation extends TurnController {

		private Turn[] turnList = new Turn[10];
		private short currentEntry = -1;
		private int currentTurn = -1;
		private RunnableTurn run;
		private boolean notReady = true;
		
		private RunnableTurn getCurrentTurn() {
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
			RunnableTurn turn = getCurrentTurn();
			if(turn.getPhase() > RunnableTurn.PHASE_PREPARE){
				return false;
			}
			turn.prepare();
			return true;
		}

		@Override
		public void playTick() {
			RunnableTurn turn = getCurrentTurn();
			turn.tick();
			if(turn.getPhase() == RunnableTurn.PHASE_FINISHED) {
				endOfTurn();
			}
		}

		@Override
		public void playTurn() {
			RunnableTurn turn = getCurrentTurn();
			turn.prepare();
			turn.step();
			turn.endStep();
			endOfTurn();
		}

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
			turnList[currentEntry] = new Turn(run.sensors, SensorComparator.SORT_BY_ID, currentTurn, false);
			run = null;
		}
		
		@Override
		public void fieldHasBeenAltered() {
			if(this.currentTurn < 0) {
				currentTurn = 0;
				currentEntry = 0;
				Sensor.findRoutes();
				turnList[currentEntry] = new Turn(Sensor.idToSensor.values(), SensorComparator.SORT_BY_ID, currentTurn, false);			
				Sensor.generateNewData();
			} else {
				//TODO split.
			}
			notReady = false;
		}

		@Override
		public Element generateXMLElement(Document doc) {
			Element turnListNode = doc.createElement("turns");
			for(int i = 0 ; i < currentEntry ; i++) {
				turnListNode.appendChild(turnList[i].generateXMLElement(doc));
			}
			if(run != null) {
				turnListNode.appendChild(run.generateXMLElement(doc));
			}
			return turnListNode;
		}

		public void draw(Graphics g) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void playTickBackwards() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void playTurnBackwards() {
			// TODO Auto-generated method stub
			
		}

		
	}
}
