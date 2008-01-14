package gui;

public interface GuiInterface {
	public final static int MENU_NEW = 1;
	public final static int MENU_OPEN = 2;
	public final static int MENU_SAVE = 3;
	public final static int MENU_SAVE_AS = 4;
	public final static int MENU_QUIT = 0;
	
	public final static int BUTTON_KILL = 101;
	public final static int BUTTON_ADD = 102;
	public final static int BUTTON_MOVE = 103;
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
	
	
	
	public final static int PLAYBACK_REWIND = -2;
	public final static int PLAYBACK_PLAY_BACKWARDS = -1;
	public final static int PLAYBACK_PAUSE = 0;
	public final static int PLAYBACK_PLAY = 1;
	public final static int PLAYBACK_FAST_FORWARD = 2;
	
	
	
	public final static int MODE_SELECT = 0;
	public final static int MODE_KILL = 1;
	public final static int MODE_ADD = 2;
	public final static int MODE_MOVE = 3;
}
