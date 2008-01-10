package message;

public interface Transmitter {
	public static final int TRANSMIT_TYPE_TARGET = 1;
	public static final int TRANSMIT_TYPE_BROADCAST = 2;
	public static final int TIMEOUT_IN_STEPS = 3;
	public void receive(Message msg);
	public boolean transmit(Message msg);
	public boolean transmit(Message msg, Transmitter[] through);
}
