package nodes;


/**
 * @author Frederik Nordahl Sabroe
 * @author Morten Soerensen
 */
public class GlobalAddressBook {
	private static GlobalAddressBook globalAdressBook;
	private int sensorsAccountedFor;

	private GlobalAddressBook() {
		sensorsAccountedFor = 0;
	}

	public static GlobalAddressBook clearBook() {
		Sensor.clearAllLinks();
		return globalAdressBook = new GlobalAddressBook();
	}

	public static GlobalAddressBook getBook() {
		if(globalAdressBook == null) {
			globalAdressBook = new GlobalAddressBook();
			globalAdressBook.generateDirectConnections();
		}
		return globalAdressBook;
	}

	public void generateDirectConnections(){
		int totalAmountOfSensors = Sensor.idToSensor.size();
		if(totalAmountOfSensors>1){
			for(int i=sensorsAccountedFor;i<totalAmountOfSensors;i++){
				for(int j=0;j<totalAmountOfSensors;j++){
					if(i != j) {
						Sensor sen1 = Sensor.idToSensor.get(i);
						Sensor sen2 = Sensor.idToSensor.get(j);
						if(sen1.canCommunicate(sen2)){
							sen1.addLinkToSensor(sen2);
							sen2.addLinkToSensor(sen1);
						}
					}
				}
			}
			sensorsAccountedFor = totalAmountOfSensors;
		}
	}
}