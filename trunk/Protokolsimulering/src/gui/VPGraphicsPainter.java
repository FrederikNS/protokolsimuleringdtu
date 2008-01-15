package gui;

import graphics.Scaling;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import nodes.Location;

import shape.DrawableCircle;
import shape.Shape;

/**
 * @author Frederik Nordahl Sabroe
 *
 */
public class VPGraphicsPainter extends JPanel implements MouseListener,GuiInterface {
	private static final long serialVersionUID = 4244383889572154127L;

	private ArrayList<Shape> nodesList = new ArrayList<Shape>();

	public VPGraphicsPainter(){
		this.setBackground(Color.white);
		this.addMouseListener(this);
	}

	/**
	 * @param g
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for(int i=0;i<nodesList.size();i++){
			nodesList.get(i).draw(g);
		}
	}

	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		//System.out.println("("+arg0.getX()+","+arg0.getY()+")");
		switch(GuiStuff.mode) {
		case MODE_SELECT:
			Scaling.setWindowSize(this.getWidth(), this.getHeight());
			break;
		case MODE_ADD:
			try{
				nodesList.add(new DrawableCircle(new Location(arg0.getX(),arg0.getY()),4));
			} catch(Throwable e) {
				System.err.println(e);
			}
			repaint();
			break;
		case MODE_KILL:
			break;
		}
			

	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}
