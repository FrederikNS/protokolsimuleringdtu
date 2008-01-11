package exceptions;

public class LabelNotRecognizedException extends Exception {

	/**
	 * Required by Serializable
	 */
	private static final long serialVersionUID = -5925696577160575197L;

	public LabelNotRecognizedException() {
	}

	public LabelNotRecognizedException(String arg0) {
		super(arg0);
	}

	public LabelNotRecognizedException(Throwable arg0) {
		super(arg0);
	}

	public LabelNotRecognizedException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
