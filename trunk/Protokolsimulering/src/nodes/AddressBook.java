package nodes;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

/**
 * This class contains informations about, who to pass a message on to.
 * @author Morten Soerensen
 */
public class AddressBook {

	/**
	 * Integer that contains information on the sensor closer to the terminal.
	 */
	private int toTerminal;
	/**
	 * The Hashtable that contains informations about who to send to.
	 */
	private Hashtable<Integer, Integer>away;

	/**
	 * Constructor for the class.
	 * When the class is initialized, the Hashtable 'away' will be created.
	 */
	public AddressBook() {
		away = new Hashtable<Integer, Integer>();
	}

	/**
	 * This method sets the ID of the sensor closer to the terminal.
	 * @param toTerm the sensor closer to the terminal
	 */
	public void setToTerminal(int toTerm) {
		toTerminal = toTerm;
	}

	/**
	 * This method adds pair of sensors to a hashtable.
	 * @param endSensor the destination that is used to see, who to send the message through
	 * @param nextSensor the next sensor in line away from the terminal
	 */
	public void addAway(int endSensor, int nextSensor) {
		away.put(endSensor, nextSensor);
	}

	/**
	 * This method looks through the Hashtable to find out where to send the message to, to reach a certain sensor.
	 * @param endSensor the sensor a message is addressed to
	 * @return the next sensor to send to
	 */
	public int getNextSensor(int endSensor) {
		return away.get(endSensor);
	}

	/**
	 * This method gets the ID of the sensor closer to the terminal.
	 * @return the ID on the sensor closer to the terminal
	 */
	public int getToTerminal() {
		return toTerminal;
	}

	/**
	 * Clears the entire list of entries in the Hashtable.
	 */
	public void clearList() {
		away.clear();
	}

	/**
	 * This method extracts a list of all the keys from the Hashtable and returns it.
	 * @return a set of keys in the Hashtable
	 */
	public Set<Integer> getBook() {
		return away.keySet();
	}

	/**
	 * Loops through the Hashtable and rmeoves any keys that points at the dead sensor.
	 * @param deadSensor The id of newly died sensor
	 */
	public void remove(int deadSensor) {
		Set<Integer> toBeRemoved = new HashSet<Integer>();
		for(int key :away.keySet()){
			if(away.get(key) == deadSensor) {
				toBeRemoved.add(key);
			}
		}
		for(int key : toBeRemoved) {
			away.remove(key);
		}
	}
}
