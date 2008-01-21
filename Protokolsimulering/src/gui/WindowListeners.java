package gui;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class WindowListeners implements WindowListener, GUIConstants {
	
	public void windowActivated(WindowEvent arg0) {
		switch(arg0.getWindow().getName().charAt(0)) {
		case WINDOW_CONTROL_FRAME:
			if(GUIReferences.console != null) {
				GUIReferences.console.toFront();
			}
			if(GUIReferences.sensorNetwork != null) {
				GUIReferences.sensorNetwork.toFront();
			}
			break;
		}
		
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowClosing(WindowEvent e) {
		boolean isChanged = GUIReferences.saveMenuItem.isEnabled();
		char window = e.getWindow().getName().charAt(0);
		switch(window) {
		case WINDOW_VIEW_PORT:
		case WINDOW_CONTROL_FRAME:
			if(isChanged) {
				//Confirm dialog.
			}
			if(window == WINDOW_CONTROL_FRAME){
				ControlPanelFrame.getFrame().quit();
			}
			break;
		}
	}

	public void windowDeactivated(WindowEvent e) {}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowIconified(WindowEvent e) {}

	public void windowOpened(WindowEvent e) {}

}
