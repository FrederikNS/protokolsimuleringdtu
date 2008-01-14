package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ControlPanelFrame extends JFrame implements GuiInterface{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8026416994513756565L;
	
	private static ControlPanelFrame controlPanelFrame;
	
	JPanel controlPanelPane = new JPanel();
	
	public ControlPanelFrame() {
		super("Control Panel");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(203,602));
		setResizable(false);
		ActionListener actionListener = new CPActionListener();
		BoxLayout controlPanelPaneLayout = new BoxLayout(controlPanelPane, BoxLayout.Y_AXIS);
		
		if(controlPanelFrame != null) {
			throw new RuntimeException();
		}
		controlPanelFrame = this;
		
		add(controlPanelPane,BorderLayout.NORTH);
		controlPanelPane.setLayout(controlPanelPaneLayout);
		
		new CPMenuBar(this,actionListener);
		new CPModes(actionListener);
		new CPViewSettings(actionListener);
		new CPStepper(actionListener);
		
		JPanel statusBarPanel = new JPanel();
		FlowLayout statusBarLayout = new FlowLayout(FlowLayout.LEFT,0,0);
		statusBarPanel.setLayout(statusBarLayout);
		statusBarPanel.setBorder(BorderFactory.createTitledBorder(""));
		JLabel status = new JLabel("WAAARGH!");
		
		add(statusBarPanel,BorderLayout.SOUTH);
		statusBarPanel.add(status);

		pack();
		setVisible(true);
	}
	public static ControlPanelFrame getFrame(){
		return controlPanelFrame;
	}
}