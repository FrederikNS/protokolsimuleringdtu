package gui;

import graphics.Scaling;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import nodes.Location;
import nodes.Sensor;
import nodes.SplitField;
import notification.Note;
import shape.DrawableCircle;
import shape.Shape;
import tests.SelectionTest;

/**
 * @author Frederik Nordahl Sabroe
 *
 */
public class VPGraphicsPainter extends JPanel implements MouseListener,GUIConstants {
	private static final long serialVersionUID = 4244383889572154127L;

	private ArrayList<Shape> nodesList = new ArrayList<Shape>();
	
	private SplitField splitField;
	private JPopupMenu jPop;

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
		g.setColor(GUIReferences.sensorColor);
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
		Location loc = new Location(Scaling.pointToLocation(arg0.getPoint()));
		int dist = (int)Math.pow(5,2);
		switch(GUIReferences.mode) {
		case MODE_SELECT:
			/* 
			Sensor sen = splitField.selectSensor(new Location(Scaling.convertToRealX(arg0.getX()),Scaling.convertToRealY(arg0.getX())), 5);
			if(sen!=null){
				sen.changeColor(Color.RED);
				repaint();
			} else {
				System.out.println("Could not find sensor at location: ("+Scaling.convertToRealX(arg0.getX())+","+Scaling.convertToRealY(arg0.getY())+")");
			}*/
			if(GUIReferences.selectedSensor!=null){
				GUIReferences.selectedSensor.setSelected(false);
				GUIReferences.selectedSensor = null;
			}
			selectSensor(loc,dist);
			if(GUIReferences.selectedSensor != null) {
				GUIReferences.selectedSensor.setSelected(true);
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
			if(GUIReferences.selectedSensor != null) {
				if(GUIReferences.selectedSensor.isEnabled()){
					GUIReferences.selectedSensor.setEnabled(false);
				}else{
					GUIReferences.selectedSensor.setEnabled(true);
				}
				GUIReferences.selectedSensor = null;
			} else {
				Note.sendNote("No Sensor found at: " + loc);
			}
			break;
		}
		repaint();
	}
	
	private void selectSensor(Location loc,int dist){
		Sensor sen = null;
		int check = 0;
		int maxDist = dist;
		for(int i=0;i<Sensor.usedIDs;i++){
			sen = Sensor.idToSensor.get(i);
			check = sen.internalDistanceCheck(loc);
			if(check < maxDist) {
				maxDist = check;
				GUIReferences.selectedSensor = sen;
			}
		}
		
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	private void makePopup(Point point) {
		jPop = new JPopupMenu("JPopupMenu");
		if(GUIReferences.selectedSensor!=null){
			GUIReferences.selectedSensor.setSelected(false);
			GUIReferences.selectedSensor = null;
		}
		selectSensor(Scaling.pointToLocation(point),(int)Math.pow(4, 2));
		if(GUIReferences.selectedSensor != null) {
			GUIReferences.selectedSensor.setSelected(true);
			JMenuItem item = new JMenuItem("View " + GUIReferences.selectedSensor);
			item.setActionCommand(String.valueOf(POPUP_BUTTON_VIEW_SENSOR));
			item.addActionListener(GUIReferences.listener);
			jPop.add(item);
		} else {
			jPop.add(new JMenuItem("Nothing here"));
		}
		jPop.show(this, point.x, point.y);
	}
	
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		if( e.isPopupTrigger() ) {
			makePopup(e.getPoint());
		}
	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		if( e.isPopupTrigger() ) {
			makePopup(e.getPoint());
		}
	}
}