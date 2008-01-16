
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
			javax.xml.parsers.DocumentBuilderFactory factory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
			javax.xml.parsers.DocumentBuilder docbuilder = factory.newDocumentBuilder();
			org.w3c.dom.Document doc = docbuilder.newDocument();
			doc.setXmlStandalone(true);
			Element root = doc.createElement("root");
			doc.appendChild(root);
			root.appendChild(new Sensor().generateXMLElement(doc));
			root.appendChild(new Sensor().generateXMLElement(doc));
			root.appendChild(new Sensor().generateXMLElement(doc));
			doc.setDocumentURI("/home/moon/workspace/ProtokolSimulator/src/test.xml");

	        // Prepare the DOM document for writing
            Source source = new DOMSource(doc);
    
    
            // Write the DOM document to the file
            TransformerFactory fact = TransformerFactory.newInstance();
            fact.setAttribute("indent-number", new Integer(4));
            Result result = new StreamResult(new OutputStreamWriter(System.out, "utf-8"));
            Transformer xformer = fact.newTransformer();
            xformer.setOutputProperty(OutputKeys.INDENT, "yes");
            xformer.transform(source, result);
			 
			
		} catch (Exception e) {
			System.out.flush();
			System.err.flush();
			e.printStackTrace();
		}*/
	}
}