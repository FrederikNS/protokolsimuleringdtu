package transmissions;

import java.util.ArrayList;

import turns.Prepareable;

import nodes.Sensor.SensorImplementation;

public class Protocol implements Transmitter, DataConstants, Prepareable {

	@SuppressWarnings("unused")
	private ArrayList<Transmission> ingoing = new ArrayList<Transmission>();
	@SuppressWarnings("unused")
	private ArrayList<Transmission> outgoing = new ArrayList<Transmission>();

	protected static final int OPTION_SEND_DISABLED 	 	= 0x00000001;
	protected static final int OPTION_RECEIVE_DISABLED 	= 0x00000002;
	protected static final int ACTION_RECEIVING = 0x00000004;
	protected static final int ACTION_SENDING	= 0x00000008;
	protected static final int ACTION_WAIT		= 0x00000010;
	protected static final int ACTION_NOTHING_TO_DO		= 0x00000020;
	
	protected int currentTick = 0;
	
	private SensorImplementation main;
	
	public Protocol(SensorImplementation sen) {
		main = sen;
	}
	
	/* (non-Javadoc)
	 * @see transmissions.Transmitter#receive(transmissions.Transmission)
	 */
	public void receive(Transmission msg) {
		// TODO Auto-generated method stub
		boolean isReceiver = main.id == msg.getReceiver();
		switch(msg.getMessageType()){
		case TYPE_SENDING:
			//Someone wishes to send
			if(isReceiver) {
				//to me. If I can receive and can send, reply with ok.
				
			} else {
				//cannot receive.
				//if expecting to receive a message, flag need to send "Unsuccessful received"
			}
			break;
		case TYPE_LISTENING:
			//Someone wishes to receive.
			if(isReceiver) {
				//wee It is waiting for me to send.
			} else {
				//Not allowed to send.
				//if wasTryingToSendToThatTarget, wait for target comfirmation message!
			}
			break;
		case TYPE_NETWORK:
		case TYPE_RECEIVED_SUCCESSFULLY:
		case TYPE_RECEIVED_UNSUCCESSFULLY:
		case TYPE_DATA:
			//Wee data!
			/*if(isReceiver || msg.getRespondsableTransmitter() == this.id) {
				ingoing = msg;
			}*/
			break;
		default:
			//This ought not to happen o.O
			//Broadcast warning to user.
			break;
		}
	}

	public void transmit(Transmission msg) {
		// TODO Auto-generated method stub

	}

	public void prepare() {
		// TODO Auto-generated method stub
		
	}

	public void step() {
		// TODO Auto-generated method stub		
	}

}
