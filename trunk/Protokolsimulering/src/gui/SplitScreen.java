package gui;

import nodes.Sensor;

public class SplitScreen {
	
	private int[] SL;
	private int size;
	private SplitScreen[] ss;
	
	public SplitScreen(){
		SL = new int[10];
		size = 0;
	}
	
	private SplitScreen(SplitScreen splitMe, int block) {
		
	}
	
	public void addSensor(Sensor sensor) {
		if(ss != null) {
			
		} else if(size == SL.length) {
			ss = new SplitScreen[4];
			for(int i = 0 ; i < 4 ; i++){
				ss[i] = new SplitScreen(this,i);
			}
		} else {
			SL[size] = sensor.id;
			size++;
		}
	}
	
	public void removeSensor(int sensor) {
		
	}
}
