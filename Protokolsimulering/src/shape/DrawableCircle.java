package shape;

import graphics.Scaling;
import gui.GUIReferences;

import java.awt.Color;
import java.awt.Graphics;

import nodes.Location;
import nodes.Sensor;

/**
 * A circle that is optimized to be drawn but is scaled up before each draw.
 * @author Niels Thykier
 */
public class DrawableCircle extends Shape {

	/**
	 * Variable for locating the center of the circle.
	 */
	protected Location center;
	/**
	 * Variable for the diameter.
	 */
	protected int radius;

	/**
	 * Create a black circle.
	 * @param center The center of the circle
	 * @param radius The radius of the circle.
	 */
	public DrawableCircle(Location center, int radius) {
		this.center = new Location(center.getX(), center.getY());
		this.radius = radius;
	}

	/**
	 * Gets the center of the circle.
	 * @return The center.
	 */
	public Location getCenter() {
		try {
			return (Location) center.clone();
		} catch (CloneNotSupportedException e) {
			//Not gonna happen.
			throw new RuntimeException(e);
		}
	}

	/**
	 * Gets the diameter of the circle.
	 * @return The diameter
	 */
	public int getDiameter() {
		return radius *2;
	}

	/**
	 * Calculate the radius of the circle.
	 * @return The radius
	 */
	public int getRadius() {
		return radius;
	}
	
	/* (non-Javadoc)
	 * @see graphics.Drawable#draw(java.awt.Graphics)
	 */
	public void draw(Graphics g) {
		int diameter = radius *2;
		g.fillOval(Scaling.convertToPicX(center.getX() - radius), Scaling.convertToPicY(center.getY() - radius)
				, Scaling.convertToPicX(diameter), Scaling.convertToPicY(diameter));
	}
	
	public static class SensorCircle extends DrawableCircle{
		
		public SensorCircle(Location center, int radius) {
			super(center, radius);
		}
		
		
		/* (non-Javadoc)
		 * @see shape.DrawableCircle#draw(java.awt.Graphics)
		 */
		@Override
		public void draw(Graphics g) {
			super.draw(g);
			if(0 == (GUIReferences.view & GUIReferences.VIEW_RADII)) {
				return;
			}
			Color temp = g.getColor();
			g.setColor(GUIReferences.transmissionRadiusColor);
			int transRadius = Sensor.getTransmissionRadius();
			int diameter = transRadius * 2;
			g.drawOval(Scaling.convertToPicX(center.getX() - transRadius), Scaling.convertToPicY(center.getY() - transRadius)
					, Scaling.convertToPicX(diameter), Scaling.convertToPicY(diameter));
			g.setColor(temp);
		}
	}

}
