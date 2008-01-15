package gui;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;

import nodes.Sensor;

/**
 * @author Frederik Nordahl Sabroe
 *
 */
public class GuiStuff implements GuiInterface{ 
	static JPanel constructPanel;
	static JPanel simulatePanel;
	static JPanel controlPanelPane;
	static ViewPort sensorNetwork;
	static int playback = PLAYBACK_PAUSE;
	static int mode = MODE_SELECT;
	static ButtonGroup modeGroup;
	static ButtonGroup stepperGroup;
	static Sensor selectedSensor;
	
	public static Color sensorColor = Color.BLACK;
	public static Color selectedColor = Color.RED;
	public static Color deadColor = Color.LIGHT_GRAY;
	public static Color sendingColor = Color.GREEN;
	public static Color receivingColor = Color.BLUE;
	
	
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