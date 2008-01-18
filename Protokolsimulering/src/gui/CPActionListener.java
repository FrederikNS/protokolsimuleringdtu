package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import nodes.GlobalAddressBook;
import nodes.Sensor;
import notification.Note;
import shape.Shape;

/**
 * @author Frederik Nordahl Sabroe
 *
 */
public class CPActionListener implements ActionListener,GUIConstants{
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
				
				ViewPort.disposeViewPort();
				xml.DOMxmlParser.parse(openFile);
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
				if(saveChooser.getSelectedFile().getPath().contains(".")==false){
					File tempFilePath = new File(saveChooser.getSelectedFile().getPath()+".stuff");
					saveChooser.setSelectedFile(tempFilePath);
				}
				saveFile = saveChooser.getSelectedFile();
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
			//TODO
			if(GUIReferences.currentFile != null) {
				GUIReferences.sensorNetwork.setTitle("*"+GUIReferences.currentFile.getName());
			} else {
				GUIReferences.sensorNetwork.setTitle("*Untitled");
			}
			GUIReferences.turnController.goToTurn(0);
			GUIReferences.stepperGroup.clearSelection();
			GUIReferences.saveMenuItem.setEnabled(true);
			break;
		case BUTTON_REWIND:
			//TODO
			GUIReferences.saveMenuItem.setEnabled(true);
			if(GUIReferences.currentFile != null) {
				GUIReferences.sensorNetwork.setTitle("*"+GUIReferences.currentFile.getName());
			} else {
				GUIReferences.sensorNetwork.setTitle("*Untitled");
			}
			for(int i=1;i<1000;i++){
			GUIReferences.turnController.playTurnBackwards();
			}
			break;
		case BUTTON_STEP_BACKWARD:
			GUIReferences.saveMenuItem.setEnabled(true);
			if(GUIReferences.currentFile != null) {
				GUIReferences.sensorNetwork.setTitle("*"+GUIReferences.currentFile.getName());
			} else {
				GUIReferences.sensorNetwork.setTitle("*Untitled");
			}
			GUIReferences.stepperGroup.clearSelection();
			GUIReferences.turnController.playTurnBackwards();
			break;
		case BUTTON_PLAY_BACKWARDS:
			//TODO
			GUIReferences.saveMenuItem.setEnabled(true);
			if(GUIReferences.currentFile != null) {
				GUIReferences.sensorNetwork.setTitle("*"+GUIReferences.currentFile.getName());
			} else {
				GUIReferences.sensorNetwork.setTitle("*Untitled");
			}
			for(int i=0;i<1000;i++){
				GUIReferences.turnController.playTickBackwards();
			}
			break;
		case BUTTON_STOP:
			//TODO
			//stop();
			GUIReferences.stepperGroup.clearSelection();
			break;
		case BUTTON_PLAY:
			//TODO
			GUIReferences.saveMenuItem.setEnabled(true);
			if(GUIReferences.currentFile != null) {
				GUIReferences.sensorNetwork.setTitle("*"+GUIReferences.currentFile.getName());
			} else {
				GUIReferences.sensorNetwork.setTitle("*Untitled");
			}
			for(int i=0;i<1000;i++){
				GUIReferences.turnController.playTick();
			}
			GUIReferences.turnController.playTick();
			break;
		case BUTTON_NEXT_SENSOR:
			GUIReferences.saveMenuItem.setEnabled(true);
			if(GUIReferences.currentFile != null) {
				GUIReferences.sensorNetwork.setTitle("*"+GUIReferences.currentFile.getName());
			} else {
				GUIReferences.sensorNetwork.setTitle("*Untitled");
			}
			GUIReferences.turnController.playTick();
			GUIReferences.stepperGroup.clearSelection();
			break;
		case BUTTON_STEP_FORWARD:
			GUIReferences.saveMenuItem.setEnabled(true);
			if(GUIReferences.currentFile != null) {
				GUIReferences.sensorNetwork.setTitle("*"+GUIReferences.currentFile.getName());
			} else {
				GUIReferences.sensorNetwork.setTitle("*Untitled");
			}
			GUIReferences.turnController.playTurn();
			GUIReferences.stepperGroup.clearSelection();
			break;
		case BUTTON_FAST_FORWARD:
			GUIReferences.saveMenuItem.setEnabled(true);
			if(GUIReferences.currentFile != null) {
				GUIReferences.sensorNetwork.setTitle("*"+GUIReferences.currentFile.getName());
			} else {
				GUIReferences.sensorNetwork.setTitle("*Untitled");
			}
			//TODO
			int i=0;
			while(i<1000){
			GUIReferences.turnController.playTurn();
			i++;
			}
			//fastForward();
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
			if(GUIReferences.sensorNetwork != null) {
				GUIReferences.sensorNetwork.repaint();
			}
			break;
		case CHECKBOX_CONNECTIONS:
			if(0 != (GUIReferences.view & VIEW_CONNECTIONS)) {
				GUIReferences.view &= ~VIEW_CONNECTIONS;
			} else {
				GUIReferences.view |= VIEW_CONNECTIONS;
			}
			if(GUIReferences.sensorNetwork != null) {
				GUIReferences.sensorNetwork.repaint();
			}
			break;
		case CHECKBOX_NEIGHBOURS:
			if(0 != (GUIReferences.view & VIEW_NEIGHBOURS)) {
				GUIReferences.view &= ~VIEW_NEIGHBOURS;
			} else {
				GUIReferences.view |= VIEW_NEIGHBOURS;
			}
			if(GUIReferences.sensorNetwork != null) {
				GUIReferences.sensorNetwork.repaint();
			}
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
				GUIReferences.view &= ~VIEW_CONSOLE;
				GUIReferences.console.setVisible(false);
			} else {
				GUIReferences.view |= VIEW_CONSOLE;
				GUIReferences.console.setVisible(true);
			}
			break;
		case CHECKBOX_ROUTES:
			if(0 != (GUIReferences.view & VIEW_ROUTES)) {
				GUIReferences.view &= ~VIEW_ROUTES;
				GUIReferences.sensorNetwork.getGraphicsPainter().setToDraw(new ArrayList<Shape>(), Color.BLACK);
			} else {
				GUIReferences.view |= VIEW_ROUTES;
			}
			if(GUIReferences.selectedSensor != null) {
				GUIReferences.sensorNetwork.getGraphicsPainter()
					.setToDraw(GUIReferences.selectedSensor.getRouteToTerminal()
							, GUIReferences.connectionColor);
			}
			if(GUIReferences.sensorNetwork != null) {
				
				GUIReferences.sensorNetwork.repaint();
			}
			break;
		case CHECKBOX_ALL_ROUTES:
			ArrayList<Shape> newShapes = new ArrayList<Shape>();
			if(0 != (GUIReferences.view & VIEW_ALL_ROUTES)) {
				GUIReferences.view &= ~VIEW_ALL_ROUTES;
				if(0 != (GUIReferences.view & VIEW_ROUTES)) {
					if(GUIReferences.selectedSensor != null) {
						newShapes = GUIReferences.selectedSensor.getRouteToTerminal();
					}
				}
			} else {
				GUIReferences.view |= VIEW_ALL_ROUTES;
				for(Sensor sen : Sensor.idToSensor.values()) {
					newShapes.addAll(sen.getRouteToTerminal());
				}
			}
			if(GUIReferences.sensorNetwork != null) {
				GUIReferences.sensorNetwork.getGraphicsPainter().setToDraw(newShapes, GUIReferences.connectionColor);
				GUIReferences.sensorNetwork.repaint();
			}
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