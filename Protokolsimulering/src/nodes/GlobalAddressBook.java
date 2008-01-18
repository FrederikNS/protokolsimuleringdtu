package nodes;

import java.util.ArrayList;
import java.util.Hashtable;

import nodes.Sensor;

/**
 * @author Frederik Nordahl Sabroe
 * @author Morten Soerensen
 */
public class GlobalAddressBook {
	private static GlobalAddressBook globalAdressBook;
	private ArrayList<int[]> directConnections;
	private boolean generatedBefore;
	private int sensorsAccountedFor;
	private static ArrayList<Sensor> listOfTerminals = new ArrayList<Sensor>();
	private static ArrayList<Hashtable<Sensor, Integer>> hash = new ArrayList<Hashtable<Sensor, Integer>>();

	private GlobalAddressBook() {
		directConnections=new ArrayList<int[]>();
		sensorsAccountedFor = 0;
		generatedBefore = false;
	}

	public static GlobalAddressBook clearBook() {
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
	
	/**
	 * This method adds a terminal to the list.
	 * @param term the terminal to be added
	 */
	public static void addTerminal(Sensor term) {
		listOfTerminals.add(term);
		setStepsHashtable(term);
	}
	
	/**
	 * This method removes a terminal from the list.
	 * @param term the terminal to be removed
	 */
	public static void removeTerminal(Sensor term) {
		int remove = listOfTerminals.indexOf(term);
		listOfTerminals.remove(term);
		hash.remove(remove);
	}
	
	/**
	 * Looks up which terminal is the closest, and returns the path to it.
	 * @param from the sensor where the search starts
	 * @return a path to the closest sensor
	 */
	public ArrayList<Sensor> closestConnectionToTerminal(Sensor from) {
		Sensor theClosest = null;
		int stepCount = -1;
		int block = -1;
		for(int i = 0; i < listOfTerminals.size(); i++) {
			int steps = hash.get(i).get(from);
			if((steps < stepCount) || (stepCount == -1)) {
				theClosest = listOfTerminals.get(i);
				block = i;
			}
		}
		return closestConnection(from, theClosest, block);
	}

	/**
	 * This method gets the shortest path between two sensors.
	 * It uses the Hashtable with number of steps to any sensor, and then it backtracks.
	 * @param from the sensor where the search starts
	 * @param to the distination
	 * @param block the index of the hashtable
	 * @return an ArrayList containing the shortest path
	 */
	public ArrayList<Sensor> closestConnection(Sensor from, Sensor to, int block) {
		Hashtable<Sensor, Integer> dist = hash.get(block); 
		ArrayList<Sensor> path = new ArrayList<Sensor>();
		ArrayList<Sensor> skip = new ArrayList<Sensor>();
		ArrayList<Sensor> temp = new ArrayList<Sensor>();
		path.add(to);
		Sensor tempSensor;
		while(!path.contains(from)) {
			Sensor firstInQueue = path.get(0);
			Sensor[] neighbors = firstInQueue.getLinks();
			if(neighbors == null) {
				skip.add(firstInQueue);
				continue;
			}
			for(int i = 0; i < neighbors.length; i++) {
				Integer a = dist.get(neighbors[i]);
				Integer b = dist.get(firstInQueue); 
				if(a!= null && b != null && a == (b-1)) {
					temp.add(neighbors[i]);
				}
			}
			boolean added = false;
			for(int i = 0 ; i < temp.size() ; i++) {
				tempSensor = temp.get(0);
				if(!skip.contains(tempSensor)) {
					path.add(0, tempSensor);
					added = true;
					break;
				}
			}
			temp.clear();
			if(!added) {
				break;
			}
			
		}
		return path;
	}

	/**
	 * This method uses the Width-first algorithm to find the shortest distance from a sensor to any.
	 * @param from the sensor where the search starts
	 */
	private static void setStepsHashtable(Sensor from) {
		Hashtable<Sensor, Integer> dist = new Hashtable<Sensor, Integer>();
		for(Sensor sen : Sensor.idToSensor.values()) {
			if(!sen.equals(from)) {
				dist.put(sen, Integer.MAX_VALUE);
			} else {
				dist.put(sen, 0);
			}
		}
		ArrayList<Sensor> queue = new ArrayList<Sensor>();
		queue.add(from);
		while(!queue.isEmpty()) {
			Sensor firstInQueue = queue.get(0);
			Sensor[] neighbors = firstInQueue.getLinks();
			if(neighbors == null) {
				break;
			}
			for(int i = 0; i < neighbors.length; i++) {
				if(dist.get(neighbors[i]) == Integer.MAX_VALUE) {
					dist.remove(neighbors[i]);
					dist.put(neighbors[i], dist.get(firstInQueue)+1);
					queue.add(neighbors[i]);
				} else {
					if(dist.get(neighbors[i]) > dist.get(firstInQueue)+1) {
						dist.remove(neighbors[i]);
						dist.put(neighbors[i], dist.get(firstInQueue)+1);
						queue.add(neighbors[i]);
					}
				}
			}
			queue.remove(0);
		}
		hash.add(dist);
	}
}