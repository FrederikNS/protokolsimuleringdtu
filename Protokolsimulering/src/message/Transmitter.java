package message;

/**
 * A transmitter can send and receive messages.
 * @author Niels Thykier
 */
public interface Transmitter extends Cloneable{

	/**
	 * The "steps" it takes before a transmission is "timed-out".
	 */
	public static final int TIMEOUT_IN_STEPS = 3;
	
	/**
	 * Invoked on the receiving transmitter, when it receives it message. 
	 * @param msg The message.
	 */
	public void receive(Message msg);
	/**
	 * Invoked when a message is to be transmitted. 
	 * @param msg the message to send.
	 */
	public void transmit(Message msg);
	/**
	 * Invoked when a message is to be transmitted. 
	 * @param msg the message to send.
	 * @param through the transmitters it should be sent via.
	 */
	//public void transmit(Message msg, Transmitter[] through);
	
	/**
	 * Must have a public clone method following the standards of Cloneable.
	 * @return A copy of the object.
	 */
	public Object clone();
}
