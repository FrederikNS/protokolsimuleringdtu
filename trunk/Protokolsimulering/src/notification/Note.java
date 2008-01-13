package notification;

import java.util.ArrayList;

/**
 * Notes are (usually) instantly broadcasted to all registered NotificationListeners and
 * contain live data from the Sensor network. Notes are for the user and not the program.
 * @author Niels Thykier
 */
public class Note implements NoteConstants {
	/**
	 * Type of message, used by receivers that filters notes.
	 */
	private int type;
	/**
	 * The message to be displayed.
	 */
	private String message;
	/**
	 * All registered listeners
	 */
	private static ArrayList<NotificationListener> listeners = new ArrayList<NotificationListener>();
	//private Note() {}
	
	/**
	 * Generate a new message auto-broadcasted Note with the type INFORMATION
	 * @param message The text
	 * @throws NullPointerException if the message is null
	 */
	public Note(String message) {
		this(INFORMATION, message, true);
	}
	
	/**
	 * Generate a new message auto-broadcasted Note.
	 * @param message The text
	 * @param type The type of the message
	 * @throws NullPointerException if the message is null
	 */
	public Note(String message, int type) {
		this(type, message, true);
	}
	
	/**
	 * Generate a new message auto-broadcasted Note.
	 * @param type The type of the message
	 * @param message The text
	 * @throws NullPointerException if the message is null
	 */	
	public Note(int type, String message) {
		this(type, message, true);
	}
	/**
	 * Generate a new message auto-broadcasted Note.
	 * @param message The text
	 * @param type The type of the message
	 * @param broadcast true if the message should be auto-broadcasted.
	 * @throws NullPointerException if the message is null
	 */
	public Note(String message, int type, boolean broadcast) {
		this(type, message, broadcast);
	}
	
	/**
	 * Generate a new message auto-broadcasted Note.
	 * @param type The type of the message
	 * @param message The text
	 * @param broadcast true if the message should be auto-broadcasted.
	 * @throws NullPointerException if the message is null
	 */
	public Note(int type, String message, boolean broadcast) {
		this.type = type;
		if(message == null) {
			throw new NullPointerException("Message cannot be null");
		}
		this.message = message;
		if(broadcast) {
			broadcast();
		}
	}
	
	/**
	 * Registers a new NotificationListener
	 * @param newListener The new listener to register.
	 * @return true, if it could be registered.
	 */
	public static boolean registerListener(NotificationListener newListener) {
		return listeners.add(newListener);
	}
	
	/**
	 * Unregisters a NotificationListener
	 * @param listener The listener to unregister.
	 * @return true, if it could be unregistered.
	 */
	public static boolean unregisterListener(NotificationListener listener) {
		return listeners.remove(listener);
	}
	
	/**
	 * Broadcasts the note to all listeners.
	 */
	public void broadcast() {
		int size = listeners.size();
		for(int i = 0 ; i < size; i++) {
			listeners.get(i).note(this);
		}
	}
	
	/**
	 * Get the type of Note.
	 * @return The type.
	 */
	public int getType() {
		return type;
	}
	
	/**
	 * Get the message of the Note
	 * @return The message.
	 */
	public String getMessage() {
		return message;
	}
}
