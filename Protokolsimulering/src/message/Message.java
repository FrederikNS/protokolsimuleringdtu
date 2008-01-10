package message;

import nodes.Location;
import nodes.Sensor;

public class Message {
	private Sensor destination;
	
	public Location getDestination() {
		return destination.getLocation();
	}
}
