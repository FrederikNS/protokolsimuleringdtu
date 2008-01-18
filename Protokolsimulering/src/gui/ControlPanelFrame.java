package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author Frederik Nordahl Sabroe
 *
 */
public class ControlPanelFrame extends JFrame implements GUIConstants,ChangeListener{

	private static final long serialVersionUID = -8026416994513756565L;

	private static ControlPanelFrame controlPanelFrame;
	JTabbedPane modeTabPanes = new JTabbedPane();
	private boolean constructTabSelected = true;
	private JLabel status;

	public ControlPanelFrame() {
		//ControlPanelFrame gets initialized
		super("Control Panel");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(200,/*(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()*/ 572)); //TODO
		setResizable(false);

		if(controlPanelFrame != null) {
			throw new RuntimeException();
		}
		controlPanelFrame = this;

		GUIReferences.console = new Console().init();

		//The additional panels are created
		ActionListener actionListener = new CPActionListener();
		WindowListener windowListener = new WindowListeners();
		new CPMenuBar(this,actionListener);
		new CPViewSettings(actionListener);
		new CPModes(actionListener);
		new CPStepper(actionListener);
		GUIReferences.listener = actionListener;
		GUIReferences.windowListener = windowListener;

		//The panel used for the content of the control panel is created and added
		add(GUIReferences.controlPanelPane,BorderLayout.NORTH);

		//The two tabs on are created and filled
		modeTabPanes.addTab("Construct", GUIReferences.constructPanel);
		modeTabPanes.addTab("Simulate", GUIReferences.simulatePanel);
		GUIReferences.controlPanelPane.add(modeTabPanes);
		modeTabPanes.addChangeListener(this);

		//Console prefs
		JPanel consoleOptionsPane = new JPanel();
		consoleOptionsPane.setLayout(new GridLayout(0,1));
		JCheckBox consoleEnable = new JCheckBox("Console");
		consoleEnable.addActionListener(actionListener);
		consoleEnable.setActionCommand(String.valueOf(CHECKBOX_ENABLE_CONSOLE));
		GUIReferences.controlPanelPane.add(consoleOptionsPane);
		consoleOptionsPane.add(consoleEnable);

		//The statusbar is created
		JPanel statusBarPanel = new JPanel();
		FlowLayout statusBarLayout = new FlowLayout(FlowLayout.LEFT,0,0);
		statusBarPanel.setLayout(statusBarLayout);
		statusBarPanel.setBorder(BorderFactory.createTitledBorder(""));
		status = new JLabel("Ready");
		add(statusBarPanel,BorderLayout.SOUTH);
		statusBarPanel.add(status);
		
		//frame is packed and shown
		pack();
		setVisible(true);
	}

	public void setJLabelStatus(int x, int y, int i) {
		status.setText("(" + x + "," + y + ") Sensors: "+i);
	}

	public static ControlPanelFrame getFrame(){
		return controlPanelFrame;
	}

	public void stateChanged(ChangeEvent arg0) {
		//TODO
		constructTabSelected = !constructTabSelected;
		if(constructTabSelected) {
			//construct tab selected
		} else {
			//simulate tab selected
			//rebuild?
		}
	}
}