package graphics;

import math.Fraction;

/**
 * This class is responsible for scaling coordinates so they fit the window.
 * @author Morten Soerensen
 */
public class Scaling {
	
	/**
	 * The minimum x-coordinate in the window.
	 */
	private static int picXMin;
	/**
	 * The minimum y-coordinate in the window.
	 */
	private static int picYMin;
	/**
	 * The maximum x-coordinate in the window.
	 */
	private static int picXMax;
	/**
	 * The maximum y-coordinate in the window.
	 */
	private static int picYMax;
	/**
	 * The current height of the window.
	 */
	private static int currH;
	/**
	 * The current width of the window.
	 */
	private static int currW;
	
	/**
	 * Prevents creation of static object.
	 */
	private Scaling() {
	}
	
	/**
	 * This method sets the minimum and maximum the x-coordinate can be.
	 * @param xMax the maximum number an x-coordinate can be
	 * @param xMin the minimum number an x-coordinate can be
	 * @throws IllegalArgumentException
	 */
	public static void setPicCoordsX(int xMax, int xMin) throws IllegalArgumentException {
		if(xMax <= xMin) {
			throw new IllegalArgumentException("Illegal x-coordinates.");
		}
		picXMax = xMax;
		picXMin = xMin;
	}
	
	/**
	 * This method sets the minimum and maximum the y-coordinate can be.
	 * @param yMax the maximum number an y-coordinate can be
	 * @param yMin the minimum number an y-coordinate can be
	 * @throws IllegalArgumentException
	 */
	public static void setPicCoordsY(int yMax, int yMin) throws IllegalArgumentException {
		if(yMax <= yMin) {
			throw new IllegalArgumentException("Illegal y-coordinates.");
		}
		picYMax = yMax;
		picYMin = yMin;
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
	 * Scales an x-coordinate to fit the current size of the window.
	 * @param x-coordinate
	 * @param currH the current height of the window
	 * @return an x-coordinate that is scaled to fit the window
	 */
	public static int convertToPicX(int x) {
		return new Fraction(x-picXMin,picXMax-picXMin).multiply(currH).evaluateRoundDown();
	}
	
	/**
	 * Scales an y-coordinate to fit the current size of the window.
	 * @param y-coordinate
	 * @param currW the current width of the window
	 * @return an y-coordinate that is scaled to fit the window
	 */
	public static int convertToPicY(int y) {
		return new Fraction(y-picYMin,picYMax-picYMin).multiply(currW).evaluateRoundDown();
	}
	
	/**
	 * Rewind the scaling to calculate the real x-coordinate.
	 * @param x-coordinate that is scaled
	 * @param currH the current height of the window
	 * @return an x-coordinate that isn't scaled
	 */
	public static int convertToRealX(int x) {
		return new Fraction(x,currH).multiply(picXMax-picXMin).add(picXMin).evaluateRoundDown();
	}
	
	/**
	 * Rewind the scaling to calculate the real y-coordinate.
	 * @param y-coordinate that is scaled
	 * @param currW the current width of the window
	 * @return an y-coordinate that isn't scaled
	 */
	public static int convertToRealY(int y) {
		return new Fraction(y,currW).multiply(picYMax-picYMin).add(picYMin).evaluateRoundDown();
	}
}