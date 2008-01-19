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
		viewRoutes.setToolTipText("Shows the route from the selected sensor to it's closest terminal");
		viewRoutes.addActionListener(actionListener);
		viewRoutes.setActionCommand(String.valueOf(CHECKBOX_ROUTES));
		
		JCheckBox viewIsolated = new JCheckBox("Isolated");
		viewIsolated.setToolTipText("Shows all the routes from all sensors to their terminals");
		viewIsolated.addActionListener(actionListener);
		viewIsolated.setActionCommand(String.valueOf(CHECKBOX_ISOLATED));

		GUIReferences.controlPanelPane.add(viewSettingsPanel);
		viewSettingsPanel.setLayout(viewSettingsLayout);
		viewSettingsPanel.setBorder(BorderFactory.createTitledBorder("Views"));
		viewSettingsPanel.add(viewRadii);
		viewSettingsPanel.add(viewConnections);
		viewSettingsPanel.add(viewNeighbours);
		viewSettingsPanel.add(viewRoutes);
		viewSettingsPanel.add(viewIsolated);
	}
}
