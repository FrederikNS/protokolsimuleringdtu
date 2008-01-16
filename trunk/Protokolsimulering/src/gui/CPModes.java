package gui;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

/**
 * @author Frederik Nordahl Sabroe
 *
 */
public class CPModes implements GUIConstants {
	public CPModes(ActionListener actionListener){
		JPanel modesPanel = new JPanel();
		FlowLayout modesPanelLayout = new FlowLayout(FlowLayout.LEFT,0,0);
		//GridLayout modesPanelLayout = new GridLayout(1,1);
		JButton generateButton = new JButton("Generate...");
		generateButton.setToolTipText("Generates a given number of random sensors");
		generateButton.addActionListener(actionListener);
		generateButton.setActionCommand(String.valueOf(BUTTON_GENERATE));
		JToggleButton killButton = new JToggleButton("En-/Disable");
		killButton.setToolTipText("Removes the sensor you click");
		killButton.addActionListener(actionListener);
		killButton.setActionCommand(String.valueOf(BUTTON_KILL));
		JToggleButton addButton = new JToggleButton("Add");
		addButton.setToolTipText("Adds another sensor where you click");
		addButton.addActionListener(actionListener);
		addButton.setActionCommand(String.valueOf(BUTTON_ADD));
		JToggleButton clearButton = new JToggleButton("Clear");
		clearButton.setToolTipText("Removes all sensors");
		clearButton.addActionListener(actionListener);
		clearButton.setActionCommand(String.valueOf(BUTTON_CLEAR));
		JToggleButton removeButton = new JToggleButton("Remove");
		removeButton.setToolTipText("Removes the sensor you click")

		GUIReferences.constructPanel.add(modesPanel);
		modesPanel.setLayout(modesPanelLayout);
		//modesPanel.setBorder(BorderFactory.createTitledBorder("Mode"));
		GUIReferences.modeGroup.add(addButton);
		GUIReferences.modeGroup.add(killButton);
		
		GUIReferences.modeGroup.add(clearButton);
		modesPanel.add(addButton);
		modesPanel.add(generateButton);
		modesPanel.add(removeButton);
		modesPanel.add(clearButton);
		modesPanel.add(killButton);
		
	}
}
