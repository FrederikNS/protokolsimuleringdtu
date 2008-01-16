package nodes;

import java.util.ArrayList;

public class GlobalAdressBook {

	ArrayList<int[]> directConnections=new ArrayList<int[]>();
//	int globalAdressBook[][]=new int[Sensor.idToSensor.size()][4];

	public GlobalAdressBook() {
	}

	public void generateDirectConnections() {
		for(int i=0;i<Sensor.idToSensor.size();i++){
			for(int j=i+1;j<Sensor.idToSensor.size();j++){
				Sensor sen = Sensor.idToSensor.get(i);
				if(sen.canCommunicate(Sensor.idToSensor.get(j))){
					int[] temp = {i,j};
					directConnections.add(temp);
					int[] temp2 = {j,i};
					directConnections.add(temp2);
				}
			}
		}
	}
	
	public void generateGlobalAdressBook() {
		
	}
}