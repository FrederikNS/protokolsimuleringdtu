package nodes;

import graphics.Drawable;

import java.awt.Graphics;
import java.util.Random;

public class Location implements Drawable,Cloneable {
	private int x;
	private int y;
	
	public Location(int coordX, int coordY) {
		x = coordX;
		y = coordY;
	}
	
	public Location() {
		Random ran = new Random();
		x = ran.nextInt(100);
		y = ran.nextInt(100);
	}
	
	public Location(Location loc) {
		x = loc.x;
		y = loc.y;
	}
	
	public static double getDistance(Location loc1, Location loc2) {
		return loc1.getDistance(loc2);
	}
	
	public double getDistance(Location loc) {
		return Math.sqrt( (x - loc.x)^2 + (y - loc.y)^2 );
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void draw(Graphics g) {
		g.drawOval(x, y, 3, 3);
		/*g.drawLine(x-1, y-1, x+1, y+1);
		g.drawLine(x+1, y+1, x-1, y-1);*/
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Location) {
			Location loc = (Location)obj;
			return x == loc.x && y == loc.y;
			}
		return false;
	}
	@Override
	public Location clone() {
		return new Location(this);
	}
	
}

