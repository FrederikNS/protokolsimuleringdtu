package exceptions;

public class LabalAlreadyAssignedException extends Exception {
	/**
	 * Required by Serializable
	 */
	private static final long serialVersionUID = -5925696577160575198L;

	public LabalAlreadyAssignedException() {
	}

	public LabalAlreadyAssignedException(String arg0) {
		super(arg0);
	}

	public LabalAlreadyAssignedException(Throwable arg0) {
		super(arg0);
	}
}
