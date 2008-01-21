package notification;

/**
 * Constant list for the Note class. NoticationListeners will have this implemented automatically.
 * @author Niels Thykier
 */
public interface NoteConstants {
	public static final int ALL_MESSAGES  = 0x0000ffff;
	public static final int NO_MESSAGES   = 0x00000000;
	public static final int INFORMATION   = 0x00000001;
	public static final int WARNING  	  = 0x00000002;
	public static final int ERROR	  	  = 0x00000004;
	public static final int DEBUG	 	  = 0x00000008;
}
