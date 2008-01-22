package gui;

import java.awt.Dimension;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFrame;

import nodes.Sensor;

/**
 * @author Frederik Nordahl Sabroe
 *
 */
public class ViewPort extends JFrame implements GUIConstants{
	
	

	/**
	 * Serialized ID
	 */
	private static final long serialVersionUID = 1960058377833441994L;
	/**
	 * Contains the Graphics painter
	 */
	private VPGraphicsPainter graphicsPainter;
	/**
	 * The title of the window (when unchanged: the filename, when changed: a star followed by the filename
	 */
	String halfTitle;
	
	/**
	 * The View Port for showing the network
	 * @param title the title the window should have
	 */
	public ViewPort(String title){
		super(title);
		halfTitle = title;
		int screenHeight = 600;
		int screenWidth =  600;
		setSize(new Dimension(screenWidth,screenHeight));
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setLocation(GUIReferences.controlPanelPane.getWidth()+10, 0);
		addWindowListener(GUIReferences.windowListener);
		graphicsPainter = new VPGraphicsPainter();
		this.getContentPane().add(graphicsPainter);
		this.setName(String.valueOf(WINDOW_VIEW_PORT));
		setVisible(true);
	}
	
	/**
	 * Method to grab the Graphics Painter
	 * @return The Graphics Painter
	 */
	public VPGraphicsPainter getGraphicsPainter(){
		return graphicsPainter;
	}
	
	/**
	 * 
	 */
	public static void disposeViewPort(){
		Sensor.disposeAllSensors();
		if(GUIReferences.viewPort!=null){
			GUIReferences.listener.stopTimer();
			GUIReferences.viewPort.dispose();
			GUIReferences.viewPort = null;
		}
	}
	
	/**
	 * Updates the title in the View Port without the mouses current coordinates
	 */
	public void updateTitleCoordinates(){
		setTitle((GUIReferences.saveMenuItem.isEnabled()?"*":"")+this.halfTitle);
	}
	
	/**
	 * Updates the title in the View Port with the mouses current coordinates
	 * @param x The X coordinate of the mouse
	 * @param y The Y coordinate of the mouse
	 */
	public void updateTitleCoordinates(int x,int y){
		setTitle("("+x+","+y+") "+(GUIReferences.saveMenuItem.isEnabled()?"*":"")+this.halfTitle);
	}

	/**
	 * Grabs the open files absolute path
	 * @param openFile The open file
	 */
	public ViewPort(File openFile){
		this(openFile.getAbsolutePath());
	}	
}