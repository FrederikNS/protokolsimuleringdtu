package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import notification.Note;
import notification.NotificationListener;

/**
 * @author Frederik Nordahl Sabroe
 *
 */
public class ControlPanelFrame extends JFrame implements GuiInterface,NotificationListener{

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
		
		//The console is created
		JTextArea console = new JTextArea("test?\nwaaagh");
		FlowLayout consolePanelLayout = new FlowLayout(FlowLayout.LEFT,0,0);
		GridLayout consoleScrollerPanelLayout = new GridLayout(0,1);
		JPanel consolePanel = new JPanel();
		JPanel consoleScrollerPanel = new JPanel();
		consoleScrollerPanel.setBorder(BorderFactory.createTitledBorder("Console"));
		JScrollPane consoleScroller = new JScrollPane(consolePanel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		consoleScrollerPanel.setLayout(consoleScrollerPanelLayout);
		consolePanel.setLayout(consolePanelLayout);
		add(consoleScrollerPanel);
		consoleScrollerPanel.add(consoleScroller);
		consolePanel.add(console);
		console.setEditable(false);
		console.setBackground(new Color(0x00EEEEEE));
		
		//The statusbar is created
		JPanel statusBarPanel = new JPanel();
		FlowLayout statusBarLayout = new FlowLayout(FlowLayout.LEFT,0,0);
		statusBarPanel.setLayout(statusBarLayout);
		statusBarPanel.setBorder(BorderFactory.createTitledBorder(""));
		JLabel status = new JLabel("WAAAGH!");
		add(statusBarPanel,BorderLayout.SOUTH);
		statusBarPanel.add(status);
		
		
		//frame is packed and shown
		pack();
		setVisible(true);
	}
	
	public static ControlPanelFrame getFrame(){
		return controlPanelFrame;
	}

	public void note(Note newNote) {
		// TODO Auto-generated method stub
		//consolePanel.
		newNote.getMessage();
	}
}