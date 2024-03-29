package nodes;

import java.awt.Graphics;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import shape.Drawable;
import xml.DOMxmlParser;
import xml.Saveable;
import exceptions.XMLParseException;

/**
 * A (2D) location in the field.
 * If it is to be shown or drawn, it should be scaled by using the Scaling.locationToPoint(loc) method.
 * @author Niels Thykier
 */
public class Location implements Drawable,Cloneable, Saveable {

	/**
	 * x-coordinate of the location.
	 */
	private int x;
	/**
	 * y-coordinate of the location.
	 */
	private int y;
	
	/**
	 * Generates a new location from the input.
	 * @param coordX The x-coordinate.
	 * @param coordY The y-coordinate.
	 */
	public Location(int coordX, int coordY) {
		x = coordX;
		y = coordY;
	}

	
	/**
	 * Generates a (copy of) location given a location. Exists mainly for its child-classes.
	 * @param loc The location to copy.
	 */
	public Location(Location loc) {
		x = loc.x;
		y = loc.y;
	}
	
	/**
	 * Gets the distances between two locations.
	 * @param loc1 First location.
	 * @param loc2 Second location.
	 * @return The distance.
	 */
	public static double getDistance(Location loc1, Location loc2) {
		return loc1.getDistance(loc2);
	}
	
	/**
	 * @param loc1
	 * @param loc2
	 * @return The distance sqaured.
	 */
	public static int internalDistanceCheck(Location loc1, Location loc2) {
		return loc1.internalDistanceCheck(loc2);
	}
	
	/**
	 * @param loc
	 * @return The distance sqaured.
	 */
	public int internalDistanceCheck(Location loc) {
		return (int) ( Math.pow(x - loc.x,2) + Math.pow(y - loc.y,2) );
	}
	
	/**
	 * Gets the distances between two locations.
	 * @param loc First location.
	 * @return The distance.
	 */
	public double getDistance(Location loc) {
		return Math.sqrt( internalDistanceCheck(loc) );
	}
	
	/**
	 * Get the x-coordinate of the location.
	 * @return The x-coordinate
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Get the y-coordinate of the location.
	 * @return The y-coordinate
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * This method uses the method draw from Drawable.
	 * @param g the graphical object to be draw
	 * @see shape.Drawable#draw(java.awt.Graphics)
	 */
	public void draw(Graphics g) {
		g.drawOval(x-1, y-1, 3, 3);
	}
	
	/**
	 * This method overrides the equals-method from Object.
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Location) {
			Location loc = (Location) obj;
			return (x == loc.x && y == loc.y) 
				|| (x == loc.y && y == loc.x);
			}
		return false;
	}
	
	/**
	 * This method overrides the clone-method from Object.
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		return new Location(this);
	}
	
	/**
	 * This method moves a location to a new location.
	 * @param newLocation the new location
	 */
	protected void relocate(Location newLocation) {
		this.x = newLocation.x;
		this.y = newLocation.y;
	}
	
	/**
	 * Loads a location from a XML-node
	 * @param locationNode The location node.
	 * @return The location stored in the node.
	 * @throws XMLParseException If the location node was malformatted.
	 */
	public static Location loadFromXMLElement(Node locationNode) throws XMLParseException {
		if(locationNode.getNodeType() != Node.ELEMENT_NODE || !locationNode.getNodeName().equals("location")) {
			throw new IllegalArgumentException("Node was not a locationNode");
		}
		NodeList list = locationNode.getChildNodes();
		int size = list.getLength();
		Node current;
		Integer locX = null;
		Integer locY = null;
		for(int i = 0; i < size ; i++) {
			current = list.item(i);
			switch(current.getNodeName().charAt(0)) {
			case 'x':
				if(current.getNodeName().length() < 2 && locX == null) {
					try {
						locX = Integer.parseInt(DOMxmlParser.getTextNodeValue(current).trim());
					} catch(Exception e) {
						throw new XMLParseException("Illegal content value for the tag, " + current.getNodeName() + ": Expected int:" + e);
					}
				} else {
					throw new XMLParseException("The tag, " + current.getNodeName() + " is allowed only once");
				}
				break;
			case 'y':
				if(current.getNodeName().length() < 2 && locY == null) {
					try {
						locY = Integer.parseInt(DOMxmlParser.getTextNodeValue(current).trim());
					} catch(Exception e) {
						throw new XMLParseException("Illegal content value for the tag, " + current.getNodeName() + ": Expected int");
					}
				} else {
					throw new XMLParseException("The tag, " + current.getNodeName() + " is allowed only once");
				}
				break;
				
			}
			
		}
		
		
		return new Location(locX, locY);
	}
	
	public Element generateXMLElement(Document doc) {
		Element element = doc.createElement("location"); 
		Element nodeX = doc.createElement("x");		
		Element nodeY = doc.createElement("y");
		nodeX.appendChild(doc.createTextNode(String.valueOf(this.x)));
		nodeY.appendChild(doc.createTextNode(String.valueOf(this.y)));
		
		element.appendChild(nodeX);
		element.appendChild(nodeY);
		return element;
	}
	
	/**
	 * This method overrides the toString-method from Object.
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}
}

