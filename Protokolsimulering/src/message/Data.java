package message;

public class Data {
	
	public static final int TYPE_RECEIVED = 1;
	public static final int TYPE_NETWORK  = 2;
	public static final int TYPE_DATA  = 3; 
	private int dataType;
	
	private Data() {}
	
	public static Data generateMessageReceived() {
		Data toReturn = new Data();
		toReturn.dataType = TYPE_RECEIVED;
		return toReturn;
	}
	
	public static Data generateNetworkMessage(Object obj) {
		Data toReturn = new Data();
		toReturn.dataType = TYPE_NETWORK;
		return toReturn;
	}
	
	public static Data generateData(Object obj) {
		Data toReturn = new Data();
		toReturn.dataType = TYPE_DATA;
		return toReturn;
	}
	
	public int getDataType() {
		return dataType;
	}
}
