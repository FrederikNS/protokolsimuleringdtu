package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class ControlPanelFrame extends JFrame implements GuiInterface{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8026416994513756565L;
	
	private static ControlPanelFrame controlPanelFrame;
	JTabbedPane modeTabPanes = new JTabbedPane();
	
	
	public ControlPanelFrame() {
		//ControlPanelFrame gets initialized
		super("Control Panel");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(203,602));
		setResizable(true);
		
		//Niels Hack
		if(controlPanelFrame != null) {
			throw new RuntimeException();
		}
		controlPanelFrame = this;
		
		//The additional panels are created
		ActionListener actionListener = new CPActionListener();
		System.out.println("menubar");
		new CPMenuBar(this,actionListener);
		System.out.println("viewsettings");
		new CPViewSettings(actionListener);
		System.out.println("modes");
		new CPModes(actionListener);
		System.out.println("stepper");
		new CPStepper(actionListener);
		
		//The panel used for the content of the control panel is created and added
		//BoxLayout controlPanelPaneLayout = new BoxLayout(controlPanelPane,BoxLayout.Y_AXIS);
		System.out.println("adding controlpanelpane");
		add(GuiStuff.controlPanelPane,BorderLayout.NORTH);
		
		//The two tabs on are created and filled
		System.out.println("filling tabs");
		modeTabPanes.addTab("Construct", GuiStuff.constructPanel);
		modeTabPanes.addTab("Simulate", GuiStuff.simulatePanel);
		GuiStuff.controlPanelPane.add(modeTabPanes);
		
		//The statusbar is created
		JPanel statusBarPanel = new JPanel();
		FlowLayout statusBarLayout = new FlowLayout(FlowLayout.LEFT,0,0);
		statusBarPanel.setLayout(statusBarLayout);
		statusBarPanel.setBorder(BorderFactory.createTitledBorder(""));
		JLabel status = new JLabel("WAAARGH!");
		add(statusBarPanel,BorderLayout.SOUTH);
		statusBarPanel.add(status);
		
		//frame is packed and shown
		pack();
		setVisible(true);
	}
	
	public static ControlPanelFrame getFrame(){
		return controlPanelFrame;
	}
}