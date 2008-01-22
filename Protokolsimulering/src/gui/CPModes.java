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
		
		JButton generateButton = new JButton("Generate");
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
		
		/*JToggleButton removeButton = new JToggleButton("Remove");
		removeButton.setToolTipText("Removes the sensor you click");
		removeButton.addActionListener(actionListener);
		removeButton.setActionCommand(String.valueOf(BUTTON_REMOVE));
		removeButton.setEnabled(false);*/
		
		JToggleButton promoteButton = new JToggleButton("Promote");
		promoteButton.setToolTipText("");
		promoteButton.addActionListener(actionListener);
		promoteButton.setActionCommand(String.valueOf(BUTTON_PROMOTE));
		
		JToggleButton demoteButton = new JToggleButton("Demote");
		promoteButton.setToolTipText("");
		demoteButton.addActionListener(actionListener);
		demoteButton.setActionCommand(String.valueOf(BUTTON_DEMOTE));
		
		JLabel radiusLabel = new JLabel("Communication Radius");
		SpinnerNumberModel model = new SpinnerNumberModel(Sensor.getTransmissionRadius(),10,40,1);
		GUIReferences.radiusSpinner = new JSpinner(model);
//		GUIReferences.radiusSpinner.setValue(Sensor.getTransmissionRadius());
		GUIReferences.radiusSpinner.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				Sensor.setTransmissionRadius(Integer.parseInt(GUIReferences.radiusSpinner.getValue().toString()));
				
				if(GUIReferences.sensorNetwork != null) {
					GUIReferences.sensorNetwork.repaint();
				}
			}
		});
		JButton generateAddressBook = new JButton("Generate Address Book");
		generateAddressBook.addActionListener(actionListener);
		generateAddressBook.setActionCommand(String.valueOf(BUTTON_GENERATE_ADDRESS_BOOK));
		
		GUIReferences.constructPanel.add(modePanel);
		modePanel.add(constructDestructPanel);
		modePanel.add(modificationPanel);
		modePanel.add(tweakPanel);
		tweakPanel.add(radiusPanel);
		tweakPanel.add(addressBookPanel);
		
		GUIReferences.modeGroup.add(addButton);
		GUIReferences.modeGroup.add(enableButton);
		GUIReferences.modeGroup.add(disableButton);
//		GUIReferences.modeGroup.add(removeButton);
		GUIReferences.modeGroup.add(promoteButton);
		GUIReferences.modeGroup.add(demoteButton);
		
		constructDestructPanel.add(addButton);
		constructDestructPanel.add(generateButton);
//		constructDestructPanel.add(removeButton);
		constructDestructPanel.add(clearButton);
		modificationPanel.add(enableButton);
		modificationPanel.add(disableButton);
		modificationPanel.add(promoteButton);
		modificationPanel.add(demoteButton);
		
		radiusPanel.add(radiusLabel);
		radiusPanel.add(GUIReferences.radiusSpinner);
		addressBookPanel.add(generateAddressBook);
	}
}