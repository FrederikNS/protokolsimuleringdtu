package exceptions;

/**
 * This exception will be thrown when there are any problems with the XML code.
 * @author Niels Thykier
 */
public class XMLParseException extends Exception {

	/**
	 * Required by Serializable.
	 */
	private static final long serialVersionUID = -723270251801910258L;

	/**
	 * A default constructor.
	 */
	public XMLParseException() {
		super();
	}

	/**
	 * A default constructor.
	 * @param arg0
	 * @param arg1
	 */
	public XMLParseException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * A default constructor.
	 * @param arg0
	 */
	public XMLParseException(String arg0) {
		super(arg0);
	}

	/**
	 * A default constructor.
	 * @param arg0
	 */
	public XMLParseException(Throwable arg0) {
		super(arg0);
	}

}
