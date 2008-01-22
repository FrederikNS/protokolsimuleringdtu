package exceptions;

/**
 * This exception is thrown when a label (sensor ID) is already assigned.
 * @author Niels Thykier
 */
public class LabelAlreadyAssignedException extends Exception {
	/**
	 * Required by Serializable
	 */
	private static final long serialVersionUID = -5925696577160575198L;

	/**
	 * A default empty constructor for an exception.
	 */
	public LabelAlreadyAssignedException() {
	}

	/**
	 * A default constructor for an exception.
	 * @param arg0
	 */
	public LabelAlreadyAssignedException(String arg0) {
		super(arg0);
	}

	/**
	 * A default constructor for an exception.
	 * @param arg0
	 */
	public LabelAlreadyAssignedException(Throwable arg0) {
		super(arg0);
	}
}
