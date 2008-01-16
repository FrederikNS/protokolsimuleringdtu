package gui;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ConfigFrame extends JDialog implements GUIConstants{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3360612748266382475L;
	
	private JButton sensorColorChanger = new JButton("Change Sensor Color");
	private JLabel sensorColorShower = new JLabel("Sensor Color");
	private JButton deadSensorColorChanger = new JButton("Change Dead Sensor Color");
	private JLabel deadSensorColorShower = new JLabel("Dead Sensor Color");
	
	private ConfigFrame() {

	}
	private ConfigFrame(JFrame frame) {

	}
	
	private void init() {
		//TODO 
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setPreferredSize(new Dimension(203,200));
		setResizable(true);
		JPanel pane = new JPanel();
		setContentPane(pane);
		//pane.setLayout();
		sensorColorChanger.setBackground(GUIReferences.sensorColor);
		deadSensorColorChanger.setBackground(GUIReferences.deadColor);
		add(sensorColorChanger);
		add(sensorColorShower);
		add(deadSensorColorChanger);
		add(deadSensorColorShower);
		setTitle("Preferences");
		pack();
	}
	
	
	public static ConfigFrame openConfigFrame() {
		ConfigFrame dialog;
		if(ControlPanelFrame.getFrame() != null) {
			dialog = new ConfigFrame(ControlPanelFrame.getFrame());
		} else {
			dialog = new ConfigFrame();
		}
		dialog.init();
		return dialog;
	}
}
