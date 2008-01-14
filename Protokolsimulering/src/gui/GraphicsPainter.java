package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import nodes.Location;

import shape.DrawableCircle;
import shape.Shape;

public class GraphicsPainter extends JPanel implements MouseListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4244383889572154127L;
	
	private ArrayList<Shape> nodesList = new ArrayList<Shape>();

	public GraphicsPainter(){
		this.setBackground(Color.white);
		this.addMouseListener(this);
	}
	
	/**
     * @param g
     */
    public void paintComponent(Graphics g) {
    	super.paintComponent(g);
    	for(int i=0;i<nodesList.size();i++){
    		nodesList.get(i).draw(g);
    	}
    }
    
    public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
    	//System.out.println("("+arg0.getX()+","+arg0.getY()+")");
    	if(ControlPanelFrame.mode==ControlPanelFrame.MODE_ADD){
    		try{
    			nodesList.add(new DrawableCircle(new Location(arg0.getX(),arg0.getY()),4));
    			
    		} catch(Throwable e) {
    			System.err.println(e);
    		}
    		repaint();
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
