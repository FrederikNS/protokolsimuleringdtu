package shape;

import java.awt.Graphics;
import java.util.ArrayList;

/**
 * This class is responsible for holding all the shapes in a list.
 * @author Niels Thykier
 */
public class ShapeList implements Drawable{

	/**
	 * An ArrayList with all the shapes.
	 */
	private ArrayList<Shape> shapes = new ArrayList<Shape>();
	
	/**
	 * This method returns the index of where a specific shape is.
	 * @param device the desired shape
	 * @return the index of a shape
	 */
	public int getIndexOf(Shape device) {
		int size = shapes.size();
		for(int i = 0 ; i < size ; i++) {
			if(shapes.get(i).equals(device)) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * This method returns the size of the ArrayList.
	 * @return the size
	 */
	public int size() {
		return shapes.size();
	}
	
	/**
	 * This method adds a new shape to the ArrayList.
	 * @param newShape the new shape to be added
	 */
	public void add(Shape newShape) {
		if(getIndexOf(newShape) == -1) {
			shapes.add(newShape);
		}
	}
	
	/**
	 * this method adds a list of shapes to the ArrayList.
	 * @param list the list of shapes to be added
	 */
	public void addAll(ShapeList list) {
		int size = list.shapes.size();
		for(int i = 0 ; i< size; i++) {
			add(list.shapes.get(i));
		}
	}

	/**
	 * This method overrides the draw-method in Drawable.
	 * @see shape.Drawable#draw(java.awt.Graphics)
	 */
	public void draw(Graphics g) {	
		int size = size();
		for(int i = 0 ; i < size ; i++) {
			shapes.get(i).draw(g);
		}
	}
}
