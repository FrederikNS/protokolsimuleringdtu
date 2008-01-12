package nodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import transmissions.Data;
import transmissions.Transmission;
import transmissions.TransmissionTimeOutChecker;
import transmissions.Transmitter;
import turns.Prepareable;


/**
 * 
 * @author Niels Thykier
 */
public class Sensor extends Location implements Transmitter, Prepareable, Comparable<Sensor>{
	
	private static SensorList allRealSensors = new SensorList(50);
	private static ArrayList<Sensor> fakeSensors = new ArrayList<Sensor>();
	
	public final int id;
	
	public Sensor() {
		this(new Location());
	}
	
	public Sensor(int x, int y) {
		this(new Location(x,y));
	}
	
	public Sensor(Location loc) {
		this(loc, new RealSensor(loc).id);
		fakeSensors.add(this);
	}
	
	/**
	 * Used by the RealSensor constructor.
	 * @param loc
	 * @param id The ID of the sensor.
	 */
	protected Sensor(Location loc, int id) {
		super(loc);
		this.id = id;
	}
	
	/**
	 * Generates a fake sensor using a real or another fake sensor.
	 * @param sen The sensor, who's ID this should claim to be.
	 */
	private Sensor(Sensor sen) {
		this(sen, sen.id);
		fakeSensors.add(this);
	}

	public void receive(Transmission msg) {
		RealSensor real = allRealSensors.get(id);
		if(real != null) {
			real.receive(msg);
		}
	}

	public void transmit(Transmission msg) {
		RealSensor real = allRealSensors.get(id);
		if(real != null) {
			real.transmit(msg);
		}
	}

	public void prepare() {
		RealSensor real = allRealSensors.get(id);
		if(real != null) {
			real.prepare();
		}
	}

	public void step() {
		RealSensor real = allRealSensors.get(id);
		if(real != null) {
			real.step();
		}
	}
	
	
	public static Sensor getSensor(int sensorID) {
		return new Sensor(allRealSensors.get(sensorID));
	}
	
	protected RealSensor getRealSensor() {
		return allRealSensors.get(id);
	}
	
	/* (non-Javadoc)
	 * @see nodes.Location#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Sensor) {
			Sensor sen = (Sensor) obj;
			return sen.id == id;
		}
		return false;
	}
	
	public void discardSensors() {
		allRealSensors = new SensorList(10);
		RealSensor.usedIDs = 0;
	}
	
	/**
	 * Gets the locaiton of the sensor.
	 * @return The location of the sensor.
	 */
	public Location getLocation()  {	
		try {
			return (Location) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}
	
	public boolean isDown() {
		boolean toReturn = true;
		RealSensor real = allRealSensors.get(id);
		if(real != null) {
			toReturn = real.isDown();
		}
		return toReturn;
	}
	
	/* (non-Javadoc)
	 * @see nodes.Location#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Sensor arg0) {
		return Integer.valueOf(id).compareTo(arg0.id);
	}
	
	public static final class SensorComparator implements Comparator<Sensor> {
		private final int compareType;
		public static final int SORT_DEFAULT = 0;
		public static final int SORT_BY_TURNS = 1;
		public static final int SORT_BY_ID = 2;
		public SensorComparator(int type) {
			compareType = type;
		}

		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(Sensor o1, Sensor o2) {
			int toReturn = 0;
			switch(compareType) {
			case SORT_BY_TURNS:
				RealSensor r1 = o1.getRealSensor();
				RealSensor r2 = o2.getRealSensor();
				if(r1.transmissionRoll < r2.transmissionRoll) {
					toReturn = -1;
				} else if(r1.transmissionRoll == r2.transmissionRoll) {
					//Tie, randomly choose one. (50/50)
					if((RealSensor.ran.nextInt(50) & 1) == 0) {
						toReturn = -1;
					} else {
						toReturn = 1;
					}
				} else {
					toReturn = 1;
				}
				break;
			default:
			case SORT_DEFAULT:
			case SORT_BY_ID:
				toReturn = o1.compareTo(o2);
				break;
			}
			return toReturn;
		}
	}

	
	private static class SensorList{
		
		private static boolean unsorted = true;
		private static RealSensor[] list;
		private static int size;
		
		private SensorList(int size){
			resize(size);
		}
		
		private void resize(int newSize) {
			RealSensor[] temp = new RealSensor[newSize];
			if(list != null) {
				if(newSize < size) {
					return;
				}
				for(int i = 0 ; i < size ; i++) {
					temp[i] = list[i];
				}
			}
			list = temp;
		}
		
		private RealSensor get(int sensorID) {
			if(sensorID > size || sensorID < 0) {
				throw new IndexOutOfBoundsException("Max size: " + size);
			}
			if(unsorted) {
				sort();
			}
			for(int i = 0 ; i <= sensorID ; i++) {
				if(sensorID == list[i].id) {
					return list[i];
				}
			}
			return null;
		}
		
		private void add(RealSensor sen) {
			list[size++] = sen;
			unsorted = true;
			if(size == list.length) {
				resize(size + 10);
			}
		}
		
		private void sort() {
			Arrays.sort(list);
			unsorted = false;
		}
	}
	
	protected static class RealSensor extends Sensor{

		private static int usedIDs = 0;
		private static int maxRoll = 100; 
		protected static Random ran = new Random();
		private static int CANNOT_RECEIVE = 0x01;
		private static int CANNOT_SEND	  = 0x02;
		private static int IS_RECEIVING   = 0x04;
		private static int IS_SENDING	  = 0x08;
		private static int CORRUPTION	  = 0x10;
		
		private ArrayList<Transmission> toTransmit;
		private ArrayList<Transmission> received; 
		private TransmissionTimeOutChecker sent;
	
		private int transmissionRoll = 0;
		private int transmissionDelay = 0;
		private int transmissionRestriction = 0;
		
		/**
		 * Generate a sensor at a random location.
		 */
		protected RealSensor() {
			this(new Location());
		}

		/**
		 * Generate a sensor at a given location.
		 * @param coordX The x-coordiate of the location 
		 * @param coordY The y-coordiate of the location
		 */
		protected RealSensor(int coordX, int coordY) {
			this(new Location(coordX, coordY));
		}

		/**
		 * Generates a Sensor from a location.
		 * @param loc The location of the new sensor.
		 */
		protected RealSensor(Location loc) {
			super(loc, usedIDs++);
			initSensor();
		}

		/**
		 * Initialization, called from the constructors. 
		 */
		private void initSensor() {
			allRealSensors.add(this);
			regenerateLists();
			sent = new TransmissionTimeOutChecker();
		}

		/**
		 * Throws out the old lists and create some new ones, forcing a clean up of the lists.
		 * Non-removed data are copied to the new lists.
		 */
		private void regenerateLists() {
			ArrayList<Transmission> tempToTransmit = new ArrayList<Transmission>();
			ArrayList<Transmission> tempReceived = new ArrayList<Transmission>();
			int size = 0;
			
			if(received != null) {
				size = received.size();
				for(int i = 0 ; i< size ; i++) {
					tempReceived.add(received.get(i));
				}
			}
			if(toTransmit != null) {
				size = toTransmit.size();
				for(int i = 0 ; i< size ; i++) {
					tempToTransmit.add(toTransmit.get(i));
				}
			}
			received = tempReceived;
			toTransmit = tempToTransmit;
		}
		
		protected void prepareMessages() {
			int size = received.size();
			for(int i = 0 ; i < size ; i++) {
				toTransmit.add(received.get(i).generateConfirmationMessage());
			}
			received = new ArrayList<Transmission>();
			
		}
		
		/**
		 * Test if the Sensor is operational.
		 * @return true if the sensor is down/unavailable. 
		 */
		@Override
		public boolean isDown() {
			return false;
		}
		
		/* (non-Javadoc)
		 * @see transmissions.Transmitter#receive(transmissions.Transmission)
		 */
		@Override
		public void receive(Transmission msg) {
			if(isDown()) {
				return;
			}
			switch(msg.getMessageType()) {
			case Data.TYPE_SENDING:
				if(0 == (transmissionRestriction & IS_RECEIVING)) {
					if(msg.getReceiver() == id) {
						transmissionRestriction |= IS_RECEIVING;
					} else {
						transmissionRestriction |= CANNOT_RECEIVE;
					}
				} else {
					// due to our implementation, this ought not to happen.
					transmissionRestriction |= CORRUPTION;
				}
				break;
			case Data.TYPE_RECEIVING:
				if(msg.getSender() != id) {
					transmissionRestriction |= CANNOT_SEND;
				}
				break;
			case Data.TYPE_RECEIVED_SUCCESSFULLY:
				sent.remove(msg.getSender());
				break;
			case Data.TYPE_RECEIVED_UNSUCCESSFULLY:
				break;
			case Data.TYPE_DATA:
				if(msg.getReceiver() == id) {
					received.add(msg);
				} else {
					toTransmit.add(msg);
				}
				break;
			}
		}

		/* (non-Javadoc)
		 * @see transmissions.Transmitter#transmit(transmissions.Transmission)
		 */
		@Override
		public void transmit(Transmission msg) {
			if(msg.getMessageType() != Data.TYPE_RECEIVING && msg.getMessageType() != Data.TYPE_SENDING) {
				sent.add(msg);
			}
		}

		/* (non-Javadoc)
		 * @see turns.Prepareable#prepare()
		 */
		@Override
		public void prepare() {
			// TODO Auto-generated method stub
			prepareMessages();
			transmissionRoll = ran.nextInt(maxRoll); 
		}

		/* (non-Javadoc)
		 * @see turns.Steppable#step()
		 */
		@Override
		public void step() {
			// TODO Auto-generated method stub	
		}

		/* (non-Javadoc)
		 * @see nodes.Sensor#getRealSensor()
		 */
		@Override
		protected RealSensor getRealSensor() {
			return this;
		}

	}
}