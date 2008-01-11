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
	
	public static int convertToPicX(int x, int currH) {
		return new Fraction(x-picXMin,picXMax-picXMin).multiply(currH).evaluateRoundDown();
	}
	
	public static int convertToPicY(int y, int currW) {
		return new Fraction(y-picYMin,picYMax-picYMin).multiply(currW).evaluateRoundDown();
	}
	
	public static int convertToRealX(int x, int currH) {
		return new Fraction(x,currH).multiply(picXMax-picXMin).add(picXMin).evaluateRoundDown();
	}
	
	public static int convertToRealY(int y, int currW) {
		return new Fraction(y,currW).multiply(picYMax-picYMin).add(picYMin).evaluateRoundDown();
	}
}
