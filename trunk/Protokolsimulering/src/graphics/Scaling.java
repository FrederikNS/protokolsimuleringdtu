package graphics;

import math.Fraction;

public class Scaling {
	
	private int picXMin;
	private int picYMin;
	private int picXMax;
	private int picYMax;
	
	public void setPicCoordsMax(int x, int y) {
		picXMax = x;
		picYMax = y;
	}
	
	public void setPicCoordsMin(int x, int y) {
		picXMin = x;
		picYMin = y;
	}
	
	public int convertToPicX(int x, int currH) {
		return new Fraction(x-picXMin, picXMax-picXMax).multiply(currH).evaluateRoundDown();
	}
	
	public int convertToPicY(int y, int currW) {
		return new Fraction(y - picYMin,picYMax - picYMax).multiply(currW).evaluateRoundDown();
	}
	
	public int convertToRealX(int x, int currH) {
		return new Fraction(x,currH).multiply(picXMax - picXMax).add(picXMin).evaluateRoundDown();
	}
	
	public int convertToRealY(int y, int currW) {
		return new Fraction(y,currW).multiply(picYMax-picYMax).add(picYMin).evaluateRoundDown();
	}
}
