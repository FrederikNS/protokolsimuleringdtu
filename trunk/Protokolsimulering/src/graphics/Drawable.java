package graphics;

import java.awt.Graphics;

public interface Drawable {
	/**
	 * A Drawable object must be able to draw itself using this method.
	 * @param g Grahpics to draw with.
	 */
	public void draw(Graphics g);
}
