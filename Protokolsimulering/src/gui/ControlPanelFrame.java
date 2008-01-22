package gui;

import static gui.GUIReferences.mode;
import static gui.GUIReferences.modeGroup;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.WindowListener;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nodes.Sensor;

/**
 * @author Frederik Nordahl Sabroe
 *
 */
public class ControlPanelFrame extends JFrame implements GUIConstants,ChangeListener{

	private static final long serialVersionUID = -8026416994513756565L;

	private static ControlPanelFrame controlPanelFrame;
	private JTabbedPane modeTabPanes = new JTabbedPane();
	private boolean constructTabSelected = true;
	

	/**
	 * Generates a new Control panel frame and inits a lot of related 
	 * GUI elements and variables in the GUIReferences class.
	 */
	private ControlPanelFrame() {
		//ControlPanelFrame gets initialized
		super("Control Panel");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setPreferredSize(new Dimension(200, 572));
		this.setName(String.valueOf(WINDOW_CONTROL_FRAME));
		setResizable(false);


		GUIReferences.console = new Console().init();
		GUIReferences.informationFrame = new InformationFrame().init();

		//The additional panels are created
		CPActionListener actionListener = new CPActionListener();
		WindowListener localWindowListener = new WindowListeners();
		new CPMenuBar(this,actionListener);
		new CPViewSettings(actionListener);
		new CPModes(actionListener);
		new CPStepper(actionListener);
		GUIReferences.listener = actionListener;
		GUIReferences.windowListener = localWindowListener;

		this.addWindowListener(localWindowListener);
		
		//The panel used for the content of the control panel is created and added
		add(GUIReferences.controlPanelPane,BorderLayout.NORTH);

		//The two tabs on are created and filled
		modeTabPanes.addTab("Construct", GUIReferences.constructPanel);
		modeTabPanes.addTab("Simulate", GUIReferences.simulatePanel);
		GUIReferences.controlPanelPane.add(modeTabPanes);
		modeTabPanes.addChangeListener(this);
		
		//infobox
		JPanel infoBoxOptions = new JPanel();
		infoBoxOptions.setLayout(new GridLayout(0,1));
		JCheckBox infoBoxEnable = new JCheckBox("Information Frame");
		infoBoxEnable.addActionListener(actionListener);
		infoBoxEnable.setActionCommand(String.valueOf(CHECKBOX_ENABLE_INFOBOX));
		GUIReferences.controlPanelPane.add(infoBoxOptions);
		infoBoxOptions.add(infoBoxEnable);

		//Console prefs
		JPanel consoleOptionsPane = new JPanel();
		consoleOptionsPane.setBorder(BorderFactory.createTitledBorder("Console Options"));
		consoleOptionsPane.setLayout(new GridLayout(0,1));
		JCheckBox consoleEnable = new JCheckBox("Console");
		consoleEnable.addActionListener(actionListener);
		consoleEnable.setActionCommand(String.valueOf(CHECKBOX_ENABLE_CONSOLE));
		JCheckBox consoleInformation = new JCheckBox("Information");
		consoleInformation.addActionListener(actionListener);
		consoleInformation.setActionCommand(String.valueOf(CHECKBOX_CONSOLE_INFORMATION));
		consoleInformation.setSelected(true);
		JCheckBox consoleWarning = new JCheckBox("Warning");
		consoleWarning.addActionListener(actionListener);
		consoleWarning.setActionCommand(String.valueOf(CHECKBOX_CONSOLE_WARNING));
		consoleWarning.setSelected(true);
		JCheckBox consoleError = new JCheckBox("Error");
		consoleError.addActionListener(actionListener);
		consoleError.setActionCommand(String.valueOf(CHECKBOX_CONSOLE_ERROR));
		consoleError.setSelected(true);
		consoleError.setEnabled(false);
		JCheckBox consoleDebug = new JCheckBox("Debug");
		consoleDebug.addActionListener(actionListener);
		consoleDebug.setActionCommand(String.valueOf(CHECKBOX_CONSOLE_DEBUG));
		GUIReferences.controlPanelPane.add(consoleOptionsPane);
		consoleOptionsPane.add(consoleEnable);
		consoleOptionsPane.add(consoleInformation);
		consoleOptionsPane.add(consoleWarning);
		consoleOptionsPane.add(consoleError);
		consoleOptionsPane.add(consoleDebug);

		//The statusbar is created
		JPanel statusBarPanel = new JPanel();
		FlowLayout statusBarLayout = new FlowLayout(FlowLayout.LEFT,0,0);
		statusBarPanel.setLayout(statusBarLayout);
		statusBarPanel.setBorder(BorderFactory.createTitledBorder(""));
		GUIReferences.status = new JLabel("Ready");
		add(statusBarPanel,BorderLayout.SOUTH);
		statusBarPanel.add(GUIReferences.status);
		
		//frame is packed and shown
		pack();	
	}
	
	/**
	 * Opens the frame
	 */
	public void open() {
		setVisible(true);
	}
	
	/**
	 * Generic method called when the program should exit.
	 */
	public void quit() {
		this.setVisible(false);
		System.exit(0);
	}

	/**
	 * Static get method for fetching the frame.
	 * @return Return the frame.
	 */
	public static ControlPanelFrame getFrame(){
		if(controlPanelFrame == null) {
			controlPanelFrame = new ControlPanelFrame();
		}
		return controlPanelFrame;
	}

	public void stateChanged(ChangeEvent arg0) {
		constructTabSelected = !constructTabSelected;
		if(constructTabSelected) {
			//construct tab selected
		} else {
			//simulate tab selected
			if(mode != MODE_SELECT) {
				modeGroup.clearSelection();
				mode = MODE_SELECT;
			}
			Enumeration<AbstractButton> buttons = GUIReferences.stepperGroup.getElements();
			if(Sensor.getAmountOfTerminals() > 0) {
				while(buttons.hasMoreElements()) {
					buttons.nextElement().setEnabled(true);
				}
				GUIReferences.nextSensor.setEnabled(true);
			} else {
				while(buttons.hasMoreElements()) {
					buttons.nextElement().setEnabled(false);
				}
				GUIReferences.nextSensor.setEnabled(false);
			}
		}
	}
}