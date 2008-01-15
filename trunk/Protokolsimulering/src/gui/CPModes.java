package gui;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JToggleButton;

/**
 * @author Frederik Nordahl Sabroe
 *
 */
public class CPModes implements GuiInterface {
	public CPModes(ActionListener actionListener){
		JPanel modesPanel = new JPanel();
		FlowLayout modesPanelLayout = new FlowLayout(FlowLayout.CENTER,0,0);
		//GridLayout modesPanelLayout = new GridLayout(1,1);
		JToggleButton killButton = new JToggleButton("Kill");
		killButton.setToolTipText("Removes the sensor you click");
		killButton.addActionListener(actionListener);
		killButton.setActionCommand(String.valueOf(BUTTON_KILL));
		JToggleButton addButton = new JToggleButton("Add");
		addButton.setToolTipText("Adds another sensor where you click");
		addButton.addActionListener(actionListener);
		addButton.setActionCommand(String.valueOf(BUTTON_ADD));
		JToggleButton moveButton = new JToggleButton("Move");
		moveButton.setToolTipText("Makes sensors draggable");
		moveButton.addActionListener(actionListener);
		moveButton.setActionCommand(String.valueOf(BUTTON_MOVE));
		moveButton.setEnabled(false);

		GuiStuff.constructPanel.add(modesPanel);
		modesPanel.setLayout(modesPanelLayout);
		//modesPanel.setBorder(BorderFactory.createTitledBorder("Mode"));
		GuiStuff.modeGroup.add(killButton);
		GuiStuff.modeGroup.add(addButton);
		GuiStuff.modeGroup.add(moveButton);
		modesPanel.add(killButton);
		modesPanel.add(addButton);
		modesPanel.add(moveButton);
	}
}
