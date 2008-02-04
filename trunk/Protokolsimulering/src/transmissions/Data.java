package transmissions;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import exceptions.XMLParseException;

import xml.DOMxmlParser;
import xml.Saveable;

/**
 * This class is responsible for all the data in transmissions.
 * @author Niels Thykier
 */
public class Data implements DataConstants, Saveable{

	/**
	 * An integer-variable for defining which type of data it is.
	 */
	protected int dataType;
	/**
	 * The one and only GarbageData object. 
	 */
	public static final Data GarbageData;
	/**
	 * A variable for the data.
	 */
	protected Object data;
	
	static {
		Data temp = new Data();
		temp.dataType = TYPE_GARBAGE;
		GarbageData = temp;
	}
	/**
	 * A protected constructor.
	 */
	protected Data() {}
	
	/**
	 * This method generates a responce, saying the message was received.
	 * @return the message received-message
	 */
	public static Data generateMessageReceiving() {
		Data toReturn = new Data();
		toReturn.dataType = TYPE_LISTENING | PRIORITY_VERY_HIGH;
		return toReturn;
	}
	
	/**
	 * This method generates a message, that is want to send a message.
	 * @return the "i want to send"-message
	 */
	public static Data generateMessageSending() {
		Data toReturn = new Data();
		toReturn.dataType = TYPE_SENDING | PRIORITY_VERY_HIGH;
		return toReturn;
	}

	/**
	 * This method generates a reply message, saying the message was received unsuccessfully.
	 * @return the unsuccessfully received-message
	 */
	public static Data generateMessageReceivedUnsuccessfully() {
		Data toReturn = new Data();
		toReturn.dataType = TYPE_RECEIVED_UNSUCCESSFULLY | PRIORITY_HIGH;
		return toReturn;
	}
	
	/**
	 * This method generates a reply message, saying the message was received successfully.
	 * @return the successfully received-message
	 */
	public static Data generateMessageReceivedSuccessfully() {
		Data toReturn = new Data();
		toReturn.dataType = TYPE_RECEIVED_SUCCESSFULLY | PRIORITY_HIGH;
		return toReturn;
	}
	
	/**
	 * This method generates a message about the network.
	 * @param distance distance to terminal, meassured in number of sensors
	 * @param link the sensor send through
	 * @return a message about the network
	 */
	public static Data generateNetworkMessage(int distance, int link) {
		Data toReturn = new NetworkData(distance, link);
		toReturn.dataType = TYPE_NETWORK | PRIORITY_MEDIUM;
		return toReturn;
	}
	
	/**
	 * This method generates data to be sendt.
	 * @param obj the data to send
	 * @return a message with the data
	 */
	public static Data generateData(Object obj) {
		Data toReturn = new Data();
		toReturn.dataType = TYPE_DATA | PRIORITY_LOW;
		toReturn.data = obj;
		return toReturn;
	}
	
	/**
	 * This method gets the data type.
	 * @return the data type
	 */
	public int getDataType() {
		return dataType & TYPE_ALL;
	}
	
	/**
	 * This method gets the priority of the data.
	 * @return tje priority of the data
	 */
	public int getSendingPriority() {
		return dataType & PRIORITY_ALL;
	}
	
	public Element generateXMLElement(Document doc) {
		Element dataNode = doc.createElement("data");
		dataNode.appendChild(doc.createTextNode(String.valueOf(dataType)));
		return dataNode;
	}
	
	/**
	 * Loads data from an XML-node
	 * @param dataElement The XML-node
	 * @return The data stored in that node.
	 * @throws XMLParseException Thrown if the node was malformatted.
	 */
	public static Data loadFromXMLElement(Node dataElement) throws XMLParseException {
		if(dataElement.getNodeName().equals("networkData")) {
			return NetworkData.loadFromXMLElement(dataElement);
		}
		if(dataElement.getNodeType() != Node.ELEMENT_NODE || !dataElement.getNodeName().equals("data")) {
			throw new IllegalArgumentException("Node was not a dataElement");
		}
		int dataType = 0;
		try {
			dataType = Integer.parseInt(DOMxmlParser.getTextNodeValue(dataElement).trim());
		} catch(RuntimeException e) {
			throw new XMLParseException("Data tag must contain value dataType integer.");
		}
		Data data = new Data();
		data.dataType = dataType;
		return data;

	}
	
	/**
	 * 
	 * @return The data as NetworkData.
	 * @throws ClassCastException If the data is not NetworkData.
	 */
	public NetworkData asNetworkData() throws ClassCastException{
		return (NetworkData) this;
	}
	
	/**
	 * This method gets the data.
	 * @return the data
	 */
	public Object getData() {
		return data;
	}
	
	/**
	 * This method overrides the clone-method in Object.
	 */
	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			//Not gonna happen
			throw new RuntimeException(e);
		}
	}
}
