package shape;

import java.awt.Graphics;
import java.awt.Point;

import math.Scaling;
import nodes.Location;

public class Line extends Shape {

	private Location from;
	private Location to;
	
	public Line(Location locA, Location locB) {
		from = locA;
		to = locB;
	}
	
	public void draw(Graphics g) {
		Point a = Scaling.locationToPoint(from);
		Point b = Scaling.locationToPoint(to);
		g.drawLine(a.x, a.y, b.x, b.y);
	}

}
