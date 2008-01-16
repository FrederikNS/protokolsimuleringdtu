package gui;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

/**
 * @author Frederik Nordahl Sabroe
 *
 */
public class CPStepper implements GUIConstants {
	public CPStepper(ActionListener actionListener){
		JPanel stepperPanel = new JPanel();
		//FlowLayout stepperLayout = new FlowLayout(FlowLayout.CENTER,0,0);
		GridLayout stepperLayout = new GridLayout(3,1);
		stepperPanel.setLayout(stepperLayout);
		JButton toStart = new JButton("|<");
		toStart.setToolTipText("Goto Start");
		toStart.addActionListener(actionListener);
		toStart.setActionCommand(String.valueOf(BUTTON_TO_START));
		JToggleButton rewind = new JToggleButton("<<");
		rewind.setToolTipText("Rewind");
		rewind.addActionListener(actionListener);
		rewind.setActionCommand(String.valueOf(BUTTON_REWIND));
		JButton stepBackwards = new JButton("<|");
		stepBackwards.setToolTipText("Step Backward");
		stepBackwards.addActionListener(actionListener);
		stepBackwards.setActionCommand(String.valueOf(BUTTON_STEP_BACKWARD));
		JToggleButton playBackwards = new JToggleButton("<");
		playBackwards.setToolTipText("Play Backwards");
		playBackwards.addActionListener(actionListener);
		playBackwards.setActionCommand(String.valueOf(BUTTON_PLAY_BACKWARDS));
		JButton stop = new JButton("■");
		stop.setToolTipText("Stop");
		stop.addActionListener(actionListener);
		stop.setActionCommand(String.valueOf(BUTTON_STOP));
		JToggleButton play = new JToggleButton(">");
		play.setToolTipText("Play");
		play.addActionListener(actionListener);
		play.setActionCommand(String.valueOf(BUTTON_PLAY));
		JButton nextSensor = new JButton("||>");
		nextSensor.setToolTipText("Next Sensor");
		nextSensor.addActionListener(actionListener);
		nextSensor.setActionCommand(String.valueOf(BUTTON_NEXT_SENSOR));
		JButton stepForward = new JButton("|>");
		stepForward.setToolTipText("Step Forward");
		stepForward.addActionListener(actionListener);
		stepForward.setActionCommand(String.valueOf(BUTTON_STEP_FORWARD));
		JToggleButton fastForward = new JToggleButton(">>");
		fastForward.setToolTipText("Fast Forward");
		fastForward.addActionListener(actionListener);
		fastForward.setActionCommand(String.valueOf(BUTTON_FAST_FORWARD));

		GUIReferences.simulatePanel.add(stepperPanel);
		//stepperPanel.setBorder(BorderFactory.createTitledBorder("Mode"));
		GUIReferences.stepperGroup.add(rewind);
		GUIReferences.stepperGroup.add(playBackwards);
		GUIReferences.stepperGroup.add(play);
		GUIReferences.stepperGroup.add(stepForward);
		GUIReferences.stepperGroup.add(fastForward);
		stepperPanel.add(toStart);
		stepperPanel.add(rewind);
		stepperPanel.add(fastForward);
		stepperPanel.add(playBackwards);
		stepperPanel.add(stop);
		stepperPanel.add(play);
		stepperPanel.add(stepBackwards);
		stepperPanel.add(nextSensor);
		stepperPanel.add(stepForward);
	}
}
