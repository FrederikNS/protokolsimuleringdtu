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
	public final static int MENU_PREFERENCES = 5;

	public final static int BUTTON_GENERATE = 101;
	public final static int BUTTON_DISABLE = 102;
	public final static int BUTTON_ADD = 103;
	public final static int BUTTON_CLEAR = 104;
	public final static int BUTTON_ENABLE = 105;
	public final static int BUTTON_REMOVE = 106;
	public final static int BUTTON_STOP = 114;
	public final static int BUTTON_PLAY = 115;
	public final static int BUTTON_NEXT_SENSOR = 116;
	public final static int BUTTON_FAST_FORWARD = 118;
	public final static int BUTTON_NEW_CANCEL = 119;
	public final static int BUTTON_NEW_OK = 120;
	public final static int BUTTON_PROMOTE = 121;
	public final static int BUTTON_DEMOTE = 122;
	public final static int BUTTON_COLOR_CANVAS = 134;
	public final static int BUTTON_COLOR_SENSOR = 123;
	public final static int BUTTON_COLOR_SELECTED = 124;
	public final static int BUTTON_COLOR_DEAD = 125;
	public final static int BUTTON_COLOR_SENDING = 126;
	public final static int BUTTON_COLOR_RECEIVING = 127;
	public final static int BUTTON_COLOR_TRANSMISSION_RADIUS = 128;
	public final static int BUTTON_COLOR_CONNECTION = 129;
	public final static int BUTTON_COLOR_SECONDARY_SELECTED = 130;
	public final static int BUTTON_COLOR_TERMINAL = 131;
	public final static int BUTTON_COLOR_CURRENT_TURN = 132;
	public final static int BUTTON_COLOR_ISOLATED = 133;
	public final static int BUTTON_GENERATE_ADDRESS_BOOK = 135;

	public final static int CHECKBOX_RADII = 201;
	public final static int CHECKBOX_CONNECTIONS = 202;
	public final static int CHECKBOX_NEIGHBOURS = 203;
	public final static int CHECKBOX_ENABLE_CONSOLE = 204;
	public final static int CHECKBOX_ROUTES = 205;
	public final static int CHECKBOX_ISOLATED = 206;
	public final static int CHECKBOX_ENABLE_INFOBOX = 207;
	public final static int CHECKBOX_CONSOLE_INFORMATION = 208;
	public final static int CHECKBOX_CONSOLE_WARNING = 209;
	public final static int CHECKBOX_CONSOLE_ERROR = 210;
	public final static int CHECKBOX_CONSOLE_DEBUG = 211;

	public final static int TIMER_EVENT = 401;

	public final static int PLAYBACK_PAUSE = 0;
	public final static int PLAYBACK_PLAY = 1;
	public final static int PLAYBACK_FAST_FORWARD = 2;

	public final static int MODE_SELECT = 0;
	public final static int MODE_DISABLE = 1;
	public final static int MODE_ADD = 2;
	public final static int MODE_ENABLE = 3;
	public final static int MODE_REMOVE = 4;
	public final static int MODE_PROMOTE = 5;
	public final static int MODE_DEMOTE = 6;


	/**
	 * The "name" of the view port frame (used by the window listener)
	 */
	public final static char WINDOW_VIEW_PORT = '1';
	/**
	 * The "name" of the control frame (used by the window listener)
	 */
	public final static char WINDOW_CONTROL_FRAME = '2';
	/**
	 * The "name" of the console (used by the window listener)
	 */
	public final static char WINDOW_CONSOLE = '3';


	/**
	 * View bit-mask for whether the Radii view is active or not 
	 */
	public final static int VIEW_RADII		  = 0x00000001;
	/**
	 * View bit-mask for whether the Connections view is active or not 
	 */
	public final static int VIEW_CONNECTIONS  = 0x00000002;
	/**
	 * View bit-mask for whether the Connections view is active or not 
	 */
	public final static int VIEW_NEIGHBOURS	  = 0x00000004;
	/**
	 * View bit-mask filter for no bit-masks 
	 */
	public final static int VIEW_NONE  		  = 0x00000000;
	/**
	 * View bit-mask filter for all bit-masks 
	 */
	public final static int VIEW_ALL 		  = 0x000000ff;
	/**
	 * View bit-mask for whether the console should be shown.
	 */
	public final static int VIEW_CONSOLE	  = 0x00000008;
	/**
	 * View bit-mask for whether sensor routes to terminals should be shown.
	 */
	public final static int VIEW_ROUTES		  = 0x00000010;
	/**
	 * View bit-mask for whether the isolated sensors should be highlighted
	 */
	public final static int VIEW_ISOLATED     = 0x00000020;
	/**
	 * View bit-mask for whether the info box should be shown
	 */
	public final static int VIEW_INFOBOX	  = 0x00000040;
}
