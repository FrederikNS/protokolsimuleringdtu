package shape;

import java.awt.Graphics;

public class ShapeList implements Drawable{

	private Shape[] shapes;
	private int size;
	
	public ShapeList() {
		resize(10);
	}
	
	private void resize(int newSize) {
		Shape[] temp = new Shape[newSize];
		if(shapes != null) {
			if(size > newSize) {
				size = newSize;
			} 
			for(int i = 0 ; i < size ; i++) {
				temp[i] = shapes[i];
			}
		}
		shapes = temp;
	}
	
	public int getIndexOf(Shape device) {
		for(int i = 0 ; i < size ; i++) {
			if(shapes[i].equals(device)) {
				return i;
			}
		}
		return -1;
	}
	
	public int size() {
		return size;
	}
	
	public void add(Shape newShape) {
		if(getIndexOf(newShape) == -1) {
			shapes[size++] = newShape;
			if(shapes.length == size) {
				resize(10);
			}
		}
	}
	
	public void addAll(ShapeList list) {
		resize(shapes.length + list.shapes.length + 10);
		for(int i = 0 ; i<list.shapes.length ; i++) {
			shapes[size +i] = list.shapes[i];
		}
	}

	public void draw(Graphics g) {
		for ( int i = 0 ; i < size ; i++) {
			shapes[i].draw(g);
		}
	}
}
