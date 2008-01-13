package nodes;

import java.util.Hashtable;

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
		Hashtable away = new Hashtable<Integer, Integer>();
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
}
