package gui;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.filechooser.FileNameExtensionFilter;

import math.Scaling;
import nodes.Sensor;
import turns.TurnController;
import xml.XMLSaver;

/**
 * @author Frederik Nordahl Sabroe
 *
 */
public class GUIReferences implements GUIConstants{ 
	/**
	 * Reference to the panel containing the contruction elements
	 */
	static JPanel constructPanel;
	/**
	 * Reference to the panel containing the simulation elements
	 */
	static JPanel simulatePanel;
	/**
	 * Reference to the control panel
	 */
	static JPanel controlPanelPane;
	/**
	 * Reference to the status bar
	 */
	static JLabel status;
	/**
	 * Reference to the spinner for changing radius
	 */
	static JSpinner radiusSpinner;
	/**
	 * References to the button to go to the next sensor (simulation)
	 */
	static JButton nextSensor;
	/**
	 * Reference to the Generate button
	 */
	static JButton generateButton = new JButton("Generate");
	/**
	 * Reference to the Enable button
	 */
	static JToggleButton enableButton;
	/**
	 * Reference to the Disable button
	 */
	static JToggleButton disableButton;
	/**
	 * Reference to the Add button
	 */
	static JToggleButton addButton;
	/**
	 * Reference to the Clear button
	 */
	static JButton clearButton;
	/**
	 * Reference to the Generate Address Book button
	 */
	static JButton generateAddressBook;
	/**
	 * Reference to the Promote button
	 */
	static JToggleButton promoteButton;
	/**
	 * Reference to the Demote button
	 */
	static JToggleButton demoteButton;
	/**
	 * Reference to the view of the sensor network currently opened
	 */
	public static ViewPort viewPort;
	/**
	 * Reference to the configFrame
	 */
	public static ConfigFrame configFrame;
	/**
	 * Reference to the information frame for viewing statistics for the selected sensor
	 */
	public static InformationFrame informationFrame;
	/**
	 * Reference to the console
	 */
	static Console console;
	/**
	 * Reference to the current playback mode
	 */
	static int playback = PLAYBACK_PAUSE;
	/**
	 * Reference to the current mode
	 */
	static int mode = MODE_SELECT;
	/**
	 * Reference to the group of buttons for construction
	 */
	public static ButtonGroup modeGroup;
	/**
	 * Reference to the group of buttons for simulation
	 */
	static ButtonGroup stepperGroup;
	/**
	 * Reference to the save option in the menu
	 */
	static JMenuItem saveMenuItem;
	/**
	 * Reference to the Console Checkbox
	 */
	static JCheckBox consoleEnable;
	/**
	 * Reference to the Infobox Checkbox
	 */
	static JCheckBox infoBoxEnable;
	/**
	 * Reference to the variable which keeps track of if the mouse is inside the view port
	 */
	static boolean mouseWithinViewPort;
	/**
	 * The currently used file.
	 */
	static File currentFile;
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
	 * Convenience method for opening a new ViewPort with a internal width / height
	 * @param w The (internal) width of the new field.
	 * @param h The (internal) height of the new field.
	 * @param title The title of the new window. (Usually just the file name or Untitled)
	 */
	public static void generateNewField(int w,int h,String title){
		Scaling.setPicCoords(w,h);
		viewPort = new ViewPort(title);
	}

	/**
	 * Marks the current work as modified (enables the save menu)
	 */
	public static void markAsModified() {
		if(isSensorNetworkAvailable()) {
			saveMenuItem.setEnabled(true);
			if(GUIReferences.currentFile != null) {
				GUIReferences.viewPort.setTitle("*"+GUIReferences.currentFile.getName());
			} else {
				GUIReferences.viewPort.setTitle("*Untitled");
			}
		}
	}

	/**
	 * Clears the view-port, removes all sensors and re-enables modification.
	 */
	public static void clearViewPort() {
		Sensor.disposeAllSensors();
		GUIReferences.setConstructEnabled(true);
		TurnController.clearAll();
		GUIReferences.updateStatusBar();
	}
	/**
	 * Updates the statusbar of the Control Panel
	 */
	public static void updateStatusBar(){
		status.setText("Sensors: "+Sensor.idToSensor.size()+(TurnController.getInstance().getCurrentTurn()!=null?", Turn: "+TurnController.getInstance().getCurrentTurn().turn:""));
	}

	/**
	 * Convience method for repainting and updating the information frame.
	 */
	public static void updateViewSettings() {
		if(isSensorNetworkAvailable()) {
			informationFrame.update(selectedSensor);
			viewPort.getGraphicsPainter().repaint();
		}
	}

	/**
	 * Convience method for checking if the sensor network / VPGraphicsPanter is available.
	 * @return true if the ViewPort / VPGraphicsPainter is available.
	 */
	public static boolean isSensorNetworkAvailable() {
		return viewPort != null;
	}

	/**
	 * Convience method for saving.
	 * @see GUIReferences#saveAs()
	 */
	public static void save(){
		if(GUIReferences.currentFile != null) {
			xml.XMLSaver.saveSensorList(GUIReferences.currentFile);
			GUIReferences.saveMenuItem.setEnabled(false);
		} else {
			saveAs();
		}
	}

	/**
	 * Convenience method for saving as.
	 */
	public static void saveAs(){
		File saveFile;
		JFileChooser saveChooser = new JFileChooser();
		FileNameExtensionFilter saveFilter = new FileNameExtensionFilter("Sensormap Files (.stuff)", "stuff");
		FileNameExtensionFilter saveFilter2 = new FileNameExtensionFilter("Gzipped Sensormap Files (.stuff.gz)", "stuff.gz");
		saveChooser.setFileFilter(saveFilter);
		saveChooser.setFileFilter(saveFilter2);
		int saveReturnVal = saveChooser.showSaveDialog(ControlPanelFrame.getFrame());
		if(saveReturnVal == JFileChooser.APPROVE_OPTION) {
			if(saveChooser.getFileFilter() == saveFilter && saveChooser.getSelectedFile().getName().endsWith(".stuff")==false){
				saveFile = new File(saveChooser.getSelectedFile().getAbsolutePath()+".stuff");
			} else if(saveChooser.getFileFilter() == saveFilter2 && saveChooser.getSelectedFile().getName().endsWith(".stuff.gz")==false){
				saveFile = new File(saveChooser.getSelectedFile().getAbsolutePath()+".stuff.gz");
			} else {
				saveFile = saveChooser.getSelectedFile();
			}
			XMLSaver.saveSensorList(saveFile);
			GUIReferences.viewPort.halfTitle = saveFile.getName();
			GUIReferences.viewPort.setTitle(saveFile.getName());
			GUIReferences.currentFile = saveFile;
			GUIReferences.saveMenuItem.setEnabled(false);
		}
	}

	/**
	 * Enables or disables all buttons on the construction tab
	 * @param isEnabled true if it should be enabled, false if it should be disabled.
	 */
	public static void setConstructEnabled(boolean isEnabled){
		addButton.setEnabled(isEnabled);
		generateButton.setEnabled(isEnabled);
		clearButton.setEnabled(isEnabled);
		enableButton.setEnabled(isEnabled);
		disableButton.setEnabled(isEnabled);
		promoteButton.setEnabled(isEnabled);
		demoteButton.setEnabled(isEnabled);
		radiusSpinner.setEnabled(isEnabled);
		generateAddressBook.setEnabled(isEnabled);
	}

	/**
	 * Enables or disables all buttons on the simulate tab
	 * @param isEnabled true if it should be enabled, false if it should be disabled.
	 */
	public static void setSimulateEnabled(boolean isEnabled){
		Enumeration<AbstractButton> buttons = GUIReferences.stepperGroup.getElements();
		while(buttons.hasMoreElements()) {
			buttons.nextElement().setEnabled(isEnabled);
		}
		GUIReferences.nextSensor.setEnabled(isEnabled);
	}

	/**
	 * Convience method for reopening the Config-frame.
	 * @param where The location on the screen where it should be placed.
	 */
	public static void reopenConfigFrame(Point where){			
		configFrame.dispose();
		ConfigFrame.openConfigFrame();
		configFrame.setLocation(where);
		configFrame.setVisible(true);
	}

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