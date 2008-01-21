package xml;

import exceptions.XMLParseException;
import gui.ControlPanelFrame;
import gui.GUIReferences;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import nodes.GlobalAddressBook;
import nodes.Sensor.SensorImplementation;
import notification.Note;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import turns.TurnController;

/**
 * Semi-static file for loading data from a XML file.
 * @author Niels Thykier
 */
public class DOMxmlParser {
	private Document doc;
	
	private DOMxmlParser(File xmlFile) throws ParserConfigurationException, UnsupportedEncodingException, FileNotFoundException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docbuilder = factory.newDocumentBuilder();
		FileInputStream inputFile = null;
		try {
			inputFile = new FileInputStream(xmlFile);
			doc =  docbuilder.parse(new InputSource(new InputStreamReader(
				inputFile, "UTF-8")));
		} finally {
			if(inputFile != null) {
				inputFile.close();
			}
		}
		

	}
	
	/**
	 * Opens, parses and loads the status from a file.
	 * @param xmlFile The xmlfile to load.
	 * @param panel The panel is used to update the status bar label.
	 */
	public static void parse(File xmlFile, ControlPanelFrame panel) {
		Document result;
		int x = 0, y = 0; // sizes of the field.
		try {
			//TODO generate better notes for errors.
			result = new DOMxmlParser(xmlFile).doc;
			
			SensorImplementation.loadGeneralDataFromXML(result);
			NodeList field = result.getElementsByTagName("field").item(0).getChildNodes();
			Node child;
			for(int i = 0; i < field.getLength() ; i++) {
				child = field.item(i);
				if(child.getNodeName().equals("x")) {
					x = Integer.parseInt(getTextNodeValue(child).trim());
				} else if(child.getNodeName().equals("y")) {
					y = Integer.parseInt(getTextNodeValue(child).trim());
				}
			}
			NodeList turnController = result.getElementsByTagName("turnController");
			if(turnController.getLength() > 0) {
				TurnController.getInstance().loadFromXMLElement(turnController.item(0));
			} else {
				SensorImplementation.loadAllSensorsFromXML(result);
			}
			if(x == 0 || y == 0) {
				throw new XMLParseException("The field size could not be determined.");
			}
		} catch (RuntimeException e){
			Note.sendNote(Note.ERROR, "Loading, " + xmlFile.getName() + " failed!");
			Note.sendNote(Note.DEBUG, "Load fail: " + e );
			return;
		} catch (UnsupportedEncodingException e) {
			Note.sendNote(Note.ERROR, "Loading, " + xmlFile.getName() + " failed!");
			Note.sendNote(Note.DEBUG, "Load fail: " + e );
			return;
		} catch (FileNotFoundException e) {
			Note.sendNote(Note.ERROR, "Loading, " + xmlFile.getName() + " failed!");
			Note.sendNote(Note.DEBUG, "Load fail: " + e );
			return;
		} catch (ParserConfigurationException e) {
			Note.sendNote(Note.ERROR, "Loading, " + xmlFile.getName() + " failed!");
			Note.sendNote(Note.DEBUG, "Load fail: " + e );
			return;
		} catch (SAXException e) {
			Note.sendNote(Note.ERROR, "Loading, " + xmlFile.getName() + " failed!");
			Note.sendNote(Note.DEBUG, "Load fail: " + e );
			return;
		} catch (IOException e) {
			Note.sendNote(Note.ERROR, "Loading, " + xmlFile.getName() + " failed!");
			Note.sendNote(Note.DEBUG, "Load fail: " + e );
			return;
		} catch (XMLParseException e) {
			Note.sendNote(Note.ERROR, "Loading, " + xmlFile.getName() + " failed!");
			Note.sendNote(Note.DEBUG, "Load fail: " + e );
			return;
		}
		GlobalAddressBook.getBook().loadedSensors();
		GUIReferences.generateNewField(x, y, xmlFile.getName());
		Note.sendNote("Data from " + xmlFile.getName() + " was loaded successfully!");
	}
	
	/**
	 * Assuming the first text-node within the node is the primary text node.
	 * This method will find it and retrieve the text from it.
	 * @param node The node you want the text of.
	 * @return The text in the node or null.
	 */
	public static String getTextNodeValue(Node node) {
		NodeList list = node.getChildNodes();
		int length = list.getLength();
		for(int i = 0 ; i < length ; i++) {
			if(list.item(i).getNodeType() == Node.TEXT_NODE) {
				return list.item(i).getNodeValue();
			}
		}
		return null;
	}
}
