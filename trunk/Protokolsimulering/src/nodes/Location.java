package nodes;

import exceptions.XMLParseException;
import graphics.Drawable;

import java.awt.Graphics;
import java.util.Random;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A (2D) location in the field.
 * @author Niels Thykier
 */
public class Location implements Drawable,Cloneable {

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
	 * Generates a random location.
	 * //TODO Make dynamic limits!
	 */
	public Location() {
		Random ran = new Random();
		x = ran.nextInt(100);
		y = ran.nextInt(100);
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
	
	/* (non-Javadoc)
	 * @see graphics.Drawable#draw(java.awt.Graphics)
	 */
	public void draw(Graphics g) {
		g.drawOval(x-1, y-1, 3, 3);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Location) {
			Location loc = (Location) obj;
			return x == loc.x && y == loc.y;
			}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		return new Location(this);
	}
	
	protected void relocate(Location newLocation) {
		this.x = newLocation.x;
		this.y = newLocation.y;
	}
	
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
						locX = Integer.parseInt(current.getNodeValue());
					} catch(NumberFormatException e) {
						throw new XMLParseException("Illegal content value for the tag, " + current.getNodeName() + ": Expected int");
					}
				} else {
					throw new XMLParseException("The tag, " + current.getNodeName() + " is allowed only once");
				}
				break;
			case 'y':
				if(current.getNodeName().length() < 2 && locY == null) {
					try {
						locY = Integer.parseInt(current.getNodeValue());
					} catch(NumberFormatException e) {
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
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}
}

