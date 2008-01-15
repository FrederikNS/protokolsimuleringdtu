package tests;

import graphics.Scaling;
import nodes.Sensor;
import nodes.SplitField;

public class SelectionTest {
	public SelectionTest(SplitField splitField){
		Scaling.setPicCoordsX(0, 500);
		Scaling.setPicCoordsY(0, 500);
		//splitField.addSensor(new Sensor(100,100));
		//splitField.addSensor(new Sensor(400,400));
		//splitField.addSensor(new Sensor(102,102));
		for(int i=0;i<1000;i++) {
			//splitField.addSensor(new Sensor());
			new Sensor();
		}
	}
}