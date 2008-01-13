package transmissions;

public interface DataConstants {
	/**
	 * Last transmission received successfully
	 */
	public static final int TYPE_RECEIVED_SUCCESSFULLY      = 0x10000000;
	/**
	 * Last transmission received contained garbage data or
	 * could not be successfully received.
	 */
	public static final int TYPE_RECEIVED_UNSUCCESSFULLY    = 0x20000000;
	/**
	 * The Data is regarding the Sensor network
	 * (e.g. Sensor has stopped responding or new sensor
	 * discovered)
	 */
	public static final int TYPE_NETWORK   				    = 0x40000000;
	/**
	 * Data transmission
	 */
	public static final int TYPE_DATA  					    = 0x80000000;
	/**
	 * A "I planning to send message so do not accept data" transmission
	 */
	public static final int TYPE_SENDING    				= 0x01000000;
	/**
	 * A "Someone is talking to me, so keep quiet" transmission
	 */
	public static final int TYPE_RECEIVING  				= 0x02000000;
	/**
	 * Type Bit-mask filter.
	 */
	public static final int TYPE_ALL 						= 0xff000000;
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