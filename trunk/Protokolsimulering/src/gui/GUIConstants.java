package gui;

/**
 * List of constants related to the GUI.
 * @author Frederik Nordahl Sabroe
 */
public interface GUIConstants {
	public final static int MENU_NEW = 1;
	public final static int MENU_OPEN = 2;
	public final static int MENU_SAVE = 3;
	public final static int MENU_SAVE_AS = 4;
	public final static int MENU_QUIT = 0;
	
	public final static int BUTTON_GENERATE = 101;
	public final static int BUTTON_DISABLE = 102;
	public final static int BUTTON_ADD = 103;
	public final static int BUTTON_CLEAR = 104;
	public final static int BUTTON_ENABLE = 105;
	public final static int BUTTON_REMOVE = 106;
	public final static int BUTTON_TO_START = 110;
	public final static int BUTTON_REWIND = 111;
	public final static int BUTTON_STEP_BACKWARD = 112;
	public final static int BUTTON_PLAY_BACKWARDS = 113;
	public final static int BUTTON_STOP = 114;
	public final static int BUTTON_PLAY = 115;
	public final static int BUTTON_NEXT_SENSOR = 116;
	public final static int BUTTON_STEP_FORWARD = 117;
	public final static int BUTTON_FAST_FORWARD = 118;
	
	public final static int CHECKBOX_RADII = 201;
	public final static int CHECKBOX_CONNECTIONS = 202;
	public final static int CHECKBOX_BATTERY = 203;
	public final static int CHECKBOX_ID = 204;
	public final static int CHECKBOX_NEIGHBOURS = 205;
	
	public final static int POPUP_BUTTON_VIEW_SENSOR = 301;
	
	public final static int PLAYBACK_REWIND = -2;
	public final static int PLAYBACK_PLAY_BACKWARDS = -1;
	public final static int PLAYBACK_PAUSE = 0;
	public final static int PLAYBACK_PLAY = 1;
	public final static int PLAYBACK_FAST_FORWARD = 2;
	
	public final static int MODE_SELECT = 0;
	public final static int MODE_DISABLE = 1;
	public final static int MODE_ADD = 2;
	public final static int MODE_ENABLE = 3;
	public final static int MODE_REMOVE = 4;
	
	public final static int VIEW_RADII		  = 0x00000001;
	public final static int VIEW_CONNECTIONS  = 0x00000002;
	public final static int VIEW_NONE  		  = 0x00000000;
	public final static int VIEW_ALL 		  = 0x000000ff;
}
