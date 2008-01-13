package nodes;

import graphics.Drawable;

import java.awt.Graphics;
import java.util.Random;

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
	 * Gets the distances between two locations.
	 * @param loc First location.
	 * @return The distance.
	 */
	public double getDistance(Location loc) {
		return Math.sqrt( (x - loc.x)^2 + (y - loc.y)^2 );
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
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}
}

