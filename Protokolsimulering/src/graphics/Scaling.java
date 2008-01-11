package graphics;

import math.Fraction;

public class Scaling {
	
	private static int picXMin;
	private static int picYMin;
	private static int picXMax;
	private static int picYMax;
	
	private Scaling() {
	}
	
	public static void checkCoordsMax(int x, int y) {
		if(x > picXMax) {
			setPicCoordsMaxX(x);
		}
		if(y > picYMax) {
			setPicCoordsMaxY(y);
		}
	}
	
	public static void setPicCoordsMaxX(int x) {
		picXMax = x;
	}
	
	public static void setPicCoordsMaxY(int y) {
		picYMax = y;
	}
	
	public static void setPicCoordsMin(int x, int y) {
		picXMin = x;
		picYMin = y;
	}
	
	/**
	 * Scales an x-coordinate to fit the current size of the window
	 * @param x-coordinate
	 * @param currH the current height of the window
	 * @return an x-coordinate that is scaled to fit the window
	 */
	public static int convertToPicX(int x, int currH) {
		return new Fraction(x-picXMin,picXMax-picXMin).multiply(currH).evaluateRoundDown();
	}
	
	/**
	 * Scales an y-coordinate to fit the current size of the window
	 * @param y-coordinate
	 * @param currW the current width of the window
	 * @return an y-coordinate that is scaled to fit the window
	 */
	public static int convertToPicY(int y, int currW) {
		return new Fraction(y-picYMin,picYMax-picYMin).multiply(currW).evaluateRoundDown();
	}
	
	/**
	 * Rewind the scaling to calculate the real x-coordinate
	 * @param x-coordinate that is scaled
	 * @param currH the current height of the window
	 * @return an x-coordinate that isn't scaled
	 */
	public static int convertToRealX(int x, int currH) {
		return new Fraction(x,currH).multiply(picXMax-picXMin).add(picXMin).evaluateRoundDown();
	}
	
	/**
	 * Rewind the scaling to calculate the real y-coordinate
	 * @param y-coordinate that is scaled
	 * @param currW the current width of the window
	 * @return an y-coordinate that isn't scaled
	 */
	public static int convertToRealY(int y, int currW) {
		return new Fraction(y,currW).multiply(picYMax-picYMin).add(picYMin).evaluateRoundDown();
	}
}
