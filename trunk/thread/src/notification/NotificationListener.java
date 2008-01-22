package notification;

/**
 * Objects that should receive live data from the Sensor network.
 * @author Niels Thykier
 */
public interface NotificationListener extends NoteConstants {
	/**
	 * This method is called whenever a new note is broadcasted.
	 * @param newNote The new note.
	 */
	public void note(Note newNote);
}
