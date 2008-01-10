package nodes;

import java.util.ArrayList;

import message.Message;
import message.Transmitter;

public class Sensor extends Location implements Transmitter, Cloneable{

	private ArrayList<Message> toTransmit;
	private ArrayList<Message> received;
	@SuppressWarnings("unused")
	private ArrayList<Transmitter> sent;
	
	
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
		initSensor();
	}

	/**
	 * Initialization, called from the constructors. 
	 */
	private void initSensor() {
		toTransmit = new ArrayList<Message>();
		received = new ArrayList<Message>();
		sent = new ArrayList<Transmitter>();
	}

	protected void prepareMessages() {
	}
	
	/**
	 * Test if the Sensor is operational.
	 * @return true if the sensor is down/unavailable. 
	 */
	public boolean isDown() {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see message.Transmitter#receive(message.Message)
	 */
	public void receive(Message msg) {
		if(isDown()) {
			return;
		}
		if(msg.getReceiver().equals(this)) {
			received.add(msg);
		} else {
			toTransmit.add(msg);
		}
		
	}
	
	/**
	 * Gets the locaiton of the sensor.
	 * @return The location of the sensor.
	 */
	public Location getLocation() {
		return (Location) super.clone();
	}

	/* (non-Javadoc)
	 * @see message.Transmitter#transmit(message.Message)
	 */
	public void transmit(Message msg) {
		Transmitter[] through = null;//findShortestPath
		transmit(msg, through);
	}

	/* (non-Javadoc)
	 * @see message.Transmitter#transmit(message.Message, message.Transmitter[])
	 */
	public void transmit(Message msg, Transmitter[] through) {
		//sent.add(through[0]);
	}
	
	/* (non-Javadoc)
	 * @see nodes.Location#clone()
	 */
	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (Exception e) {
			throw new RuntimeException(e) ; 
		}
	}
	
}
