package xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * For classes that have a convient self-xmlizing method for saving 
 * them as an xml-file.
 * @author Niels Thykier
 */
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
