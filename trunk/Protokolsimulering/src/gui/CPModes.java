package gui;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nodes.Sensor;

/**
 * @author Frederik Nordahl Sabroe
 *
 */
public class CPModes implements GUIConstants {
	/**
	 * Contructor of the CPModes
	 * @param actionListener The ActionListener for all the buttons
	 */
	public CPModes(ActionListener actionListener){
		JPanel modePanel = new JPanel();
		JPanel constructDestructPanel = new JPanel();
		JPanel modificationPanel = new JPanel();
		JPanel tweakPanel = new JPanel();
		JPanel radiusPanel = new JPanel();
		JPanel addressBookPanel = new JPanel();
		modePanel.setLayout(new BoxLayout(modePanel,BoxLayout.Y_AXIS));
		constructDestructPanel.setLayout(new GridLayout(0,2));
		modificationPanel.setLayout(new GridLayout(0,2));
		tweakPanel.setLayout(new BoxLayout(tweakPanel,BoxLayout.Y_AXIS));
		addressBookPanel.setLayout(new GridLayout(0,1));
		
		GUIReferences.generateButton = new JButton("Generate");
		GUIReferences.generateButton.setToolTipText("Generates a given number of random sensors");
		GUIReferences.generateButton.addActionListener(actionListener);
		GUIReferences.generateButton.setActionCommand(String.valueOf(BUTTON_GENERATE));
		
		GUIReferences.enableButton = new JToggleButton("Enable");
		GUIReferences.enableButton.setToolTipText("Enables the sensor you click");
		GUIReferences.enableButton.addActionListener(actionListener);
		GUIReferences.enableButton.setActionCommand(String.valueOf(BUTTON_ENABLE));
		
		GUIReferences.disableButton = new JToggleButton("Disable");
		GUIReferences.disableButton.setToolTipText("Disables the sensor you click");
		GUIReferences.disableButton.addActionListener(actionListener);
		GUIReferences.disableButton.setActionCommand(String.valueOf(BUTTON_DISABLE));
		
		GUIReferences.addButton = new JToggleButton("Add");
		GUIReferences.addButton.setToolTipText("Adds another sensor where you click");
		GUIReferences.addButton.addActionListener(actionListener);
		GUIReferences.addButton.setActionCommand(String.valueOf(BUTTON_ADD));
		
		GUIReferences.clearButton = new JButton("Clear");
		GUIReferences.clearButton.setToolTipText("Removes all sensors");
		GUIReferences.clearButton.addActionListener(actionListener);
		GUIReferences.clearButton.setActionCommand(String.valueOf(BUTTON_CLEAR));
		
		/*JToggleButton removeButton = new JToggleButton("Remove");
		removeButton.setToolTipText("Removes the sensor you click");
		removeButton.addActionListener(actionListener);
		removeButton.setActionCommand(String.valueOf(BUTTON_REMOVE));
		removeButton.setEnabled(false);*/
		
		GUIReferences.promoteButton = new JToggleButton("Promote");
		GUIReferences.promoteButton.setToolTipText("");
		GUIReferences.promoteButton.addActionListener(actionListener);
		GUIReferences.promoteButton.setActionCommand(String.valueOf(BUTTON_PROMOTE));
		
		GUIReferences.demoteButton = new JToggleButton("Demote");
		GUIReferences.promoteButton.setToolTipText("");
		GUIReferences.demoteButton.addActionListener(actionListener);
		GUIReferences.demoteButton.setActionCommand(String.valueOf(BUTTON_DEMOTE));
		
		JLabel radiusLabel = new JLabel("Communication Radius");
		SpinnerNumberModel model = new SpinnerNumberModel(Sensor.getTransmissionRadius(),10,40,1);
		GUIReferences.radiusSpinner = new JSpinner(model);
//		GUIReferences.radiusSpinner.setValue(Sensor.getTransmissionRadius());
		GUIReferences.radiusSpinner.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				Sensor.setTransmissionRadius(Integer.parseInt(GUIReferences.radiusSpinner.getValue().toString()));
				
				if(GUIReferences.viewPort != null) {
					GUIReferences.viewPort.repaint();
				}
			}
		});
		GUIReferences.generateAddressBook = new JButton("Regenerate Connections");
		GUIReferences.generateAddressBook.addActionListener(actionListener);
		GUIReferences.generateAddressBook.setActionCommand(String.valueOf(BUTTON_GENERATE_ADDRESS_BOOK));
		
		GUIReferences.constructPanel.add(modePanel);
		modePanel.add(constructDestructPanel);
		modePanel.add(modificationPanel);
		modePanel.add(tweakPanel);
		tweakPanel.add(radiusPanel);
		tweakPanel.add(addressBookPanel);
		
		GUIReferences.modeGroup.add(GUIReferences.addButton);
		GUIReferences.modeGroup.add(GUIReferences.enableButton);
		GUIReferences.modeGroup.add(GUIReferences.disableButton);
//		GUIReferences.modeGroup.add(removeButton);
		GUIReferences.modeGroup.add(GUIReferences.promoteButton);
		GUIReferences.modeGroup.add(GUIReferences.demoteButton);
		
		constructDestructPanel.add(GUIReferences.addButton);
		constructDestructPanel.add(GUIReferences.generateButton);
//		constructDestructPanel.add(removeButton);
		constructDestructPanel.add(GUIReferences.clearButton);
		modificationPanel.add(GUIReferences.enableButton);
		modificationPanel.add(GUIReferences.disableButton);
		modificationPanel.add(GUIReferences.promoteButton);
		modificationPanel.add(GUIReferences.demoteButton);
		
		radiusPanel.add(radiusLabel);
		radiusPanel.add(GUIReferences.radiusSpinner);
		addressBookPanel.add(GUIReferences.generateAddressBook);
	}
}