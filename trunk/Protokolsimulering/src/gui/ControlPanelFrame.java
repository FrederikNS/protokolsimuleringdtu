package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ControlPanelFrame extends JFrame implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8026416994513756565L;
	
	public ViewFrame sensorNetwork;
	
	private ButtonGroup modeGroup = new ButtonGroup();
	private ButtonGroup stepperGroup = new ButtonGroup();
	private JPanel controlPanelPane = new JPanel();
	
	private final static int MENU_NEW = 1;
	private final static int MENU_OPEN = 2;
	private final static int MENU_SAVE = 3;
	private final static int MENU_SAVE_AS = 4;
	private final static int MENU_QUIT = 0;
	private final static int BUTTON_KILL = 101;
	private final static int BUTTON_ADD = 102;
	private final static int BUTTON_MOVE = 103;
	private final static int BUTTON_TO_START = 110;
	private final static int BUTTON_REWIND = 111;
	private final static int BUTTON_STEP_BACKWARD = 112;
	private final static int BUTTON_PLAY_BACKWARDS = 113;
	private final static int BUTTON_STOP = 114;
	private final static int BUTTON_PLAY = 115;
	private final static int BUTTON_NEXT_SENSOR = 116;
	private final static int BUTTON_STEP_FORWARD = 117;
	private final static int BUTTON_FAST_FORWARD = 118;
	private final static int CHECKBOX_RADII = 201;
	private final static int CHECKBOX_CONNECTIONS = 202;
	private final static int CHECKBOX_BATTERY = 203;
	private final static int CHECKBOX_ID = 204;
	private final static int CHECKBOX_NEIGHBOURS = 205;
	
	//private final static int  = ;
	
	public final static int PLAYBACK_REWIND = -2;
	public final static int PLAYBACK_PLAY_BACKWARDS = -1;
	public final static int PLAYBACK_PAUSE = 0;
	public final static int PLAYBACK_PLAY = 1;
	public final static int PLAYBACK_FAST_FORWARD = 2;
	public int playback = PLAYBACK_PAUSE;
	
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

		//BorderLayout contentPaneLayout = new BorderLayout(0,0);
		//BoxLayout controlPanelPaneLayout = new BoxLayout(controlPanelPane, BoxLayout.Y_AXIS);
		GridLayout controlPanelPaneLayout = new GridLayout(0,1);
		//GridBagLayout controlPanelPaneLayout = new GridBagLayout();
		//SpringLayout controlPanelPaneLayout = new SpringLayout();
		//this.setLayout(contentPaneLayout);
		controlPanelPane.setLayout(controlPanelPaneLayout);
		
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem newMenuItem = new JMenuItem("New");
		newMenuItem.addActionListener(this);
		newMenuItem.setActionCommand(String.valueOf(MENU_NEW));
		JMenuItem openMenuItem = new JMenuItem("Open...");
		openMenuItem.addActionListener(this);
		openMenuItem.setActionCommand(String.valueOf(MENU_OPEN));
		JMenuItem saveMenuItem = new JMenuItem("Save");
		saveMenuItem.addActionListener(this);
		saveMenuItem.setActionCommand(String.valueOf(MENU_SAVE));
		JMenuItem saveAsMenuItem = new JMenuItem("Save As...");
		saveAsMenuItem.addActionListener(this);
		saveAsMenuItem.setActionCommand(String.valueOf(MENU_SAVE_AS));
		JMenuItem quitMenuItem = new JMenuItem("Quit");
		quitMenuItem.addActionListener(this);
		quitMenuItem.setActionCommand(String.valueOf(MENU_QUIT));

		
		setJMenuBar(menuBar);

		menuBar.add(fileMenu);
		fileMenu.add(newMenuItem);
		fileMenu.add(openMenuItem);
		fileMenu.add(saveMenuItem);
		fileMenu.add(saveAsMenuItem);
		fileMenu.add(quitMenuItem);

		
		add(controlPanelPane,BorderLayout.CENTER);
		modes();
		viewSettings();
		stepper();
		statusBar();

		pack();
		setVisible(true);
	}

	public void modes() {
		JPanel modesPanel = new JPanel();
		FlowLayout modesPanelLayout = new FlowLayout(FlowLayout.CENTER,0,0);
		JToggleButton killButton = new JToggleButton("Kill");
		killButton.setToolTipText("Removes the sensor you click");
		killButton.addActionListener(this);
		killButton.setActionCommand(String.valueOf(BUTTON_KILL));
		JToggleButton addButton = new JToggleButton("Add");
		addButton.setToolTipText("Adds another sensor where you click");
		addButton.addActionListener(this);
		addButton.setActionCommand(String.valueOf(BUTTON_ADD));
		JToggleButton moveButton = new JToggleButton("Move");
		moveButton.setToolTipText("Makes sensors draggable");
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
		//FlowLayout viewSettingsLayout = new FlowLayout(FlowLayout.LEFT,0,0);
		BoxLayout viewSettingsLayout = new BoxLayout(viewSettingsPanel, BoxLayout.Y_AXIS);

		JCheckBox viewRadii = new JCheckBox("Radii");
		viewRadii.setToolTipText("Shows how far the sensors can communicate");
		viewRadii.addActionListener(this);
		viewRadii.setActionCommand(String.valueOf(CHECKBOX_RADII));
		JCheckBox viewConnections = new JCheckBox("Connections");
		viewConnections.setToolTipText("Shows lines between connected sensors");
		viewConnections.addActionListener(this);
		viewConnections.setActionCommand(String.valueOf(CHECKBOX_CONNECTIONS));
		JCheckBox viewBattery = new JCheckBox("Battery");
		viewBattery.setToolTipText("Shows the battery level of the sensors");
		viewConnections.addActionListener(this);
		viewConnections.setActionCommand(String.valueOf(CHECKBOX_BATTERY));
		JCheckBox viewID = new JCheckBox("ID");
		viewID.setToolTipText("Shows the ID of the sensors");
		viewConnections.addActionListener(this);
		viewConnections.setActionCommand(String.valueOf(CHECKBOX_ID));
		JCheckBox viewNeighbours = new JCheckBox("Neighbours");
		viewNeighbours.setToolTipText("Highlights the neighbouring sensors of the selected sensor");
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
		FlowLayout stepperLayout = new FlowLayout(FlowLayout.CENTER,0,0);
		stepperPanel.setLayout(stepperLayout);
		JButton toStart = new JButton("|<");
		toStart.setToolTipText("Goto Start");
		toStart.addActionListener(this);
		toStart.setActionCommand(String.valueOf(BUTTON_TO_START));
		JToggleButton rewind = new JToggleButton("<<");
		rewind.setToolTipText("Rewind");
		rewind.addActionListener(this);
		rewind.setActionCommand(String.valueOf(BUTTON_REWIND));
		JButton stepBackwards = new JButton("<|");
		stepBackwards.setToolTipText("Step Backward");
		stepBackwards.addActionListener(this);
		stepBackwards.setActionCommand(String.valueOf(BUTTON_STEP_BACKWARD));
		JToggleButton playBackwards = new JToggleButton("<");
		playBackwards.setToolTipText("Play Backwards");
		playBackwards.addActionListener(this);
		playBackwards.setActionCommand(String.valueOf(BUTTON_PLAY_BACKWARDS));
		JButton stop = new JButton("■");
		stop.setToolTipText("Stop");
		stop.addActionListener(this);
		stop.setActionCommand(String.valueOf(BUTTON_STOP));
		JToggleButton play = new JToggleButton(">");
		play.setToolTipText("Play");
		play.addActionListener(this);
		play.setActionCommand(String.valueOf(BUTTON_PLAY));
		JButton nextSensor = new JButton("||>");
		nextSensor.setToolTipText("Next Sensor");
		nextSensor.addActionListener(this);
		nextSensor.setActionCommand(String.valueOf(BUTTON_NEXT_SENSOR));
		JButton stepForward = new JButton("|>");
		stepForward.setToolTipText("Step Forward");
		stepForward.addActionListener(this);
		stepForward.setActionCommand(String.valueOf(BUTTON_STEP_FORWARD));
		JToggleButton fastForward = new JToggleButton(">>");
		fastForward.setToolTipText("Fast Forward");
		fastForward.addActionListener(this);
		fastForward.setActionCommand(String.valueOf(BUTTON_FAST_FORWARD));

		controlPanelPane.add(stepperPanel);
		stepperPanel.setBorder(BorderFactory.createTitledBorder("Mode"));
		stepperGroup.add(rewind);
		stepperGroup.add(playBackwards);
		stepperGroup.add(play);
		stepperGroup.add(stepForward);
		stepperGroup.add(fastForward);
		stepperPanel.add(toStart);
		stepperPanel.add(rewind);
		stepperPanel.add(fastForward);
		stepperPanel.add(playBackwards);
		stepperPanel.add(stop);
		stepperPanel.add(play);
		stepperPanel.add(stepBackwards);
		stepperPanel.add(nextSensor);
		stepperPanel.add(stepForward);
	}
	
	public void statusBar() {
		JPanel statusBarPanel = new JPanel();
		FlowLayout statusBarLayout = new FlowLayout(FlowLayout.LEFT,0,0);
		statusBarPanel.setLayout(statusBarLayout);
		statusBarPanel.setBorder(BorderFactory.createTitledBorder(""));
		JLabel status = new JLabel("WAAARGH!");
		
		add(statusBarPanel,BorderLayout.SOUTH);
		statusBarPanel.add(status);
	}

	public void actionPerformed(ActionEvent arg0) {
		switch(Integer.parseInt(arg0.getActionCommand())) {
		case MENU_QUIT:
			//TODO
			System.exit(0);
			break;
		case MENU_NEW:
			//TODO
			System.out.println("rrr");
			sensorNetwork = new ViewFrame("Untitled");
			//new();
			break;
		case MENU_OPEN:
			//TODO - check if session is already running?
			File openFile;
			JFileChooser openChooser = new JFileChooser();
		    FileNameExtensionFilter openFilter = new FileNameExtensionFilter("Sensormap Files (.stuff)", "stuff");
		    openChooser.setFileFilter(openFilter);
		    int openReturnVal = openChooser.showOpenDialog(rootPane);
		    if(openReturnVal == JFileChooser.APPROVE_OPTION) {
		            openFile = openChooser.getSelectedFile();
		            System.out.println("Selected: " + openFile);
		            if(openFile.exists()) {
		            	if(openFile.canRead()) {
		            		//load
				            sensorNetwork = new ViewFrame(openFile.getName());
		            	} else {
		            		//could not be read.
		            	}
		            } else {
		            	//Does not exist? Mispelled?
		            }
		            //openFile();
		    }
			break;
		case MENU_SAVE:
			//TODO
			if(false){ //if(file has already been saved once)
				//save(fileName);
				break;
			}
		case MENU_SAVE_AS:
			//TODO
			File saveFile;
			JFileChooser saveChooser = new JFileChooser();
		    FileNameExtensionFilter saveFilter = new FileNameExtensionFilter("Sensormap Files (.stuff)", "stuff");
		    saveChooser.setFileFilter(saveFilter);
		    int saveReturnVal = saveChooser.showSaveDialog(rootPane);
		    if(saveReturnVal == JFileChooser.APPROVE_OPTION) {
		            saveFile = saveChooser.getSelectedFile();
		            if(saveFile.canWrite()) {
		            	if(saveFile.exists()) {
		            		// TODO override or cancel
		            		saveFile.delete();
		            	}
		            	try {
		            		saveFile.createNewFile();
		            		//doSomeSaving.
		            	} catch(IOException e) {
		            		//
		            	}
		            } else {
		            	//Do not have write permissions.
		            	System.out.println(saveFile);
		            }
		    }
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
		case BUTTON_TO_START:
			//TODO
			//toStart();
			stepperGroup.clearSelection();
			break;
		case BUTTON_REWIND:
			//TODO
			//rewind();
			break;
		case BUTTON_STEP_BACKWARD:
			//TODO
			//stepBackwards;
			stepperGroup.clearSelection();
			break;
		case BUTTON_PLAY_BACKWARDS:
			//TODO
			//playBackwards();
			break;
		case BUTTON_STOP:
			//TODO
			//stop();
			stepperGroup.clearSelection();
			break;
		case BUTTON_PLAY:
			//TODO
			//play();
			break;
		case BUTTON_NEXT_SENSOR:
			//TODO
			//nextSensor();
			break;
		case BUTTON_STEP_FORWARD:
			//TODO
			//stepForward();
			stepperGroup.clearSelection();
			break;
		case BUTTON_FAST_FORWARD:
			//TODO
			//fastForward();
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
}