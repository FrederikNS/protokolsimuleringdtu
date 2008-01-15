package transmissions;

import java.util.ArrayList;
import java.util.Collection;

import nodes.Sensor;


/**
 * A transmissions can be sent between two transmitters and may contain data.
 * A transmissions is also used to confirm receiving a transmissions previously.
 * @author Niels Thykier
 */
public class Transmission implements Comparable<Transmission>, DataConstants{
	
	/**
	 * The receiver of the transmissions.
	 */
	private int receiver = Sensor.INVALID_SENSOR_ID;
	/**
	 * Whom is respondable for passing it on?
	 */
	private int through = Sensor.INVALID_SENSOR_ID;
	/**
	 * The sender of the transmissions.
	 */
	private int sender = Sensor.INVALID_SENSOR_ID;
	/**
	 * The transmissions type.
	 */
	private int messageType;
	/**
	 * The data being transmitted.
	 */
	private ArrayList<Data> data;

	/**
	 * Generate a Transmission.
	 * @param receiver The receiver of the transmission
	 * @param sender The original sender of the transmission.
	 * @param information The Data it contains.
	 */
	public Transmission(int receiver, int sender, Data information) {
		this.receiver = receiver;
		this.sender = sender;
		data = new ArrayList<Data>();
		data.add(information);
		messageType = information.getDataType();
	}
	
	public Transmission(int receiver, int sender, Collection<? extends Data> dataList) {
		this.receiver = receiver;
		this.sender = sender;
		data = new ArrayList<Data>(dataList);
		messageType = data.get(0).getDataType();
	}

	/**
	 * Generates a confirmation Transmission to this Transmission.
	 * If this transmission was a send request, it will return a "Can receive" Transmission.
	 * @return A "received successfully" Transmission or a "Can receive" Transmission 
	 * 		depending on this Transmission
	 */
	public Transmission generateConfirmationMessage() {
		if(messageType == Data.TYPE_SENDING) {
			return new Transmission(sender, receiver, Data.generateMessageReceiving());
		}
		return new Transmission(sender, receiver, Data.generateMessageReceivedSuccessfully());
	}
	
	/**
	 * Generates a send-request Transmission.
	 * @param wishingToSendTo The one to be receiving.
	 * @param sendingFrom The one wishing to send.
	 * @return The Transmission.
	 */
	public static Transmission generateSendRequest(int wishingToSendTo, int sendingFrom) {
		return new Transmission(wishingToSendTo, sendingFrom, Data.generateMessageSending());
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
	 * Fetches the ID of the transmitter respondsable for passing it on.
	 * @return The id of the transmitter respondsable for passing it on.
	 */
	public int getRespondsableTransmitter() {
		return through;
	}
	
	/**
	 * Gets the type of transmissions.
	 * @return The type of Transmission
	 */
	public int getMessageType() {
		return messageType;
	}
	
	/**
	 * Fetches data from the transmission.
	 * @param index The index of the transmission.
	 * @return The data.
	 */
	public Data getData(int index) {
		return data.get(index);
	}
	
	/**
	 * Fetches the amount of data packages in this transmission.
	 * @return The amount of data packages.
	 */
	public int size(){
		return data.size();
	}
	
	/**
	 * Appends some data to the list if and only if it is of the same type.
	 * @param toAdd Data to add
	 * @return true if it could be added.
	 */
	public boolean appendData(Data toAdd) {
		if(toAdd.getDataType() == messageType) {
			return data.add(toAdd);
		}
		return false;
	}
	
	/**
	 * Remove some data from the transmissions at index.
	 * @param index The index of the transmissions to be removed.
	 * @return true if it could be moved.
	 */
	public Data remove(int index) {
		return data.remove(index);
	}
	
	/**
	 * Remove some data from the transmissions
	 * @param toRemove The data to be removed.
	 * @return true if the data was in the transmissions.
	 */
	public boolean remove(Data toRemove) {
		return data.remove(toRemove);
	}
	
	public Transmission generateCorruptTransmission() {
		return new Transmission(Sensor.INVALID_SENSOR_ID, Sensor.INVALID_SENSOR_ID, Data.GarbageData);
	}
	
	public void corruptTransmission() {
		data = new ArrayList<Data>();
		data.add(Data.GarbageData);
		messageType = TYPE_GARBAGE;
		sender = Sensor.INVALID_SENSOR_ID;
		receiver = Sensor.INVALID_SENSOR_ID;
		through = Sensor.INVALID_SENSOR_ID;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Transmission arg0) {
		int priority = messageType & PRIORITY_ALL;
		int comparePriority = arg0.messageType & PRIORITY_ALL;
		if(priority < comparePriority) {
			return -1;
		} else if(priority == comparePriority) {
			return 0;
		}
		return 1;
	}
}