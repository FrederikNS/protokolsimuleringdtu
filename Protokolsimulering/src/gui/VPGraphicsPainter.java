package gui;

import graphics.Scaling;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import nodes.Location;
import nodes.Sensor;
import nodes.SplitField;

import shape.DrawableCircle;
import shape.Shape;

import tests.SelectionTest;

/**
 * @author Frederik Nordahl Sabroe
 *
 */
public class VPGraphicsPainter extends JPanel implements MouseListener,GuiInterface {
	private static final long serialVersionUID = 4244383889572154127L;

	private ArrayList<Shape> nodesList = new ArrayList<Shape>();
	
	private SplitField splitField;
	
	public VPGraphicsPainter(){
		this.setBackground(Color.white);
		this.addMouseListener(this);
		splitField = new SplitField(0, 500, 0, 500);
		new SelectionTest(splitField);
	}

	/**
	 * @param g
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Scaling.setWindowSize(this.getWidth(), this.getHeight());
		
		/*for(int i=0;i<nodesList.size();i++){
			//nodesList.get(i).draw(g);
		}*/
		for(int i=0;i<Sensor.usedIDs;i++){
			Sensor.idToSensor.get(i).draw(g);
		}
	}

	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		//System.out.println("("+arg0.getX()+","+arg0.getY()+")");
		Location loc = new Location(Scaling.convertToRealX(arg0.getX()), Scaling.convertToRealY(arg0.getY()));
		int dist = (int)Math.pow(3,2);
		switch(GuiStuff.mode) {
		case MODE_SELECT:
			/*
			Sensor sen = splitField.selectSensor(new Location(Scaling.convertToRealX(arg0.getX()),Scaling.convertToRealY(arg0.getX())), 5);
			if(sen!=null){
				sen.changeColor(Color.RED);
				repaint();
			} else {
				System.out.println("Could not find sensor at location: ("+Scaling.convertToRealX(arg0.getX())+","+Scaling.convertToRealY(arg0.getY())+")");
			}*/
			if(GuiStuff.selectedSensor!=null){
				GuiStuff.selectedSensor.changeColor(Color.BLACK);
				GuiStuff.selectedSensor = null;
			}
			
			selectSensor(loc,dist);
			
			if(GuiStuff.selectedSensor != null) {
				GuiStuff.selectedSensor.changeColor(Color.GRAY);
			} else {
				System.out.println("No Sensor found at: " + loc);
			}
			break;
		case MODE_ADD:
			try{
				nodesList.add(new DrawableCircle(new Location(arg0.getX(),arg0.getY()),4));
			} catch(Throwable e) {
				System.err.println(e);
			}
			break;
		case MODE_KILL:
			selectSensor(loc,dist);
			if(GuiStuff.selectedSensor != null) {
				GuiStuff.selectedSensor.changeColor(Color.BLUE);
				GuiStuff.selectedSensor.setEnabled(false);
				GuiStuff.selectedSensor = null;
			} else {
				System.out.println("No Sensor found at: " + loc);
			}
			break;
		}
		repaint();
	}
	
	private void selectSensor(Location loc,int dist){
		Sensor sen = null;
		int check = 0;
		for(int i=0;i<Sensor.usedIDs;i++){
			sen = Sensor.idToSensor.get(i);
			check = sen.internalDistanceCheck(loc);
			if(check < dist) {
				dist = check;
				GuiStuff.selectedSensor = sen;
			}
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