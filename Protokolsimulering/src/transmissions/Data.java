package transmissions;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import exceptions.XMLParseException;

import xml.DOMxmlParser;

public class Data implements DataConstants{

	protected int dataType;
	public static final Data GarbageData;
	protected Object data;
	
	static {
		Data temp = new Data();
		temp.dataType = TYPE_GARBAGE;
		GarbageData = temp;
	}
	protected Data() {}
	
	public static Data generateMessageReceiving() {
		Data toReturn = new Data();
		toReturn.dataType = TYPE_LISTENING | PRIORITY_VERY_HIGH;
		return toReturn;
	}
	
	public static Data generateMessageSending() {
		Data toReturn = new Data();
		toReturn.dataType = TYPE_SENDING | PRIORITY_VERY_HIGH;
		return toReturn;
	}

	public static Data generateMessageReceivedUnsuccessfully() {
		Data toReturn = new Data();
		toReturn.dataType = TYPE_RECEIVED_UNSUCCESSFULLY | PRIORITY_HIGH;
		return toReturn;
	}
	
	public static Data generateMessageReceivedSuccessfully() {
		Data toReturn = new Data();
		toReturn.dataType = TYPE_RECEIVED_SUCCESSFULLY | PRIORITY_HIGH;
		return toReturn;
	}
	
	public static Data generateNetworkMessage(int distance, int link) {
		Data toReturn = new NetworkData(distance, link);
		toReturn.dataType = TYPE_NETWORK | PRIORITY_MEDIUM;
		return toReturn;
	}
	
	public static Data generateData(Object obj) {
		Data toReturn = new Data();
		toReturn.dataType = TYPE_DATA | PRIORITY_LOW;
		toReturn.data = obj;
		return toReturn;
	}
	
	public int getDataType() {
		return dataType & TYPE_ALL;
	}
	
	public int getSendingPriority() {
		return dataType & PRIORITY_ALL;
	}
	
	public Element generateXMLElement(Document doc) {
		Element dataNode = doc.createElement("data");
		dataNode.appendChild(doc.createTextNode(String.valueOf(dataType)));
		return dataNode;
	}
	
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
	
	public Object getData() {
		return data;
	}
	
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
