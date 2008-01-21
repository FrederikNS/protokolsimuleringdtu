package transmissions;

import nodes.Sensor;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import xml.DOMxmlParser;

import exceptions.XMLParseException;


/**
 * A transmissions can be sent between two transmitters and may contain data.
 * A transmissions is also used to confirm receiving a transmissions previously.
 * @author Niels Thykier
 */
public class Transmission implements Comparable<Transmission>, DataConstants, Cloneable{
	
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
	private Data data;

	/**
	 * Generate a Transmission.
	 * @param receiver The receiver of the transmission
	 * @param through The respondsable transmitter.
	 * @param sender The original sender of the transmission.
	 * @param information The Data it contains.
	 */
	public Transmission(int receiver, int through, int sender, Data information) {
		this.receiver = receiver;
		this.through = through;
		this.sender = sender;
		data = information;
		messageType = information.getDataType();
	}
	
	/**
	 * This method creates a transmission.
	 * @param receiver the receiver
	 * @param sender the sender
	 * @param information the informations in the transmission
	 */
	public Transmission(int receiver, int sender, Data information) {
		this(receiver, Sensor.INVALID_SENSOR_ID, sender, information);
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
	
	public static Transmission generateReceivedGarbage(int receiverOfGarbageData) {
		return new Transmission(-1, receiverOfGarbageData, Data.generateMessageReceivedUnsuccessfully());
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
	 * @return The data.
	 */
	public Data getData() {
		return data;
	}
	
	
	/**
	 * Update the respondsable transmitter field of the message.
	 * @param sensorID The new sensorID of the respondsable transmitter.
	 */
	public void setRespondsableTransmitter(int sensorID) {
		this.through = sensorID;
	}

	
	public static Transmission generateCorruptTransmission() {
		return new Transmission(Sensor.INVALID_SENSOR_ID, Sensor.INVALID_SENSOR_ID, Data.GarbageData);
	}
	
	public void corruptTransmission() {
		data = Data.GarbageData;
		messageType = TYPE_GARBAGE;
		sender = Sensor.INVALID_SENSOR_ID;
		receiver = Sensor.INVALID_SENSOR_ID;
		through = Sensor.INVALID_SENSOR_ID;
	}

	public static Transmission loadFromXMLElement(Node transmissionNode) throws XMLParseException {
		if(transmissionNode.getNodeType() != Node.ELEMENT_NODE || !transmissionNode.getNodeName().equals("transmission")) {
			throw new XMLParseException("Node was not a transmission: " + transmissionNode.getNodeName());
		}
		NodeList list = transmissionNode.getChildNodes();
		Node current;
		Data data = null;
		int receiver = Sensor.INVALID_SENSOR_ID;
		int sender = Sensor.INVALID_SENSOR_ID;
		int through= Sensor.INVALID_SENSOR_ID;
		int size = list.getLength();
		for(int i = 0; i < size ; i++) {
			current = list.item(i);
			switch(current.getNodeName().charAt(0)) {
			case 'c':
				if(current.getNodeName().equals("content")) {
					NodeList dataList = current.getChildNodes();
					Node currentDataNode;
					for(int j = 0 ; j < dataList.getLength() ; j++) {
						currentDataNode = dataList.item(j);
						switch(currentDataNode.getNodeName().charAt(0)){
						case 'd':
						case 'n':
							data = Data.loadFromXMLElement(currentDataNode);
							break;
						}
					}
					
				}
				break;
			case 'r':
				if(current.getNodeName().equals("receiver")) {
					try {
						receiver = Integer.parseInt(DOMxmlParser.getTextNodeValue(current));
					} catch(RuntimeException e) {
						throw new XMLParseException("receiver tag within transmission tag did not contain valid int.");
					}
				}
				break;
			case 's':
				if(current.getNodeName().equals("sender")) {
					try {
						sender = Integer.parseInt(DOMxmlParser.getTextNodeValue(current));
					} catch(RuntimeException e) {
						throw new XMLParseException("sender tag within transmission tag did not contain valid int.");
					}
				}
				break;
			case 't':
				if(current.getNodeName().equals("through")) {
					try {
						through = Integer.parseInt(DOMxmlParser.getTextNodeValue(current));
					} catch(RuntimeException e) {
						throw new XMLParseException("through tag within transmission tag did not contain valid int.");
					}
				}
				break;
			}
			
		}
		if(data == null)
			throw new XMLParseException("data was missing in transmission tag");
		return new Transmission(receiver, through, sender, data);
	}
	
	public Element generateXMLElement(Document doc) {
		Element element = doc.createElement("transmission");
		if(receiver != Sensor.INVALID_SENSOR_ID) {
			Node receiverNode = doc.createElement("receiver");
			Text receivingId = doc.createTextNode(String.valueOf(receiver));
			receiverNode.appendChild(receivingId);
			element.appendChild(receiverNode);
		}
		if(sender != Sensor.INVALID_SENSOR_ID) {
			Node sendingNode = doc.createElement("sender");
			Text sendingId = doc.createTextNode(String.valueOf(sender));
			sendingNode.appendChild(sendingId);
			element.appendChild(sendingNode);
		}
		if(through > -1) {
			Node throughNode = doc.createElement("through");
			Text throughId = doc.createTextNode(String.valueOf(through));
			throughNode.appendChild(throughId);
			element.appendChild(throughNode);
		}
		
		
		if(data != null) {	
			Node contentNode = doc.createElement("content");
			contentNode.appendChild(data.generateXMLElement(doc));
			element.appendChild(contentNode);
		}
		
		return element;
	}	
	
	
	/**
	 * This method compares two priorities.
	 * @param arg0 the transmissions
	 * @return 0 or -1 whenever the compared priorities is equal or not
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
	
	/**
	 * Overrides the clone-method in Object.
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			//Not gonna happen.
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * This method overrides the toString-method in Object.
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String toReturn = "From: #";
		if(this.sender > 0){
			toReturn += this.sender;
		} else {
			toReturn += "Unknown";
		}
		if(through > 0) {
			toReturn += ", through: #" + this.through;
		} else if(through == Sensor.ALL_SENSORS) {
			toReturn += ", through all";
		}
		if(receiver > 0) {
			toReturn += ", for: #" + this.through;
		} else if(receiver == Sensor.ALL_SENSORS) {
			toReturn += ", for all";
		}
		toReturn += ". Type: ";
		int type = getMessageType();
		switch(type) {
		case Data.TYPE_GARBAGE:
			toReturn += "Corrupted.";
			break;
		case Data.TYPE_LISTENING:
			toReturn += "Listening.";
			break;
		case Data.TYPE_SENDING:
			toReturn += "Sending.";
			break;
		case Data.TYPE_NETWORK:
			toReturn += "Network.";
			break;
		case Data.TYPE_RECEIVED_SUCCESSFULLY:
			toReturn += "Success.";
			break;
		case Data.TYPE_RECEIVED_UNSUCCESSFULLY:
			toReturn += "Failure.";
			break;
		case Data.TYPE_DATA:
			toReturn += "Data."; 
			break;
		default:
			toReturn += "unknown: 0x" + Integer.toHexString(type) ;
			break;
		}
		return toReturn;
	}

}
