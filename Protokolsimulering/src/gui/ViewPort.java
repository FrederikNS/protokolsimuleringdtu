package gui;

import java.io.File;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

public class ViewPort extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1960058377833441994L;
	
	public ViewPort(String title,int posX,int posY){
		super(title);
		setSize(new Dimension(600,600));
		setLocation(posX, posY);
		setVisible(true);
	}

	public ViewPort(File openFile,int posX,int posY){
		this(openFile.getName(),posX,posY);
	}
	
	public void actionPerformed(ActionEvent arg0) {
		switch(Integer.parseInt(arg0.getActionCommand())) {
		
		}
	}
}
