package xml;

import java.io.PrintStream;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXContentHandler extends DefaultHandler {
	int level = 0;
	boolean[] hasContents;
	PrintStream out = System.out;
	boolean doIndent = false;

	@Override
	public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
		String contents = new String(arg0, arg1, arg2).trim();
		if(contents.equals("")) {
			return;
		}
		if(!hasContents[level]) {
			println(">");
			hasContents[level] = true;
		}
		
		print(contents);
		println();
	}

	@Override
	public void endDocument() throws SAXException {
		level = 0;
		println("Document End");
		hasContents = null;
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if(hasContents[level--]) {
			println("</"+ qName + ">");
		} else {
			println("/>");
		}
	}

	@Override
	public void ignorableWhitespace(char[] arg0, int arg1, int arg2) throws SAXException {
		println("WhiteSpace detected:" + new String(arg0, arg1, arg2));
	}
	
	@Override
	public void skippedEntity(String arg0) throws SAXException {
		println("Entity: " + arg0);
	}

	@Override
	public void startDocument() throws SAXException {
		println("Document start");
		hasContents = new boolean[10];
		hasContents[0] = true;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		if(!hasContents[level]) {
			hasContents[level] = true;
			println(">");
		}
		print("<" + qName);
		int amount = atts.getLength();
		for(int i = 0 ; i < amount ; i++) {
			print(" " +atts.getQName(i) + "='" + atts.getValue(i) + "'");
		}
		++level;
		hasContents[level] = false;
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#error(org.xml.sax.SAXParseException)
	 */
	@Override
	public void error(SAXParseException arg0) throws SAXException {
		
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#fatalError(org.xml.sax.SAXParseException)
	 */
	@Override
	public void fatalError(SAXParseException arg0) throws SAXException {
		
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#warning(org.xml.sax.SAXParseException)
	 */
	@Override
	public void warning(SAXParseException arg0) throws SAXException {
		
	}

	public void println() {
		println("");
	}
	public void println(String text) {
		print(text + "\n");
		doIndent = true;
	}
	
	public void print(String text) {
		
		String indent = "";
		if(doIndent) {
			for(int i = 0 ; i < level ; i++) {
				indent += "    ";
			}
		}
		out.print(indent + text);
		doIndent = false;
	}

}
