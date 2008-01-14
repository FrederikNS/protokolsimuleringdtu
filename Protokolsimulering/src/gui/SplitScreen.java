package gui;

import nodes.Sensor;

public class SplitScreen {
	
	private int[] SL;
	private int size;
	private SplitScreen[] ss;
	private int xMin;
	private int xMax;
	private int yMin;
	private int yMax;
	
	
	public SplitScreen(int xMin, int xMax, int yMin, int yMax) {
		SL = new int[10];
		size = 0;
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;
	}

	private SplitScreen(SplitScreen splitMe, int block, int sensor) {
		switch(block) {
		case 0:
			this.xMin = splitMe.xMin;
			this.xMax = splitMe.xMax/2;
			this.yMin = splitMe.yMin;
			this.yMax = splitMe.yMax/2;
			break;
		case 1:
			this.xMin = (splitMe.xMax/2)+1;
			this.xMax = splitMe.xMax;
			this.yMin = splitMe.yMin;
			this.yMax = splitMe.yMax/2;
			break;
		case 2:
			this.xMin = splitMe.xMin;
			this.xMax = splitMe.xMax/2;
			this.yMin = (splitMe.yMax/2)+1;
			this.yMax = splitMe.yMax;
			break;
		case 3:
			this.xMin = (splitMe.xMax/2)+1;
			this.xMax = splitMe.xMax;
			this.yMin = (splitMe.yMax/2)+1;
			this.yMax = splitMe.yMax;
			break;
		}
		this.SL[0] = sensor;
		this.size = 1;
	}
	
	public void addSensor(Sensor sensor) {
		if(ss != null) {
			
		} else if(size == SL.length) {
			ss = new SplitScreen[4];
			for(int i = 0 ; i < 4 ; i++){
				ss[i] = new SplitScreen(this,i, sensor.id);
			}
			//TODO
		} else {
			SL[size] = sensor.id;
			size++;
		}
	}
	
	public void removeSensor(int sensor) {
		
	}
}
