package message;

import java.util.ArrayList;


/**
 * A message can be sent between two transmitters and may contain data.
 * A message is also used to confirm receiving a message previously.
 * @author Niels Thykier
 */
public class Transmission {
	
	/**
	 * The receiver of the message.
	 */
	private int receiver;
	/**
	 * The sender of the message.
	 */
	private int sender;
	/**
	 * The message type.
	 */
	private int messageType;
	/**
	 * The data being transmitted.
	 */
	private ArrayList<Data> data;
	
	private int dataType;
	
	public Transmission(int receiver, int sender, Data information) {
		this.receiver = receiver;
		this.sender = sender;
		data = new ArrayList<Data>();
		data.add(information);
		dataType = information.getDataType();
	}
	
	public Transmission generateConfirmationMessage() {
		return new Transmission(sender, receiver, Data.generateMessageReceived());
	}
	
	/**
	 * Fetches the ID of the receiving transmitter.
	 * @return The id of the receiving transmitter.
	 */
	public int getReceiver() {
		return receiver;
	}
	
	/**
	 * Fetches the ID of the sending transmitter.
	 * @return The id of the sending transmitter.
	 */
	public int getSender() {
		return sender;
	}
	
	/**
	 * Gets the type of message.
	 * @return The type of Transmission
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
	
	public int getDataType() {
		return dataType;
	}
	
	/**
	 * Appends some data to the list if and only if it is of the same type.
	 * @param toAdd Data to add
	 * @return true if it could be added.
	 */
	public boolean appendData(Data toAdd) {
		if(toAdd.getDataType() == dataType) {
			return data.add(toAdd);
		}
		return false;
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
