import gui.ControlPanelFrame;

import javax.swing.UIManager;

import nodes.GlobalAddressBook;




/**
 * The initializor of the program.
 */
public class Initiator {
	
	/**
	 * The main method
	 * @param args The command-line arguments.
	 */
	public static void main(String[] args) {
		if(args.length > 0) {
			for(int i = 0 ; i < args.length ; i++) {
				if(args[i].equals("make") && args.length > i +1) {
					if(args[i+1].equals("coffee") || args[i+1].equals("tea")) {
						System.out.println("             (\n"
+"               )     (\n"
+"                ___...(-------)-....___\n"
+"            .-\"\"       )    (          \"\"-.\n"
+"      .-'``'|-._             )         _.-|\n"
+"     /  .--.|   `\"\"---...........---\"\"`   |\n"
+"    /  /    |                             |\n"
+"    |  |    |                             |\n"
+"     \\  \\   |                             |\n"
+"      `\\ `\\ |                             |\n"
+"        `\\ `|                             |\n"
+"        _/ /\\                             /\n"
+"       (__/  \\                           /\n"
+"    _..---\"\"` \\                         /`\"\"---.._\n"
+" .-'           \\                       /          '-.\n"
+":               `-.__             __.-'              :\n"
+":                  ) \"\"---...---\"\" (                 :\n"
+" '._               `\"--...___...--\"`              _.'\n"
+"   \\\"\"--..__                              __..--\"\"/\n"
+"   '._     \"\"\"----.....______.....----\"\"\"     _.'\n"
+"      `\"\"--..,,_____            _____,,..--\"\"`\n"
+"                    `\"\"\"----\"\"\"`\n");
						System.out.println("Here you go, one large cup of " + args[i+1]);
					} else if(args[i+1].equals("cake")) {
						System.err.println("The cake is a lie!");
						System.err.println("THE CAKE IS A LIE!");
					} else {
						System.out.println("We do not serve " + args[i+1] + " here.");
					}
					return;
				}
			}
		}
		UIManager.put("swing.boldMetal", Boolean.FALSE);
		ControlPanelFrame.getFrame().open();
		GlobalAddressBook.getBook().capture();
	}
}