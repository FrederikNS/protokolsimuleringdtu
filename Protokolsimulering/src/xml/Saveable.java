package xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface Saveable {

	/**
	 * Generates the XML Element that the class can be loaded from.
	 * The element (which is returned) is NOT attached to the document, 
	 * merely created. 
	 * @param doc The Document of the XML file.
	 * @return The element with the whole substructure completed.
	 */
	public Element generateXMLElement(Document doc);
}
