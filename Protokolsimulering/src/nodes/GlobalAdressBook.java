package nodes;

import java.util.ArrayList;

public class GlobalAdressBook {
	private static GlobalAdressBook globalAdressBook;
	private ArrayList<int[]> directConnections;
	private boolean generatedBefore;
	private int sensorsAccountedFor;

	private GlobalAdressBook() {
		directConnections=new ArrayList<int[]>();
		sensorsAccountedFor = 0;
		generatedBefore = false;
	}

	public static GlobalAdressBook clearBook() {
		return globalAdressBook = new GlobalAdressBook();
	}
	
	public static GlobalAdressBook getAdressBook() {
		if(globalAdressBook == null) {
			globalAdressBook = new GlobalAdressBook();
			globalAdressBook.generateDirectConnections();
		}
		return globalAdressBook;
	}

	public void generateDirectConnections(){
		int totalAmountOfSensors = Sensor.idToSensor.size();
		if(!generatedBefore && totalAmountOfSensors>1){
			for(int i=0;i<totalAmountOfSensors;i++){
				for(int j=i+1;j<totalAmountOfSensors;j++){
					Sensor sen1 = Sensor.idToSensor.get(i);
					Sensor sen2 = Sensor.idToSensor.get(j);
					if(sen1.canCommunicate(sen2)){
						int[] temp = {i,j};
						directConnections.add(temp);
						sen1.addLinkToSensor(sen2);
						sen2.addLinkToSensor(sen1);
					}

				}
			}
			sensorsAccountedFor = totalAmountOfSensors;
			
			//update boolean to avoid complete re-run each time.
			generatedBefore = directConnections.size()>1;
			
		} else if(generatedBefore && totalAmountOfSensors>1) {
			for(int i=sensorsAccountedFor;i<totalAmountOfSensors;i++){
				for(int j=0;j<totalAmountOfSensors;j++){
					if(j!=i){
						Sensor sen1 = Sensor.idToSensor.get(i);
						Sensor sen2 = Sensor.idToSensor.get(j);
						if(sen1.canCommunicate(sen2)){
							int[] temp = {i,j};
							directConnections.add(temp);
							sen1.addLinkToSensor(sen2);
							sen2.addLinkToSensor(sen1);
						}
					}
				}
			}
		}
	}
}