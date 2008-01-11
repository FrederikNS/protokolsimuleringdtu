package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

public class ViewFrame extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1960058377833441994L;

	public ViewFrame(String fileName){
		super(fileName);
		setSize(new Dimension(600,600));
		//setLocation(posX, posY);
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent arg0) {
		switch(Integer.parseInt(arg0.getActionCommand())) {
		
		}
	}
}
