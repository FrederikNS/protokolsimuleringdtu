package transmissions;

/**
 * This class contains all the constants for the class Data.
 * @author Niels Thykier
 */
public interface DataConstants {
	/**
	 * Last transmission received successfully
	 */
	public static final int TYPE_RECEIVED_SUCCESSFULLY      = 0x01000000;
	/**
	 * Last transmission received contained garbage data or
	 * could not be successfully received.
	 */
	public static final int TYPE_RECEIVED_UNSUCCESSFULLY    = 0x02000000;
	/**
	 * The Data is regarding the Sensor network
	 * (e.g. Sensor has stopped responding or new sensor
	 * discovered)
	 */
	public static final int TYPE_NETWORK   				    = 0x04000000;
	/**
	 * Data transmission
	 */
	public static final int TYPE_DATA  					    = 0x08000000;
	
	/**
	 * A "I planning to send message so do not accept data" transmission
	 */
	public static final int TYPE_SENDING    				= 0x00100000;
	/**
	 * A "Someone is talking to me, so keep quiet" transmission
	 */
	public static final int TYPE_LISTENING  				= 0x00200000;
	/**
	 * Garbage data. Disregard all information about this package.
	 */
	public static final int TYPE_GARBAGE 					= 0x00400000;
	/**
	 * Type Bit-mask filter.
	 */
	public static final int TYPE_ALL 						= 0x0ff00000;
	/**
	 * Priority Bit-mask filter.
	 */
	public static final int PRIORITY_ALL					= 0x0000000f;
	/**
	 * Low priority Data/Transmission
	 */
	public static final int PRIORITY_LOW					= 0x00000001;
	/**
	 * Medium priority Data/Transmission
	 */
	public static final int PRIORITY_MEDIUM					= 0x00000002;
	/**
	 * High priority Data/Transmission
	 */
	public static final int PRIORITY_HIGH					= 0x00000003;
	/**
	 * Very high priority Data/Transmission
	 */
	public static final int PRIORITY_VERY_HIGH				= 0x000000dd;
	/**
	 * The ASAP priority Data/Transmission
	 */
	public static final int PRIORITY_INSTANT				= 0x000000ff;
}