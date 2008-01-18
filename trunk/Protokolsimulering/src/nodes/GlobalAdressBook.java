package nodes;

import java.util.ArrayList;
import java.util.Hashtable;

import nodes.Sensor;

/**
 *
 * @author Frederik Nordahl Sabroe
 * @author Morten Soerensen
 */
public class GlobalAdressBook {
	private static GlobalAdressBook globalAdressBook;
	private ArrayList<int[]> directConnections;
	private boolean generatedBefore;
	private int sensorsAccountedFor;
	private static ArrayList<Sensor> listOfTerminals;

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
	
	/**
	 * This method adds a terminal to the list.
	 * @param term the terminal to be added
	 */
	public static void addTerminal(Sensor term) {
		listOfTerminals.add(term);
	}
	
	/**
	 * This method removes a terminal from the list.
	 * @param term the terminal to be removed
	 */
	public static void removeTerminal(Sensor term) {
		listOfTerminals.remove(term);
	}
	
	/**
	 * This method returns the number of steps a sensor is from another.
	 * @param from the "from" sensor
	 * @param to the distination
	 * @return number of steps
	 */
	public int numberOfSteps(Sensor from, Sensor to) {
		return getStepsHashtable(from).get(to);
	}
	
	/**
	 * Looks up which terminal is the closest, and returns the path to it.
	 * @param from the sensor where the search starts
	 * @return a path to the closest sensor
	 */
	public ArrayList<Sensor> closestConnectionToTerminal(Sensor from) {
		Sensor theClosest = null;
		int stepCount = -1;
		for(int i = 0; i < listOfTerminals.size(); i++) {
			int steps = numberOfSteps(from, listOfTerminals.get(i));
			if((steps < stepCount) || (stepCount == -1)) {
				theClosest = listOfTerminals.get(i);
			}
		}
		return closestConnectionToAny(from, theClosest);
	}

	/**
	 * This method gets the shortest path between two sensors.
	 * It uses the Hashtable with number of steps to any sensor, and then it backtracks.
	 * @param from the sensor where the search starts
	 * @param to the distination
	 * @return an ArrayList containing the shortest path
	 */
	public ArrayList<Sensor> closestConnectionToAny(Sensor from, Sensor to) {
		Hashtable<Sensor, Integer> dist = getStepsHashtable(from); 
		ArrayList<Sensor> path = new ArrayList<Sensor>();
		ArrayList<Sensor> temp = new ArrayList<Sensor>();
		path.add(to);
		while(!path.contains(from)) {
			Sensor firstInQueue = path.get(0);
			Sensor[] neighbors = firstInQueue.getLinks();
			for(int i = 0; i < neighbors.length; i++) {
				if(dist.get(neighbors[i]) == dist.get(firstInQueue)-1) {
					temp.add(neighbors[i]);
				}
			}
			path.add(0, temp.get(0));
			temp.clear();
		}
		return path;
	}

	/**
	 * This method uses the Width-first algorithm to find the shortest distance between two sensors.
	 * @param from the sensor where the search starts
	 * @return a Hashtable containing the number of steps from the starting point to any sensor
	 */
	private Hashtable<Sensor, Integer> getStepsHashtable(Sensor from) {
		Hashtable<Sensor, Integer> dist = new Hashtable<Sensor, Integer>();
		for(Sensor sen : Sensor.idToSensor.values()) {
			if(sen != from) {
				dist.put(sen, -1);
			} else {
				dist.put(sen, 0);
			}
		}
		ArrayList<Sensor> queue = new ArrayList<Sensor>();
		queue.add(from);
		while(queue != null) {
			Sensor firstInQueue = queue.get(0);
			Sensor[] neighbors = firstInQueue.getLinks();
			for(int i = 0; i < neighbors.length; i++) {
				if(dist.get(neighbors[i]) == -1) {
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
		return dist;
	}
}