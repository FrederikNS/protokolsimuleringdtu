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
		GridLayout viewSettingsLayout = new GridLayout(0,1);

		JCheckBox viewRadii = new JCheckBox("Radii");
		viewRadii.setToolTipText("Shows how far the sensors can communicate");
		viewRadii.addActionListener(actionListener);
		viewRadii.setActionCommand(String.valueOf(CHECKBOX_RADII));
		
		JCheckBox viewConnections = new JCheckBox("Connections");
		viewConnections.setToolTipText("Shows lines between connected sensors");
		viewConnections.addActionListener(actionListener);
		viewConnections.setActionCommand(String.valueOf(CHECKBOX_CONNECTIONS));
		
		JCheckBox viewNeighbours = new JCheckBox("Neighbours");
		viewNeighbours.setToolTipText("Highlights the neighbouring sensors of the selected sensor");
		viewNeighbours.addActionListener(actionListener);
		viewNeighbours.setActionCommand(String.valueOf(CHECKBOX_NEIGHBOURS));
		
		JCheckBox viewRoutes = new JCheckBox("Routes");
		viewRoutes.setToolTipText("Shows routes from sensors to their terminals");
		viewRoutes.addActionListener(actionListener);
		viewRoutes.setActionCommand("CHECKBOX_ROUTES");

		GUIReferences.controlPanelPane.add(viewSettingsPanel);
		viewSettingsPanel.setLayout(viewSettingsLayout);
		viewSettingsPanel.setBorder(BorderFactory.createTitledBorder("Views"));
		viewSettingsPanel.add(viewRadii);
		viewSettingsPanel.add(viewConnections);
		viewSettingsPanel.add(viewNeighbours);
		viewSettingsPanel.add(viewRoutes);
	}
}
