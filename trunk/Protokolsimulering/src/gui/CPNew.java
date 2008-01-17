package gui;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;

public class CPNew extends JDialog implements GUIConstants{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1551499666154155856L;

	static JSpinner widthSpinner;
	static JSpinner heightSpinner;
	private static CPNew cPNew;

	private CPNew(){

	}

	private CPNew(JFrame frame){

	}

	private void init(){
		setTitle("New...");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		BoxLayout dialogLayout = new BoxLayout(this.getContentPane(),BoxLayout.Y_AXIS);
		setLayout(dialogLayout);
		JPanel newDialogPanel = new JPanel();
		JPanel widthPanel = new JPanel();
		JPanel heightPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		widthSpinner = new JSpinner();
		heightSpinner = new JSpinner();
		JLabel widthLabel = new JLabel("Width: ");
		JLabel heightLabel = new JLabel("Height: ");

		JButton okButton = new JButton("Ok");
		JButton cancelButton = new JButton("Cancel");

		newDialogPanel.setLayout(new BoxLayout(newDialogPanel,BoxLayout.Y_AXIS));
//		widthPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
//		heightPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		add(newDialogPanel);

		newDialogPanel.add(widthPanel);
		newDialogPanel.add(heightPanel);
		newDialogPanel.add(buttonPanel);

		widthPanel.add(widthLabel);
		widthPanel.add(widthSpinner);
		heightPanel.add(heightLabel);
		heightPanel.add(heightSpinner);
		buttonPanel.add(cancelButton);
		buttonPanel.add(okButton);
		cancelButton.addActionListener(GUIReferences.listener);
		okButton.addActionListener(GUIReferences.listener);
		cancelButton.setActionCommand(String.valueOf(BUTTON_NEW_CANCEL));
		okButton.setActionCommand(String.valueOf(BUTTON_NEW_OK));

		widthSpinner.setValue(50000);
		heightSpinner.setValue(50000);

		pack();
		setVisible(true);
		widthSpinner.setValue(500);
		heightSpinner.setValue(500);
	}

	public static void disposeWindow(){
		if(cPNew!=null){
			cPNew.dispose();
			cPNew=null;
		}
	}

	public static CPNew openCPNew() {
		if(cPNew!=null){
			disposeWindow();
		}
		if(ControlPanelFrame.getFrame() != null) {
			cPNew = new CPNew(ControlPanelFrame.getFrame());
		} else {
			cPNew = new CPNew();
		}
		cPNew.init();
		return cPNew;
	}
}
