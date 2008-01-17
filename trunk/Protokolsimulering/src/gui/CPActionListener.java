package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import math.Scaling;
import nodes.GlobalAdressBook;
import nodes.Sensor;

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
				System.out.println("Selected: " + openFile);
				if(openFile.exists()) {
					if(openFile.canRead()) {
						//load
						GUIReferences.sensorNetwork = new ViewPort(openFile, 200, 0);
					} else {
						//could not be read.
					}
				} else {
					//Does not exist? Mispelled?
				}
				//openFile();
			}
			break;
		case MENU_SAVE:
			//TODO
			if(false){ //if(file has already been saved once)
				//save(fileName);
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
				if(saveFile.canWrite()) {
					if(saveFile.exists()) {
						// TODO override or cancel
						saveFile.delete();
					}
					try {
						saveFile.createNewFile();
						//doSomeSaving.
					} catch(IOException e) {
						//
					}
				} else {
					//Do not have write permissions.
					System.out.println(saveFile);
				}
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
					System.out.println(generateDialog);
					for(int i=0;i<number;i++) {
						//GUIReferences.sensorNetwork.getField().addSensor(new Sensor());
						new Sensor();
					}
					GlobalAdressBook.getAdressBook().generateDirectConnections();
					GUIReferences.sensorNetwork.repaint();
				}
			} catch (NumberFormatException e) {
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
			// FIXME
			if(Sensor.idToSensor.size() > 0) {
				int returnValue = JOptionPane.showConfirmDialog(GUIReferences.constructPanel, "Do you really wish to clear?", "", JOptionPane.OK_CANCEL_OPTION);
				System.out.println(returnValue);
				if(returnValue == JOptionPane.OK_OPTION) {
					GlobalAdressBook.clearBook();
					Sensor.idToSensor = new Hashtable<Integer, Sensor>();
					Sensor.usedIDs = 0;
					if(GUIReferences.sensorNetwork != null) {
						GUIReferences.sensorNetwork.repaint();
					}
				}
			}
			break;
		case BUTTON_TO_START:
			//TODO
			GUIReferences.turnController.goToTurn(0);
			GUIReferences.stepperGroup.clearSelection();
			break;
		case BUTTON_REWIND:
			//TODO
			for(int i=1;i<1000;i++){
			GUIReferences.turnController.playTurnBackwards();
			}
			break;
		case BUTTON_STEP_BACKWARD:
			GUIReferences.stepperGroup.clearSelection();
			GUIReferences.turnController.playStepBackwards();
			break;
		case BUTTON_PLAY_BACKWARDS:
			//TODO
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
			for(int i=0;i<1000;i++){
				GUIReferences.turnController.playTick()
			}
			GUIReferences.turnController.playTick();
			break;
		case BUTTON_NEXT_SENSOR:
			GUIReferences.turnController.playTick();
			GUIReferences.stepperGroup.clearSelection();
			break;
		case BUTTON_STEP_FORWARD:
			GUIReferences.turnController.playTurn();
			GUIReferences.stepperGroup.clearSelection();
			break;
		case BUTTON_FAST_FORWARD:
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

			Scaling.setPicCoordsX(0, width);
			Scaling.setPicCoordsY(0, height);

			GUIReferences.sensorNetwork = new ViewPort("Untitled", 200, 0);
			GUIReferences.sensorNetwork.generateField(width,height);
			CPNew.disposeWindow();
			break;
		case BUTTON_NEW_CANCEL:
			CPNew.disposeWindow();
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
		case POPUP_BUTTON_VIEW_SENSOR:
			//TODO - make nicer
			JDialog log = new JDialog(GUIReferences.sensorNetwork, GUIReferences.selectedSensor.toString());
			JPanel pane = new JPanel();
			log.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			pane.setLayout(new BorderLayout());
			log.setContentPane(pane);
			pane.add(new JLabel(GUIReferences.selectedSensor.toString()));
			log.pack();
			log.setVisible(true);
			break;
		}
	}
}