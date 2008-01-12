package transmissions;

public class Data {
	
	public static final int TYPE_RECEIVED_SUCCESSFULLY      = 0x10000000;
	public static final int TYPE_RECEIVED_UNSUCCESSFULLY    = 0x20000000;
	public static final int TYPE_NETWORK   				    = 0x40000000;
	public static final int TYPE_DATA  					    = 0x80000000;
	public static final int TYPE_SENDING    				= 0x01000000;
	public static final int TYPE_RECEIVING  				= 0x02000000;
	private static final int TYPE_ALL 						= 0xff000000;
	private static final int PRIORITY_ALL					= 0x0000000f;
	public static final int PRIORITY_LOW					= 0x00000001;
	public static final int PRIORITY_MEDIUM					= 0x00000002;
	public static final int PRIORITY_HIGH					= 0x00000003;
	public static final int PRIORITY_VERY_HIGH				= 0x000000dd;
	public static final int PRIORITY_INSTANT				= 0x000000ff;
	private int dataType;
	
	private Data() {}
	
	public static Data generateMessageReceiving() {
		Data toReturn = new Data();
		toReturn.dataType = TYPE_RECEIVING | PRIORITY_INSTANT;
		return toReturn;
	}
	
	public static Data generateMessageSending() {
		Data toReturn = new Data();
		toReturn.dataType = TYPE_SENDING | PRIORITY_INSTANT;
		return toReturn;
	}

	public static Data generateMessageReceivedUnsuccessfully() {
		Data toReturn = new Data();
		toReturn.dataType = TYPE_RECEIVED_UNSUCCESSFULLY | PRIORITY_HIGH;
		return toReturn;
	}
	
	public static Data generateMessageReceivedSuccessfully() {
		Data toReturn = new Data();
		toReturn.dataType = TYPE_RECEIVED_SUCCESSFULLY | PRIORITY_HIGH;
		return toReturn;
	}
	
	public static Data generateNetworkMessage(Object obj) {
		Data toReturn = new Data();
		toReturn.dataType = TYPE_NETWORK | PRIORITY_MEDIUM;
		return toReturn;
	}
	
	public static Data generateData(Object obj) {
		Data toReturn = new Data();
		toReturn.dataType = TYPE_DATA | PRIORITY_LOW;
		return toReturn;
	}
	
	public int getDataType() {
		return dataType & TYPE_ALL;
	}
	
	public int getSendingPriority() {
		return dataType & PRIORITY_ALL;
	}
}
