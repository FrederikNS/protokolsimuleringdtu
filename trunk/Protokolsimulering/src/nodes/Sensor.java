package nodes;

import java.util.ArrayList;

import message.Message;
import message.Transmitter;

public class Sensor extends Location implements Transmitter, Cloneable{

	private ArrayList<Message> toTransmit;
	private ArrayList<Message> received;
	@SuppressWarnings("unused")
	private ArrayList<Transmitter> sent;
	
	
	public Sensor() {
		super();
	}

	public Sensor(int coordX, int coordY) {
		super(coordX, coordY);
	}

	public Sensor(Location loc) {
		super(loc);
		initSensor();
	}

	private void initSensor() {
		toTransmit = new ArrayList<Message>();
		received = new ArrayList<Message>();
		sent = new ArrayList<Transmitter>();
	}

	protected void prepareMessages() {
	}
	
	public boolean isDown() {
		return false;
	}
	
	public void receive(Message msg) {
		if(isDown()) {
			return;
		}
		if(msg.getDestination().equals(this)) {
			received.add(msg);
		} else {
			toTransmit.add(msg);
		}
		
	}
	
	public Location getLocation() {
		return (Location) super.clone();
	}

	public boolean transmit(Message msg) {
		Transmitter[] through = null;//findShortestPath
		return transmit(msg, through);
	}

	public boolean transmit(Message msg, Transmitter[] through) {
		//sent.add(through[0]);
		return false;
	}
	
	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (Exception e) {
			throw new RuntimeException(e) ; 
		}
	}
	
}
