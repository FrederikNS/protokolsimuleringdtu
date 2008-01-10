package message;


/**
 * A message can be sent between two transmitters and may contain data.
 * A message is also used to confirm receiving a message previously.
 * @author Niels Thykier
 */
public class Message {
	
	/**
	 * The receiver of the message.
	 */
	private Transmitter receiver;
	/**
	 * The sender of the message.
	 */
	private Transmitter sender;
	/**
	 * The message type.
	 */
	private int messageType;
	
	/**
	 * Fetches (a copy of) the receiving transmitter.
	 * @return (a copy of) the receiving transmitter.
	 */
	public Transmitter getReceiver() {
		return (Transmitter) receiver.clone();
	}
	
	/**
	 * Fetches (a copy of) the sending transmitter.
	 * @return (a copy of) the sending transmitter.
	 */
	public Transmitter getSender() {
		return (Transmitter) sender.clone();
	}
	
	/**
	 * Gets the type of message.
	 * @return The type of Message
	 */
	public int getMessageType() {
		return messageType;
	}
}
