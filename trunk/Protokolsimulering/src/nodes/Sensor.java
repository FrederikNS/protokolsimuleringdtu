package nodes;

import exceptions.LabelNotRecognizedException;
import exceptions.XMLParseException;
import gui.GUIReferences;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Collection;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Random;
import java.util.TreeSet;

import math.Scaling;
import notification.Note;
import notification.NoteConstants;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import shape.Drawable;
import shape.Line;
import shape.DrawableCircle.SensorCircle;
import transmissions.Data;
import transmissions.DataConstants;
import transmissions.Protocol;
import transmissions.Transmission;
import transmissions.Transmitter;
import turns.EndSteppable;
import turns.Prepareable;
import xml.DOMxmlParser;
import xml.Saveable;

/**
 * This is the Sensor class. It uses a protocol instance to handle its transmissions and a location instance to determine is locaiton.
 * @author Niels Thykier
 */
public class Sensor implements Transmitter, Saveable, Prepareable, Comparable<Sensor>, NoteConstants, DataConstants, EndSteppable, Cloneable, Drawable {

	//********************* STATICS ***********************//
	/**
	 * The id of "invalid sensor".
	 */
	public static final int INVALID_SENSOR_ID = -1;
	/**
	 * The "all sensor" id (used in transmissions)
	 */
	public static final int ALL_SENSORS = -2;
	/**
	 * The max allowed transmission radius.
	 */
	public static final int SENSOR_TRANSMISSION_RADIUS_MAX 		= 40;
	/**
	 * The default transmission radius.
	 */
	public static final int SENSOR_TRANSMISSION_RADIUS_DEFAULT  = 20;
	/**
	 * The minimum allowed transmission radius.
	 */
	public static final int SENSOR_TRANSMISSION_RADIUS_MIN 		= 10;
	
	/**
	 * Bit-flag for whether the sensor is transmitting something this turn/step
	 */
	public static final int STATUS_SENDING 				    = 0x00000001;
	/**
	 * Bit-flag for whether the sensor is receiving something this turn/step. (The transmission is not neccesarily for this sensor though)
	 */
	public static final int STATUS_RECEIVING				= 0x00000002;
	/**
	 * Bit-flag for whether the sensor is operational or not.
	 */
	public static final int STATUS_DEAD					    = 0x00000004;
	/**
	 * Bit-flag for whether the sensor is selected.
	 */
	public static final int STATUS_SELECTED				    = 0x00000008;
	/**
	 * Bit-flag for whether the sensor is linked to the currently selected sensor.
	 */
	public static final int STATUS_SECONDARY_SELECTED	    = 0x00000010;
	/**
	 * Bit-flag for whether the current sensor has turn.
	 */
	public static final int STATUS_HAS_TURN 				= 0x00000020;
	/**
	 * Bit-flag for whether this sensor is a terminal
	 */
	public static final int STATUS_IS_TERMINAL				= 0x00000040;
	/**
	 * Bit-flag for whether the terminal has broadcasted initially (For terminals only)
	 */
	public static final int STATUS_HAS_BROADCASTED			= 0x00000080;
	/**
	 * These flags are cleared at the end of a round/phase
	 */
	protected static final int STATUS_CLEAR_END_OF_ROUND     = STATUS_SENDING | STATUS_RECEIVING | STATUS_HAS_TURN;

	/**
	 * Internal random generator (for internal convience)
	 */
	protected static Random ran = new Random();
	
	/**
	 * 
	 */
	protected static int transmissionRadius = SENSOR_TRANSMISSION_RADIUS_DEFAULT;
	

	//********************* NON-STATIC ***********************//
	/**
	 * Internal counter for see what ids have been taken.
	 */
	private static int usedIDs = 0;

	/**
	 * The id of the sensor.
	 */
	public final int id;

	
	/**
	 * List of sensors this sensor can reach.
	 */
	protected TreeSet<Sensor> links = new TreeSet<Sensor>();
	/**
	 * The sensor's current status.
	 * Mainly used for determing coloring.
	 */
	protected int status;
	/**
	 * The sensor's last transmission roll. 
	 */
	protected int transmissionRoll;
	/**
	 * The location of the sensor.
	 */
	protected Location loc;
	/**
	 * The id of the nearest terminal.
	 */
	protected int nearestTerminalID = Sensor.INVALID_SENSOR_ID;
	/**
	 * The distance to the nearest terminal (measured in sensors).
	 */
	protected int nearestTerminalDist = Integer.MAX_VALUE;
	/**
	 * The id of the sensor to transmit through when it should transmit to the terminal.
	 */
	protected int sendThrough = Sensor.INVALID_SENSOR_ID;
	/**
	 * The sensor's label.
	 */
	protected String sensorLabel = null;		
	/**
	 * The sensor's protocol object to handle transmissions.
	 */
	protected Protocol protocol;

	/**
	 * Handles label look up.
	 */
	private static Hashtable<String,Integer> labelToID = new Hashtable<String,Integer>();
	
	/**
	 * Handles id-look-up
	 */
	public static Hashtable<Integer,Sensor> idToSensor = new Hashtable<Integer,Sensor>();

	/**
	 * List of terminals.
	 */
	private static TreeSet<Sensor> terminals = new TreeSet<Sensor>();
	
	/**
	 * The radius of the sensor (when drawn).
	 */
	protected static int sensorDrawRadius = 4;
	
	/**
	 * Internal constructor that sets the id and adds the sensor to the hashtable. 
	 * All other Sensor constructors use this constructor.
	 * @param id The id of the sensor.
	 */
	protected Sensor(int id) {
		this.id = id;	
		idToSensor.put(id, this);
		protocol = new Protocol(this);
	}

	/**
	 * Generate a new sensor with a random location.
	 */
	public Sensor() {
		this(new Location(ran.nextInt(Scaling.getPicXMax()),
					ran.nextInt(Scaling.getPicYMax())));
	}
	
	/**
	 * Generates a new sensor with a given location.
	 * @param loc The sensor location
	 */
	public Sensor(Location loc) {
		this(loc, usedIDs++);
	}
	
	/**
	 * Internal Constructor for sensors with a specific id (used by the loader)
	 * @param loc The sensor location
	 * @param id The sensor id.
	 */
	private Sensor(Location loc, int id) {
		this(id);
		this.loc = loc;
	}
	
	/**
	 * Assigns a label to the sensor.
	 * @param label The new label of the sensor. If null or "", the sensor's current label will be removed.
	 * @return true, if the label was updated.
	 */
	public boolean assignLabel(String label) {
		boolean toReturn = false;
		if(label == null || label.trim().equals("")) {
			toReturn = labelToID.remove(this.sensorLabel) != null;
		} else {
			labelToID.put(label, id);
			toReturn = true;
		}
		this.sensorLabel = label;
		return toReturn;
	}


	
	
	//************************* STATUS MODIFIERS *******************************//
	
	

	
	/**
	 * Change whether the sensor is operational.
	 * @param running true, if the sensor should work, false if it should not.
	 */
	public void setEnabled(boolean running) {
		if(running) {
			status &= ~STATUS_DEAD;
		} else {
			status |= STATUS_DEAD;
		}
	}
	
	/**
	 * Update whether the sensor currently has a turn.
	 * @param isTurn true, if the sensor has turn, false if it does not.
	 */
	public void setHasTurn(boolean isTurn) {
		if(isTurn) {
			status |= STATUS_HAS_TURN;
		} else {
			status &= ~STATUS_HAS_TURN;
		}
	}

	/**
	 * Internal method for updating "secondary selection". Secondary selection is when the sensor can be reached directly 
	 * by a selected sensor.
	 * @param selectedStatus true, if a sensor that can reach this sensor is selected, otherwise false.
	 */
	protected void setSecondarySelection(boolean selectedStatus) {
		if(selectedStatus) {
			status |= STATUS_SECONDARY_SELECTED;
		} else {
			status &= ~STATUS_SECONDARY_SELECTED;
		}
	}

	/**
	 * Notifies the sensor of a new terminal being available.
	 * @param through The sensor to send through to reach the terminal.
	 * @param terminalID The id of the terminal.
	 * @param steps The amount of steps to the new terminal (measured in sensors)
	 */
	public void newTerminal(int through, int terminalID, int steps) {
		if(steps < this.nearestTerminalDist && terminalID != Sensor.INVALID_SENSOR_ID) {
			this.sendThrough = through;
			this.nearestTerminalID = terminalID;
			this.nearestTerminalDist = steps;
		}
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
			idToSensor.get(sen.id).setSecondarySelection(selectedStatus);
		}
	}

	
	/**
	 * Turn a sensor into a terminal or a terminal into a sensor. 
	 * 
	 * @param terminal true if it should be upgrade to a terminal, false if downgraded to normal sensor.
	 * @return A new terminal or sensor depending on the input.
	 */
	public Sensor setTerminalStatus(boolean terminal) {
		if(terminal) {
			if(0 == (this.status & STATUS_IS_TERMINAL)) {
				Note.sendNote(this + " has been promoted to Terminal");
				this.status |= STATUS_IS_TERMINAL;
				this.nearestTerminalID = this.id;
				this.nearestTerminalDist = -1;
				this.sendThrough = this.id;
				terminals.add(this);
				return this;
			}

		} else {
			if(0 != (this.status & STATUS_IS_TERMINAL)) {
				Note.sendNote(this + " has been demoted to Sensor");
				this.status &= ~STATUS_IS_TERMINAL;
				this.nearestTerminalID = Sensor.INVALID_SENSOR_ID;
				this.nearestTerminalDist = Integer.MAX_VALUE;
				this.sendThrough = Sensor.INVALID_SENSOR_ID;
				terminals.remove(this);
				return this;
			}
		}
		return this;
	}

	/**
	 * Test if the Sensor is operational.
	 * @return true if the sensor is available. 
	 */
	public boolean isEnabled() {
		return 0 == (status & STATUS_DEAD);
	}
	
	
	//************************* GETTERS *******************************//
	
	
	/**
	 * The current status of the sensor (Bit-masked)
	 * @return The status.
	 */
	public int getStatus() {
		return this.status;
	}

	
	/**
	 * Returns the first sensor in the route towards the terminal.
	 * @return The first link to the terminal.
	 */
	public int getFirstLinkToTerminal() {
		return this.sendThrough;
	}
	
	/**
	 * Get the distance to the nearest terminal measured in sensors.
	 * @return The distance to the terminal.
	 */
	public int getStepsFromNearestTerminal() {
		return this.nearestTerminalDist;
	}

	/**
	 * Fetches the ID of nearest ID (subject to change as it receives notifications of new terminals).
	 * @return The nearest terminal ID.
	 */
	public int getNearestTerminal(){
		return this.nearestTerminalID;
	}

	/**
	 * Fetches the protocol object in the sensor.
	 * @return The sensor's protocol object.
	 */
	public Protocol getProtocol() {
		return this.protocol;
	}	
	
	//************************************** TRANSMIT ****************************//
	
	@Override
	public void receive(Transmission msg) {
		if(isEnabled()) {
			protocol.receive(msg);
		}
	}

	@Override
	public void transmit(Transmission msg) {
		if(isEnabled()) {
			if(msg.getMessageType() != Data.TYPE_LISTENING) {
				status |= STATUS_SENDING;
			} else {
				status |= STATUS_RECEIVING;
			}
			Sensor receiver = null;
			int toSensor = msg.getRespondsableTransmitter();
			for(Sensor sen : links) {
				if(sen.id == toSensor) {
					receiver = sen;
					continue;
				}
				sen.status |= STATUS_RECEIVING;
				sen.receive(msg);
			}
			if(receiver != null) {
				receiver.receive(msg);
			}
		}
	}

	//************************************  STEP  *******************************// 
	
	@Override
	public void prepare() {
		if(isEnabled()) {
			if(STATUS_IS_TERMINAL == (status & (STATUS_HAS_BROADCASTED | STATUS_IS_TERMINAL))) {
				Transmission trans = new Transmission(Sensor.ALL_SENSORS, Sensor.ALL_SENSORS
						, this.id, Data.generateNetworkMessage(0, this.id));
				protocol.addTransmissionToSend(trans);
				status |= STATUS_HAS_BROADCASTED;
			}
			protocol.prepare();
		}
	}

	@Override
	public void step() {
		if(isEnabled()) {
			protocol.step();
		}
	}

	@Override
	public void endStep() {
		if(isEnabled()) {
			protocol.endStep();
		}
	}
	
	
	//**********************************  LINKING & LOCATIONS  **************************//
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
	protected void updateLinks() {
		if(0 != (status & STATUS_SELECTED)) {
			for(Sensor sen : links) {
				idToSensor.get(sen.id).setSecondarySelection(true);
			}
		}
	}
	
	/**
	 * Fetches a list of the sensors this sensor can reach. (Requires that the GlobalAddressBook has been updated)
	 * @return An array of Sensors that this sensor can reach.
	 */
	public Sensor[] getLinks() {
		if(links.size() == 0) {
			return null;
		}
		return links.toArray(new Sensor[links.size()]);
	}
	

	/**
	 * Check if two sensors can communicate with each other using the current 
	 * transmission radius.
	 * This does NOT take their routing protocols into considerations.
	 * @param sen The other sensor
	 * @return true if they can communicate
	 */
	public boolean canCommunicate(Sensor sen) {
		return loc.internalDistanceCheck(sen.loc) <=  Math.pow(transmissionRadius, 2);
	}

	/**
	 * Gets the location of the sensor.
	 * @return The location of the sensor.
	 */
	public Location getLocation()  {	
		return loc;
	}
	
	//************************* OVERWRITTEN / IMPLEMENTED METHODS *********************// 
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Sensor arg0) {
		return Integer.valueOf(id).compareTo(arg0.id);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Sensor) {
			return this.id == ((Sensor)obj).id;
		}
		return false;
	}
	
	
	public void draw(Graphics g) {
		Color temp = g.getColor();
		g.setColor(chooseColor(GUIReferences.sensorColor));
		new SensorCircle(loc, sensorDrawRadius).draw(g);
		g.setColor(temp);
	}

	//************************* DRAWING (Sensor specific) ******************//

	/**
	 * Draws the route to the terminal from this sensor, if the route exists.
	 * @param g The graphics to draw with.
	 */
	public void drawRouteToTerminal(Graphics g) {
		if(this.nearestTerminalID == Sensor.INVALID_SENSOR_ID)
			return;
		if(0 != (GUIReferences.view & GUIReferences.VIEW_ROUTES)) {
			Sensor current = this;
			Sensor through;
			while(current.nearestTerminalID != current.id) {
				through = idToSensor.get(current.sendThrough);
				new Line(current.getLocation(), through.getLocation()).draw(g);
				current = through;
			}
		}
	}


	/**
	 * Draws lines between this sensor and all its links.
	 * @param g The graphics to draw with.
	 */
	public void drawConnections(Graphics g) {
		for(Sensor sen : links) {
			new Line(loc, sen.getLocation()).draw(g);
		}
	}

	/**
	 * Method for choosing which color to use.
	 * @param defaultColor The default sensor color
	 * @return The color to use.
	 */
	protected Color chooseColor(Color defaultColor) {
		Color toReturn = defaultColor;

		if(0 == (status & STATUS_SELECTED)) {
			if(0 != (status & STATUS_DEAD)) {
				toReturn = GUIReferences.deadColor;
			} else if (0 != (status & STATUS_SECONDARY_SELECTED) 
					&& 0 != (GUIReferences.view & GUIReferences.VIEW_NEIGHBOURS)) {
				toReturn = GUIReferences.secondarySelectedColor;
			} else if(0 != (status & STATUS_HAS_TURN)) {
				toReturn = GUIReferences.currentTurnColor;
			} else if(0 != (status & STATUS_RECEIVING)) {
				toReturn = GUIReferences.receivingColor;
			} else if(0 != (status & STATUS_SENDING)) {
				toReturn = GUIReferences.sendingColor;
			} else if(0 != (GUIReferences.view & GUIReferences.VIEW_ISOLATED) &&
					links.isEmpty()) {
				toReturn = GUIReferences.isolatedColor;
			} else if(0 != (status & STATUS_IS_TERMINAL)) {
				toReturn = GUIReferences.terminalColor;
			}
		} else {
			toReturn = GUIReferences.selectedColor;
		}
		return toReturn;
	}
	

	//************************* MISC **********************//
	/* (non-Javadoc)
	 * @see nodes.Location#toString()
	 */
	@Override
	public String toString() {
		String toReturn = " #" + id + " " + (sensorLabel != null?sensorLabel + " ":"") + getLocation().toString();
		if(0 == (status & STATUS_IS_TERMINAL)) {
			return "Sensor" + toReturn;
		}  
		return "Terminal" + toReturn;
	}

	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			// Not gonna happen
			throw new RuntimeException(e);
		}
	}


	//************************************* XML METHODS ***********************************//
	
	public Element generateXMLElement(Document doc) {
		Element element = doc.createElement("sensor");
		element.setAttribute("id", String.valueOf(id));
		element.setIdAttribute("id", true);
		element.appendChild(this.getLocation().generateXMLElement(doc));
		if(0 != (status & STATUS_IS_TERMINAL) ) {
			element.setAttribute("terminal", "true");
			if(0 != (status & Sensor.STATUS_HAS_BROADCASTED) ) {
				element.setAttribute("hasBroadcasted", "true");
			}
		}
		if(sensorLabel != null){
			element.setAttribute("label", sensorLabel);
		}
		if(this.links.size() > 0) {
			Element linksElement = doc.createElement("links");
			Element currentLink;
			for(Sensor sen : links) {
				currentLink = doc.createElement("link");
				currentLink.appendChild(doc.createTextNode(String.valueOf(sen.id)));
				linksElement.appendChild(currentLink);
			}
			element.appendChild(linksElement);
		}
		if(this.nearestTerminalID != Sensor.INVALID_SENSOR_ID) {
			Element nearestTermElement = doc.createElement("nearestTerm");
			Element nearestTermIDElement = doc.createElement("id");
			Element nearestTermDistElement = doc.createElement("dist");
			Element nearestTermLinkElement = doc.createElement("link");
			nearestTermIDElement.appendChild(doc.createTextNode(String.valueOf(this.nearestTerminalID)));
			nearestTermDistElement.appendChild(doc.createTextNode(String.valueOf(this.nearestTerminalDist)));
			nearestTermLinkElement.appendChild(doc.createTextNode(String.valueOf(this.sendThrough)));
			nearestTermElement.appendChild(nearestTermIDElement);
			nearestTermElement.appendChild(nearestTermDistElement);
			nearestTermElement.appendChild(nearestTermLinkElement);
			element.appendChild(nearestTermElement);
		}
		if(status != 0) {
			Element statusElement = doc.createElement("status");
			statusElement.appendChild(doc.createTextNode(String.valueOf(status)));
			element.appendChild(statusElement);
		}
		element.appendChild(protocol.generateXMLElement(doc));
		return element;
	}
	//************************************* STATIC METHODS ********************************//
	
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
	public static Sensor getSensor(int sensorID) {
		return idToSensor.get(sensorID);
	}

	/**
	 * Fetches a collection of all the sensors.
	 * @return A collection of all the sensors.
	 */
	public static Collection<Sensor> getAllSensors() {
		return idToSensor.values();
	}

	/**
	 * Fetches a sensors by its label. 
	 * Sensors do not have a label unless one has been assigned to them!
	 * @param label The label.
	 * @return The Sensor
	 * @throws LabelNotRecognizedException If no sensor has been assigned that label.
	 */
	public static Sensor getSensor(String label) throws LabelNotRecognizedException {
		return getSensor(getSensorID(label));
	}

	/**
	 * Assigns a label to a sensor using the sensor's ID.
	 * @param label The new label of the sensor. If null or "", the sensor's current label will be removed.
	 * @param id The id of the sensor.
	 * @return true, if the label was updated.
	 */
	public static boolean assignLabel(String label, int id) {
		Sensor sen = idToSensor.get(id);
		if(sen.id == Sensor.INVALID_SENSOR_ID) {
			return false;
		}
		return sen.assignLabel(label);
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
	 * Get the amount of terminals
	 * @return The amount of terminals.
	 */
	public static int getAmountOfTerminals() {
		return terminals.size();
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
	
	/**
	 * Convience method for drawing all sensors.
	 * @param g The graphics to use when drawing.
	 */
	public static void drawAll(Graphics g) {
		for(Sensor sen : idToSensor.values()) {
			sen.draw(g);
		}
	}


	/**
	 * Removes all sensors from the list.
	 */
	public static void disposeAllSensors() {
		GlobalAddressBook.clearBook();
		idToSensor = new Hashtable<Integer, Sensor>();
		usedIDs = 0;
	}

	/**
	 * Clears all links from the sensors. They have to be re-generated using the GlobalAddressBook.
	 */
	public static void clearAllLinks() {
		for(Sensor sen : idToSensor.values()) {
			sen.links = new TreeSet<Sensor>();
		}
	}

	/**
	 * Reroll the turn order.
	 */
	public static void rollTurnOrder() {
		for(Sensor sen : idToSensor.values()) {
			sen.transmissionRoll = ran.nextInt(100);
		}
	}	

	/**
	 * Generate a new sensor.
	 * @param loc The location of the sensor.
	 * @return A reference to the new Sensor.
	 */
	public static Sensor newInstance(Location loc) {
		return new Sensor(loc);
	}

	/**
	 * Generate a new sensor at random location.
	 * @return A reference to the new Sensor.
	 */
	public static Sensor newInstance() {
		return newInstance(new Location(ran.nextInt(Scaling.getPicXMax()), ran.nextInt(Scaling.getPicYMax())));
	}

	/**
	 * End of phase update, clearing all "turn"-based status bits from all sensors.
	 */
	public static void endOfPhase() {
		for(Sensor sen : idToSensor.values()) {
			sen.status &= ~Sensor.STATUS_CLEAR_END_OF_ROUND;
		}
	}

	/**
	 * Draws all connections between the sensors, this is faster than asking all the sensors to drawing all their connections..
	 * @param g The graphics to draw with.
	 */
	public static void drawAllConnections(Graphics g) {
		for(Sensor sen : idToSensor.values()) {
			for(Sensor link : sen.links) {
				if(sen.id < link.id) {
					new Line(sen.loc, link.loc).draw(g);
				}
			}
		}
	}

	/**
	 * Draw all known routes to terminals, this is faster than asking all the sensors to draw their route to the termianl
	 * @param g The graphics to draw with.
	 */
	public static void drawAllRoutesToTerminals(Graphics g){
		for(Sensor sen : idToSensor.values()) {
			if(sen.nearestTerminalID == Sensor.INVALID_SENSOR_ID 
					|| sen.sendThrough == Sensor.INVALID_SENSOR_ID) {
				continue;
			}
			new Line(sen.getLocation(), idToSensor.get(sen.sendThrough).getLocation()).draw(g);
		}

	}




	//*********************** XML **********************************//
	/**
	 * Load all sensors from the XML file
	 * @param doc The DOM Document reference of the XML file
	 * @throws XMLParseException Throw if invalid tags/informations was found.
	 */
	public static void loadGeneralDataFromXML(Document doc) throws XMLParseException {
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
	 * Loads all sensors from the xml file.
	 * @param doc The DOM Document
	 * @throws XMLParseException
	 */
	public static void loadAllSensorsFromXML(Document doc) throws XMLParseException{
		NodeList sensorList = doc.getElementsByTagName("sensor");
		int size = sensorList.getLength();
		for(int i = 0 ; i < size ; i++) {
			loadFromXMLElement(sensorList.item(i));
		}
		usedIDs = size;
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
		Node protoNode = null;
		Integer nearestTermID = null;
		Integer nearestTermDist = null;
		Integer nearestTermLink = null;
		Integer status = null;
		int size = list.getLength();
		TreeSet<Integer> linkList = new TreeSet<Integer>();
		for(int i = 0; i < size ; i++) {
			current = list.item(i);
			switch(current.getNodeName().charAt(0)) {
			case 'l':
				if(current.getNodeName().equals("location")) {
					loc = Location.loadFromXMLElement(current);
					noLocation= false;
				} else if(current.getNodeName().equals("links")){
					NodeList linkNodeList = current.getChildNodes();
					int linkNodeListLength = linkNodeList.getLength();
					Node currentLink;
					try {
						for(int j = 0 ; j < linkNodeListLength ; j++) {
							currentLink = linkNodeList.item(j);
							switch(currentLink.getNodeName().charAt(0)) {
							case 'l':
								linkList.add(Integer.parseInt(DOMxmlParser.getTextNodeValue(currentLink).trim()));
								break;
							}
						}
					} catch(Exception e) {
						throw new XMLParseException("links tag was malformatted. ");
					}
				}
				break;
			case 'p':
				if(current.getNodeName().equals("protocol")) {
					protoNode = current;
				}
				break;
			case 'n':
				if(current.getNodeName().equals("nearestTerm")){
					NodeList nearestList = current.getChildNodes();
					Node currentJ;
					try {
						for(int j = 0 ; j < nearestList.getLength() ; j++) {
							currentJ = nearestList.item(j);
							switch(currentJ.getNodeName().charAt(0)) {
							case 'i':
								nearestTermID = Integer.parseInt(DOMxmlParser.getTextNodeValue(currentJ).trim());
								break;
							case 'd':
								nearestTermDist = Integer.parseInt(DOMxmlParser.getTextNodeValue(currentJ).trim());
								break;
							case 'l':
								nearestTermLink = Integer.parseInt(DOMxmlParser.getTextNodeValue(currentJ).trim());
								break;
							}
						}
					} catch(Exception e){
						throw new XMLParseException("nearestTerm or its contents was malformatted.");
					}

				}
				break;
			case 's':
				if(current.getNodeName().equals("status")) {
					try {
						status = Integer.parseInt(DOMxmlParser.getTextNodeValue(current).trim());
					} catch(Exception e) {
						throw new XMLParseException("status tag (or its contents) was malformatted");
					}
				}
				break;
			}


		}

		NamedNodeMap attrMap = sensorElement.getAttributes();
		Node attribute;
		int sensorID = -1;
		int initiative = -1;
		boolean isTerminal = false;
		boolean hasBroadcasted = false;
		try {
			attribute = attrMap.getNamedItem("id");
			if(attribute == null) {
				throw new XMLParseException("Sensor tag missing id attribute!");
			}
			sensorID = Integer.parseInt(attribute.getNodeValue().trim());
			if(sensorID < 0) {
				throw new XMLParseException("Sensor tag id attribute contained invalid value (Mut be int above -1)");
			}
		} catch(RuntimeException e) {
			throw new XMLParseException("Sensor tag id attribute contained invalid value (Mut be int above -1)");
		}
		try {
			attribute = attrMap.getNamedItem("terminal");
			if(attribute != null) {
				isTerminal = Boolean.valueOf(attribute.getNodeValue().trim());
				if(isTerminal) {
					attribute = attrMap.getNamedItem("hasBroadcasted");
					hasBroadcasted = Boolean.valueOf(attribute.getNodeValue().trim());
				}
			}
		} catch(RuntimeException e) {
			isTerminal = false;
		}
		try {
			attribute = attrMap.getNamedItem("initiative");
			if(attribute != null) {
				initiative = Integer.valueOf(attribute.getNodeValue().trim());
			}
		} catch(RuntimeException e) {
			initiative = -1;
		}

		if(noLocation) {
			sen = idToSensor.get(sensorID);
			if(sen == null) {
				throw new XMLParseException("Did not contain Location of Sensor with id: " + sensorID + " and sensor was not loaded! Perhaps another XML file should be loaded first?");
			} 
			if(initiative > -1) {
				sen.transmissionRoll = initiative;
			}
		} else {
			sen = new Sensor(loc, sensorID);
			if(isTerminal) {
				sen.status |= STATUS_IS_TERMINAL;
				if(hasBroadcasted) {
					sen.status |= STATUS_HAS_BROADCASTED;
				}
			}
		}
		if(protoNode != null){
			sen.protocol = Protocol.loadFromXMLElement(protoNode, sen);
		}
		if(nearestTermID != null) {
			sen.nearestTerminalDist = nearestTermDist;
			sen.nearestTerminalID = nearestTermID;
			sen.sendThrough = nearestTermLink;
		}
		if(status != null) {
			sen.status = status;
		}
		if(linkList.size() > 0) {
			TreeSet<Sensor> sensorLinkList = new TreeSet<Sensor>();
			Sensor currentSensorLink;
			for(Integer linkID : linkList) {
				currentSensorLink = idToSensor.get(linkID);
				if(currentSensorLink == null){
					currentSensorLink = new Sensor(linkID);
				}
				sensorLinkList.add(currentSensorLink);
			}
			sen.links = sensorLinkList;
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


	/**
	 * A sorter for sensors that can sort sensors by various things. Other than the "SORT_BY_ID" and "SORT_NATURAL_ORDER" sort, 
	 * the sorts VIOLATES(!) the natural sorting order for sensors.
	 * @author Niels Thykier
	 */
	public static final class SensorComparator implements Comparator<Sensor> {
		private final int compareType;
		/**
		 * Use the natural ordering.
		 */
		public static final int SORT_NATURAL_ORDER = 0;
		/**
		 * Sort sensors by when their turn. If two sensors have a tie, the tie will be cleared by a single (unstored) re-roll.
		 */
		public static final int SORT_BY_TURNS = 1;
		/**
		 * Sort sensors by their ID.
		 */
		public static final int SORT_BY_ID = 2;
		/**
		 * Sort sensors by their distance to the nearest terminal.
		 */
		public static final int SORT_BY_DIST_TO_TERMINAL = 3;

		/**
		 * Constructs a SensorComparator with a sorting type.
		 * @param type The type of sorting, if invalid will default to SORT_NATURAL_ORDER.
		 */
		public SensorComparator(int type) {
			compareType = type;
		}

		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(Sensor o1, Sensor o2) {
			int toReturn = 0;
			switch(compareType) {
			case SORT_BY_DIST_TO_TERMINAL:
				if(o1.nearestTerminalDist < o2.nearestTerminalDist) {
					toReturn = -1;
				} else if(o1.nearestTerminalDist == o2.nearestTerminalDist) {
					toReturn = 0;
				} else {
					toReturn = 1;
				}
				break;
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
			case SORT_NATURAL_ORDER:
			case SORT_BY_ID:
				toReturn = o1.compareTo(o2);
				break;
			}
			return toReturn;
		}
	}




}
