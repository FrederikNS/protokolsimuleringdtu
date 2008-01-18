package gui;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class InformationFrame extends JFrame  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -83659491420303025L;
	
//	JLabel sensorLabel;
	JLabel sensorNumberOfNeighbours;
//	JLabel sensorNeighbours;
	JLabel sensorIsTerminal;
	JLabel sensorDistanceToTerminal;
	JLabel sensorSentMessagesAwaitingReply;
	JLabel sensorOutbox;
	JLabel sensorInbox;
	
	public InformationFrame(){
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		setLayout(new GridLayout(0,1));
		
		setTitle("Sensor #X @ (-1,-1)");
//		sensorLabel = new JLabel();
		sensorNumberOfNeighbours = new JLabel("Number of neighbours: 0");
//		sensorNeighbours = new JLabel();
		sensorIsTerminal = new JLabel("Sensor is terminal: false");
		sensorDistanceToTerminal = new JLabel("Sensor has X steps to nearest terminal");
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
