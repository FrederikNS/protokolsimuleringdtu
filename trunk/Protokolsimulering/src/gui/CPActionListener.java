package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

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
			//TODO
			System.exit(0);
			break;
		case MENU_NEW:
			//TODO
			GUIReferences.sensorNetwork = new ViewPort("Untitled", 200, 0);

			//new();
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
		case BUTTON_GENERATE:
			int number;
			String generateDialog = JOptionPane.showInputDialog(GUIReferences.constructPanel, "Please enter a number of sensors to generate", "Generate...", JOptionPane.QUESTION_MESSAGE);
			try{
				number = Integer.parseInt(generateDialog);
				if(number>0){
					System.out.println(generateDialog);
					for(int i=0;i<number;i++) {
//						splitField.addSensor(new Sensor());
						new Sensor();
					}
					GlobalAdressBook.getAdressBook().generateDirectConnections();
					GUIReferences.sensorNetwork.repaint();
				}
			} catch (NumberFormatException e) {
			}
		case BUTTON_KILL:
			//TODO
			if(GUIReferences.mode==MODE_KILL){
				GUIReferences.modeGroup.clearSelection();
				GUIReferences.mode = MODE_SELECT;
			} else {
				GUIReferences.mode = MODE_KILL;
			}
			break;
		case BUTTON_ADD:
			//TODO
			if(GUIReferences.mode==MODE_ADD){
				GUIReferences.modeGroup.clearSelection();
				GUIReferences.mode = MODE_SELECT;
			}else{
				GUIReferences.mode = MODE_ADD;
			}
			break;
		case BUTTON_CLEAR:
			//TODO
			if(GUIReferences.mode == MODE_MOVE){
				GUIReferences.modeGroup.clearSelection();
				GUIReferences.mode = MODE_SELECT;
			}else{
				GUIReferences.mode = MODE_MOVE;
			}
			break;
		case BUTTON_TO_START:
			//TODO
			//toStart();
			GUIReferences.stepperGroup.clearSelection();
			break;
		case BUTTON_REWIND:
			//TODO
			//rewind();
			break;
		case BUTTON_STEP_BACKWARD:
			//TODO
			//stepBackwards;
			GUIReferences.stepperGroup.clearSelection();
			break;
		case BUTTON_PLAY_BACKWARDS:
			//TODO
			//playBackwards();
			break;
		case BUTTON_STOP:
			//TODO
			//stop();
			GUIReferences.stepperGroup.clearSelection();
			break;
		case BUTTON_PLAY:
			//TODO
			//play();
			break;
		case BUTTON_NEXT_SENSOR:
			//TODO
			//nextSensor();
			GUIReferences.stepperGroup.clearSelection();
			break;
		case BUTTON_STEP_FORWARD:
			//TODO
			//stepForward();
			GUIReferences.stepperGroup.clearSelection();
			break;
		case BUTTON_FAST_FORWARD:
			//TODO
			//fastForward();
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
		case CHECKBOX_BATTERY:
			//TODO
			break;
		case CHECKBOX_ID:
			//TODO
			break;
		case CHECKBOX_NEIGHBOURS:
			//TODO
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