package nodes;


/**
 * This class is responsible for checking which sensors can communicate.
 * @author Frederik Nordahl Sabroe
 * @author Morten Soerensen
 */
public class GlobalAddressBook {
	/**
	 * A variable for itself.
	 */
	private static GlobalAddressBook globalAdressBook;
	/**
	 * A variable for knowing, how many sensors it has accounted for.
	 */
	private int sensorsAccountedFor;

	/**
	 * The constructor of this class.
	 */
	private GlobalAddressBook() {
		sensorsAccountedFor = 0;
	}

	/**
	 * This method clears all links between sensors and returns a new blank address book.
	 * @return a new address book
	 */
	public static GlobalAddressBook clearBook() {
		Sensor.clearAllLinks();
		return (globalAdressBook = new GlobalAddressBook());
	}

	/**
	 * This method returns the address book.
	 * @return the address book
	 */
	public static GlobalAddressBook getBook() {
		if(globalAdressBook == null) {
			globalAdressBook = new GlobalAddressBook();
		}
		return globalAdressBook;
	}
	
	/**
	 * Method used by the xml-loader to have the addressbook ignore
	 * loaded sensors.
	 */
	public void loadedSensors() {
		sensorsAccountedFor = Sensor.idToSensor.size();
	}

	/**
	 * This method is responsible for detecting which sensors can communicate.
	 */
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