package gui;

import java.awt.Color;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import math.Scaling;
import nodes.Sensor;
import turns.TurnController;

/**
 * @author Frederik Nordahl Sabroe
 *
 */
public class GUIReferences implements GUIConstants{ 
	static JPanel constructPanel;
	static JPanel simulatePanel;
	static JPanel controlPanelPane;
	public static ViewPort sensorNetwork;
	public static InformationFrame informationFrame;
	static Console console;
	static int playback = PLAYBACK_PAUSE;
	static int mode = MODE_SELECT;
	public static ButtonGroup modeGroup;
	static ButtonGroup stepperGroup;
	static JMenuItem saveMenuItem;
	static boolean mouseWithinViewPort;
	
	/**
	 * The currently used file.
	 */
	static File currentFile;
	
	
	/**
	 * Convience method for opening a new ViewPort with a internal width / height
	 * @param w The (internal) width of the new field.
	 * @param h The (internal) height of the new field.
	 * @param title The title of the new window. (Usually just the file name or Untitled)
	 */
	public static void generateNewField(int w,int h,String title){
		Scaling.setPicCoords(w,h);
		sensorNetwork = new ViewPort(title);
	}
	
	/**
	 * Marks the current work as modified (enables the save menu)
	 */
	public static void markAsModified() {
		if(isSensorNetworkAvailable()) {
			saveMenuItem.setEnabled(true);
		}
	}
	
	/**
	 * Convience method for repainting and updating the information frame.
	 */
	public static void updateViewSettings() {
		if(isSensorNetworkAvailable()) {
			informationFrame.update(selectedSensor);
			sensorNetwork.getGraphicsPainter().repaint();
		}
	}
	
	/**
	 * Convience method for checking if the sensor network / VPGraphicsPanter is available.
	 * @return true if the ViewPort / VPGraphicsPainter is available.
	 */
	public static boolean isSensorNetworkAvailable() {
		return sensorNetwork != null;
	}
	
	/**
	 * Convience method for saving.
	 * @see GUIReferences#saveAs()
	 */
	public static void save(){
		if(GUIReferences.currentFile != null) {
			xml.XMLSaver.saveSensorList(Sensor.idToSensor.values(), GUIReferences.currentFile);
			GUIReferences.saveMenuItem.setEnabled(false);
		} else {
			saveAs();
		}
	}
	
	/**
	 * Convience method for saving as.
	 */
	public static void saveAs(){
		File saveFile;
		JFileChooser saveChooser = new JFileChooser();
		FileNameExtensionFilter saveFilter = new FileNameExtensionFilter("Sensormap Files (.stuff)", "stuff");
		saveChooser.setFileFilter(saveFilter);
		int saveReturnVal = saveChooser.showSaveDialog(ControlPanelFrame.getFrame());
		if(saveReturnVal == JFileChooser.APPROVE_OPTION) {
			saveFile = saveChooser.getSelectedFile();
			if(saveFile.getPath().contains(".")==false){
				saveFile = new File(saveFile.getPath()+".stuff");
			}
			xml.XMLSaver.saveSensorList(Sensor.idToSensor.values(), saveFile);
			GUIReferences.sensorNetwork.halfTitle = saveFile.getName();
			GUIReferences.sensorNetwork.setTitle(saveFile.getName());
			GUIReferences.currentFile = saveFile;
			GUIReferences.saveMenuItem.setEnabled(false);
		}
	}
	
	/**
	 * The currently selected sensor (or null if none is selected)
	 */
	static Sensor selectedSensor;
	/**
	 * The window listener.
	 */
	static WindowListener windowListener;
	/**
	 * The ActionListener
	 */
	static CPActionListener listener;
	/**
	 * Bit mask of the selected views.
	 */
	public static int view = 0;
	/**
	 * The background color.
	 */
	public static Color canvasColor = Color.WHITE;
	/**
	 * Default Sensor color
	 */
	public static Color sensorColor = Color.BLACK;
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
	/**
	 * Color of the transmission radius circle.
	 */
	public static Color transmissionRadiusColor = Color.MAGENTA;
	/**
	 * Color of a connection between two sensors.
	 */
	public static Color connectionColor = Color.RED;
	/**
	 * Color of a "secondary" selected sensor (one the selected sensor can reach)
	 */
	public static Color secondarySelectedColor = Color.CYAN;
	
	/**
	 * Color of terminal sensors.
	 */
	public static Color terminalColor = new Color(0x00007700);
	/**
	 * Color of the sensor, which currently have the turn.
	 */
	public static Color currentTurnColor = new Color(0x00ff00ff);
	/**
	 * Color of isolated sensors.
	 */
	public static Color isolatedColor    = Color.PINK;
	
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