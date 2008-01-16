package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * @author Frederik Nordahl Sabroe
 *
 */
public class CPActionListener implements ActionListener,GuiInterface{
	public void actionPerformed(ActionEvent arg0) {
		switch(Integer.parseInt(arg0.getActionCommand())) {
		case MENU_QUIT:
			//TODO
			System.exit(0);
			break;
		case MENU_NEW:
			//TODO
			System.out.println("rrr");
			
			GuiStuff.sensorNetwork = new ViewPort("Untitled", 200, 0);
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
				            GuiStuff.sensorNetwork = new ViewPort(openFile, 200, 0);
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
		case BUTTON_KILL:
			//TODO
			if(GuiStuff.mode==MODE_KILL){
				GuiStuff.modeGroup.clearSelection();
				GuiStuff.mode = MODE_SELECT;
				System.out.println("Select");
			} else {
				GuiStuff.mode = MODE_KILL;
				System.out.println("Kill");
			}
			break;
		case BUTTON_ADD:
			//TODO
			if(GuiStuff.mode==MODE_ADD){
				GuiStuff.modeGroup.clearSelection();
				GuiStuff.mode = MODE_SELECT;
				System.out.println("Select");
			}else{
				GuiStuff.mode = MODE_ADD;
				System.out.println("Add");
			}
			break;
		case BUTTON_MOVE:
			//TODO
			if(GuiStuff.mode == MODE_MOVE){
				GuiStuff.modeGroup.clearSelection();
				GuiStuff.mode = MODE_SELECT;
				System.out.println("Select");
			}else{
				GuiStuff.mode = MODE_MOVE;
				System.out.println("Move");
			}
			break;
		case BUTTON_TO_START:
			//TODO
			//toStart();
			GuiStuff.stepperGroup.clearSelection();
			break;
		case BUTTON_REWIND:
			//TODO
			//rewind();
			break;
		case BUTTON_STEP_BACKWARD:
			//TODO
			//stepBackwards;
			GuiStuff.stepperGroup.clearSelection();
			break;
		case BUTTON_PLAY_BACKWARDS:
			//TODO
			//playBackwards();
			break;
		case BUTTON_STOP:
			//TODO
			//stop();
			GuiStuff.stepperGroup.clearSelection();
			break;
		case BUTTON_PLAY:
			//TODO
			//play();
			break;
		case BUTTON_NEXT_SENSOR:
			//TODO
			//nextSensor();
			GuiStuff.stepperGroup.clearSelection();
			break;
		case BUTTON_STEP_FORWARD:
			//TODO
			//stepForward();
			GuiStuff.stepperGroup.clearSelection();
			break;
		case BUTTON_FAST_FORWARD:
			//TODO
			//fastForward();
			break;
		case CHECKBOX_RADII:
			//TODO
			if(0 != (GuiStuff.view & VIEW_RADII)) {
				GuiStuff.view &= ~VIEW_RADII;
			} else {
				GuiStuff.view |= VIEW_RADII;
			}
			if(GuiStuff.sensorNetwork != null) {
				GuiStuff.sensorNetwork.repaint();
			}
			break;
		case CHECKBOX_CONNECTIONS:
			//TODO
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
		}
	}
}