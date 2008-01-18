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
public class Sensor implements Transmitter, Prepareable, Comparable<Sensor>, NoteConstants, DataConstants, EndSteppable {

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
	
	protected static final int STATUS_SENDING 				    = 0x00000001;
	protected static final int STATUS_RECEIVING				= 0x00000002;
	protected static final int STATUS_DEAD					    = 0x00000004;
	protected static final int STATUS_SELECTED				    = 0x00000008;
	protected static final int STATUS_SECONDARY_SELECTED	    = 0x00000010;
	
	protected static Random ran = new Random();
	public static int usedIDs = 0;
	
	public final int id;
	protected static int transmissionRadius = SENSOR_TRANSMISSION_RADIUS_DEFAULT;
	
	/**
	 * Handles labelling.
	 */
	private static Hashtable<String,Integer> labelToID = new Hashtable<String,Integer>();
	/**
	 * Handles id-look-up
	 */
	public static Hashtable<Integer,Sensor> idToSensor = new Hashtable<Integer,Sensor>();
	
	protected static Hashtable<Integer, SensorImplementation> idToRealSensor = new Hashtable<Integer, SensorImplementation>();

	protected Sensor(int id) {
		this.id = id;
	}
	protected Sensor(Sensor sen) {
		this(sen.id);
		idToSensor.put(id, this);
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
		SensorImplementation sen = idToRealSensor.get(id);
		if(sen.id == Sensor.INVALID_SENSOR_ID) {
			return false;
		}
		return sen.assignLabel(label);
	}
	
	/**
	 * Assigns a label to the sensor.
	 * @param label The new label of the sensor. If null or "", the sensor's current label will be removed.
	 * @return true, if the label was updated.
	 */
	public boolean assignLabel(String label) {
		boolean toReturn = false;
		SensorImplementation sen = getReal();
		if(label == null || label.trim().equals("")) {
			toReturn = labelToID.remove(sen.sensorLabel) != null;
		} else {
			labelToID.put(label, id);
			toReturn = true;
		}
		sen.sensorLabel = label;
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
	
	
	public boolean canCommunicate(Sensor sen) {
		return getReal().canCommunicate(sen);
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
	
	public boolean isEnabled() {
		return getReal().isEnabled();
	}
	public void setEnabled(boolean running) {
		getReal().setEnabled(running);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Sensor arg0) {
		return Integer.valueOf(id).compareTo(arg0.id);
	}
	
	/**
	 * Adds a link from this sensor to the other.
	 * @param sen The other sensor.
	 */
	public void addLinkToSensor(Sensor sen) {
		getReal().addLinkToSensor(sen);
	}
	
	/**
	 * Fetches a list of the sensors this sensor can reach. (Requires that the GlobalAddressBook has been updated)
	 * @return An array of Sensors that this sensor can reach.
	 */
	public Sensor[] getLinks() {
		return getReal().getLinks();
	}
	
	/**
	 * Flag a sensor as selected or deselected. It will notify its links that they are now / no longer "secondarily selected"
	 * @param selectedStatus true if the sensor is selected, false if it is deselected.
	 */
	public void setSelected(boolean selectedStatus) {
		getReal().setSelected(selectedStatus);
	}
	
	/**
	 * Gets the locaiton of the sensor.
	 * @return The location of the sensor.
	 */
	public Location getLocation() {
		return getReal().getLocation();
	}
	
	/**
	 * Removes all sensors from the list.
	 */
	public static void disposeAllSensors() {
		GlobalAddressBook.clearBook();
		idToSensor = new Hashtable<Integer, Sensor>();
		usedIDs = 0;
		idToRealSensor = new Hashtable<Integer, SensorImplementation>();
	}
	

	public Element generateXMLTurnElement(Document doc) {
		return getReal().generateXMLTurnElement(doc);
	}
	
	public Element generateXMLElement(Document doc) {
		return getReal().generateXMLElement(doc);
	}

	public void setLinkToNearestTerminal(int nearestTerminal) {
		getReal().setLinkToNearestTerminal(nearestTerminal);
	}
	
	
	/**
	 * Turn a sensor into a terminal or a terminal into a sensor. 
	 * 
	 * @param terminal true if it should be upgrade to a terminal, false if downgraded to normal sensor.
	 * @return A new terminal or sensor depending on the input.
	 */
	public Sensor setTerminalStatus(boolean terminal) {
		Sensor sen = idToRealSensor.get(id);
		if(terminal) {
			if(!(sen instanceof Terminal)) {
				Note.sendNote(sen + " has been promoted to Terminal");
				return copySensor(new Terminal(sen));
			}
			
		} else {
			if(sen instanceof Terminal) {
				Note.sendNote(sen + " has been demoted to Sensor");
				GlobalAddressBook.removeTerminal(sen);
				return copySensor(new SensorImplementation(sen));
			}
		}
		return this;
	}
	
	protected SensorImplementation getReal() {
		SensorImplementation sen = idToRealSensor.get(id);
		if(sen == null) {
			sen = SensorImplementation.SENSOR_INVALID;
		}
		return sen;
	}
	
	/**
	 * Generate a new sensor.
	 * @param loc The location of the sensor.
	 * @return A reference to the new Sensor.
	 */
	public static Sensor newInstance(Location loc) {
		return copySensor(new SensorImplementation(loc));
	}
	/**
	 * Generate a new sensor at random location.
	 * @return A reference to the new Sensor.
	 */
	public static Sensor newInstance() {
		return newInstance(new Location(ran.nextInt(Scaling.getPicXMax()), ran.nextInt(Scaling.getPicYMax())));
	}
	
	private static Sensor copySensor(Sensor sen) {
		Sensor toReturn = idToSensor.get(sen.id);
		if(toReturn == null) {
			toReturn = new Sensor(sen);
		}
		return toReturn;
	}

	public static void generateNewData() {
		for(SensorImplementation sen : idToRealSensor.values()) {
			sen.unsentData.add(Data.generateData(new Object()));
		}
	}
	
	/**
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @author Niels Thykier
	 */
	public static class SensorImplementation extends Sensor{
		
		
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
		protected Location loc;
		protected int nearestTerminalID = Sensor.INVALID_SENSOR_ID;
		protected String sensorLabel = null;		
		
		

		public static SensorImplementation SENSOR_INVALID = new SensorImplementation(new Location(-1, -1), Sensor.INVALID_SENSOR_ID);
		
		private SensorImplementation(Location loc, int id) {
			super(id);
			idToRealSensor.put(id, this);
			draw = new SensorCircle(loc, 2);
			this.loc = loc;
		}
	
		protected SensorImplementation(int x,int y){
			this(new Location(x,y));
		}
		
		protected SensorImplementation(){
			this(ran.nextInt(Scaling.getPicXMax()), ran.nextInt(Scaling.getPicYMax()));
		}
		
		protected SensorImplementation(Location loc) {
			this(loc, usedIDs++);
		}
		
		protected SensorImplementation(Sensor sensor) {
			this(sensor, sensor.id);
		}
		
		protected SensorImplementation(Sensor sensor, int id) {
			this(sensor.getLocation(), id);
			SensorImplementation sen = idToRealSensor.get(id);
			this.ingoing = sen.ingoing;
			this.outgoing = sen.outgoing;
			this.links = sen.links;
			this.draw = sen.draw;
			this.transmissionRoll = sen.transmissionRoll;
			this.waiting = sen.waiting;
			this.resendDelay = sen.resendDelay;
			this.status = sen.status;
			SensorImplementation senImp;
			for(Sensor loop : links) {
				senImp = idToRealSensor.get(loop.id);
				senImp.links.remove(sen);
				senImp.addLinkToSensor(this);
			}
		}
		
		
		
		/**
		 * Test if the Sensor is operational.
		 * @return true if the sensor is available. 
		 */
		@Override
		public boolean isEnabled() {
			return 0 == (status & STATUS_DEAD);
		}

		@Override
		public void setEnabled(boolean running) {
			if(running) {
				status &= ~STATUS_DEAD;
			} else {
				status |= STATUS_DEAD;
			}
		}
		
		protected void setSecondaySelection(boolean selectedStatus) {
			if(selectedStatus) {
				status |= STATUS_SECONDARY_SELECTED;
			} else {
				status &= ~STATUS_SECONDARY_SELECTED;
			}
		}
		
		/**
		 * Flag a sensor as selected or deselected. It will notify its links that they are now / no longer "secondarily selected"
		 * @param selectedStatus true if the sensor is selected, false if it is deselected.
		 */
		@Override
		public void setSelected(boolean selectedStatus) {
			if(selectedStatus) {
				status |= STATUS_SELECTED;
			} else {
				status &= ~STATUS_SELECTED;
			}
		
			for(Sensor sen : links) {
				idToRealSensor.get(sen.id).setSecondaySelection(selectedStatus);
			}
		}
		
		/**
		 * Check if two sensors can communicate with each other using the current 
		 * transmission radius.
		 * This does NOT take their routing protocols into considerations.
		 * @param sen The other sensor
		 * @return true if they can communicate
		 */
		@Override
		public boolean canCommunicate(Sensor sen) {
			return getLocation().internalDistanceCheck(sen.getLocation()) <=  Math.pow(transmissionRadius, 2);
		}

		/**
		 * Gets the locaiton of the sensor.
		 * @return The location of the sensor.
		 */
		@Override
		public Location getLocation()  {	
			return loc;
		}
		
		/**
		 * Forces the sensor to update the secondary selected. Used by the addLinkToSensor method but can also
		 * be called explicitly if the links have been modified without a call to that method.
		 */
		protected void updateLinks() {
			if(0 != (status & STATUS_SELECTED)) {
				for(Sensor sen : links) {
					idToRealSensor.get(sen.id).setSecondaySelection(true);
				}
			}
		}
		/**
		 * Adds a link from this sensor to the other.
		 * @param sen The other sensor.
		 */
		@Override
		public void addLinkToSensor(Sensor sen) {
			links.add(sen);
			updateLinks();
		}

		/**
		 * Fetches a list of the sensors this sensor can reach. (Requires that the GlobalAddressBook has been updated)
		 * @return An array of Sensors that this sensor can reach.
		 */
		@Override
		public Sensor[] getLinks() {
			return links.toArray(new Sensor[1]);
		}

		
		
		@Override
		public void setLinkToNearestTerminal(int nearestTerminal) {
			Note.sendNote(this + " - Nearest terminal id: " + nearestTerminal);
			this.nearestTerminalID = nearestTerminal;
		}

			
		
		//*********************** XML **********************************//
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
		
		
		@Override
		public Element generateXMLElement(Document doc) {
			Element element = doc.createElement("sensor");
			element.setAttribute("id", String.valueOf(id));
			element.setIdAttribute("id", true);
			element.appendChild(this.getLocation().generateXMLElement(doc));
			return element;
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
				sen = new Sensor(new SensorImplementation(loc, sensorID));
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
		
		/* (non-Javadoc)
		 * @see nodes.Sensor#generateXMLTurnElement(org.w3c.dom.Document)
		 */
		@Override
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
		
		//*********************** DRAW *******************************//
		protected void internalDraw(Graphics g) {
			if( 0 != (GUIReferences.view & GUIReferences.VIEW_CONNECTIONS)) {
				g.setColor(GUIReferences.connectionColor);
				Point senPoint = Scaling.locationToPoint(this.getLocation());
				Point target;
				int size = links.size();
				Sensor sen;
				for(int i = 0 ; i < size ; i++) {
					sen = links.get(i);
					if(sen.id > this.id) {
						target = Scaling.locationToPoint(links.get(i).getLocation());
						g.drawLine(senPoint.x, senPoint.y, target.x, target.y);
					}
				}
			}
			
			g.setColor(chooseColor(GUIReferences.sensorColor));
			draw.draw(g);
			
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
		/* (non-Javadoc)
		 * @see nodes.Location#draw(java.awt.Graphics)
		 */
		@Override
		public void draw(Graphics g) {
			Color temp = g.getColor();
			internalDraw(g);
			g.setColor(temp);
		}
		
		
		//************************* MISC **********************//
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

		@Override
		public void receive(Transmission msg) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void transmit(Transmission msg) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void prepare() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void step() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void endStep() {
			// TODO Auto-generated method stub
			
		}

	}

	
	/**
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @author Niels Thykier
	 */
	public static class Terminal extends SensorImplementation {
		protected Terminal(Sensor sen) {
			super(sen, sen.id);
			GlobalAddressBook.addTerminal(this);
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
				SensorImplementation r1 = idToRealSensor.get(o1.id), r2 = idToRealSensor.get(o2.id);
				if(r1.transmissionRoll < r2.transmissionRoll) {
					toReturn = -1;
				} else if(r1.transmissionRoll == r2.transmissionRoll) {
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


//TODO move
	public void receive(Transmission msg) {
		idToRealSensor.get(id).receive(msg);
	}
	public void transmit(Transmission msg) {
		idToRealSensor.get(id).transmit(msg);	
	}
	public void prepare() {
		idToRealSensor.get(id).prepare();	
	}
	public void step() {
		idToRealSensor.get(id).step();
	}
	public void endStep() {
		idToRealSensor.get(id).endStep();
	}
	public void draw(Graphics g) {
		idToRealSensor.get(id).draw(g);
	}
	

}
