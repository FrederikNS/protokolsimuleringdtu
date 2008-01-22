package shape;

import java.awt.Graphics;
import java.awt.Point;

import math.Scaling;
import nodes.Location;

/**
 * This class is responsible for drawing lines.
 * @author Niels Thykier
 */
public class Line extends Shape{

	/**
	 * The from-location.
	 */
	private Location from;
	/**
	 * The to-location.
	 */
	private Location to;
	
	/**
	 * The constructor of this class.
	 * @param locA the from location
	 * @param locB the to location
	 */
	public Line(Location locA, Location locB) {
		from = locA;
		to = locB;
	}
	
	/**
	 * Overrides the draw-method in Drawable.
	 * @see shape.Drawable#draw(java.awt.Graphics)
	 */
	public void draw(Graphics g) {
		Point a = Scaling.locationToPoint(from);
		Point b = Scaling.locationToPoint(to);
		g.drawLine(a.x, a.y, b.x, b.y);
	}

	/**
	 * Overrides the equals-method in Object.
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Line){
			Line line = (Line) obj;
			return (line.from.equals(this.from) && line.to.equals(this.to))
				|| (line.from.equals(this.to) && line.to.equals(this.from));
		}
		return false;
	}
}
