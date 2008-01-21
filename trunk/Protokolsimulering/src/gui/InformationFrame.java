package gui;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;

import nodes.Sensor;
import transmissions.Protocol;
import transmissions.Transmission;

/**
 * InformationFrame is a frame that contains information about the 
 * currently selected sensor.
 * @author Frederik Nordahl Sabroe
 */
public class InformationFrame extends JFrame  {

	private static final long serialVersionUID = -83659491420303025L;


	private JLabel sensorNumberOfNeighbours;
	private JLabel sensorIsTerminal;
	private JLabel sensorDistanceToTerminal;
	private JLabel sensorSentMessagesAwaitingReply;
	private JLabel sensorOutbox;
	private JLabel sensorInbox;
	private JLabel sensorCurrentTick;
	private JLabel sensorDelayNextTransmission;
	private JLabel sensorReceivedMessage;
	private JLabel sensorSentMessage;
	private JLabel sensorWaitingForSensor;

	/**
	 * Inits the information frame.
	 * @return Returns itself for convience.
	 */
	public InformationFrame init(){
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		setLayout(new BoxLayout(this.getContentPane(),BoxLayout.Y_AXIS));

		setTitle("No Sensor Selected");
		sensorNumberOfNeighbours = new JLabel(" ");
		sensorIsTerminal = new JLabel(" ");
		sensorDistanceToTerminal = new JLabel(" ");
		sensorSentMessagesAwaitingReply = new JLabel(" ");
		sensorOutbox = new JLabel(" ");
		sensorInbox = new JLabel(" ");
		sensorCurrentTick = new JLabel(" ");
		sensorDelayNextTransmission = new JLabel(" ");
		sensorReceivedMessage = new JLabel(" ");
		sensorSentMessage = new JLabel(" ");
		sensorWaitingForSensor = new JLabel(" ");

		add(sensorNumberOfNeighbours);
		add(sensorIsTerminal);
		add(sensorDistanceToTerminal);
		add(sensorInbox);
		add(sensorSentMessagesAwaitingReply);
		add(sensorOutbox);
		add(sensorCurrentTick);
		add(sensorDelayNextTransmission);
		add(sensorReceivedMessage);
		add(sensorSentMessage);
		add(sensorWaitingForSensor);
		return this;
	}

	/**
	 * Updates the InformationFrame to reflect the newly selected sensor.
	 * @param selected The newly selected sensor.
	 */
	public void update(Sensor selected) {
		if(selected != null) {
			String sentMessage = ("");
			String inboxMessages = ("");
			String outboxMessages = ("");
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
			Protocol protocol = selected.getProtocol();
			
			sensorCurrentTick.setText("Current Tick: "+ protocol.getCurrentTick());
			sensorDelayNextTransmission.setText("Delay Next Transmission: "+protocol.getDelayNextTransmission());
			sensorReceivedMessage.setText("Received Message: "+protocol.getIncomming());
			
			for(Transmission transmission:protocol.getSent()){
				sentMessage += transmission.toString();
			}
			sensorSentMessage.setText("Sent Message: "+sentMessage);
			for(Transmission transmission:protocol.getIngoing()){
				inboxMessages += transmission.toString();
			}
			sensorInbox.setText("Ingoing: " + inboxMessages);
			
			for(Transmission transmission:protocol.getOutgoing()){
				outboxMessages += transmission.toString();
			}
			sensorOutbox.setText("Outgoing: " + outboxMessages);
			
			sensorWaitingForSensor.setText("Waiting For Sensor: "+protocol.getWaitingForSensor());
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
