package gui;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

/**
 * @author Frederik Nordahl Sabroe
 *
 */
public class CPStepper implements GUIConstants {
	/**
	 * Constructor for the CPStepper.
	 * @param actionListener The ActionListener to which all buttons should be registered.
	 */
	public CPStepper(ActionListener actionListener){
		JPanel stepperPanel = new JPanel();
		GridLayout stepperLayout = new GridLayout(4,0);
		stepperPanel.setLayout(stepperLayout);
		JButton stop = new JButton("Stop");
		stop.setToolTipText("Stop");
		stop.addActionListener(actionListener);
		stop.setActionCommand(String.valueOf(BUTTON_STOP));
		JToggleButton play = new JToggleButton("Play");
		play.setToolTipText("Play");
		play.addActionListener(actionListener);
		play.setActionCommand(String.valueOf(BUTTON_PLAY));
		GUIReferences.nextSensor = new JButton("Next Step");
		GUIReferences.nextSensor.setToolTipText("Next Sensor");
		GUIReferences.nextSensor.addActionListener(actionListener);
		GUIReferences.nextSensor.setActionCommand(String.valueOf(BUTTON_NEXT_SENSOR));
		JToggleButton fastForward = new JToggleButton("Fast Forward");
		fastForward.setToolTipText("Fast Forward");
		fastForward.addActionListener(actionListener);
		fastForward.setActionCommand(String.valueOf(BUTTON_FAST_FORWARD));

		GUIReferences.simulatePanel.add(stepperPanel);
		GUIReferences.stepperGroup.add(play);
		GUIReferences.stepperGroup.add(fastForward);
		Enumeration<AbstractButton> buttons = GUIReferences.stepperGroup.getElements();
		while(buttons.hasMoreElements()) {
			buttons.nextElement().setEnabled(false);
		}
		
		stepperPanel.add(stop);
		stepperPanel.add(play);
		stepperPanel.add(GUIReferences.nextSensor);
		stepperPanel.add(fastForward);
	}
}
