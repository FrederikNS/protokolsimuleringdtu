package gui;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Hashtable;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import math.Scaling;
import nodes.GlobalAdressBook;
import nodes.Location;
import nodes.Sensor;
import nodes.SplitField;

/**
 * @author Frederik Nordahl Sabroe
 *
 */
public class VPGraphicsPainter extends JPanel implements MouseListener,MouseMotionListener,GUIConstants {
	private static final long serialVersionUID = 4244383889572154127L;
	
	private SplitField splitField;
	private JPopupMenu jPop;
	private ControlPanelFrame cpf;

	public VPGraphicsPainter(){
		this.setBackground(Color.white);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		cpf = ControlPanelFrame.getFrame();
	}

	/**
	 * @param g
	 */
	@Override
	public synchronized void paintComponent(Graphics g) {
		super.paintComponent(g);
		Scaling.setWindowSize(this.getWidth(), this.getHeight());
		g.setColor(GUIReferences.sensorColor);
		Hashtable<Integer,Sensor> draw = Sensor.idToSensor;
		for(int i=0;i<draw.size();i++){
			draw.get(i).draw(g);
		}
	}

	public void mouseClicked(MouseEvent arg0) {
		Location loc = new Location(Scaling.pointToLocation(arg0.getPoint()));
		int dist = (int)Math.pow(5,2);
		switch(GUIReferences.mode) {
		case MODE_SELECT:
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
			splitField.addSensor(new Sensor(new Location(Scaling.convertToRealX(arg0.getX()),Scaling.convertToRealY(arg0.getY()))));
			GlobalAdressBook.getAdressBook().generateDirectConnections();
			break;
		case MODE_REMOVE:
			if(GUIReferences.selectedSensor!=null){
				GUIReferences.selectedSensor.setSelected(false);
				GUIReferences.selectedSensor = null;
			}
			selectSensor(loc,dist);
			if(GUIReferences.selectedSensor != null) {

				System.err.println("Ought to remove " + GUIReferences.selectedSensor);
			}
			break;
		case MODE_DISABLE:
			selectSensor(loc,dist);
			if(GUIReferences.selectedSensor != null) {
				if(GUIReferences.selectedSensor.isEnabled()){
					GUIReferences.selectedSensor.setEnabled(false);
				}else{
					GUIReferences.selectedSensor.setEnabled(true);
				}
				GUIReferences.selectedSensor = null;
			} else {
			}
			break;
		}
		repaint();
	}
	
	private void selectSensor(Location loc,int dist){
		GUIReferences.selectedSensor = splitField.selectSensor(loc, dist);
		/*for(int i=0;i<Sensor.usedIDs;i++){
			sen = Sensor.idToSensor.get(i);
			check = sen.internalDistanceCheck(loc);
			if(check < maxDist) {
				maxDist = check;
				GUIReferences.selectedSensor = sen;
			}
		}*/
		
	}

	public SplitField getField() {
		return splitField;
	}
	
	public void createNewField(int w,int h) {
		splitField = new SplitField(0,w,0,h);
	}
	
	//Not used
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {
		cpf.setJLabelStatus(-1, -1);
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
		if( e.isPopupTrigger() ) {
			makePopup(e.getPoint());
		}
	}

	public void mouseReleased(MouseEvent e) {
		if( e.isPopupTrigger() ) {
			makePopup(e.getPoint());
		}
	}

	//Not used
	/* (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	public void mouseDragged(MouseEvent arg0) {}

	public void mouseMoved(MouseEvent e) {
		cpf.setJLabelStatus(Scaling.convertToRealX(e.getX()), Scaling.convertToRealY(e.getY()));
	}
}
