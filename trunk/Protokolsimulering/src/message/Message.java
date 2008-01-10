package message;


public class Message {
	
	private Transmitter destination;
	private Transmitter transmitter;
	private int messageType;
	
	public Transmitter getDestination() {
		return (Transmitter) destination.clone();
	}
	
	public Transmitter getSender() {
		return (Transmitter) transmitter.clone();
	}
	
	public int getMessageType() {
		return messageType;
	}
}
