package gui;

import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * @author Frederik Nordahl Sabroe
 *
 */
public class CPMenuBar implements GUIConstants {
	/**
	 * Constructor of the CPMenuBar
	 * @param frame The ControlPanelFrame to which the menu should be attached.
	 * @param actionListener The ActionListener for all the menu items.
	 */
	public CPMenuBar(ControlPanelFrame frame, ActionListener actionListener){
		
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem newMenuItem = new JMenuItem("New");
		JMenuItem openMenuItem = new JMenuItem("Open...");
		GUIReferences.saveMenuItem = new JMenuItem("Save");
		JMenuItem saveAsMenuItem = new JMenuItem("Save As...");
		JMenuItem quitMenuItem = new JMenuItem("Quit"); 
		JMenu editMenu = new JMenu("Edit");
		JMenuItem preferencesMenuItem = new JMenuItem("Perferences");
		
		newMenuItem.addActionListener(actionListener);
		newMenuItem.setActionCommand(String.valueOf(MENU_NEW));
		openMenuItem.addActionListener(actionListener);
		openMenuItem.setActionCommand(String.valueOf(MENU_OPEN));
		GUIReferences.saveMenuItem.addActionListener(actionListener);
		GUIReferences.saveMenuItem.setActionCommand(String.valueOf(MENU_SAVE));
		saveAsMenuItem.addActionListener(actionListener);
		saveAsMenuItem.setActionCommand(String.valueOf(MENU_SAVE_AS));
		GUIReferences.saveMenuItem.setEnabled(false);
		quitMenuItem.addActionListener(actionListener);
		quitMenuItem.setActionCommand(String.valueOf(MENU_QUIT));
		preferencesMenuItem.addActionListener(actionListener);
		preferencesMenuItem.setActionCommand(String.valueOf(MENU_PREFERENCES));
		
		frame.setJMenuBar(menuBar);
		menuBar.add(fileMenu);
		fileMenu.add(newMenuItem);
		fileMenu.add(openMenuItem);
		fileMenu.add(GUIReferences.saveMenuItem);
		fileMenu.add(saveAsMenuItem);
		fileMenu.add(quitMenuItem);	
		menuBar.add(editMenu);
		editMenu.add(preferencesMenuItem);
	}
}
