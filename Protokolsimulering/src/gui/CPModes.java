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
		
		JButton generateButton = new JButton("Generate...");
		generateButton.setToolTipText("Generates a given number of random sensors");
		generateButton.addActionListener(actionListener);
		generateButton.setActionCommand(String.valueOf(BUTTON_GENERATE));
		
		JToggleButton enableButton = new JToggleButton("Enable");
		enableButton.setToolTipText("Enables the sensor you click");
		enableButton.addActionListener(actionListener);
		enableButton.setActionCommand(String.valueOf(BUTTON_ENABLE));
		
		JToggleButton disableButton = new JToggleButton("Disable");
		disableButton.setToolTipText("Disables the sensor you click");
		disableButton.addActionListener(actionListener);
		disableButton.setActionCommand(String.valueOf(BUTTON_DISABLE));
		
		JToggleButton addButton = new JToggleButton("Add");
		addButton.setToolTipText("Adds another sensor where you click");
		addButton.addActionListener(actionListener);
		addButton.setActionCommand(String.valueOf(BUTTON_ADD));
		
		JButton clearButton = new JButton("Clear");
		clearButton.setToolTipText("Removes all sensors");
		clearButton.addActionListener(actionListener);
		clearButton.setActionCommand(String.valueOf(BUTTON_CLEAR));
		
		JToggleButton removeButton = new JToggleButton("Remove");
		removeButton.setToolTipText("Removes the sensor you click");
		removeButton.addActionListener(actionListener);
		removeButton.setActionCommand(String.valueOf(BUTTON_REMOVE));
		removeButton.setEnabled(false);
		
		GUIReferences.constructPanel.add(modesPanel);
		modesPanel.setLayout(modesPanelLayout);
		
		GUIReferences.modeGroup.add(addButton);
		GUIReferences.modeGroup.add(enableButton);
		GUIReferences.modeGroup.add(disableButton);
		GUIReferences.modeGroup.add(removeButton);
		
		modesPanel.add(addButton);
		modesPanel.add(generateButton);
		modesPanel.add(removeButton);
		modesPanel.add(clearButton);
		modesPanel.add(enableButton);
		modesPanel.add(disableButton);
	}
}