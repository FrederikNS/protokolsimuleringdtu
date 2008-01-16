package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.JFrame;

/**
 * @author Frederik Nordahl Sabroe
 *
 */
public class ViewPort extends JFrame{

	private static final long serialVersionUID = 1960058377833441994L;
	
	public ViewPort(String title,int posX,int posY){
		super(title);
		int screenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		int screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		setSize(new Dimension((screenWidth-200),screenHeight));
//		setSize(new Dimension(601,601));
		setLocation(posX, posY);
		VPGraphicsPainter graphicsPainter = new VPGraphicsPainter();
		this.getContentPane().add(graphicsPainter);
		setVisible(true);
		
	}

	public ViewPort(File openFile,int posX,int posY){
		this(openFile.getAbsolutePath(),posX,posY);
	}
	
	/*public void actionPerformed(ActionEvent arg0) {
		switch(Integer.parseInt(arg0.getActionCommand())) {
		}
	}*/

	
}