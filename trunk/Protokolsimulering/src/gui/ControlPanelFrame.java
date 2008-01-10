package gui;

import javax.swing.JFrame;

public class ControlPanelFrame extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8026416994513756565L;

	public ControlPanelFrame() {
		super("Controlpanel");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new ControlPanelFrame();
	}
}
