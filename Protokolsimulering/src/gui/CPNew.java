package gui;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;

/**
 * The dialog for when opening a new ViewFrame.
 * 
 * @author Frederik Nordahl Sabroe
 */
public class CPNew extends JDialog implements GUIConstants{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1551499666154155856L;

	/**
	 * Handles the width of the new field
	 */
	static JSpinner widthSpinner;
	/**
	 * Handles the height of the new field
	 */
	static JSpinner heightSpinner;
	/**
	 * This Dialog
	 */
	private static CPNew cPNew;

	/**
	 * The Constructor
	 */
	private CPNew(){

	}

	/**
	 * The constructor
	 * @param frame Sets the given frame as parent
	 */
	private CPNew(JFrame frame){
		super(frame);
	}

	/**
	 * initializes this dialog
	 */
	private void init(){
		setTitle("New...");
		setSize(200,200);
		setMinimumSize(new Dimension(200,200));
		setPreferredSize(new Dimension(200,200));
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

		widthSpinner.setValue(250);
		heightSpinner.setValue(250);

		pack();
		setVisible(true);
		
	}

	/**
	 * Static method for easily disposing the dialog if it exists.
	 */
	public static void disposeWindow(){
		if(cPNew!=null){
			cPNew.dispose();
			cPNew=null;
		}
	}

	/**
	 * Opens a new dialog.
	 * @return The newly opened (or still existing) dialog.
	 */
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
