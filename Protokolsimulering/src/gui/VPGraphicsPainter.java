package gui;


import static gui.GUIReferences.connectionColor;
import static gui.GUIReferences.selectedSensor;
import static gui.GUIReferences.view;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import math.Scaling;
import nodes.GlobalAddressBook;
import nodes.Location;
import nodes.Sensor;

/**
 * The Canvas for drawing the sensor network
 * @author Frederik Nordahl Sabroe
 * @author Morten Soerensen
 */
public class VPGraphicsPainter extends JPanel implements MouseListener,MouseMotionListener,GUIConstants {
	/**
	 * Serialized ID
	 */
	private static final long serialVersionUID = 4244383889572154127L;
	
	//private PainterThread painter;

	/**
	 * Contructor, creates a new GraphicsPainter (Canvas) for the sensor
	 * network. 
	 */
	public VPGraphicsPainter(){
		this.setBackground(GUIReferences.canvasColor);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		//painter = this.new PainterThread();
		//painter.start();
	}

	@Override
	public void paintComponent(Graphics g) {
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
		if(0 != (view & VIEW_RADII)) {
			g.setColor(GUIReferences.transmissionRadiusColor);
			if(isSelected) {
				selectedSensor.drawTransmissionRadius(g);
			} else {
				for(Sensor sen : Sensor.idToSensor.values()) {
					sen.drawTransmissionRadius(g);
				}
			}
			g.setColor(GUIReferences.sensorColor);
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
			new Sensor(new Location(Scaling.convertToRealX(arg0.getX()),Scaling.convertToRealY(arg0.getY())));
			GlobalAddressBook.getBook().generateDirectConnections();
			GUIReferences.markAsModified();
			GUIReferences.updateStatusBar();
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

	/**
	 * This method takes care of selecting a sensor, given a maximum range and the location of a click
	 * @param loc Where a click was registered
	 * @param dist How far away a sensor may be away from where the click was registered to still be selected
	 */
	private void selectSensor(Location loc,int dist){
		int check;
		int maxDist = dist;
		for (Sensor sensor:Sensor.idToSensor.values()){
			check = sensor.getLocation().internalDistanceCheck(loc);
			if(check < maxDist) {
				maxDist = check;
				GUIReferences.selectedSensor = sensor;
			}
		}
	}

	
	protected class PainterThread extends Thread {
		
		private Graphics paint;
		private volatile boolean isLocked = false; 
		
		protected PainterThread() {
		}
		
		@Override
		public synchronized void run() {
			while(true) {
				try {
					this.wait();
				} catch(Exception e) {
				}
				if(ControlPanelFrame.getFrame() == null || !ControlPanelFrame.getFrame().isVisible()) {
					break;
				}
				if(paint != null) {
					
				}
				paint.dispose();
				paint = null;
			}
		}
		
		public synchronized void repaint(Graphics g) {
			while(isLocked) {
				try {
					this.wait();
				} catch (InterruptedException e) {
				}
			}
			isLocked = true;
			paint = g;
			isLocked = false;
			this.notifyAll();
		}
	}

	//Not used
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
		GUIReferences.viewPort.updateTitleCoordinates();
	}


	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}

	//Not used
	/* (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	public void mouseDragged(MouseEvent arg0) {}

	public void mouseMoved(MouseEvent e) {
		GUIReferences.viewPort.updateTitleCoordinates(Scaling.convertToRealX(e.getX()), Scaling.convertToRealY(e.getY()));
//		cpf.setJLabelStatus(Scaling.convertToRealX(e.getX()), Scaling.convertToRealY(e.getY()), nodes.Sensor.idToSensor.size(),(GUIReferences.turnController.getCurrentTurn()!=null?GUIReferences.turnController.getCurrentTurn().turn:0));
	}
}
