package gui;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JOptionPane;

/**
 * WindowListener for all the windows.
 * @author Frederik Nordahl Sabroe
 * @author Niels Thykier
 */
public class WindowListeners implements WindowListener, GUIConstants {
	
	public void windowActivated(WindowEvent arg0) {}

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
					GUIReferences.viewPort.setVisible(true);
					cancel = true;
					break;
				}
			}
			if(!cancel) {
				if(window == WINDOW_CONTROL_FRAME){
					ControlPanelFrame.getFrame().quit();
				}
				else {
					ViewPort.disposeViewPort();
				}
			}
			break;
		}
	}

	public void windowDeactivated(WindowEvent e) {}

	public void windowDeiconified(WindowEvent e) {}

	public void windowIconified(WindowEvent e) {}

	public void windowOpened(WindowEvent e) {}

}
