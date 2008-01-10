package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

public class ControlPanelFrame extends JFrame implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8026416994513756565L;
	private ButtonGroup modeGroup = new ButtonGroup();
	public final static int MODE_SELECT = 0;
	public final static int MODE_KILL = 1;
	public final static int MODE_ADD = 2;
	public int mode = MODE_SELECT;

	public ControlPanelFrame() {
		super("Control Panel");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(200,600));
		setResizable(false);
		
		JPanel controlPanelPane = new JPanel();
		BoxLayout controlPanelPaneLayout = new BoxLayout(controlPanelPane, BoxLayout.Y_AXIS);
		
		controlPanelPane.setLayout(controlPanelPaneLayout);
		
		add(controlPanelPane);
		modes();
		viewSettings();
		stepper();
		
		pack();
		setVisible(true);
	}
	
	public void modes() {
		JPanel modesPanel = new JPanel();
		JToggleButton killButton = new JToggleButton("Kill");
		killButton.addActionListener(this);
		JToggleButton addButton = new JToggleButton("Add");
		addButton.addActionListener(this);
		
		
		this.add(modesPanel);
		modeGroup.add(killButton);
		modeGroup.add(addButton);
		modesPanel.add(killButton);
		modesPanel.add(addButton);
	}
	
	public void viewSettings() {
		
	}
	
	public void stepper() {
		
	}
	
	public void actionPerformed(ActionEvent arg0) {
		String action=arg0.getActionCommand();
		switch(action.charAt(0)) {
		case 'A':
			if(action.equals("Add")) {
				if(mode==MODE_ADD){
					modeGroup.clearSelection();
					mode = MODE_SELECT;
					System.out.println("Select");
				}else{
					mode = MODE_ADD;
					System.out.println("Add");
				}
				break;
			}
			break;
		case 'K':
			if(action.equals("Kill")) {
				if(mode==MODE_KILL){
					modeGroup.clearSelection();
					mode = MODE_SELECT;
					System.out.println("Select");
				} else {
					mode = MODE_KILL;
					System.out.println("Kill");
				}
				break;
			}
			break;
		}
	}
	
	public static void main(String[] args) {
		new ControlPanelFrame();
	}
}