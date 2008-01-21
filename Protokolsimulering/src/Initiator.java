import javax.swing.UIManager;




/**
 * The initializor of the program.
 */
public class Initiator {
	
	/**
	 * The main method
	 * @param args The command-line arguments.
	 */
	public static void main(String[] args) {
		UIManager.put("swing.boldMetal", Boolean.FALSE);
		new gui.ControlPanelFrame().open();
		System.err.println("Shutdown!");
		System.err.println("Threat done!");
		System.exit(0);
	}
}