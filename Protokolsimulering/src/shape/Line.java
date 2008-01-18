package shape;

import java.awt.Graphics;
import java.awt.Point;

import math.Scaling;
import nodes.Location;

public class Line extends Shape implements Comparable<Line>{

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

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Line){
			Line line = (Line) obj;
			return (line.from.equals(this.from) && line.to.equals(this.to))
				|| (line.from.equals(this.to) && line.to.equals(this.from));
		}
		return false;
	}

	public int compareTo(Line o) {
		return 0;
	}
}
