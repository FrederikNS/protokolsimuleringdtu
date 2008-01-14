/**
 * The initializor of the program.
 */
public class Initiator {

	/**
	 * The main method
	 * @param args The command-line arguments.
	 */
	public static void main(String[] args) {
		new gui.ControlPanelFrame();
		/*try {
	      javax.xml.parsers.SAXParserFactory factory = javax.xml.parsers.SAXParserFactory.newInstance();
	      javax.xml.parsers.SAXParser parser = factory.newSAXParser();
		  parser.parse(new org.xml.sax.InputSource(new java.io.InputStreamReader(
						new java.io.FileInputStream("/home/moon/workspace/ProtokolSimulering/src/text.xml"), "UTF-8")),
						new xml.SAXContentHandler());
		} catch (Exception e) {
			System.out.flush();
			System.err.flush();
			e.printStackTrace();
		}*/
	}
}