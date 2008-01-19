package transmissions;

import java.util.ArrayList;
import java.util.Random;
import java.util.TreeSet;

import turns.EndSteppable;
import turns.Prepareable;

import nodes.Sensor;
import nodes.Sensor.SensorImplementation;
import notification.Note;

public class Protocol implements Transmitter, DataConstants, Prepareable, EndSteppable {

	
	protected TreeSet<Transmission> ingoing = new TreeSet<Transmission>();
	protected TreeSet<Transmission> outgoing = new TreeSet<Transmission>();
	protected ArrayList<Transmission> sent = new ArrayList<Transmission>();
	protected Transmission incomming;
	protected int transmissionFrom = Sensor.INVALID_SENSOR_ID;
	
	public static final int OPTION_SEND_DISABLED 	 	= 0x00000001;
	public static final int OPTION_RECEIVE_DISABLED 	= 0x00000002;
	public static final int ACTION_RECEIVING = 0x00000004;
	public static final int ACTION_SENDING	= 0x00000008;
	public static final int ACTION_WAIT		= 0x00000010;
	public static final int ACTION_NOTHING_TO_DO		= 0x00000020;
	
	protected int currentTick = 0;
	protected int waitingForSensor = Sensor.INVALID_SENSOR_ID;
	protected int delayNextTransmission = 0;
	protected static Random ran = new Random();
	
	
	private SensorImplementation main;
	
	public Protocol(SensorImplementation sen) {
		main = sen;
	}
	
	/* (non-Javadoc)
	 * @see transmissions.Transmitter#receive(transmissions.Transmission)
	 */
	public void receive(Transmission msg) {
		// TODO Auto-generated method stub
		if(incomming != null) {
			Note.sendNote(Note.WARNING, main +": Message from "+ msg.getSender() + " corrupted already received message!");
			incomming = Transmission.generateCorruptTransmission();
			return;
		}
		boolean isReceiver = (main.id == msg.getReceiver() || msg.getRespondsableTransmitter() == main.id );
		boolean isForAll = (msg.getRespondsableTransmitter() == Sensor.ALL_SENSORS || msg.getReceiver() == Sensor.ALL_SENSORS);
		
		switch(msg.getMessageType()){
		case TYPE_SENDING:
			//Someone wishes to send
			if(isReceiver || isForAll) {
				//to me. If I can receive and can send, reply with ok.
				if(0 == (currentTick & (OPTION_RECEIVE_DISABLED | OPTION_SEND_DISABLED))) {
					currentTick |= Protocol.ACTION_RECEIVING;
					if(isReceiver) {
						transmit(msg.generateConfirmationMessage());
					}
				} else if (isForAll) {
					currentTick |= Protocol.ACTION_RECEIVING;
				}
			} else {
				//cannot receive.
				currentTick |= OPTION_RECEIVE_DISABLED;
			}
			return;
		case TYPE_LISTENING:
			//Someone wishes to receive.
			if(isReceiver) {
				//wee It is waiting for me to send.
				if(0 == (currentTick & Protocol.OPTION_RECEIVE_DISABLED)) {
						currentTick |= Protocol.ACTION_SENDING;
				}
			} else {
				//Not allowed to send.
				currentTick |= Protocol.OPTION_SEND_DISABLED;
				if(outgoing.size() > 0 && msg.getRespondsableTransmitter() == outgoing.first().getRespondsableTransmitter()) {
					//wait for message.
					waitingForSensor = msg.getRespondsableTransmitter();
				}
			}
			break;
		case TYPE_RECEIVED_SUCCESSFULLY:
			if(waitingForSensor != Sensor.INVALID_SENSOR_ID 
					&& outgoing.first().getRespondsableTransmitter() == incomming.getRespondsableTransmitter()) {
				waitingForSensor = Sensor.INVALID_SENSOR_ID;
				break;
			}
			//fall through
		case TYPE_RECEIVED_UNSUCCESSFULLY:
		case TYPE_DATA:
		case TYPE_NETWORK:
			if(isReceiver || isForAll) {
				//Note.sendNote(Note.DEBUG, main + " received transmission from " +  msg.getSender());
				if(isForAll) {
					delayNextTransmission = ran.nextInt(10)+1;
				}
				incomming = msg;
			}
			break;
		default:
			//This ought not to happen o.O
			//Broadcast warning to user.
			Note.sendNote(Note.WARNING, "Received message with undefined type: " + Integer.toHexString(msg.getMessageType()));
			break;
		}
	}

	public void transmit(Transmission msg) {
		if(msg.getReceiver() == Sensor.INVALID_SENSOR_ID) {
			return;
		}
		if(msg.getRespondsableTransmitter() == Sensor.INVALID_SENSOR_ID) {
			msg.setRespondsableTransmitter(msg.getReceiver());
		}
		switch(msg.getMessageType()) {
		case TYPE_LISTENING:
		case TYPE_RECEIVED_SUCCESSFULLY:
		case TYPE_RECEIVED_UNSUCCESSFULLY:
			//Do not need confirmation on these.
			break;
		case TYPE_SENDING:
		default:
			if(msg.getReceiver() != Sensor.ALL_SENSORS) {
				sent.add(msg);
			}
			break;
		}
		main.transmit(msg);
	}

	public void addTransmissionToSend(Transmission trans){
		outgoing.add(trans);
	}
	
	public int getStatus() {
		return currentTick;
	}
	
	public void prepare() {
		if(delayNextTransmission <= 0) {
			if(0 == (currentTick & (Protocol.OPTION_SEND_DISABLED | Protocol.ACTION_RECEIVING)) 
				&& outgoing.size() > 0 && incomming == null) {
				Transmission trans = outgoing.last();
				if(trans.getReceiver() == Sensor.ALL_SENSORS) {
					//If it is for all, do not wait for a confirmation.
					currentTick |= Protocol.ACTION_SENDING;
				}
				transmit(Transmission.generateSendRequest(trans.getRespondsableTransmitter(), trans.getSender()));
			} else {
				
			}
		} else {
			//Note.sendNote(Note.DEBUG, main + " could not send this round.");
			--delayNextTransmission;
		}
	}

	public void step() {
		if(0 != (currentTick & Protocol.ACTION_SENDING) && 0 == (currentTick & Protocol.ACTION_RECEIVING)) {
			if(0 == (currentTick & Protocol.OPTION_SEND_DISABLED)) {
				delayNextTransmission = ran.nextInt(10)+5;
				transmit(outgoing.pollLast());
			} else {
				delayNextTransmission = ran.nextInt(15)+ ran.nextInt(5) + 5;
			}
			
			
		} else {
			//Note.sendNote(Note.DEBUG, main + ": Nothing to do!" );
		}
	}

	public void endStep() {
		if(incomming != null) {
			if(incomming.getMessageType() != Data.TYPE_GARBAGE) {
				switch(incomming.getMessageType()) {
				case Data.TYPE_RECEIVED_SUCCESSFULLY:
					for(Transmission trans : outgoing) { 
						if(trans.getRespondsableTransmitter() == incomming.getSender()) {
							sent.remove(trans);
							break;
						}
					}
					break;
				case Data.TYPE_RECEIVED_UNSUCCESSFULLY:
					for(Transmission trans : sent) {
						if(trans.getRespondsableTransmitter() == incomming.getSender()) {
							outgoing.add(trans);
							sent.remove(trans);
							break;
						}
					}
					break;
				case Data.TYPE_NETWORK:
					try {
						NetworkData net = incomming.getData(0).asNetworkData();
						int nearestTerm = main.getNearestTerminal();
						main.newTerminal(net.getLink(), incomming.getSender(), net.getDistance());
						if(nearestTerm != main.getNearestTerminal()) {
							outgoing.add(new Transmission(Sensor.ALL_SENSORS, Sensor.ALL_SENSORS, 
									incomming.getSender(), net.nextGenerationData(main.id)));
						}
					} catch(ClassCastException e) {
						Note.sendNote(Note.ERROR, main + ": ClassCastException in Protocol");
					}
					break;
				default:
					ingoing.add(incomming);
					break;
				}
			} else {
				outgoing.add(Transmission.generateReceivedGarbage(main.id));
			}
		}
		incomming = null;
		currentTick = 0;
	}

}
