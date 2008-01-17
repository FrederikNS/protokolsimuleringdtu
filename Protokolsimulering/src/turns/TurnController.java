package turns;

import nodes.Sensor;
import nodes.Sensor.SensorComparator;
import turns.Turn.RunnableTurn;

public abstract class TurnController{
	
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
	
	/**
	 * The TurnController will attempt to go to the given turn. If it jumps forward it may have to 
	 * calculate all the turns inbetween. If it goes back it must keep a history and may have to 
	 * reload that turn.
	 * @param turn The turn number.
	 * @return true, if it could jump to that turn successfully.
	 */
	public abstract boolean goToTurn(int turn);

	public static TurnController newInstance() {
		return new TurnControllerImplementation();
	}
	
	private static class TurnControllerImplementation extends TurnController {

		private Turn[] turnList = new Turn[10];
		private short currentEntry = -1;
		private int currentTurn = -1;
		private RunnableTurn run;
		private boolean notReady = false;
		
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
			if(currentEntry == turnList.length) {
				
			} else {
				currentEntry++;
			}
			currentTurn++;
			turnList[currentEntry] = new Turn(run.sensors, SensorComparator.SORT_BY_ID, currentTurn);
			run = null;
		}
		
		@Override
		public void fieldHasBeenAltered() {
			if(this.currentTurn < 0) {
				currentTurn = 0;
				currentEntry = 0;
				turnList[currentEntry] = new Turn(Sensor.idToSensor.values(), SensorComparator.SORT_BY_ID, currentTurn);

			} else {
				//TODO split.
			}
			notReady = false;
		}

		
	}
}
