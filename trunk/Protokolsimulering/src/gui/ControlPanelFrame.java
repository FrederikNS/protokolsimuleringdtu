package gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

public class ControlPanelFrame extends JFrame implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8026416994513756565L;
	
	private ButtonGroup modeGroup = new ButtonGroup();
	private JPanel controlPanelPane = new JPanel();
	
	private final static int MENU_QUIT = 0;
	private final static int BUTTON_KILL = 101;
	private final static int BUTTON_ADD = 102;
	private final static int BUTTON_MOVE = 103;
	private final static int CHECKBOX_RADII = 201;
	private final static int CHECKBOX_CONNECTIONS = 202;
	private final static int CHECKBOX_BATTERY = 203;
	private final static int CHECKBOX_ID = 204;
	private final static int CHECKBOX_NEIGHBOURS = 205;
	//private final static int  = ;
	//private final static int  = ;
	//private final static int  = ;
	//private final static int  = ;
	//private final static int  = ;
	//private final static int  = ;
	//private final static int  = ;
	//private final static int  = ;
	//private final static int  = ;
	//private final static int  = ;
	//private final static int  = ;
	
	public final static int MODE_SELECT = 0;
	public final static int MODE_KILL = 1;
	public final static int MODE_ADD = 2;
	public final static int MODE_MOVE = 3;
	public int mode = MODE_SELECT;

	public ControlPanelFrame() {
		super("Control Panel");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(200,600));
		setResizable(false);

		//BoxLayout controlPanelPaneLayout = new BoxLayout(controlPanelPane, BoxLayout.Y_AXIS);
		GridLayout controlPanelPaneLayout = new GridLayout(0,1);

		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem quitMenuItem = new JMenuItem("Quit");

		setJMenuBar(menuBar);

		menuBar.add(fileMenu);
		fileMenu.add(quitMenuItem);

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
		FlowLayout modesPanelLayout = new FlowLayout(FlowLayout.CENTER,0,0);
		JToggleButton killButton = new JToggleButton("Kill");
		killButton.addActionListener(this);
		killButton.setActionCommand(String.valueOf(BUTTON_KILL));
		JToggleButton addButton = new JToggleButton("Add");
		addButton.addActionListener(this);
		addButton.setActionCommand(String.valueOf(BUTTON_ADD));
		JToggleButton moveButton = new JToggleButton("Move");
		moveButton.addActionListener(this);
		moveButton.setActionCommand(String.valueOf(BUTTON_MOVE));

		controlPanelPane.add(modesPanel);
		modesPanel.setLayout(modesPanelLayout);
		modesPanel.setBorder(BorderFactory.createTitledBorder("Mode"));
		modeGroup.add(killButton);
		modeGroup.add(addButton);
		modeGroup.add(moveButton);
		modesPanel.add(killButton);
		modesPanel.add(addButton);
		modesPanel.add(moveButton);
	}

	public void viewSettings() {
		JPanel viewSettingsPanel = new JPanel();
		BoxLayout viewSettingsLayout = new BoxLayout(viewSettingsPanel, BoxLayout.Y_AXIS);

		JCheckBox viewRadii = new JCheckBox("Radii");
		viewRadii.addActionListener(this);
		viewRadii.setActionCommand(String.valueOf(CHECKBOX_RADII));
		JCheckBox viewConnections = new JCheckBox("Connections");
		viewConnections.addActionListener(this);
		viewConnections.setActionCommand(String.valueOf(CHECKBOX_CONNECTIONS));
		JCheckBox viewBattery = new JCheckBox("Battery");
		viewConnections.addActionListener(this);
		viewConnections.setActionCommand(String.valueOf(CHECKBOX_BATTERY));
		JCheckBox viewID = new JCheckBox("ID");
		viewConnections.addActionListener(this);
		viewConnections.setActionCommand(String.valueOf(CHECKBOX_ID));
		JCheckBox viewNeighbours = new JCheckBox("Neighbours");
		viewConnections.addActionListener(this);
		viewConnections.setActionCommand(String.valueOf(CHECKBOX_NEIGHBOURS));

		controlPanelPane.add(viewSettingsPanel);
		viewSettingsPanel.setLayout(viewSettingsLayout);
		viewSettingsPanel.setBorder(BorderFactory.createTitledBorder("Views"));
		viewSettingsPanel.add(viewRadii);
		viewSettingsPanel.add(viewConnections);
		viewSettingsPanel.add(viewBattery);
		viewSettingsPanel.add(viewID);
		viewSettingsPanel.add(viewNeighbours);
	}

	public void stepper() {
		JPanel stepperPanel = new JPanel();
		ButtonGroup stepperControls = new ButtonGroup();
		JButton toStart = new JButton("|<");
		JToggleButton rewind = new JToggleButton("<<");
		JButton stepBackwards = new JButton("<|");
		JToggleButton playBackwards = new JToggleButton();
		JToggleButton play = new JToggleButton();
		JButton stepForward = new JButton("|>");
		JToggleButton fastForward = new JToggleButton(">>");

		controlPanelPane.add(stepperPanel);
		stepperPanel.setBorder(BorderFactory.createTitledBorder("Mode"));
		stepperControls.add(toStart);
		stepperControls.add(rewind);
		stepperControls.add(stepBackwards);
		stepperControls.add(playBackwards);
		stepperControls.add(play);
		stepperControls.add(stepForward);
		stepperControls.add(fastForward);
		stepperPanel.add(toStart);
		stepperPanel.add(rewind);
		stepperPanel.add(stepBackwards);
		stepperPanel.add(playBackwards);
		stepperPanel.add(play);
		stepperPanel.add(stepForward);
		stepperPanel.add(fastForward);
	}

	public void actionPerformed(ActionEvent arg0) {
		switch(Integer.parseInt(arg0.getActionCommand())) {
		case MENU_QUIT:
			//TODO
			System.exit(0);
			break;
		case BUTTON_KILL:
			//TODO
			if(mode==MODE_KILL){
				modeGroup.clearSelection();
				mode = MODE_SELECT;
				System.out.println("Select");
			} else {
				mode = MODE_KILL;
				System.out.println("Kill");
			}
			break;
		case BUTTON_ADD:
			//TODO
			if(mode==MODE_ADD){
				modeGroup.clearSelection();
				mode = MODE_SELECT;
				System.out.println("Select");
			}else{
				mode = MODE_ADD;
				System.out.println("Add");
			}
			break;
		case BUTTON_MOVE:
			//TODO
			if(mode == MODE_MOVE){
				modeGroup.clearSelection();
				mode = MODE_SELECT;
				System.out.println("Select");
			}else{
				mode = MODE_MOVE;
				System.out.println("Move");
			}
			break;
		case CHECKBOX_RADII:
			//TODO
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

	public static void main(String[] args) {
		new ControlPanelFrame();
	}
}