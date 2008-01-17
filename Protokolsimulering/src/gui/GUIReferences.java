package gui;

import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;

import turns.TurnController;

import math.Scaling;
import nodes.Sensor;

/**
 * @author Frederik Nordahl Sabroe
 *
 */
public class GUIReferences implements GUIConstants{ 
	static JPanel constructPanel;
	static JPanel simulatePanel;
	static JPanel controlPanelPane;
	static ViewPort sensorNetwork;
	static Console console;
	static int playback = PLAYBACK_PAUSE;
	static int mode = MODE_SELECT;
	static ButtonGroup modeGroup;
	static ButtonGroup stepperGroup;
	static TurnController turnController = TurnController.newInstance();
	
	public static void generateNewField(int w,int h,String title){
		Scaling.setPicCoords(w,h);
		GUIReferences.sensorNetwork = new ViewPort(title, 200, 0);
		GUIReferences.sensorNetwork.generateField(w,h);
	}
	/**
	 * The currently selected sensor (or null if none is selected)
	 */
	static Sensor selectedSensor;
	/**
	 * The ActionListener (used whenever a right-click menu is re-generated)
	 */
	static ActionListener listener;
	/**
	 * Bit mask of the selected views.
	 */
	public static int view = 0;
	
	/**
	 * Default Sensor color
	 */
	public static Color sensorColor = /*Color.BLACK*/new Color(0x00007700);
	/**
	 * Selected Sensor color
	 */
	public static Color selectedColor = Color.ORANGE;
	/**
	 * Color of a Sensor which is down.
	 */
	public static Color deadColor = Color.LIGHT_GRAY;
	/**
	 * Color of a Sensor which is sending
	 */
	public static Color sendingColor = Color.GREEN;
	/**
	 * Color of a Sensor which is receiving
	 */
	public static Color receivingColor = Color.BLUE;
	
	public static Color transmissionRadiusColor = Color.MAGENTA;
	
	public static Color connectionColor = Color.RED;
	
	public static Color secondarySelectedColor = Color.CYAN;
	
//	public static Color terminalColor = new Color(0x00AAFFAA);
	
	/**
	 * Init the static variables in the correct order.
	 * Some values is inited through the ControlPanelFrame's constructor. 
	 */
	static{
		controlPanelPane = new JPanel();
		constructPanel = new JPanel();
		simulatePanel = new JPanel();
		modeGroup = new ButtonGroup();
		stepperGroup = new ButtonGroup();
		controlPanelPane.setLayout(new BoxLayout(controlPanelPane, BoxLayout.Y_AXIS));
		constructPanel.setLayout(new BoxLayout(constructPanel, BoxLayout.Y_AXIS));
		simulatePanel.setLayout(new BoxLayout(simulatePanel, BoxLayout.Y_AXIS));
	}
}