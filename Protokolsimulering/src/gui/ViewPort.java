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

	private static final long serialVersionUID = 1960058377833441994L;
	private VPGraphicsPainter graphicsPainter;
	public ViewPort(String title){
		super(title);
		int screenHeight = /*(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()*/ 600; //TODO
		int screenWidth = /*(int) Toolkit.getDefaultToolkit().getScreenSize().getWidth()-200*/ 600;
		setSize(new Dimension(screenWidth,screenHeight));
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setLocation(GUIReferences.controlPanelPane.getWidth()+10, 0);
		addWindowListener(GUIReferences.windowListener);
		graphicsPainter = new VPGraphicsPainter();
		this.getContentPane().add(graphicsPainter);
		this.setName(String.valueOf(WINDOW_VIEW_PORT));
		setVisible(true);
	}
	
	public VPGraphicsPainter getGraphicsPainter(){
		return graphicsPainter;
	}
	
	public static void disposeViewPort(){
		Sensor.disposeAllSensors();
		if(GUIReferences.sensorNetwork!=null){
			GUIReferences.sensorNetwork.dispose();
			GUIReferences.sensorNetwork = null;
		}
	}
	

	public ViewPort(File openFile){
		this(openFile.getAbsolutePath());
	}	

}