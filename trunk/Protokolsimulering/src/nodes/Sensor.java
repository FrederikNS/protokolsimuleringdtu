package nodes;

import java.util.ArrayList;

import message.Message;
import message.Transmitter;

public class Sensor extends Location implements Transmitter{

	private ArrayList<Message> toTransmit;
	private ArrayList<Message> received;
	private ArrayList<Message> sent;
	
	
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
		sent = new ArrayList<Message>();
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
		return super.clone();
	}

	public boolean transmit(Message msg) {
		Transmitter[] through = null;//findShortestPath
		return transmit(msg, through);
	}

	public boolean transmit(Message msg, Transmitter[] through) {
		sent.add(msg);
		return false;
	}
	
	
}
