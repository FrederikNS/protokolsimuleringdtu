package gui;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import math.Scaling;
import nodes.GlobalAddressBook;
import nodes.Location;
import nodes.Sensor;

import static gui.GUIReferences.*;

/**
 * @author Frederik Nordahl Sabroe
 * @author Morten Soerensen
 */
public class VPGraphicsPainter extends JPanel implements MouseListener,MouseMotionListener,GUIConstants {
	private static final long serialVersionUID = 4244383889572154127L;

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
		g.setColor(connectionColor);
		boolean isSelected = GUIReferences.selectedSensor != null;
		if( 0 != (view & VIEW_ROUTES)) {
			if(isSelected) {
				selectedSensor.drawRouteToTerminal(g);
			} else {
				Sensor.drawAllRoutesToTerminals(g);
			}
		}
		if(0 != (view & VIEW_CONNECTIONS )) {
			if(isSelected) {
				selectedSensor.drawConnections(g);
			} else {
				Sensor.drawAllConnections(g);
			}
		}
		
		Sensor.drawAll(g);
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
			/*splitField.addSensor(*/Sensor.newInstance(new Location(Scaling.convertToRealX(arg0.getX()),Scaling.convertToRealY(arg0.getY())))/*)*/;
			GlobalAddressBook.getBook().generateDirectConnections();
			GUIReferences.markAsModified();
			break;
		case MODE_REMOVE:
			if(GUIReferences.selectedSensor!=null){
				GUIReferences.selectedSensor.setSelected(false);
				GUIReferences.selectedSensor = null;
			}

			selectSensor(loc,dist);
			GUIReferences.markAsModified();
			break;
		case MODE_ENABLE:
		case MODE_DISABLE:
			selectSensor(loc,dist);
			if(GUIReferences.selectedSensor != null) {
				GUIReferences.selectedSensor.setEnabled(GUIReferences.mode == MODE_ENABLE);
				GUIReferences.markAsModified();
				GUIReferences.selectedSensor = null;
			} else {
			}
			break;
		case MODE_DEMOTE:
		case MODE_PROMOTE:
			if(GUIReferences.selectedSensor!=null){
				GUIReferences.selectedSensor.setSelected(false);
			}
			selectSensor(loc,dist);
			if(GUIReferences.selectedSensor != null) {
				GUIReferences.selectedSensor.setTerminalStatus(GUIReferences.mode == MODE_PROMOTE);
				GUIReferences.markAsModified();
				GUIReferences.selectedSensor = null;
			}
			break;
		}
		GUIReferences.updateViewSettings();
	}

	private void selectSensor(Location loc,int dist){
		Sensor sen;
		int check;
		int maxDist = dist;
		for(int i=0;i<Sensor.usedIDs;i++){
			sen = Sensor.idToSensor.get(i);
			check = sen.getLocation().internalDistanceCheck(loc);
			if(check < maxDist) {
				maxDist = check;
				GUIReferences.selectedSensor = sen;
			}
		}
	}


	//Not used
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {
		cpf.setJLabelStatus(-1, -1, nodes.Sensor.idToSensor.size());
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
		cpf.setJLabelStatus(Scaling.convertToRealX(e.getX()), Scaling.convertToRealY(e.getY()), nodes.Sensor.idToSensor.size());
	}
}
