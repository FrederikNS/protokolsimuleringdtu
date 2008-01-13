package transmissions;

public class Data implements DataConstants{

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
