package xml;

import java.io.PrintStream;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXContentHandler extends DefaultHandler {
	private int level = 0;
	private int[] contents;
	private PrintStream out = System.out;
	private boolean doIndent = false;
	public static final int NONE = 0;
	public static final int UNKNOWN = 5;
	public static final int INT = 1;
	public static final int BOOLEAN = 2;
	public static final int STRING = 3;
	public static final int DOUBLE = 4;
	public static final int TAG = 5;

	@Override
	public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
		String input = new String(arg0, arg1, arg2).trim();
		if(input.equals("")) {
			return;
		}
		switch(contents[level]) {
		case INT:
			print(">" + Integer.parseInt(input));
			break;
		case NONE:
			println(">");
			contents[level] = UNKNOWN;
		case UNKNOWN:
			println(input);
			break;
		}

	}

	@Override
	public void endDocument() throws SAXException {
		level = 0;
		println("Document End");
		contents = null;
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		switch(contents[level]) {
		case INT:
			--level;
			break;
		default:
			if(contents[level-- +1] == INT || contents[level] != NONE) {
				println("</"+ qName + ">");
			} else {
				println("/>");
			}
			break;
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
		contents = new int[20];
		contents[0] = TAG;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
	
		if(qName.equalsIgnoreCase("int")) {
			contents[level] = STRING;
			contents[++level] = INT;
		} else {
			if(contents[level] == NONE) {
				contents[level] = TAG;
				println(">");
			}
			print("<" + qName);
			int amount = atts.getLength();
			for(int i = 0 ; i < amount ; i++) {
				print(" " +atts.getQName(i) + "='" + atts.getValue(i) + "'");
			}
			contents[++level] = NONE;
		}
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
		contents = null;
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
