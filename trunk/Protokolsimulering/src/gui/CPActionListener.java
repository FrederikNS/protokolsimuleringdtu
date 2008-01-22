package gui;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Random;

import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;

import nodes.GlobalAddressBook;
import nodes.Sensor;
import notification.Note;
import notification.NoteConstants;
import turns.TurnController;



/**
 * @author Frederik Nordahl Sabroe
 *
 */
public class CPActionListener implements ActionListener,GUIConstants{

	/**
	 * The timer for delaying UI drawing
	 */
	private Timer timer;
	/**
	 * A random number
	 */
	private Random ran = new Random();
	/**
	 * The speed things should go
	 */
	private int playSpeed;
	/**
	 * For remembering where the configframe was
	 */
	Point where;

	/**
	 * Default constructor for the CPActionListener.
	 */
	public CPActionListener() {
		timer = new Timer(0, this);
		timer.setActionCommand(String.valueOf(TIMER_EVENT));
		timer.setDelay(500);
		playSpeed = PLAYBACK_PAUSE;
	}

	/**
	 * Method for stopping the timer (which will stop the simulation if automated)
	 * This will NOT clear the button selection.
	 */
	public void stopTimer() {
		timer.stop();
	}

	public void actionPerformed(ActionEvent arg0) {
		switch(Integer.parseInt(arg0.getActionCommand())) {
		case MENU_QUIT:
			ControlPanelFrame.getFrame().quit();
			break;
		case MENU_NEW:
			CPNew.openCPNew();
			break;
		case MENU_OPEN:
			//TODO - check if session is already running?
			if(GUIReferences.saveMenuItem.isEnabled()) {

			}
			File openFile;
			JFileChooser openChooser = new JFileChooser();
			FileNameExtensionFilter openFilter = new FileNameExtensionFilter("Sensormap Files (.stuff)", "stuff");
			openChooser.setFileFilter(openFilter);
			int openReturnVal = openChooser.showOpenDialog(ControlPanelFrame.getFrame());
			if(openReturnVal == JFileChooser.APPROVE_OPTION) {
				openFile = openChooser.getSelectedFile();
				if(!openFile.exists() && openFile.getPath().contains(".")==false){
					openFile = new File(openFile.getPath()+".stuff");
				}
				ViewPort.disposeViewPort();
				xml.DOMxmlParser.parse(openFile, ControlPanelFrame.getFrame());
				GUIReferences.currentFile = openFile;
				CPNew.disposeWindow();
				GUIReferences.updateStatusBar();
			}
			break;
		case MENU_SAVE:
			GUIReferences.save();
			break;
		case MENU_SAVE_AS:
			GUIReferences.saveAs();
			break;
		case MENU_PREFERENCES:
			ConfigFrame.openConfigFrame().setVisible(true);
			break;
		case BUTTON_GENERATE:
			int number;
			String generateDialog = JOptionPane.showInputDialog(GUIReferences.constructPanel, "Please enter a number of sensors to generate", "Generate...", JOptionPane.QUESTION_MESSAGE);
			if(generateDialog != null){
				try{
					number = Integer.parseInt(generateDialog);
					if(number>0){
						for(int i=0;i<number;i++) {
							Sensor.newInstance();
						}
						GlobalAddressBook.getBook().generateDirectConnections();
						GUIReferences.saveMenuItem.setEnabled(true);
						GUIReferences.viewPort.repaint();
						GUIReferences.updateStatusBar();
					}
				} catch (NumberFormatException e) {
					Note.sendNote(Note.ERROR, "Not a number.");
				}
			}
			break;
		case BUTTON_ENABLE:
			if(GUIReferences.mode==MODE_ENABLE){
				GUIReferences.modeGroup.clearSelection();
				GUIReferences.mode = MODE_SELECT;
			} else {
				GUIReferences.mode = MODE_ENABLE;
			}
			break;		
		case BUTTON_DISABLE:
			if(GUIReferences.mode==MODE_DISABLE){
				GUIReferences.modeGroup.clearSelection();
				GUIReferences.mode = MODE_SELECT;
			} else {
				GUIReferences.mode = MODE_DISABLE;
			}
			break;
		case BUTTON_REMOVE:
			if(GUIReferences.mode==MODE_REMOVE){
				GUIReferences.modeGroup.clearSelection();
				GUIReferences.mode = MODE_SELECT;
			}else{
				GUIReferences.mode = MODE_REMOVE;
			}
			break;
		case BUTTON_ADD:
			if(GUIReferences.mode==MODE_ADD){
				GUIReferences.modeGroup.clearSelection();
				GUIReferences.mode = MODE_SELECT;
			}else{
				GUIReferences.mode = MODE_ADD;
			}
			break;
		case BUTTON_CLEAR:
			if(Sensor.idToSensor.size() > 0) {
				int returnValue = JOptionPane.showConfirmDialog(GUIReferences.constructPanel, "Do you really wish to clear?", "", JOptionPane.OK_CANCEL_OPTION);
				if(returnValue == JOptionPane.OK_OPTION) {
					if(GUIReferences.selectedSensor!=null){
						GUIReferences.selectedSensor.setSelected(false);
						GUIReferences.selectedSensor = null;
					}
					Sensor.disposeAllSensors();
					GUIReferences.reenableConstruct();
					GUIReferences.saveMenuItem.setEnabled(true);
					if(GUIReferences.currentFile != null) {
						GUIReferences.viewPort.setTitle("*"+GUIReferences.currentFile.getName());
					} else {
						GUIReferences.viewPort.setTitle("*Untitled");
					}
					if(GUIReferences.viewPort != null) {
						GUIReferences.viewPort.repaint();
					}
					TurnController.clearAll();
					GUIReferences.updateStatusBar();
				}
			}
			break;
		case BUTTON_STOP:
			GUIReferences.clearButton.setEnabled(true);
			timer.stop();
			GUIReferences.stepperGroup.clearSelection();
			GUIReferences.updateStatusBar();
			break;
		case BUTTON_PLAY:
			GUIReferences.disableConstruct();
			if(!GUIReferences.isSensorNetworkAvailable()) {
				break;
			}
			GUIReferences.markAsModified();
			if(timer.isRunning()) {
				timer.stop();
			}
			playSpeed = PLAYBACK_PLAY;
			timer.setDelay(200);
			timer.start();
			break;
		case BUTTON_NEXT_SENSOR:
			GUIReferences.disableConstruct();
			if(!GUIReferences.isSensorNetworkAvailable()) {
				break;
			}
			GUIReferences.stepperGroup.clearSelection();
			GUIReferences.markAsModified();
			TurnController.getInstance().playTick();
			GUIReferences.viewPort.repaint();
			timer.stop();
			GUIReferences.updateStatusBar();
			break;
		case BUTTON_FAST_FORWARD:
			GUIReferences.disableConstruct();
			if(!GUIReferences.isSensorNetworkAvailable()) {
				break;
			}
			GUIReferences.markAsModified();
			if(timer.isRunning()) {
				timer.stop();
			}
			playSpeed = PLAYBACK_FAST_FORWARD;
			timer.setDelay(10);
			timer.start();
			break;
		case BUTTON_COLOR_CANVAS:
			GUIReferences.canvasColor = JColorChooser.showDialog(GUIReferences.controlPanelPane, "Color Selector", GUIReferences.canvasColor);
			GUIReferences.viewPort.getGraphicsPainter().setBackground(GUIReferences.canvasColor);
			GUIReferences.viewPort.getGraphicsPainter().repaint();
			where = GUIReferences.configFrame.getLocation();
			GUIReferences.configFrame.dispose();
			ConfigFrame.openConfigFrame();
			GUIReferences.configFrame.setLocation(where);
			GUIReferences.configFrame.setVisible(true);
			break;
		case BUTTON_COLOR_SENSOR:
			GUIReferences.sensorColor = JColorChooser.showDialog(GUIReferences.controlPanelPane, "Color Selector", GUIReferences.sensorColor);
			GUIReferences.viewPort.getGraphicsPainter().repaint();
			where = GUIReferences.configFrame.getLocation();
			GUIReferences.configFrame.dispose();
			ConfigFrame.openConfigFrame();
			GUIReferences.configFrame.setLocation(where);
			GUIReferences.configFrame.setVisible(true);
			break;
		case BUTTON_COLOR_SELECTED:
			GUIReferences.selectedColor = JColorChooser.showDialog(GUIReferences.controlPanelPane, "Color Selector", GUIReferences.selectedColor);
			GUIReferences.viewPort.getGraphicsPainter().repaint();
			where = GUIReferences.configFrame.getLocation();
			GUIReferences.configFrame.dispose();
			ConfigFrame.openConfigFrame();
			GUIReferences.configFrame.setLocation(where);
			GUIReferences.configFrame.setVisible(true);
			break;
		case BUTTON_COLOR_DEAD:
			GUIReferences.deadColor = JColorChooser.showDialog(GUIReferences.controlPanelPane, "Color Selector", GUIReferences.deadColor);
			GUIReferences.viewPort.getGraphicsPainter().repaint();
			where = GUIReferences.configFrame.getLocation();
			GUIReferences.configFrame.dispose();
			ConfigFrame.openConfigFrame();
			GUIReferences.configFrame.setLocation(where);
			GUIReferences.configFrame.setVisible(true);
			break;
		case BUTTON_COLOR_SENDING:
			GUIReferences.sendingColor = JColorChooser.showDialog(GUIReferences.controlPanelPane, "Color Selector", GUIReferences.sendingColor);
			GUIReferences.viewPort.getGraphicsPainter().repaint();
			where = GUIReferences.configFrame.getLocation();
			GUIReferences.configFrame.dispose();
			ConfigFrame.openConfigFrame();
			GUIReferences.configFrame.setLocation(where);
			GUIReferences.configFrame.setVisible(true);
			break;
		case BUTTON_COLOR_RECEIVING:
			GUIReferences.receivingColor = JColorChooser.showDialog(GUIReferences.controlPanelPane, "Color Selector", GUIReferences.receivingColor);
			GUIReferences.viewPort.getGraphicsPainter().repaint();
			where = GUIReferences.configFrame.getLocation();
			GUIReferences.configFrame.dispose();
			ConfigFrame.openConfigFrame();
			GUIReferences.configFrame.setLocation(where);
			GUIReferences.configFrame.setVisible(true);
			break;
		case BUTTON_COLOR_TRANSMISSION_RADIUS:
			GUIReferences.transmissionRadiusColor = JColorChooser.showDialog(GUIReferences.controlPanelPane, "Color Selector", GUIReferences.transmissionRadiusColor);
			GUIReferences.viewPort.getGraphicsPainter().repaint();
			where = GUIReferences.configFrame.getLocation();
			GUIReferences.configFrame.dispose();
			ConfigFrame.openConfigFrame();
			GUIReferences.configFrame.setLocation(where);
			GUIReferences.configFrame.setVisible(true);
			break;
		case BUTTON_COLOR_CONNECTION:
			GUIReferences.connectionColor = JColorChooser.showDialog(GUIReferences.controlPanelPane, "Color Selector", GUIReferences.connectionColor);
			GUIReferences.viewPort.getGraphicsPainter().repaint();
			where = GUIReferences.configFrame.getLocation();
			GUIReferences.configFrame.dispose();
			ConfigFrame.openConfigFrame();
			GUIReferences.configFrame.setLocation(where);
			GUIReferences.configFrame.setVisible(true);
			break;
		case BUTTON_COLOR_SECONDARY_SELECTED:
			GUIReferences.secondarySelectedColor = JColorChooser.showDialog(GUIReferences.controlPanelPane, "Color Selector", GUIReferences.secondarySelectedColor);
			GUIReferences.viewPort.getGraphicsPainter().repaint();
			where = GUIReferences.configFrame.getLocation();
			GUIReferences.configFrame.dispose();
			ConfigFrame.openConfigFrame();
			GUIReferences.configFrame.setLocation(where);
			GUIReferences.configFrame.setVisible(true);
			break;
		case BUTTON_COLOR_TERMINAL:
			GUIReferences.terminalColor = JColorChooser.showDialog(GUIReferences.controlPanelPane, "Color Selector", GUIReferences.terminalColor);
			GUIReferences.viewPort.getGraphicsPainter().repaint();
			where = GUIReferences.configFrame.getLocation();
			GUIReferences.configFrame.dispose();
			ConfigFrame.openConfigFrame();
			GUIReferences.configFrame.setLocation(where);
			GUIReferences.configFrame.setVisible(true);
			break;
		case BUTTON_COLOR_CURRENT_TURN:
			GUIReferences.currentTurnColor = JColorChooser.showDialog(GUIReferences.controlPanelPane, "Color Selector", GUIReferences.currentTurnColor);
			GUIReferences.viewPort.getGraphicsPainter().repaint();
			where = GUIReferences.configFrame.getLocation();
			GUIReferences.configFrame.dispose();
			ConfigFrame.openConfigFrame();
			GUIReferences.configFrame.setLocation(where);
			GUIReferences.configFrame.setVisible(true);
			break;
		case BUTTON_COLOR_ISOLATED:
			GUIReferences.isolatedColor = JColorChooser.showDialog(GUIReferences.controlPanelPane, "Color Selector", GUIReferences.isolatedColor);
			GUIReferences.viewPort.getGraphicsPainter().repaint();
			where = GUIReferences.configFrame.getLocation();
			GUIReferences.configFrame.dispose();
			ConfigFrame.openConfigFrame();
			GUIReferences.configFrame.setLocation(where);
			GUIReferences.configFrame.setVisible(true);
			break;
		case BUTTON_GENERATE_ADDRESS_BOOK:
			GlobalAddressBook.clearBook();
			GlobalAddressBook.getBook().generateDirectConnections();
			GUIReferences.viewPort.repaint();
			break;
		case TIMER_EVENT:
			switch(playSpeed) {
			default:
			case PLAYBACK_PAUSE:
				timer.stop();
				break;
			case PLAYBACK_FAST_FORWARD:
				TurnController.getInstance().playTick();
				if(ran.nextInt(6) == 0) {
					GUIReferences.informationFrame.update(GUIReferences.selectedSensor);
					GUIReferences.viewPort.repaint();
					GUIReferences.updateStatusBar();
				}
				break;
			case PLAYBACK_PLAY:
				TurnController.getInstance().playTick();
				GUIReferences.informationFrame.update(GUIReferences.selectedSensor);
				GUIReferences.viewPort.repaint();
				GUIReferences.updateStatusBar();
				break;
			}
			break;
		case BUTTON_NEW_OK:
			int width = 0;
			int height = 0;
			
			width = Integer.parseInt(CPNew.widthSpinner.getValue().toString());
			height = Integer.parseInt(CPNew.heightSpinner.getValue().toString());
			
			GUIReferences.reenableConstruct();
			if(Sensor.idToSensor.size() > 0) {
				if(GUIReferences.selectedSensor!=null){
					GUIReferences.selectedSensor.setSelected(false);
					GUIReferences.selectedSensor = null;
				}
				Sensor.disposeAllSensors();
				
				if(GUIReferences.viewPort != null) {
					GUIReferences.viewPort.repaint();
				}
				TurnController.clearAll();
				GUIReferences.updateStatusBar();
			}
			CPNew.disposeWindow();

			ViewPort.disposeViewPort();
			GUIReferences.saveMenuItem.setEnabled(false);

			GUIReferences.generateNewField(width, height, "Untitled");
			GUIReferences.updateStatusBar();
			break;
		case BUTTON_NEW_CANCEL:
			CPNew.disposeWindow();
			break;
		case BUTTON_PROMOTE:
			if(GUIReferences.mode==MODE_PROMOTE){
				GUIReferences.modeGroup.clearSelection();
				GUIReferences.mode = MODE_SELECT;
			}else{
				GUIReferences.mode = MODE_PROMOTE;
			}
			break;
		case BUTTON_DEMOTE:
			if(GUIReferences.mode==MODE_DEMOTE){
				GUIReferences.modeGroup.clearSelection();
				GUIReferences.mode = MODE_SELECT;
			}else{
				GUIReferences.mode = MODE_DEMOTE;
			}
			break;
		case CHECKBOX_RADII:
			if(0 != (GUIReferences.view & VIEW_RADII)) {
				GUIReferences.view &= ~VIEW_RADII;
			} else {
				GUIReferences.view |= VIEW_RADII;
			}
			GUIReferences.updateViewSettings();
			break;
		case CHECKBOX_CONNECTIONS:
			if(0 != (GUIReferences.view & VIEW_CONNECTIONS)) {
				GUIReferences.view &= ~VIEW_CONNECTIONS;
			} else {
				GUIReferences.view |= VIEW_CONNECTIONS;
			}
			GUIReferences.updateViewSettings();
			break;
		case CHECKBOX_NEIGHBOURS:
			if(0 != (GUIReferences.view & VIEW_NEIGHBOURS)) {
				GUIReferences.view &= ~VIEW_NEIGHBOURS;
			} else {
				GUIReferences.view |= VIEW_NEIGHBOURS;
			}
			GUIReferences.updateViewSettings();
			break;
		case CHECKBOX_ENABLE_CONSOLE:
			if(0 != (GUIReferences.view & VIEW_CONSOLE)) {
				GUIReferences.view &= ~VIEW_CONSOLE;
				GUIReferences.console.setVisible(false);
			} else {
				GUIReferences.view |= VIEW_CONSOLE;
				GUIReferences.console.setVisible(true);
			}
			break;
		case CHECKBOX_ENABLE_INFOBOX:
			if(0 != (GUIReferences.view & VIEW_INFOBOX)) {
				GUIReferences.view &= ~VIEW_INFOBOX;
				GUIReferences.informationFrame.setVisible(false);
			} else {
				GUIReferences.view |= VIEW_INFOBOX;
				GUIReferences.informationFrame.setVisible(true);
			}
			break;
		case CHECKBOX_ROUTES:
			if(0 != (GUIReferences.view & VIEW_ROUTES)) {
				GUIReferences.view &= ~VIEW_ROUTES;
			} else {
				GUIReferences.view |= VIEW_ROUTES;
			}
			GUIReferences.updateViewSettings();
			break;
		case CHECKBOX_ISOLATED:
			if(0 != (GUIReferences.view & VIEW_ISOLATED)) {
				GUIReferences.view &= ~VIEW_ISOLATED;

			} else {
				GUIReferences.view |= VIEW_ISOLATED;
			}
			GUIReferences.updateViewSettings();
			break;

		case CHECKBOX_CONSOLE_INFORMATION:
			GUIReferences.console.toggleShowFlag(NoteConstants.INFORMATION);
			break;
		case CHECKBOX_CONSOLE_WARNING:
			GUIReferences.console.toggleShowFlag(NoteConstants.WARNING);
			break;
		case CHECKBOX_CONSOLE_ERROR:
			GUIReferences.console.toggleShowFlag(NoteConstants.ERROR);
			break;
		case CHECKBOX_CONSOLE_DEBUG:
			GUIReferences.console.toggleShowFlag(NoteConstants.DEBUG);
			break;
		}
	}
}