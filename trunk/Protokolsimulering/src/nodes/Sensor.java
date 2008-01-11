package nodes;

import java.util.ArrayList;

import turns.Prepareable;

import message.Data;
import message.Message;
import message.Transmitter;
import message.TransmissionTimeOutChecker;

/**
 * Generates a sensor.
 * @author Niels Thykier
 */
public class Sensor extends Location implements Transmitter, Prepareable{

	private ArrayList<Message> toTransmit;
	private ArrayList<Message> received;
	private TransmissionTimeOutChecker sent;
	
	private static int usedIDs = 0;
	public final int id;
	/**
	 * Generate a sensor at a random location.
	 */
	public Sensor() {
		this(new Location());
	}

	/**
	 * Generate a sensor at a given location.
	 * @param coordX The x-coordiate of the location 
	 * @param coordY The y-coordiate of the location
	 */
	public Sensor(int coordX, int coordY) {
		this(new Location(coordX, coordY));
	}

	/**
	 * Generates a Sensor from a location.
	 * @param loc The location of the new sensor.
	 */
	public Sensor(Location loc) {
		super(loc);
		id = usedIDs++;
		initSensor();
	}

	/**
	 * Initialization, called from the constructors. 
	 */
	private void initSensor() {
		regenerateLists(true);
		sent = new TransmissionTimeOutChecker();
	}

	/**
	 * Discards all data in toTransmit list and creates some new lists.
	 * Ensures clean up in the data.
	 * 
	 * Note: if allLists is true, new lists will be generated for all fields, however the 
	 * received fields will copy their data over to the new list before being 
	 * voided.
	 * 
	 * @param allLists true, for all lists.
	 */
	private void regenerateLists(boolean allLists) {
		toTransmit = new ArrayList<Message>();
		if(allLists) {
			ArrayList<Message> tempReceived = new ArrayList<Message>();
			int size = 0;
			if(received != null) {
				size = received.size();
				for(int i = 0 ; i< size ; i++) {
					tempReceived.add(received.get(i));
				}
			}
			received = tempReceived;
		}
	}
	
	protected void prepareMessages() {
		int size = received.size();
		for(int i = 0 ; i < size ; i++) {
			toTransmit.add(received.get(i).generateConfirmationMessage());
		}
		received = new ArrayList<Message>();
		
	}
	
	/**
	 * Test if the Sensor is operational.
	 * @return true if the sensor is down/unavailable. 
	 */
	public boolean isDown() {
		return false;
	}
	
	/**
	 * Gets the locaiton of the sensor.
	 * @return The location of the sensor.
	 */
	public Location getLocation()  {	
		try {
			return (Location) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see message.Transmitter#receive(message.Message)
	 */
	public void receive(Message msg) {
		if(isDown()) {
			return;
		}
		if(msg.getDataType() == Data.TYPE_RECEIVED) {
			sent.remove(msg.getSender());
		}
		else if(msg.getReceiver() == id) {
			received.add(msg);
		} else {
			toTransmit.add(msg);
		}
		
	}

	/* (non-Javadoc)
	 * @see message.Transmitter#transmit(message.Message)
	 */
	public void transmit(Message msg) {
		sent.add(msg.getReceiver());
	}
	
	/* (non-Javadoc)
	 * @see nodes.Location#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Sensor) {
			Sensor sen = (Sensor) obj;
			return sen.id == id;
		}
		return false;
	}

	
	/* (non-Javadoc)
	 * @see nodes.Location#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public void prepare() {
		// TODO Auto-generated method stub
		prepareMessages();
	}

	public void step() {
		// TODO Auto-generated method stub
		
	}
	
}