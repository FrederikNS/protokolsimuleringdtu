package gui;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JOptionPane;

public class WindowListeners implements WindowListener, GUIConstants {
	boolean doBringToFront = false;
	public void windowActivated(WindowEvent arg0) {
		switch(arg0.getWindow().getName().charAt(0)) {
		case WINDOW_CONTROL_FRAME:
			if(doBringToFront) {
				if(GUIReferences.console != null) {
					GUIReferences.console.toFront();
				}
				if(GUIReferences.sensorNetwork != null) {
					GUIReferences.sensorNetwork.toFront();
				}
			}
			break;
		}

	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowClosing(WindowEvent e) {
		boolean cancel = false;
		boolean isChanged = GUIReferences.saveMenuItem.isEnabled();
		char window = e.getWindow().getName().charAt(0);
		switch(window) {
		case WINDOW_VIEW_PORT:
		case WINDOW_CONTROL_FRAME:
			if(isChanged) {
				int saveBeforeQuit = JOptionPane.showConfirmDialog(GUIReferences.controlPanelPane, "Do you want to save before you quit?", "Warning", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
				switch(saveBeforeQuit){
				case JOptionPane.YES_OPTION:
					GUIReferences.saveMenuItem.setEnabled(false);
					GUIReferences.save();
					break;
				case JOptionPane.NO_OPTION:
					GUIReferences.saveMenuItem.setEnabled(false);
					break;
				case JOptionPane.CANCEL_OPTION:
					GUIReferences.sensorNetwork.setVisible(true);
					cancel = true;
					break;
				}
			}
			if(window == WINDOW_CONTROL_FRAME && !cancel){
				ControlPanelFrame.getFrame().quit();
			}
			if(window == WINDOW_VIEW_PORT && !cancel){
				ViewPort.disposeViewPort();
			}
			break;
		}
	}

	public void windowDeactivated(WindowEvent e) {}

	public void windowDeiconified(WindowEvent e) {}

	public void windowIconified(WindowEvent e) {
		doBringToFront = true;
	}

	public void windowOpened(WindowEvent e) {}

}
