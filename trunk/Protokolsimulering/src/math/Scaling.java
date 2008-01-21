package math;

import java.awt.Point;

import nodes.Location;

/**
 * This class is responsible for scaling coordinates so they fit the window.
 * @author Morten Soerensen
 */
public class Scaling {
	
	/**
	 * The maximum x coordinate in the window.
	 */
	private static int picXMax = 1;
	/**
	 * The maximum y coordinate in the window.
	 */
	private static int picYMax = 1;
	/**
	 * The current height of the window.
	 */
	private static int currH = 1;
	/**
	 * The current width of the window.
	 */
	private static int currW = 1;
	
	/**
	 * Prevents creation of static object.
	 */
	private Scaling() {
	}
	
	/**
	 * This method sets the minimum and maximum the y coordinate can be.
	 * @param x The maximum x coordinate
	 * @param y The maximum y coordinate
	 * @throws IllegalArgumentException If the x- or the y-coordinate is less than 1.
	 */
	public static void setPicCoords(int x,int y) throws IllegalArgumentException {
		if(x<1||y<1) {
			throw new IllegalArgumentException("Illegal coordinates.");
		}
		picYMax = y;
		picXMax = x;
	}
	
	/**
	 * This method sets the current width and height of the window.
	 * @param w width
	 * @param h height
	 */
	public static void setWindowSize(int w, int h) {
		currW = w;
		currH = h;
	}
	
	/**
	 * Converts from mouse click point to location that's used internally.
	 * @param point where the mouse clicked
	 * @return a new location
	 */
	public static Location pointToLocation(Point point) {
		return new Location(convertToRealX(point.x), convertToRealY(point.y));
	}
	
	/**
	 * Converts from a locations that is used internally to a mouse point.
	 * @param loc location
	 * @return a new point
	 */
	public static Point locationToPoint(Location loc) {
		return new Point(convertToPicX(loc.getX()), convertToPicY(loc.getY()));
	}
	
	/**
	 * Scales an x coordinate to fit the current size of the window.
	 * @param x coordinate
	 * @return an x coordinate that is scaled to fit the window
	 */
	public static int convertToPicX(int x) {
		return new Fraction(x,picXMax).multiply(currW).evaluateRoundDown();
	}
	
	/**
	 * Scales an y coordinate to fit the current size of the window.
	 * @param y coordinate
	 * @return an y coordinate that is scaled to fit the window
	 */
	public static int convertToPicY(int y) {
		return new Fraction(y,picYMax).multiply(currH).evaluateRoundDown();
	}
	
	/**
	 * Rewind the scaling to calculate the real x coordinate.
	 * @param x coordinate that is scaled
	 * @return an x coordinate that isn't scaled
	 */
	public static int convertToRealX(int x) {
		return new Fraction(x,currW).multiply(picXMax).evaluateRoundDown();
	}
	
	/**
	 * Rewind the scaling to calculate the real y coordinate.
	 * @param y coordinate that is scaled
	 * @return an y coordinate that isn't scaled
	 */
	public static int convertToRealY(int y) {
		return new Fraction(y,currH).multiply(picYMax).evaluateRoundDown();
	}

	/**
	 * @return the picXMax
	 */
	public static int getPicXMax() {
		return picXMax;
	}

	/**
	 * @return the picYMax
	 */
	public static int getPicYMax() {
		return picYMax;
	}
}
