package nodes;

import gui.ControlPanelFrame;
import gui.GUIReferences;


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

	private volatile boolean cleared = false;
	
	private boolean skipSleep = false;
	
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
		synchronized (globalAdressBook) {
			globalAdressBook.cleared = true;
		}
		return globalAdressBook;
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
	 * This method is responsible for detecting which sensors can communicate.
	 */
	public synchronized void generateDirectConnections(){
		this.notifyAll();
	}
	
	private void generate() {
		int totalAmountOfSensors = Sensor.idToSensor.size();
		if(totalAmountOfSensors>1){
			int i;
			synchronized (this) {
				if(cleared) {
					i = 0;
					cleared = false;
				} else {
					i = sensorsAccountedFor;
				}
			}
			for( ;i<totalAmountOfSensors;i++){
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
				synchronized (this) {
					if(cleared) {
						skipSleep = true;
						return;
					}
				}
			}
			sensorsAccountedFor = totalAmountOfSensors;
		}
	}
	
	public void capture() {
		while(true) {
			if(!skipSleep) {
				try {	
					synchronized (this) {
						this.wait();
					}
				} catch(Exception e) {
				}
			}
			if(ControlPanelFrame.getFrame() == null || !ControlPanelFrame.getFrame().isVisible()) {
				break;
			}
			generate();
			GUIReferences.viewPort.repaint();
		}
	}

	public void loadedSensors() {
		this.sensorsAccountedFor = Sensor.idToSensor.size();
	}
}