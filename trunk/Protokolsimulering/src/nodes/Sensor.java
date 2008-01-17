package nodes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Random;

import math.Scaling;
import notification.Note;
import notification.NoteConstants;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import shape.DrawableCircle.SensorCircle;
import transmissions.Data;
import transmissions.DataConstants;
import transmissions.Transmission;
import transmissions.Transmitter;
import turns.EndSteppable;
import turns.Prepareable;
import xml.DOMxmlParser;
import exceptions.LabelNotRecognizedException;
import exceptions.XMLParseException;
import gui.GUIReferences;

/**
 * 
 * @author Niels Thykier
 */
public class Sensor extends Location implements Transmitter, Prepareable, Comparable<Sensor>, NoteConstants, DataConstants, EndSteppable {

	public static final int INVALID_SENSOR_ID = -1;
	public static final int ALL_SENSORS = -2;
	public static final int SENSOR_TRANSMISSION_RADIUS_MAX 		= 40;
	public static final int SENSOR_TRANSMISSION_RADIUS_DEFAULT  = 20;
	public static final int SENSOR_TRANSMISSION_RADIUS_MIN 		= 10;
	
	
	protected static final int OPTION_SEND_DISABLED 	 	= 0x00000001;
	protected static final int OPTION_RECEIVE_DISABLED 	= 0x00000002;
	protected static final int ACTION_RECEIVING = 0x00000004;
	protected static final int ACTION_SENDING	= 0x00000008;
	protected static final int ACTION_WAIT		= 0x00000010;
	protected static final int ACTION_NOTHING_TO_DO		= 0x00000020;
	
	public static final int STATUS_SENDING 				    = 0x00000001;
	public static final int STATUS_RECEIVING				= 0x00000002;
	public static final int STATUS_DEAD					    = 0x00000004;
	public static final int STATUS_SELECTED				    = 0x00000008;
	public static final int STATUS_SECONDARY_SELECTED	    = 0x00000010;
	
	protected static Random ran = new Random();
	public static int usedIDs = 0;
	
	public final int id;
	protected ArrayList<Data> unsentData = new ArrayList<Data>();
	protected ArrayList<Sensor> links = new ArrayList<Sensor>();
	protected Transmission ingoing;
	protected Transmission outgoing;
	protected boolean waiting;
	protected int currentTick;
	protected int resendDelay;
	protected int status; //Mainly used for determing coloring.
	protected int transmissionRoll;
	protected SensorCircle draw; //handles drawing of the figure and radii
	protected static int transmissionRadius = SENSOR_TRANSMISSION_RADIUS_DEFAULT;
	
	/**
	 * Handles labelling.
	 */
	private static Hashtable<String,Integer> labelToID = new Hashtable<String,Integer>();
	/**
	 * Handles id-look-up
	 */
	public static Hashtable<Integer,Sensor> idToSensor = new Hashtable<Integer,Sensor>();
		
	
	private String sensorLabel = null;
	
	public Sensor(int x,int y){
		this(new Location(x,y));
	}
	
	public Sensor(){
		this(ran.nextInt(Scaling.getPicXMax()), ran.nextInt(Scaling.getPicYMax()));
	}
	
	public Sensor(Location loc) {
		this(loc, usedIDs++);
	}
	
	protected Sensor(Sensor sen, int id) {
		this((Location)sen, id);
		this.ingoing = sen.ingoing;
		this.outgoing = sen.outgoing;
		this.links = sen.links;
		this.draw = sen.draw;
		this.transmissionRoll = sen.transmissionRoll;
		this.waiting = sen.waiting;
		this.resendDelay = sen.resendDelay;
		this.status = sen.status;
		for(Sensor loop : links) {
			loop.links.remove(sen);
			loop.addLinkToSensor(this);
		}
	}
	
	private Sensor(Location loc, int id) {
		super(loc);
		this.id = id;
		idToSensor.put(id, this);
		draw = new SensorCircle(loc, 2);
	}

	/**
	 * Fetches a sensor's ID by its label. 
	 * Sensors do not have a label unless one has been assigned to them!
	 * @param label The label.
	 * @return The id of the sensor.
	 * @throws LabelNotRecognizedException If no sensor has been assigned that label.
	 */
	public static int getSensorID(String label) throws LabelNotRecognizedException {
		Integer senID = labelToID.get(label);
		if(senID != null) {
			return senID;
		}
		throw new LabelNotRecognizedException("The label " + label + " is not recognized.");
	}
	
	/**
	 * Fetches a sensor by ID.
	 * @param sensorID The id of the sensor.
	 * @return The sensor
	 */
	public Sensor getSensor(int sensorID) {
		return idToSensor.get(sensorID);
	}

	/**
	 * Fetches a sensors by its label. 
	 * Sensors do not have a label unless one has been assigned to them!
	 * @param label The label.
	 * @return The Sensor
	 * @throws LabelNotRecognizedException If no sensor has been assigned that label.
	 */
	public Sensor getSensor(String label) throws LabelNotRecognizedException {
		return getSensor(getSensorID(label));
	}
	
	/**
	 * Assigns a label to a sensor using the sensor's ID.
	 * @param label The new label of the sensor. If null or "", the sensor's current label will be removed.
	 * @param id The id of the sensor.
	 * @return true, if the label was updated.
	 */
	public static boolean assignLabel(String label, int id) {
		return idToSensor.get(id).assignLabel(label);
	}
	
	/**
	 * Assigns a label to the sensor.
	 * @param label The new label of the sensor. If null or "", the sensor's current label will be removed.
	 * @return true, if the label was updated.
	 */
	public boolean assignLabel(String label) {
		boolean toReturn = false;
		if(label == null || label.trim().equals("")) {
			toReturn = labelToID.remove(sensorLabel) != null;
		} else {
			labelToID.put(label, id);
			toReturn = true;
		}
		sensorLabel = label;
		return toReturn;
	}
	
	
	/**
	 * Get the radius that sensors can communicate within.
	 * @return The Transmission Radius
	 */
	public static int getTransmissionRadius(){
		return transmissionRadius;
	}
	
	/**
	 * Change the transmission radius. It must be greater (or equal to) than 
	 * SENSOR_TRANSMISSION_RADIUS_MIN and less than (or equal to) SENSOR_TRANSMISSION_RADIUS_MAX
	 * @param newRadius The new radius they can communicate within.
	 * @throws IllegalArgumentException If the new radius was illegal.
	 */
	public static void setTransmissionRadius(int newRadius){
		if(newRadius < Sensor.SENSOR_TRANSMISSION_RADIUS_MIN) {
			throw new IllegalArgumentException("Transmission Radius cannot be less than " + SENSOR_TRANSMISSION_RADIUS_MIN);
		}
		if(newRadius > Sensor.SENSOR_TRANSMISSION_RADIUS_MAX) {
			throw new IllegalArgumentException("Transmission Radius cannot be greater than " + SENSOR_TRANSMISSION_RADIUS_MAX);
		}
		transmissionRadius = newRadius;
	}
	
	/**
	 * Check if two sensors can communicate with each other using the current 
	 * transmission radius.
	 * This does NOT take their routing protocols into considerations.
	 * @param sen The other sensor
	 * @return true if they can communicate
	 */
	public boolean canCommunicate(Sensor sen) {
		return this.internalDistanceCheck(sen) <=  Math.pow(transmissionRadius, 2);
	}
	
	/**
	 * Check if two sensors can communicate with each other using the current 
	 * transmission radius.
	 * This does NOT take their routing protocols into considerations.
	 * @param sen1 A sensor
	 * @param sen2 Another sensor
	 * @return true if they can communicate
	 */
	public static boolean canCommunicate(Sensor sen1, Sensor sen2) {
		return sen1.canCommunicate(sen2);
	}
	
	/* (non-Javadoc)
	 * @see transmissions.Transmitter#receive(transmissions.Transmission)
	 */
	public void receive(Transmission msg) {
		// TODO Auto-generated method stub
		if(0 != (currentTick & OPTION_RECEIVE_DISABLED)) {
			 // cannot receive, die here, if a message was expected, flag need to send "Unsuccessful received"
			if(ingoing != null || 0 != (currentTick & ACTION_RECEIVING)) {
				ingoing.corruptTransmission();
			}
			return;
		}
		boolean isReceiver = isForMe(msg);
		switch(msg.getMessageType()){
		case TYPE_SENDING:
			//Someone wishes to send
			if(isReceiver) {
				//to me. If I can receive and can send, reply with ok.
				
			} else {
				//cannot receive.
				//if expecting to receive a message, flag need to send "Unsuccessful received"
			}
			break;
		case TYPE_LISTENING:
			//Someone wishes to receive.
			if(isReceiver) {
				//wee It is waiting for me to send.
			} else {
				//Not allowed to send.
				//if wasTryingToSendToThatTarget, wait for target comfirmation message!
			}
			break;
		case TYPE_NETWORK:
		case TYPE_RECEIVED_SUCCESSFULLY:
		case TYPE_RECEIVED_UNSUCCESSFULLY:
		case TYPE_DATA:
			//Wee data!
			if(isReceiver || msg.getRespondsableTransmitter() == this.id) {
				ingoing = msg;
			}
			break;
		default:
			//This ought not to happen o.O
			//Broadcast warning to user.
			break;
		}
	}

	protected final boolean isForMe(Transmission msg) {
		return msg.getReceiver() == this.id;
	}
	
	/* (non-Javadoc)
	 * @see transmissions.Transmitter#transmit(transmissions.Transmission)
	 */
	public void transmit(Transmission msg) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see turns.Prepareable#prepare()
	 */
	public void prepare() {
		// TODO Auto-generated method stub
		if(0 == ( currentTick & OPTION_SEND_DISABLED)) {
			//Can send.
			if(outgoing == null) {
				//No unhandled outgoing message.
				if(unsentData.size() > 0) {
					//Generate datamessage.
					outgoing = new Transmission(0, 0, unsentData);
					unsentData = null;
				} else {
					//Got absolutely nothing to sent, flag "nothing to do".
					currentTick |= ACTION_NOTHING_TO_DO;
				}
			}
			
			
		}
		
		
	}

	/* (non-Javadoc)
	 * @see turns.Steppable#step()
	 */
	public void step() {
		// TODO Auto-generated method stub
		if(0 != (currentTick & ACTION_NOTHING_TO_DO)) {
			//Nothing to do.
			return;
		}
		if(0 != (currentTick & ACTION_SENDING)) {
			
			if(0 == (currentTick & OPTION_SEND_DISABLED) ) {
				//Can send.
			} else {
				//Cannot send?
			}
		}
	}

	/* (non-Javadoc)
	 * @see turns.EndSteppable#EndStep()
	 */
	public void endStep() {
		// TODO Auto-generated method stub
		if(ingoing != null) {
			switch(ingoing.getMessageType()) {
			case TYPE_DATA: 
				int size = ingoing.size();
				for(int i = 0 ; i < size ; i++) {
					unsentData.add(ingoing.getData(i));
				}
				break;
			case TYPE_NETWORK:
				//Network update. New Sensor appeared or old sensor vanished.
				// No need to reply.
				break;
			case TYPE_RECEIVED_SUCCESSFULLY:
				//Someone received a message successfully.
				if(isForMe(ingoing)) {
					//clear the time-out standing and allow new package to be sent.
					outgoing = null;
				} else if(waiting && ingoing.getSender() == outgoing.getReceiver()) {
					waiting = false;
					//add random delay?
				}
				break;
			case TYPE_RECEIVED_UNSUCCESSFULLY:
				//Someone did not receive their package.
				if(outgoing != null && outgoing.getReceiver() == ingoing.getSender()) {
					//It was the one I sent to recently, clear time-out standing
					if(isForMe(ingoing)) {
						//attempt resend during next tick.
						resendDelay++;//TODO Randomize
					} else {
						//await for a successfully received from sender.
					}				
				}
				break;
			case TYPE_GARBAGE:
				if(0 != (ACTION_RECEIVING & currentTick) ) {
					if(outgoing != null) {
						//Handle having an outgoing message.
					}
				}
				break;
			default:
				//unexpected type. 
				break;
			}
		}
		ingoing = null;
		currentTick = 0;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Sensor arg0) {
		return Integer.valueOf(id).compareTo(arg0.id);
	}
	
	/**
	 * Test if the Sensor is operational.
	 * @return true if the sensor is available. 
	 */
	public boolean isEnabled() {
		return 0 == (status & STATUS_DEAD);
	}

	public void setEnabled(boolean running) {
		if(running) {
			status &= ~STATUS_DEAD;
		} else {
			status |= STATUS_DEAD;
		}
	}
	
	private void setSecondaySelection(boolean selectedStatus) {
		if(selectedStatus) {
			status |= STATUS_SECONDARY_SELECTED;
		} else {
			status &= ~STATUS_SECONDARY_SELECTED;
		}
	}
	
	/**
	 * Adds a link from this sensor to the other.
	 * @param sen The other sensor.
	 */
	public void addLinkToSensor(Sensor sen) {
		links.add(sen);
		updateLinks();
	}
	
	/**
	 * Forces the sensor to update the secondary selected. Used by the addLinkToSensor method but can also
	 * be called explicitly if the links have been modified without a call to that method.
	 */
	public void updateLinks() {
		if(0 != (status & STATUS_SELECTED)) {
			for(Sensor sen : links) {
				sen.setSecondaySelection(true);
			}
		}
	}


	/**
	 * Fetches a list of the sensors this sensor can reach. (Requires that the GlobalAddressBook has been updated)
	 * @return An array of Sensors that this sensor can reach.
	 */
	public Sensor[] getLinks() {
		return links.toArray(new Sensor[1]);
	}
	
	
	/**
	 * Flag a sensor as selected or deselected. It will notify its links that they are now / no longer "secondarily selected"
	 * @param selectedStatus true if the sensor is selected, false if it is deselected.
	 */
	public void setSelected(boolean selectedStatus) {
		if(selectedStatus) {
			status |= STATUS_SELECTED;
		} else {
			status &= ~STATUS_SELECTED;
		}
	
		for(Sensor sen : links) {
			sen.setSecondaySelection(selectedStatus);
		}
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
	
	/**
	 * Load all sensors from the XML file
	 * @param doc The DOM Document reference of the XML file
	 * @throws XMLParseException Throw if invalid tags/informations was found.
	 */
	public static void loadFromXML(Document doc) throws XMLParseException {
		NodeList sensorList = doc.getElementsByTagName("sensor");
		int size = sensorList.getLength();
		for(int i = 0 ; i < size ; i++) {
			loadFromXMLElement(sensorList.item(i));
		}
		usedIDs = size;
		Node transmissionRadiusNode = doc.getElementsByTagName("transmissionRadius").item(0);
		if( transmissionRadiusNode != null) {
			try {
				transmissionRadius = Integer.parseInt(DOMxmlParser.getTextNodeValue(transmissionRadiusNode).trim());
			} catch(Exception e) {
				throw new XMLParseException("Transmission Radius tag did not contain an int.");
			}
		} else {
			transmissionRadius = Sensor.SENSOR_TRANSMISSION_RADIUS_DEFAULT;
		}
	}
	
	/**
	 * Removes all sensors from the list.
	 */
	public static void disposeAllSensors() {
		GlobalAdressBook.clearBook();
		idToSensor = new Hashtable<Integer, Sensor>();
		usedIDs = 0;
	}
	
	/**
	 * Load a sensor (or updates an previously loaded Sensor) from the XML file
	 * @param sensorElement The DOM Document reference of the XML file
	 * @return The newly loaded (or updated) sensor.
	 * @throws XMLParseException Throw if invalid tags/informations was found.
	 */
	public static Sensor loadFromXMLElement(Node sensorElement) throws XMLParseException {
		if(sensorElement.getNodeType() != Node.ELEMENT_NODE || !sensorElement.getNodeName().equals("sensor")) {
			throw new IllegalArgumentException("Node was not a sensorElement");
		}
		NodeList list = sensorElement.getChildNodes();
		boolean noLocation = true;
		Node current;
		Location loc = null;
		Sensor sen;
		int size = list.getLength();
		for(int i = 0; i < size ; i++) {
			current = list.item(i);
			switch(current.getNodeName().charAt(0)) {
			case 'l':
				if(current.getNodeName().equals("location")) {
					loc = Location.loadFromXMLElement(current);
					noLocation= false;
				}
				break;
				
			}
			
		}
		
		NamedNodeMap attrMap = sensorElement.getAttributes();
		int sensorID = -1;
		try {
			sensorID = Integer.parseInt(attrMap.getNamedItem("id").getNodeValue().trim());
			if(sensorID < 0) {
				throw new XMLParseException("Sensor tag missing id attribute OR id value was invalid. (Must be int above -1)");
			}
		} catch(RuntimeException e) {
			throw new XMLParseException("Sensor tag missing id attribute OR id value was invalid. (Must be int above -1)");
		}
		
		if(noLocation) {
			sen = idToSensor.get(sensorID);
			if(sen == null) {
				throw new XMLParseException("Did not contain Location of Sensor with id: " + sensorID + " and sensor was not loaded! Perhaps another XML file should be loaded first?");
			}
		} else {
			sen = new Sensor(loc, sensorID);
		}
		return sen;
	}

	/**
	 * Generates an XML element that contains the general sensor data settings.
	 * @param doc The DOM Document of the XML file
	 * @return The XML element to be added.
	 */
	public static Element generateXMLGeneralData(Document doc) {
		Element element = doc.createElement("sensordata");
		Node transmissionRadiusNode = doc.createElement("transmissionRadius");
		transmissionRadiusNode.appendChild(doc.createTextNode(String.valueOf(transmissionRadius)));
		element.appendChild(transmissionRadiusNode);
		return element;
	}
	
	public Element generateXMLTurnElement(Document doc) {
		Element element = doc.createElement("sensor");
		element.setAttribute("id", String.valueOf(id));
		element.setIdAttribute("id", true);
		if(sensorLabel != null){
			element.setAttribute("label", sensorLabel);
		}

		if(this.resendDelay > 0){
			Node resendDelayNode = doc.createElement("resendDelay");
			resendDelayNode.appendChild(doc.createTextNode(String.valueOf(resendDelay)));
			element.appendChild(resendDelayNode);
		}
		
		Node transmissionRollNode = doc.createElement("transmissionRoll");
		transmissionRollNode.appendChild(doc.createTextNode(String.valueOf(transmissionRoll)));
		element.appendChild(transmissionRollNode);
		
		if(this.ingoing != null) {
			Element ingoingNode = ingoing.generateXMLElement(doc);
			ingoingNode.setAttribute("type", "ingoing");
			element.appendChild(ingoingNode);
		}
		if(this.outgoing != null) {
			Element outgoingNode = outgoing.generateXMLElement(doc);
			outgoingNode.setAttribute("type", "outgoing");
			element.appendChild(outgoingNode);
		}
		if(this.unsentData.size() > 0 ) {
			Node unsentDataNode = doc.createElement("unsentData");
			for(Data d : unsentData) {
				unsentDataNode.appendChild(d.generateXMLElement(doc));
			}
			element.appendChild(unsentDataNode);
		}
		return element;
	}
	
	@Override
	public Element generateXMLElement(Document doc) {
		Element element = doc.createElement("sensor");
		element.setAttribute("id", String.valueOf(id));
		element.setIdAttribute("id", true);
		element.appendChild(super.generateXMLElement(doc));
		return element;
	}
	
	/* (non-Javadoc)
	 * @see nodes.Location#toString()
	 */
	@Override
	public String toString() {
		return "Sensor #" + id + " " + (sensorLabel != null?sensorLabel + " ":"") + super.toString();
	}

	/* (non-Javadoc)
	 * @see nodes.Location#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	protected Color chooseColor(Color defaultColor) {
		Color toReturn = defaultColor;

		if(0 == (status & STATUS_SELECTED)) {
			if(0 != (status & STATUS_DEAD)) {
				toReturn = GUIReferences.deadColor;
			} else if (0 != (status & STATUS_SECONDARY_SELECTED) 
					&& 0 != (GUIReferences.view & GUIReferences.VIEW_NEIGHBOURS)) {
				toReturn = GUIReferences.secondarySelectedColor;
			} 
		} else {
			toReturn = GUIReferences.selectedColor;
		}
		return toReturn;
	}
	
	protected void internalDraw(Graphics g) {
		if( 0 != (GUIReferences.view & GUIReferences.VIEW_CONNECTIONS)) {
			g.setColor(GUIReferences.connectionColor);
			Point senPoint = Scaling.locationToPoint(this);
			Point target;
			int size = links.size();
			Sensor sen;
			for(int i = 0 ; i < size ; i++) {
				sen = links.get(i);
				if(sen.id > this.id) {
					target = Scaling.locationToPoint(links.get(i));
					g.drawLine(senPoint.x, senPoint.y, target.x, target.y);
				}
			}
		}
		
		g.setColor(chooseColor(GUIReferences.sensorColor));
		draw.draw(g);
		
	}
	
	/* (non-Javadoc)
	 * @see nodes.Location#draw(java.awt.Graphics)
	 */
	@Override
	public void draw(Graphics g) {
		Color temp = g.getColor();
		internalDraw(g);
		g.setColor(temp);
	}
	
	/**
	 * Turn a sensor into a terminal or a terminal into a sensor. 
	 * 
	 * @param terminal true if it should be upgrade to a terminal, false if downgraded to normal sensor.
	 * @return A new terminal or sensor depending on the input.
	 */
	public Sensor setTerminalStatus(boolean terminal) {
		if(terminal) {
			if(!(this instanceof Terminal)) {
				Note.sendNote(this + " has been promoted to Terminal");
				return new Terminal(this);
			}
			
		} else {
			if(this instanceof Terminal) {
				Note.sendNote(this + " has been demoted to Sensor");
				return new Sensor(this, this.id);
			}
		}
		return this;
	}

	public class Terminal extends Sensor {
		private Terminal(Sensor sen) {
			super(sen, sen.id);
		}
		
		
		
		@Override
		protected Color chooseColor(Color defaultColor) {
			return super.chooseColor(GUIReferences.terminalColor);
		}
		
	}
	
	public static final class SensorComparator implements Comparator<Sensor> {
		private final int compareType;
		public static final int SORT_DEFAULT = 0;
		public static final int SORT_BY_TURNS = 1;
		public static final int SORT_BY_ID = 2;
		
		public SensorComparator(int type) {
			compareType = type;
		}

		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(Sensor o1, Sensor o2) {
			int toReturn = 0;
			switch(compareType) {
			case SORT_BY_TURNS:
				if(o1.transmissionRoll < o2.transmissionRoll) {
					toReturn = -1;
				} else if(o1.transmissionRoll == o2.transmissionRoll) {
					//Tie, randomly choose one. (50/50)
					if((Sensor.ran.nextInt(50) & 1) == 0) {
						toReturn = -1;
					} else {
						toReturn = 1;
					}
				} else {
					toReturn = 1;
				}
				break;
			default:
			case SORT_DEFAULT:
			case SORT_BY_ID:
				toReturn = o1.compareTo(o2);
				break;
			}
			return toReturn;
		}
	}

}
