package gui;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

import nodes.Sensor;

public class InformationFrame extends JFrame  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -83659491420303025L;
	
//	private JLabel sensorLabel;
	private JLabel sensorNumberOfNeighbours;
//	private JLabel sensorNeighbours;
	private JLabel sensorIsTerminal;
	private JLabel sensorDistanceToTerminal;
	private JLabel sensorSentMessagesAwaitingReply;
	private JLabel sensorOutbox;
	private JLabel sensorInbox;
	
	public InformationFrame(){
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		setLayout(new GridLayout(0,1));
		
		setTitle("Sensor #X @ (-1,-1)");
//		sensorLabel = new JLabel();
		sensorNumberOfNeighbours = new JLabel();
//		sensorNeighbours = new JLabel();
		sensorIsTerminal = new JLabel();
		sensorDistanceToTerminal = new JLabel();
		sensorSentMessagesAwaitingReply = new JLabel(" ");
		sensorOutbox = new JLabel(" ");
		sensorInbox = new JLabel(" ");
		
		add(sensorNumberOfNeighbours);
		add(sensorIsTerminal);
		add(sensorDistanceToTerminal);
		add(sensorInbox);
		add(sensorSentMessagesAwaitingReply);
		add(sensorOutbox);
		
		pack();
	}
	
	public void update(Sensor selected) {
		if(selected != null) {
			Sensor[] links = selected.getLinks();
			int nearestTerm = selected.getNearestTerminal();
			//int status = selected.getStatus();
			setTitle(selected.toString());
			if(links == null || links.length == 0) {
				sensorNumberOfNeighbours.setText("Sensor is isolated.");
			} else { 
				sensorNumberOfNeighbours.setText("Number of neighbours: " + links.length);
			}
			if(nearestTerm == selected.id) {
				sensorIsTerminal.setText("Sensor is terminal.");
				sensorDistanceToTerminal.setText("Distance to Terminal: N/A");
			} else if(nearestTerm == Sensor.INVALID_SENSOR_ID){
				if(links == null || links.length == 0) {
					sensorIsTerminal.setText("Closest Terminal is: N/A");
					sensorDistanceToTerminal.setText("Distance to Terminal: None within reach");
				} else {
					sensorIsTerminal.setText("Closest Terminal is: Unknown.");
					sensorDistanceToTerminal.setText("Distance to Terminal: Unknown.");
				}
			} else {
				sensorIsTerminal.setText("Closest Terminal is: " + nearestTerm);
				int dist = selected.getStepsFromNearestTerminal();
				sensorDistanceToTerminal.setText("Distance to Terminal: " + dist + " Sensor"+(dist == 1?"":"s") + " away.");
			}
		} else {
			setTitle("Nothing selected");
			sensorNumberOfNeighbours.setText("Number of neighbours: N/A");
//			sensorNeighbours = new JLabel();
			sensorIsTerminal.setText("Sensor is terminal: N/A");
			sensorDistanceToTerminal .setText("Distance to Terminal: N/A");
			sensorSentMessagesAwaitingReply.setText(" ");
			sensorOutbox.setText(" ");
			sensorInbox.setText(" ");
		}
		pack();
	}

	/* sendte beskeder som der ventes svar på
	 * ting der er i kø for at blive sendt
	 * modtagede ting som ikke er bearbejdet
	 * location
	 * id
	 * terminal eller ej
	 * planer for nuværende tur
	 * har haft tur
	 * distance terminal
	 * antal naboer
	 */
}
