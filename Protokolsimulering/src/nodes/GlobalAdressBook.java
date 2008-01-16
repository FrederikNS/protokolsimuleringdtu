package nodes;

import java.util.ArrayList;

public class GlobalAdressBook {
	private static GlobalAdressBook globalAdressBook;
	private ArrayList<int[]> directConnections=new ArrayList<int[]>();
//	int globalAdressBook[][]=new int[Sensor.idToSensor.size()][4];
	private boolean generatedBefore = false;
	private int sensorsAccountedFor = 0;

	private GlobalAdressBook() {
	}

	public static GlobalAdressBook getAdressBook() {
		if(globalAdressBook == null) {
			globalAdressBook = new GlobalAdressBook();
			globalAdressBook.generateDirectConnections();
		}
		return globalAdressBook;
	}

	public void generateDirectConnections(){
//		System.out.println("Generated Before: "+generatedBefore);
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
						temp[0]=j;
						temp[1]=i;
						directConnections.add(temp);
						sen2.addLinkToSensor(sen1);
//						System.out.println(Sensor.idToSensor.get(temp[1])+" & "+Sensor.idToSensor.get(temp[0])+" was linked");
					}

				}
			}
			sensorsAccountedFor = totalAmountOfSensors;
			if(directConnections.size()>1){
				generatedBefore=true;
//				System.out.println("Generated Before set to: "+generatedBefore);
			}
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
							temp[0]=j;
							temp[1]=i;
							directConnections.add(temp);
							sen2.addLinkToSensor(sen1);
//							System.out.println(Sensor.idToSensor.get(temp[1])+" & "+Sensor.idToSensor.get(temp[0])+" was linked");
						}
					}
				}
			}
		}
	}
}