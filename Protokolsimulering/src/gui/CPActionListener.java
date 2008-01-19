package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;

import nodes.GlobalAddressBook;
import nodes.Sensor;
import notification.Note;



/**
 * @author Frederik Nordahl Sabroe
 *
 */
public class CPActionListener implements ActionListener,GUIConstants{
	
	private Timer timer;
	private int playSpeed;
	public CPActionListener() {
		timer = new Timer(0, this);
		timer.setActionCommand(String.valueOf(TIMER_EVENT));
		timer.setDelay(500);
		playSpeed = PLAYBACK_PAUSE;
	}
	
	public void actionPerformed(ActionEvent arg0) {
		switch(Integer.parseInt(arg0.getActionCommand())) {
		case MENU_QUIT:
			//TODO - call another method.
			System.exit(0);
			break;
		case MENU_NEW:
			//TODO
			CPNew.openCPNew();
			break;
		case MENU_OPEN:
			//TODO - check if session is already running?
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
			}
			break;
		case MENU_SAVE:
			if(GUIReferences.currentFile != null) {
				xml.XMLSaver.saveSensorList(Sensor.idToSensor.values(), GUIReferences.currentFile);
				GUIReferences.saveMenuItem.setEnabled(false);
				GUIReferences.sensorNetwork.setTitle(GUIReferences.currentFile.getName());
				break;
			}
		case MENU_SAVE_AS:
			//TODO
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
				GUIReferences.sensorNetwork.setTitle(saveFile.getName());
				GUIReferences.currentFile = saveFile;
				GUIReferences.saveMenuItem.setEnabled(false);
			}
			break;
		case MENU_PREFERENCES:
			//TODO (in ConfigFrame)
			ConfigFrame.openConfigFrame().setVisible(true);
			break;
		case BUTTON_GENERATE:
			int number;
			String generateDialog = JOptionPane.showInputDialog(GUIReferences.constructPanel, "Please enter a number of sensors to generate", "Generate...", JOptionPane.QUESTION_MESSAGE);
			try{
				number = Integer.parseInt(generateDialog);
				if(number>0){
					for(int i=0;i<number;i++) {
						//GUIReferences.sensorNetwork.getField().addSensor(new Sensor());
						Sensor.newInstance();
					}
					GlobalAddressBook.getBook().generateDirectConnections();
					GUIReferences.saveMenuItem.setEnabled(true);
					if(GUIReferences.currentFile != null) {
						GUIReferences.sensorNetwork.setTitle("*"+GUIReferences.currentFile.getName());
					} else {
						GUIReferences.sensorNetwork.setTitle("*Untitled");
					}
					GUIReferences.sensorNetwork.repaint();
					
				}
			} catch (NumberFormatException e) {
				Note.sendNote(Note.ERROR, "Not a number.");
			}
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
				System.out.println(returnValue);
				if(returnValue == JOptionPane.OK_OPTION) {
					Sensor.disposeAllSensors();
					GUIReferences.saveMenuItem.setEnabled(true);
					if(GUIReferences.currentFile != null) {
						GUIReferences.sensorNetwork.setTitle("*"+GUIReferences.currentFile.getName());
					} else {
						GUIReferences.sensorNetwork.setTitle("*Untitled");
					}
					if(GUIReferences.sensorNetwork != null) {
						GUIReferences.sensorNetwork.repaint();
					}
				}
			}
			break;
		case BUTTON_TO_START:
			if(!GUIReferences.isSensorNetworkAvailable()) {
				break;
			}
			GUIReferences.markAsModified();
			GUIReferences.turnController.goToTurn(0);
			GUIReferences.stepperGroup.clearSelection();
			break;
		case BUTTON_REWIND:
			if(!GUIReferences.isSensorNetworkAvailable()) {
				break;
			}
			GUIReferences.markAsModified();
			timer.start();
			if(timer.isRunning()) {
				timer.stop();
			}
			playSpeed = PLAYBACK_REWIND;
			timer.setDelay(130);
			timer.start();
			break;
		case BUTTON_STEP_BACKWARD:
			if(!GUIReferences.isSensorNetworkAvailable()) {
				break;
			}
			GUIReferences.markAsModified();
			GUIReferences.stepperGroup.clearSelection();
			GUIReferences.turnController.playTurnBackwards();
			break;
		case BUTTON_PLAY_BACKWARDS:
			if(!GUIReferences.isSensorNetworkAvailable()) {
				break;
			}
			GUIReferences.markAsModified();
			if(timer.isRunning()) {
				timer.stop();
			}
			playSpeed = PLAYBACK_PLAY_BACKWARDS;
			timer.setDelay(500);
			timer.start();
			break;
		case BUTTON_STOP:
			timer.stop();
			GUIReferences.stepperGroup.clearSelection();
			break;
		case BUTTON_PLAY:
			if(!GUIReferences.isSensorNetworkAvailable()) {
				break;
			}
			GUIReferences.markAsModified();
			if(timer.isRunning()) {
				timer.stop();
			}
			playSpeed = PLAYBACK_PLAY;
			timer.setDelay(500);
			timer.start();
			break;
		case BUTTON_NEXT_SENSOR:
			if(!GUIReferences.isSensorNetworkAvailable()) {
				break;
			}
			GUIReferences.markAsModified();
			GUIReferences.turnController.playTick();
			GUIReferences.stepperGroup.clearSelection();
			break;
		case BUTTON_STEP_FORWARD:
			if(!GUIReferences.isSensorNetworkAvailable()) {
				break;
			}
			GUIReferences.markAsModified();
			GUIReferences.turnController.playTurn();
			GUIReferences.stepperGroup.clearSelection();
			break;
		case BUTTON_FAST_FORWARD:
			if(!GUIReferences.isSensorNetworkAvailable()) {
				break;
			}
			GUIReferences.markAsModified();
			if(timer.isRunning()) {
				timer.stop();
			}
			playSpeed = PLAYBACK_FAST_FORWARD;
			timer.setDelay(130);
			timer.start();			
			break;
		case TIMER_EVENT:
			switch(playSpeed) {
			default:
			case PLAYBACK_PAUSE:
				timer.stop();
				break;
			case PLAYBACK_FAST_FORWARD:
			case PLAYBACK_PLAY:
				GUIReferences.turnController.playTick();
				break;
			case PLAYBACK_PLAY_BACKWARDS:
			case PLAYBACK_REWIND:
				GUIReferences.turnController.playTurnBackwards();
				break;
			}
			break;
		case BUTTON_NEW_OK:
			//Clear()
			int width = 0;
			int height = 0;
			width = Integer.parseInt(CPNew.widthSpinner.getValue().toString());
			height = Integer.parseInt(CPNew.heightSpinner.getValue().toString());
			CPNew.disposeWindow();
			GUIReferences.generateNewField(width, height, "Untitled");
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
			if(0 != (GUIReferences.view & VIEW_CONSOLE)) {
				//GUIReferences.view &= ~VIEW_CONSOLE;
				//GUIReferences.console.setVisible(false);
			} else {
				//GUIReferences.view |= VIEW_CONSOLE;
				//GUIReferences.console.setVisible(true);
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
		case POPUP_BUTTON_VIEW_SENSOR:
			//TODO - make nicer
			

			/*JDialog log = new JDialog(GUIReferences.sensorNetwork, GUIReferences.selectedSensor.toString());
			JPanel pane = new JPanel();
			log.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			pane.setLayout(new BorderLayout());
			log.setContentPane(pane);
			pane.add(new JLabel(GUIReferences.selectedSensor.toString()));
			log.pack();
			log.setVisible(true);*/
			break;
		}
	}
}