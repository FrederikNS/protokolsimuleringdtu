package gui;

import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class CPMenuBar implements GuiInterface {
	public CPMenuBar(ControlPanelFrame frame, ActionListener actionListener){
		
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem newMenuItem = new JMenuItem("New");
		JMenuItem openMenuItem = new JMenuItem("Open...");
		JMenuItem saveMenuItem = new JMenuItem("Save");
		JMenuItem saveAsMenuItem = new JMenuItem("Save As...");
		JMenuItem quitMenuItem = new JMenuItem("Quit"); 
		
		newMenuItem.addActionListener(actionListener);
		newMenuItem.setActionCommand(String.valueOf(MENU_NEW));
		openMenuItem.addActionListener(actionListener);
		openMenuItem.setActionCommand(String.valueOf(MENU_OPEN));
		saveMenuItem.addActionListener(actionListener);
		saveMenuItem.setActionCommand(String.valueOf(MENU_SAVE));
		saveAsMenuItem.addActionListener(actionListener);
		saveAsMenuItem.setActionCommand(String.valueOf(MENU_SAVE_AS));
		quitMenuItem.addActionListener(actionListener);
		quitMenuItem.setActionCommand(String.valueOf(MENU_QUIT));
		
		frame.setJMenuBar(menuBar);
		menuBar.add(fileMenu);
		fileMenu.add(newMenuItem);
		fileMenu.add(openMenuItem);
		fileMenu.add(saveMenuItem);
		fileMenu.add(saveAsMenuItem);
		fileMenu.add(quitMenuItem);	
	}
}
