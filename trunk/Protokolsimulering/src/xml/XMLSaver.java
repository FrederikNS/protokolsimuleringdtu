package xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.TreeSet;
import java.util.zip.GZIPOutputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import math.Scaling;
import nodes.Sensor;
import notification.Note;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import turns.TurnController;

/**
 * Handles the saving of XML-files.
 * Created (in part) together with team 11
 * @author Niels Thykier
 */
public class XMLSaver {
	
	/**
	 * Saves a list of sensors, though if a turn is running the sensors in that will be saved instead.
	 * @param file The file to save it in.
	 */
	public static void saveSensorList(File file) {
		Document doc;
		if(file.exists()) {
			if(!file.isFile()) {
				Note.sendNote(Note.ERROR, "Attempting to save "+ file.getName() +  " failed: " + file.getName() + " is not a file");
				Note.sendNote(Note.DEBUG, "Save-failure, is Dir: " + file.isDirectory());			
				return;
			}
		}
		try {
			doc = generateXMLDocument(file, true);
		} catch (ParserConfigurationException e) {
			Note.sendNote(Note.ERROR, "Attempting to save "+ file.getName() +  " failed: Could not generate the DOM Document / Missing a XML handler.");
			Note.sendNote(Note.DEBUG, "Save-failure: " + e.getLocalizedMessage());
			return;
		}
		Element rootElement = doc.createElement("main");
	
		
			Element fieldNode = doc.createElement("field");
			Element fieldXElement = doc.createElement("x");
			fieldXElement.appendChild(doc.createTextNode(String.valueOf(Scaling.getPicXMax())));
			Element fieldYElement = doc.createElement("y");
			fieldYElement.appendChild(doc.createTextNode(String.valueOf(Scaling.getPicYMax())));
			fieldNode.appendChild(fieldXElement);
			fieldNode.appendChild(fieldYElement);
			rootElement.appendChild(fieldNode);
		
		rootElement.appendChild(Sensor.generateXMLGeneralData(doc));
		TurnController controller = TurnController.getInstance();
		if(controller.getCurrentTurn() != null) {
			rootElement.appendChild(controller.generateXMLElement(doc));
		} else { 
			for(Sensor sen : new TreeSet<Sensor>(Sensor.idToSensor.values())) {
				rootElement.appendChild(sen.generateXMLElement(doc));
			}
		}
		
		
		
		doc.appendChild(rootElement);
		try {
			outputXMLFile(doc, file);
		} catch (UnsupportedEncodingException e) {
			Note.sendNote(Note.ERROR, "Attempting to save "+ file.getName() +  " failed: Encoding not supported !");
			Note.sendNote(Note.DEBUG, "Save-failure: " + e.getLocalizedMessage());
			return;
		} catch (TransformerFactoryConfigurationError e) {
			Note.sendNote(Note.ERROR, "Attempting to save "+ file.getName() +  " failed: Could not create/find an DOM XML handler!");
			Note.sendNote(Note.DEBUG, "Save-failure: " + e.getLocalizedMessage());
			return;
		} catch (TransformerException e) {
			Note.sendNote(Note.ERROR, "Attempting to save "+ file.getName() +  " failed: Could not save the file.");
			Note.sendNote(Note.DEBUG, "Save-failure: " + e.getLocalizedMessage());
			return;
		} catch (FileNotFoundException e) {
			Note.sendNote(Note.ERROR, "Attempting to save "+ file.getName() +  " failed: Could not create the file!");
			Note.sendNote(Note.DEBUG, "Save-failure: " + file.getAbsolutePath() + " could not be created.");
			return;
		} catch (IOException e) {
			System.err.println(e);
			return;
		}
		Note.sendNote("File, " + file.getName() + ", was successfully saved. Size: " + ((file.length()>>10)+1) + " kBs");
	}
	
	
	/**
	 * Generates the DOM Document of the XML file to be stored.
	 * @param file The file to be stored.
	 * @param standAlone true if the file is a standalone file.
	 * @return The DOM Document to build the XML node tree.
	 * @throws ParserConfigurationException If no XML-parser could be created.
	 */
	protected static Document generateXMLDocument(File file, boolean standAlone) throws ParserConfigurationException {
		javax.xml.parsers.DocumentBuilderFactory factory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
		javax.xml.parsers.DocumentBuilder docbuilder = factory.newDocumentBuilder();
		org.w3c.dom.Document doc = docbuilder.newDocument();
		doc.setXmlStandalone(standAlone);
		doc.setDocumentURI(file.getPath());
		return doc;
	}
	
	
	/**
	 * Stores the generated DOM Document into the file.
	 * @param doc The DOM document to save.
	 * @param file The file to save it in. 
	 * @throws TransformerFactoryConfigurationError If no outputter could be created.
	 * @throws TransformerException If the outputter could not write the file.
	 * @throws IOException Other IO-related exceptions.
	 */
	protected static void outputXMLFile(Document doc, File file) throws TransformerFactoryConfigurationError, TransformerException, IOException {
        // Prepare the DOM document for writing
        Source source = new DOMSource(doc);

        // Write the DOM document to the file
        TransformerFactory fact = TransformerFactory.newInstance();
        fact.setAttribute("indent-number", new Integer(4));
        //GZIPOutputStream outfile= new GZIPOutputStream(new FileOutputStream(file, false));
        OutputStream outfile = new FileOutputStream(file, false);
        if(file.getName().endsWith(".gz")) {
        	outfile = new GZIPOutputStream(outfile);
        }
        Result result = new StreamResult(new OutputStreamWriter(outfile, "utf-8"));
        Transformer xformer;
		try {
			xformer = fact.newTransformer();
		} catch (TransformerConfigurationException e) {
			xformer = TransformerFactory.newInstance().newTransformer();
			
		}
        xformer.setOutputProperty(OutputKeys.INDENT, "yes");
        xformer.transform(source, result);
        outfile.flush();
        outfile.close();
        
	}
}
