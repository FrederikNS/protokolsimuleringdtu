package transmissions;

/**
 * A transmitter can send and receive messages.
 * @author Niels Thykier
 */
public interface Transmitter {

	/**
	 * The "steps" it takes before a transmission is "timed-out".
	 */
	public static final int TIMEOUT_IN_STEPS = 120;
	
	/**
	 * Invoked when a transmissions is to be transmitted. 
	 * @param msg the transmissions to send.
	 */
	public void transmit(Transmission msg);
	
	/**
	 * Invoked on the receiving transmitter, when it receives it transmissions. 
	 * @param msg The transmissions.
	 */
	public void receive(Transmission msg);
	
}
