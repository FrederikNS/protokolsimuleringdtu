package xml;

import java.io.File;

import nodes.Sensor;
import notification.Note;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import exceptions.XMLParseException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

public class DOMxmlParser {
	private Document doc;
	
	private DOMxmlParser(File xmlFile) throws ParserConfigurationException, UnsupportedEncodingException, FileNotFoundException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docbuilder = factory.newDocumentBuilder();
		doc =  docbuilder.parse(new InputSource(new InputStreamReader(
				new FileInputStream(xmlFile),
					"UTF-8")));

	}
	
	public static void parse(File xmlFile) {
		Document result;
		try {
			//TODO generate better notes for errors.
			result = new DOMxmlParser(xmlFile).doc;
			Sensor.loadFromXML(result);
		} catch (UnsupportedEncodingException e) {
			Note.sendNote(Note.ERROR, "Loading, " + xmlFile.getName() + " failed!");
			Note.sendNote(Note.DEBUG, "Load fail: " + e );
		} catch (FileNotFoundException e) {
			Note.sendNote(Note.ERROR, "Loading, " + xmlFile.getName() + " failed!");
			Note.sendNote(Note.DEBUG, "Load fail: " + e );
		} catch (ParserConfigurationException e) {
			Note.sendNote(Note.ERROR, "Loading, " + xmlFile.getName() + " failed!");
			Note.sendNote(Note.DEBUG, "Load fail: " + e );
		} catch (SAXException e) {
			Note.sendNote(Note.ERROR, "Loading, " + xmlFile.getName() + " failed!");
			Note.sendNote(Note.DEBUG, "Load fail: " + e );
		} catch (IOException e) {
			Note.sendNote(Note.ERROR, "Loading, " + xmlFile.getName() + " failed!");
			Note.sendNote(Note.DEBUG, "Load fail: " + e );
		} catch (XMLParseException e) {
			Note.sendNote(Note.ERROR, "Loading, " + xmlFile.getName() + " failed!");
			Note.sendNote(Note.DEBUG, "Load fail: " + e );
		}
			
		Note.sendNote("Data from " + xmlFile.getName() + " was loaded successfully!");
	}
}
