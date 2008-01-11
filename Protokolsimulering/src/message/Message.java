package message;

import java.util.ArrayList;


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
	
	@SuppressWarnings("unused")
	private ArrayList<Data> data;
	
	public Message(Transmitter receiver, Transmitter sender) {
		this.receiver = receiver;
		this.sender = sender;
		data = new ArrayList<Data>();
	}
	
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
	
	/**
	 * Fetches data from the message.
	 * @param index The index of the message.
	 * @return The data.
	 */
	public Data getData(int index) {
		return data.get(index);
	}
	
	/**
	 * Appends some data to the list.
	 * @param toAdd Data to add
	 * @return true if it could be added.
	 */
	public boolean appendData(Data toAdd) {
		return data.add(toAdd);
	}
	
	/**
	 * Remove some data from the message at index.
	 * @param index The index of the message to be removed.
	 * @return true if it could be moved.
	 */
	public Data remove(int index) {
		return data.remove(index);
	}
	
	/**
	 * Remove some data from the message
	 * @param toRemove The data to be removed.
	 * @return true if the data was in the message.
	 */
	public boolean remove(Data toRemove) {
		return data.remove(toRemove);
	}
}
