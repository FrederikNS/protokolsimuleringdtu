package message;

/**
 * A transmitter can send and receive messages.
 * @author Niels Thykier
 */
public interface Transmitter {

	/**
	 * The "steps" it takes before a transmission is "timed-out".
	 */
	public static final int TIMEOUT_IN_STEPS = 3;
	
	/**
	 * Invoked when a message is to be transmitted. 
	 * @param msg the message to send.
	 */
	public void transmit(Transmission msg);
	
	/**
	 * Invoked on the receiving transmitter, when it receives it message. 
	 * @param msg The message.
	 */
	public void receive(Transmission msg);
	
}
