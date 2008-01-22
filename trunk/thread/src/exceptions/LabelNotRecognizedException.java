package exceptions;

/**
 * This exception is thrown when a label (sensor ID) is not recognized.
 * @author Niels Thykier
 */
public class LabelNotRecognizedException extends Exception {

	/**
	 * Required by Serializable
	 */
	private static final long serialVersionUID = -5925696577160575197L;

	/**
	 * A default empty constructor.
	 */
	public LabelNotRecognizedException() {
	}

	/**
	 * A default constructor.
	 * @param arg0
	 */
	public LabelNotRecognizedException(String arg0) {
		super(arg0);
	}

	/**
	 * A default constructor.
	 * @param arg0
	 */
	public LabelNotRecognizedException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * A default constructor.
	 * @param arg0
	 * @param arg1
	 */
	public LabelNotRecognizedException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
