package notification;

/**
 * Constant list for the Note class. NoticationListeners will have this implemented automatically.
 * @author Niels Thykier
 */
public interface NoteConstants {
	/**
	 * Bit-flag filter for all message types.
	 */
	public static final int ALL_MESSAGES  = 0x0000ffff;
	/**
	 * Bit-flag filter for no message types
	 */
	public static final int NO_MESSAGES   = 0x00000000;
	/**
	 * Bit-flag for "information" type messages.
	 */
	public static final int INFORMATION   = 0x00000001;
	/**
	 * Bit-flag for "warning" type messages.
	 */
	public static final int WARNING  	  = 0x00000002;
	/**
	 * Bit-flag for "error" type messages.
	 */
	public static final int ERROR	  	  = 0x00000004;
	/**
	 * Bit-flag for "debug" type messages.
	 */
	public static final int DEBUG	 	  = 0x00000008;
}
