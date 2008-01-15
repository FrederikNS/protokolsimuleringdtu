package gui;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;

class GuiStuff implements GuiInterface{ 
	static JPanel constructPanel;
	static JPanel simulatePanel;
	static JPanel controlPanelPane;
	static ViewPort sensorNetwork;
	static int playback = PLAYBACK_PAUSE;
	static int mode = MODE_SELECT;
	static ButtonGroup modeGroup;
	static ButtonGroup stepperGroup;
	
	static{
		System.out.println("static started");
		controlPanelPane = new JPanel();
		constructPanel = new JPanel();
		simulatePanel = new JPanel();
		modeGroup = new ButtonGroup();
		stepperGroup = new ButtonGroup();
		controlPanelPane.setLayout(new BoxLayout(controlPanelPane, BoxLayout.Y_AXIS));
		constructPanel.setLayout(new BoxLayout(constructPanel, BoxLayout.Y_AXIS));
		simulatePanel.setLayout(new BoxLayout(simulatePanel, BoxLayout.Y_AXIS));
		System.out.println("static done");
	}
}