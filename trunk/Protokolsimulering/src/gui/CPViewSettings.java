package gui;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

/**
 * @author Frederik Nordahl Sabroe
 *
 */
public class CPViewSettings implements GUIConstants{
	public CPViewSettings(ActionListener actionListener){
		JPanel viewSettingsPanel = new JPanel();
		//FlowLayout viewSettingsLayout = new FlowLayout(FlowLayout.LEFT,0,0);
		//BoxLayout viewSettingsLayout = new BoxLayout(viewSettingsPanel, BoxLayout.Y_AXIS);
		GridLayout viewSettingsLayout = new GridLayout(0,1);

		JCheckBox viewRadii = new JCheckBox("Radii");
		viewRadii.setToolTipText("Shows how far the sensors can communicate");
		viewRadii.addActionListener(actionListener);
		viewRadii.setActionCommand(String.valueOf(CHECKBOX_RADII));
		JCheckBox viewConnections = new JCheckBox("Connections");
		viewConnections.setToolTipText("Shows lines between connected sensors");
		viewConnections.addActionListener(actionListener);
		viewConnections.setActionCommand(String.valueOf(CHECKBOX_CONNECTIONS));
		JCheckBox viewBattery = new JCheckBox("Battery");
		viewBattery.setToolTipText("Shows the battery level of the sensors");
		viewBattery.addActionListener(actionListener);
		viewBattery.setActionCommand(String.valueOf(CHECKBOX_BATTERY));
		JCheckBox viewID = new JCheckBox("ID");
		viewID.setToolTipText("Shows the ID of the sensors");
		viewID.addActionListener(actionListener);
		viewID.setActionCommand(String.valueOf(CHECKBOX_ID));
		JCheckBox viewNeighbours = new JCheckBox("Neighbours");
		viewNeighbours.setToolTipText("Highlights the neighbouring sensors of the selected sensor");
		viewNeighbours.addActionListener(actionListener);
		viewNeighbours.setActionCommand(String.valueOf(CHECKBOX_NEIGHBOURS));

		GUIReferences.controlPanelPane.add(viewSettingsPanel);
		viewSettingsPanel.setLayout(viewSettingsLayout);
		viewSettingsPanel.setBorder(BorderFactory.createTitledBorder("Views"));
		viewSettingsPanel.add(viewRadii);
		viewSettingsPanel.add(viewConnections);
		viewSettingsPanel.add(viewBattery);
		viewSettingsPanel.add(viewID);
		viewSettingsPanel.add(viewNeighbours);
	}
}
