package transmissions;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import xml.DOMxmlParser;

import exceptions.XMLParseException;

/**
 * This class is responsible for informations about the network.
 * @author Niels Thykier
 */
public class NetworkData extends Data {

	/**
	 * An integer for distance to terminal, meassured in number of sensors.
	 */
	private int distance;
	/**
	 * An integer for who to send through to reach the terminal.
	 */
	private int link;
	
	/**
	 * The constructor of this class, sets the informations about who to send data through to reach the terminal.
	 * @param distance the distance to terminal, meassured in sensors
	 * @param link the sensor to send through
	 */
	public NetworkData(int distance, int link) {
		this.distance = distance;
		this.link = link;
		this.dataType = Data.TYPE_NETWORK;
	}
	
	/**
	 * This method gets the sensor to send through.
	 * @return the sensor to send through
	 */
	public int getLink(){
		return link;
	}
	
	/**
	 * This method gets the distance to a terminal.
	 * @return the distance
	 */
	public int getDistance() {
		return distance;
	}
	 
	/**
	 * This method is used when telling about the network (distance and link).
	 * @param newLink the new link
	 * @return a new message about the network
	 */
	public NetworkData nextGenerationData(int newLink) {
		return new NetworkData(this.distance+1, newLink);
	}
	
	/* (non-Javadoc)
	 * @see transmissions.Data#generateXMLElement(org.w3c.dom.Element, org.w3c.dom.Document)
	 */
	@Override
	public Element generateXMLElement(Document doc) {
		Element networkDataElement = doc.createElement("networkData");
		Element distanceElement = doc.createElement("distance");
		Element linkElement = doc.createElement("link");
		distanceElement.appendChild(doc.createTextNode(String.valueOf(distance)));
		linkElement.appendChild(doc.createTextNode(String.valueOf(link)));
		networkDataElement.appendChild(distanceElement);
		networkDataElement.appendChild(linkElement);
		//networkDataElement.appendChild(super.generateXMLElement(doc));
		return networkDataElement;
	}
	
	public static NetworkData loadFromXMLElement(Node networkDataElement) throws XMLParseException {
		NodeList list = networkDataElement.getChildNodes();
		int length = list.getLength();
		Node current;
		Integer distance = null;
		Integer link = null;
		for(int i = 0; i < length ; i++) {
			current = list.item(i);
			switch(current.getNodeName().charAt(0)) {
			case 'l':
				try {
					if(current.getNodeName().equals("link")) {
						link = Integer.parseInt(DOMxmlParser.getTextNodeValue(current).trim());
					}
				} catch(RuntimeException e) {
					throw new XMLParseException("link tag malformattet in networkData tag.");
				}
				break;
			case 'd':
				try {
					if(current.getNodeName().equals("distance")) {
						distance = Integer.parseInt(DOMxmlParser.getTextNodeValue(current).trim());
					}
				} catch(RuntimeException e) {
					throw new XMLParseException("distance tag malformattet in networkData tag.");
				}
				break;
			}
		}
		if(distance == null || link == null) {
			throw new XMLParseException("link or distance tag missing in networkData tag. Link: " + link + ", distance " + distance);
		}
		
		return new NetworkData(distance, link);
	}

	/**
	 * This method overrides the toString-method in Object.
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Net, link: " + link + ", dist: " + distance;
	}
}
