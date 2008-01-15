package xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Niels Thykier
 * @deprecated
 */
@Deprecated
public class SAXContentHandler extends DefaultHandler {
	private int level = 0;
	private StringBuffer buff;
	private int type[];
	
	private static final int TYPE_IGNORE = 0;
/*	private static final int TYPE_COORD_MIN_X = 1;
	private static final int TYPE_COORD_MAX_X = 2;
	private static final int TYPE_COORD_MIN_Y = 3;
	private static final int TYPE_COORD_MAX_Y = 4;*/
	private static final int TYPE_SENSOR    = 5;
	private static final int TYPE_LOCATION = 6;
	private static final int TYPE_COORD_Y = 7;
	private static final int TYPE_COORD_X = 8;
	
	@Override
	public void characters(char[] str, int offset, int len) throws SAXException {
		System.out.println(new String(str, offset, len) + " of: " + offset  + " len: " + len);
		char[] array = str;
		if(buff != null) {
			for(int i = offset ; i < len ; i++) {
				System.out.print(str[i]);
				switch((byte)array[i]){
				case '\n':
				case '\r':
				case '\0':
				case '\t':
				case '\f':
				case '\b':
					//Ignored!
					break;
				case  ' ':
					if(i + 1 < len && array[i+i] != ' ') {
						//Ignore all but one whitespace character.
						buff.append(' ');
					}
				default:
					buff.append(array[i]);
					break;
				}
			}
			
		}
	}

	@Override
	public void endDocument() throws SAXException {}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if(type == null) {
			return;
		}
		switch(type[level]) {
		case TYPE_COORD_X:
		case TYPE_COORD_Y:
			System.out.println("'ello, number: " + buff.toString());
			break;
		case TYPE_IGNORE:
			break;
		}
		buff = null;
		if(--level == -1) {
			type = null;
		}
	}

	@Override
	public void ignorableWhitespace(char[] arg0, int arg1, int arg2) throws SAXException {}
	
	@Override
	public void skippedEntity(String arg0) throws SAXException {}

	@Override
	public void startDocument() throws SAXException {}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		switch(qName.charAt(0)) {
		case 's':
			if(qName.equals("sensor")) {
				System.out.println("Sensor tag detected");
				if(type != null) {
					throw new SAXException("Unexpected Sensor Tag.");
				}
				type = new int[6];
				type[0] = TYPE_SENSOR;
			}
			break;
		case 'l':
			if(qName.equals("location")) {
				if(type[level] != TYPE_SENSOR || type.length == level) {
					throw new SAXException("Unexpected Location Tag.");
				}
				++level;
				type[level] = TYPE_LOCATION;
			}
			break;
		case 'y':
		case 'x':
			if(type == null || type[level] != TYPE_LOCATION || type.length == level) {
				throw new SAXException("Unexpected x or y Tag.");
			}
			++level;
			if(qName.charAt(0) == 'x') {
				type[level] = TYPE_COORD_X;
			} else {
				type[level] = TYPE_COORD_Y;
			}
			buff = new StringBuffer(10);
			break;
		default:
			return;
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
		buff = null;
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#warning(org.xml.sax.SAXParseException)
	 */
	@Override
	public void warning(SAXParseException arg0) throws SAXException {
		
	}

}
