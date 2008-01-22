package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import notification.Note;
import notification.NotificationListener;

/**
 * This class is GUI "console" containing a list of messages from the
 * program.
 * @author Frederik Nordahl Sabroe
 * @author Niels Thykier
 */
public class Console extends JFrame implements NotificationListener, GUIConstants{

	/**
	 * Serialized ID
	 */
	private static final long serialVersionUID = 1925062183376423627L;
	private int note = Note.INFORMATION | Note.ERROR | Note.WARNING;
	/**
	 * Contains all the notes which should be printed in the console
	 */
	private ArrayList<Note> allNotes = new ArrayList<Note>();
	/**
	 * The text area for the console
	 */
	private JTextArea console;

	/**
	 * Inits the console window. (Layout and stuff).
	 * @return The console itself (as convience)
	 */
	public Console init() {
//		The console is created
		this.setName(String.valueOf(WINDOW_CONSOLE));
		setTitle("Console");
		setLocation(0,610);
		setMinimumSize(new Dimension(800,150));
		console = new JTextArea();
		FlowLayout consolePanelLayout = new FlowLayout(FlowLayout.LEFT,0,0);
		GridLayout consoleScrollerPanelLayout = new GridLayout(0,1);
		JPanel consolePanel = new JPanel();
		JPanel consoleScrollerPanel = new JPanel();
		JScrollPane consoleScroller = new JScrollPane(consolePanel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		//Register as NoteListener
		Note.registerListener(this);
		addWindowListener(GUIReferences.windowListener);

		consoleScrollerPanel.setLayout(consoleScrollerPanelLayout);
		consolePanel.setLayout(consolePanelLayout);
		add(consoleScrollerPanel);
		consoleScrollerPanel.add(consoleScroller);
		consolePanel.add(console);
		console.setEditable(false);
		console.setBackground(new Color(0x00EEEEEE));
		
		pack();
		return this;
	}

	/**
	 * Updates the shown notes by altering the params.
	 * @param bitFlag The bit flag to alter 
	 * @see notification.NoteConstants
	 */
	public void toggleShowFlag(int bitFlag){
		if(0 != (bitFlag & note)) {
			note &= ~bitFlag;
		} else {
			note |= bitFlag;
		}
		updateShownNoteList();
	}
	
	/**
	 * Internal command to update what notes are being showed.
	 */
	protected void updateShownNoteList() {
		console.setText("");
		Note nt = null;
		int size = allNotes.size();
		for(int i = 0; i < size ; i++) {
			nt = allNotes.get(i);
			if(0 != (nt.getType() & note)) {
				console.append(nt.getMessage()+"\n");
			}
		}
	}

	public void note(Note newNote) {
		allNotes.add(newNote);
		if(0 != (newNote.getType() & note)) {
			console.append(newNote.getMessage() +"\n");
		}
		if(0 != (newNote.getType() & Note.ERROR) && !this.isVisible()) {
			//If receiving an error, show self.
			this.setVisible(true);
		}
	}
}
