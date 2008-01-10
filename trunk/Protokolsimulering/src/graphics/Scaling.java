package graphics;

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
		return (x-picXMin)/(picXMax-picXMax)*currH;
	}
	
	public int convertToPicY(int y, int currW) {
		return (y-picYMin)/(picYMax-picYMax)*currW;
	}
	
	public int convertToRealX(int x, int currH) {
		return (x/currH)*(picXMax-picXMax)+picXMin;
	}
	
	public int convertToRealY(int y, int currW) {
		return (y/currW)*(picYMax-picYMax)+picYMin;
	}
}
