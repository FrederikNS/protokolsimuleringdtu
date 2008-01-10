package shape;

import java.awt.Graphics;

import nodes.Location;

public class DrawableCircle extends Shape {

	private Location center;
	private int diameter;
	
	public DrawableCircle(Location center, int radius) {
		this.center = new Location(center.getX() - radius, center.getY() - radius);
		this.diameter = radius*2;
	}
	
	public Location getCenter() {
		int radius = diameter/2;
		return new Location(center.getX() + radius, center.getY() + radius);
	}
	
	public int getDiameter() {
		return diameter;
	}
	
	public int getRadius() {
		return diameter/2;
	}
	public void draw(Graphics g) {
		g.drawOval(center.getX(), center.getY(), diameter, diameter);
	}

}
