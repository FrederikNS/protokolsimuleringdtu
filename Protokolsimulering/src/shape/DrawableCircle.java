package shape;

import graphics.Scaling;

import java.awt.Graphics;

import nodes.Location;

/**
 * A circle that is optimized to be drawn but is scaled up before each draw.
 * @author Niels Thykier
 */
public class DrawableCircle extends Shape {

	/**
	 * Variable for locating the center of the circle.
	 */
	private Location center;
	/**
	 * Variable for the diameter.
	 */
	private int diameter;

	/**
	 * Create a black circle.
	 * @param center The center of the circle
	 * @param radius The radius of the circle.
	 */
	public DrawableCircle(Location center, int radius) {
		this.center = new Location(center.getX() - radius, center.getY() - radius);
		this.diameter = radius*2;
	}

	/**
	 * Gets the center of the circle.
	 * @return The center.
	 */
	public Location getCenter() {
		int radius = diameter/2;
		return new Location(center.getX() + radius, center.getY() + radius);
	}

	/**
	 * Gets the diameter of the circle.
	 * @return The diameter
	 */
	public int getDiameter() {
		return diameter;
	}

	/**
	 * Calculate the radius of the circle.
	 * @return The radius
	 */
	public int getRadius() {
		return diameter/2;
	}
	
	/* (non-Javadoc)
	 * @see graphics.Drawable#draw(java.awt.Graphics)
	 */
	public void draw(Graphics g) {
		g.drawOval(Scaling.convertToPicX(center.getX()), Scaling.convertToPicY(center.getY())
				, Scaling.convertToPicX(diameter), Scaling.convertToPicY(diameter));
	}

}
