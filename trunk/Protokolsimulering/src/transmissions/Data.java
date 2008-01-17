package transmissions;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class Data implements DataConstants{

	private int dataType;
	public static final Data GarbageData;
	static {
		Data temp = new Data();
		temp.dataType = TYPE_GARBAGE;
		GarbageData = temp;
	}
	private Data() {}
	
	public static Data generateMessageReceiving() {
		Data toReturn = new Data();
		toReturn.dataType = TYPE_LISTENING | PRIORITY_INSTANT;
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

	public Node generateXMLElement(Document doc) {
		Element dataNode = doc.createElement("data");
		Text content = doc.createTextNode(String.valueOf(dataType));
		dataNode.appendChild(content);
		return dataNode;
	}
	
	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			//Not gonna happen
			throw new RuntimeException(e);
		}
	}
}
