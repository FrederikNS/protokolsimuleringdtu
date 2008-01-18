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

public class Console extends JFrame implements NotificationListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1925062183376423627L;
	private int note = ALL_MESSAGES;
	private ArrayList<Note> shownNotes = new ArrayList<Note>();
	private ArrayList<Note> allNotes = new ArrayList<Note>();
	private JTextArea console;

	public Console(){
	}
	
	public Console(JFrame frame){
	}
	
	public Console init() {
//		The console is created
		setTitle("Console");
		setLocation(0,600);
		setMinimumSize(new Dimension(803,150));
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

	protected void updateShownNoteList() {
		shownNotes.clear();
		shownNotes = new ArrayList<Note>();
		console.setText("");
		Note nt = null;
		int size = allNotes.size();
		for(int i = 0; i < size ; i++) {
			nt = allNotes.get(i);
			if(0 != (nt.getType() & note)) {
				shownNotes.add(nt);
				console.append(nt.getMessage());
			}
		}
	}

	public void note(Note newNote) {
		allNotes.add(newNote);
		if(0 != (newNote.getType() & note)) {
			shownNotes.add(newNote);
			console.append(newNote.getMessage() +"\n");
		}
	}
}
