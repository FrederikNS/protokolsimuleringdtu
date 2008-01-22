package shape;

import java.awt.Graphics;

/**
 * This is a method for making object drawable.
 * @author Niels Thykier
 */
public interface Drawable {
	/**
	 * A Drawable object must be able to draw itself using this method.
	 * @param g Grahpics to draw with.
	 */
	public void draw(Graphics g);
}
