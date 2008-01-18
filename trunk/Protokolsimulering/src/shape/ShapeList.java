package shape;

import java.awt.Graphics;
import java.util.ArrayList;

public class ShapeList implements Drawable{

	private ArrayList<Shape> shapes = new ArrayList<Shape>();
	
	public int getIndexOf(Shape device) {
		int size = shapes.size();
		for(int i = 0 ; i < size ; i++) {
			if(shapes.get(i).equals(device)) {
				return i;
			}
		}
		return -1;
	}
	
	public int size() {
		return shapes.size();
	}
	
	public void add(Shape newShape) {
		if(getIndexOf(newShape) == -1) {
			shapes.add(newShape);
		}
	}
	
	public void addAll(ShapeList list) {
		int size = list.shapes.size();
		for(int i = 0 ; i< size; i++) {
			add(list.shapes.get(i));
		}
	}

	public void draw(Graphics g) {	
		int size = size();
		for(int i = 0 ; i < size ; i++) {
			shapes.get(i).draw(g);
		}
	}
}
