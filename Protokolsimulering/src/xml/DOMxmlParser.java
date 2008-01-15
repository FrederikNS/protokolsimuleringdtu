package xml;

import java.io.File;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

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
				new FileInputStream("/home/moon/workspace/ProtokolSimulator/src/text.xml"),
					"UTF-8")));

	}
	
	public static void parse(File xmlFile) throws UnsupportedEncodingException, FileNotFoundException, ParserConfigurationException, SAXException, IOException {
		Document result = new DOMxmlParser(xmlFile).doc;
		result.getFirstChild();
	}
}
