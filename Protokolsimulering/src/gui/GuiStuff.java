package gui;

import javax.swing.ButtonGroup;

class GuiStuff implements GuiInterface{
	static ViewPort sensorNetwork;
	static int playback = PLAYBACK_PAUSE;
	static int mode = MODE_SELECT;
	static ButtonGroup modeGroup;
	static ButtonGroup stepperGroup;

	static {
		modeGroup = new ButtonGroup();
		stepperGroup = new ButtonGroup();
	}
}