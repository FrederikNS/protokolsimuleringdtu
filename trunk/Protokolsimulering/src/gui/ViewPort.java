package gui;

import java.awt.Dimension;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFrame;

import nodes.SplitField;

/**
 * @author Frederik Nordahl Sabroe
 *
 */
public class ViewPort extends JFrame{

	private static final long serialVersionUID = 1960058377833441994L;
	private VPGraphicsPainter graphicsPainter;
	public ViewPort(String title,int posX,int posY){
		super(title);
		int screenHeight = /*(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()*/ 601;
		int screenWidth = /*(int) Toolkit.getDefaultToolkit().getScreenSize().getWidth()-200*/ 601;
		setSize(new Dimension(screenWidth,screenHeight));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLocation(posX, posY);
		graphicsPainter = new VPGraphicsPainter();
		this.getContentPane().add(graphicsPainter);
		setVisible(true);
		
	}
	
	public void generateField(int w,int h){
		graphicsPainter.createNewField(w, h);
	}

	public ViewPort(File openFile,int posX,int posY){
		this(openFile.getAbsolutePath(),posX,posY);
	}	
	
	public SplitField getField() {
		return graphicsPainter.getField();
	}
}