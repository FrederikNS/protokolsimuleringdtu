package gui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Contains the options for changing colors on graphical elements
 * @author frederikns
 */
public class ConfigFrame extends JDialog implements GUIConstants{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3360612748266382475L;
	
	
	
	private ConfigFrame() {

	}
	private ConfigFrame(JFrame frame) {

	}
	
	private void init() {
		ActionListener actionListener = GUIReferences.listener;
		setTitle("Preferences");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		JPanel colorSelectionPane = new JPanel();
		colorSelectionPane.setLayout(new GridLayout(0,1));
		colorSelectionPane.setBorder(BorderFactory.createTitledBorder("Colors"));
		JPanel canvasColorPane = new JPanel();
		canvasColorPane.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
		JPanel sensorColorPane = new JPanel();
		sensorColorPane.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
		JPanel selectedColorPane = new JPanel();
		selectedColorPane.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
		JPanel deadColorPane = new JPanel();
		deadColorPane.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
		JPanel sendingColorPane = new JPanel();
		sendingColorPane.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
		JPanel receivingColorPane = new JPanel();
		receivingColorPane.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
		JPanel transmissionRadiusColorPane = new JPanel();
		transmissionRadiusColorPane.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
		JPanel connectionColorPane = new JPanel();
		connectionColorPane.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
		JPanel secondarySelectedColorPane = new JPanel();
		secondarySelectedColorPane.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
		JPanel terminalColorPane = new JPanel();
		terminalColorPane.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
		JPanel currentTurnColorPane = new JPanel();
		currentTurnColorPane.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
		JPanel isolatedColorPane = new JPanel();
		isolatedColorPane.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
		
		JLabel canvasColorLabel = new JLabel(" Canvas");
		JLabel sensorColorLabel = new JLabel(" Sensor");
		JLabel selectedColorLabel = new JLabel(" Selected Sensor");
		JLabel deadColorLabel = new JLabel(" Dead Sensor");
		JLabel sendingColorLabel = new JLabel(" Sending Sensor");
		JLabel receivingColorLabel = new JLabel(" Receiving Sensor");
		JLabel transmissionRadiusColorLabel = new JLabel(" Transmission Radius");
		JLabel connectionColorLabel = new JLabel(" Connection");
		JLabel secondarySelectedColorLabel = new JLabel(" Secondarily Selected Sensors");
		JLabel terminalColorLabel = new JLabel(" Terminal");
		JLabel currentTurnColorLabel = new JLabel(" Sensor's Turn");
		JLabel isolatedColorLabel = new JLabel(" Isolated Sensor");
		
		JButton canvasColorButton = new JButton (" ");
		canvasColorButton.setBackground(GUIReferences.canvasColor);
		canvasColorButton.addActionListener(actionListener);
		canvasColorButton.setActionCommand(String.valueOf(BUTTON_COLOR_CANVAS));
		JButton sensorColorButton = new JButton (" ");
		sensorColorButton.setBackground(GUIReferences.sensorColor);
		sensorColorButton.addActionListener(actionListener);
		sensorColorButton.setActionCommand(String.valueOf(BUTTON_COLOR_SENSOR));
		JButton selectedColorButton = new JButton (" ");
		selectedColorButton.setBackground(GUIReferences.selectedColor);
		selectedColorButton.addActionListener(actionListener);
		selectedColorButton.setActionCommand(String.valueOf(BUTTON_COLOR_SELECTED));
		JButton deadColorButton = new JButton (" ");
		deadColorButton.setBackground(GUIReferences.deadColor);
		deadColorButton.addActionListener(actionListener);
		deadColorButton.setActionCommand(String.valueOf(BUTTON_COLOR_DEAD));
		JButton sendingColorButton = new JButton (" ");
		sendingColorButton.setBackground(GUIReferences.sendingColor);
		sendingColorButton.addActionListener(actionListener);
		sendingColorButton.setActionCommand(String.valueOf(BUTTON_COLOR_SENDING));
		JButton receivingColorButton = new JButton (" ");
		receivingColorButton.setBackground(GUIReferences.receivingColor);
		receivingColorButton.addActionListener(actionListener);
		receivingColorButton.setActionCommand(String.valueOf(BUTTON_COLOR_RECEIVING));
		JButton transmissionRadiusColorButton = new JButton (" ");
		transmissionRadiusColorButton.setBackground(GUIReferences.transmissionRadiusColor);
		transmissionRadiusColorButton.addActionListener(actionListener);
		transmissionRadiusColorButton.setActionCommand(String.valueOf(BUTTON_COLOR_TRANSMISSION_RADIUS));
		JButton connectionColorButton = new JButton (" ");
		connectionColorButton.setBackground(GUIReferences.connectionColor);
		connectionColorButton.addActionListener(actionListener);
		connectionColorButton.setActionCommand(String.valueOf(BUTTON_COLOR_CONNECTION));
		JButton secondarySelectedColorButton = new JButton (" ");
		secondarySelectedColorButton.setBackground(GUIReferences.secondarySelectedColor);
		secondarySelectedColorButton.addActionListener(actionListener);
		secondarySelectedColorButton.setActionCommand(String.valueOf(BUTTON_COLOR_SECONDARY_SELECTED));
		JButton terminalColorButton = new JButton (" ");
		terminalColorButton.setBackground(GUIReferences.terminalColor);
		terminalColorButton.addActionListener(actionListener);
		terminalColorButton.setActionCommand(String.valueOf(BUTTON_COLOR_TERMINAL));
		JButton currentTurnColorButton = new JButton (" ");
		currentTurnColorButton.setBackground(GUIReferences.currentTurnColor);
		currentTurnColorButton.addActionListener(actionListener);
		currentTurnColorButton.setActionCommand(String.valueOf(BUTTON_COLOR_CURRENT_TURN));
		JButton isolatedColorButton = new JButton (" ");
		isolatedColorButton.setBackground(GUIReferences.isolatedColor);
		isolatedColorButton.addActionListener(actionListener);
		isolatedColorButton.setActionCommand(String.valueOf(BUTTON_COLOR_ISOLATED));
		
		add(colorSelectionPane);
		colorSelectionPane.add(canvasColorPane);
		colorSelectionPane.add(sensorColorPane);
		colorSelectionPane.add(selectedColorPane);
		colorSelectionPane.add(deadColorPane);
		colorSelectionPane.add(sendingColorPane);
		colorSelectionPane.add(receivingColorPane);
		colorSelectionPane.add(transmissionRadiusColorPane);
		colorSelectionPane.add(connectionColorPane);
		colorSelectionPane.add(secondarySelectedColorPane);
		colorSelectionPane.add(terminalColorPane);
		colorSelectionPane.add(currentTurnColorPane);
		colorSelectionPane.add(isolatedColorPane);
		
		canvasColorPane.add(canvasColorButton);
		sensorColorPane.add(sensorColorButton);
		selectedColorPane.add(selectedColorButton);
		deadColorPane.add(deadColorButton);
		sendingColorPane.add(sendingColorButton);
		receivingColorPane.add(receivingColorButton);
		transmissionRadiusColorPane.add(transmissionRadiusColorButton);
		connectionColorPane.add(connectionColorButton);
		secondarySelectedColorPane.add(secondarySelectedColorButton);
		terminalColorPane.add(terminalColorButton);
		currentTurnColorPane.add(currentTurnColorButton);
		isolatedColorPane.add(isolatedColorButton);
		
		canvasColorPane.add(canvasColorLabel);
		sensorColorPane.add(sensorColorLabel);
		selectedColorPane.add(selectedColorLabel);
		deadColorPane.add(deadColorLabel);
		sendingColorPane.add(sendingColorLabel);
		receivingColorPane.add(receivingColorLabel);
		transmissionRadiusColorPane.add(transmissionRadiusColorLabel);
		connectionColorPane.add(connectionColorLabel);
		secondarySelectedColorPane.add(secondarySelectedColorLabel);
		terminalColorPane.add(terminalColorLabel);
		currentTurnColorPane.add(currentTurnColorLabel);
		isolatedColorPane.add(isolatedColorLabel);

		pack();
	}
	
	
	/**
	 * Spawns the config dialog
	 * @return the config dialog
	 */
	public static ConfigFrame openConfigFrame() {
		ConfigFrame dialog;
		if(ControlPanelFrame.getFrame() != null) {
			dialog = new ConfigFrame(ControlPanelFrame.getFrame());
		} else {
			dialog = new ConfigFrame();
		}
		dialog.init();
		return dialog;
	}
}
