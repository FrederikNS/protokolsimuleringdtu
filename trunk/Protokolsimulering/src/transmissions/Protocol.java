package transmissions;

import java.util.ArrayList;
import java.util.Random;
import java.util.TreeSet;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import exceptions.XMLParseException;

import turns.EndSteppable;
import turns.Prepareable;
import xml.DOMxmlParser;
import xml.Saveable;

import nodes.Sensor;
import notification.Note;

/**
 * The protocol class handles transmitting and receiving transmissions.
 * @author Niels Thykier
 */
public class Protocol implements Transmitter, DataConstants, Prepareable, EndSteppable, Saveable {

	
	
	/**
	 * List of transmissions that should be sent on.
	 */
	protected TreeSet<Transmission> outgoing = new TreeSet<Transmission>();
	/**
	 * List of transmissions have been sent and a confirmation message is expected for.
	 */
	protected ArrayList<Transmission> sent = new ArrayList<Transmission>();
	/**
	 * The last received but yet evaluated transmission.
	 */
	protected Transmission received = null;
	/**
	 * The transmission being received this step.
	 */
	protected Transmission incomming = null;
	
	/**
	 * Bit-flag that determines whether the protocol may transmit this step
	 */
	public static final int OPTION_SEND_DISABLED 	 	= 0x00000001;
	/**
	 * Bit-flag that determines whether the protocol may receive this step
	 */
	public static final int OPTION_RECEIVE_DISABLED 	= 0x00000002;
	/**
	 * Bit-flag that determines whether the protocol expects to receive a transmission this step
	 */
	public static final int ACTION_RECEIVING = 0x00000004;
	/**
	 * Bit-flag that determines whether the protocol expects to transmit a transmission this step
	 */
	public static final int ACTION_SENDING	= 0x00000008;
	/**
	 * Bit-flag that determines whether the protocol has absolutely nothing to do this step.
	 */
	public static final int ACTION_NOTHING_TO_DO		= 0x00000020;
	
	/**
	 * Bit-mask of the current turn's options and actions.
	 */
	protected int currentTick = 0;
	/**
	 * The id of the sensor it is waiting for, or Sensor.INVALID_SENSOR_ID if it is not waiting for a sensor.
	 */
	protected int waitingForSensor = Sensor.INVALID_SENSOR_ID;
	/**
	 * How many steps it will way before transmitting the next transmission.
	 */
	protected int delayNextTransmission = 0;
	/**
	 * Random generator
	 */
	protected static Random ran = new Random();
	
	
	/**
	 * The sensor the protocol is attached too.
	 */
	private Sensor main;
	
	/**
	 * Constructor for the Protocol.
	 * @param sen The sensor the procotol is attached too.
	 */
	public Protocol(Sensor sen) {
		main = sen;
	}
	
	/* (non-Javadoc)
	 * @see transmissions.Transmitter#receive(transmissions.Transmission)
	 */
	public void receive(Transmission msg) {
		if(incomming != null) {
			Note.sendNote(Note.WARNING, main +": Message from "+ msg.getRespondsableTransmitter()
							+ " corrupted message from "+incomming.getRespondsableTransmitter() + "!");
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
				Note.sendNote(Note.DEBUG, main + " received transmission from " +  msg.getSender());
				if(isForAll) {
					delayNextTransmission = ran.nextInt(12);
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

	/**
	 * Adds a transmission to the list of outgoing transmissions.
	 * @param trans The new transmission.
	 */
	public void addTransmissionToSend(Transmission trans){
		outgoing.add(trans);
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
		if(0 != (currentTick & Protocol.ACTION_SENDING) && 0 == (currentTick & Protocol.ACTION_RECEIVING)
				&& outgoing.size() > 0) {
			if(0 == (currentTick & Protocol.OPTION_SEND_DISABLED)) {
				delayNextTransmission = ran.nextInt(10)+5;
				transmit(outgoing.pollLast());
			} else {
				delayNextTransmission = ran.nextInt(15)+ ran.nextInt(5) + 5;
			}
		} else {
			Note.sendNote(Note.DEBUG, main + ": Nothing to do!" );
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
						NetworkData net = incomming.getData().asNetworkData();
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
				case Data.TYPE_DATA:
				default:
					received = incomming;
					break;
				}
			} else {
				outgoing.add(Transmission.generateReceivedGarbage(main.id));
			}
		}
		incomming = null;
		currentTick = 0;
	}
	
	/**
	 * Loads a protocol instance from an XML node.
	 * @param protocolElement The XML procol Node.
	 * @param main The sensor it is attached too. If null, this will cause NullPointerExceptions later in the execution.
	 * @return The protocol.
	 * @throws XMLParseException Thrown if the XML Node was malformatted.
	 */
	public static Protocol loadFromXMLElement(Node protocolElement, Sensor main) throws XMLParseException {
		NodeList list = protocolElement.getChildNodes();
		int length = list.getLength();
		Node current;
		Protocol toReturn;
		Transmission received = null;
		Transmission incomming = null;
		Integer currentTick = null;
		Integer waitingForSensor = null;
		Integer delay = null;
		TreeSet<Transmission> outgoing = null;
		ArrayList<Transmission> sent = null;
		for(int i = 0; i < length ; i++) {
			current = list.item(i);
			switch(current.getNodeName().charAt(0)) {
			case 'c':
				try {
					if(current.getNodeName().equals("currentTick")) {
						currentTick = Integer.parseInt(DOMxmlParser.getTextNodeValue(current).trim());
					}
				} catch(RuntimeException e) {
					throw new XMLParseException("currentTick tag malformattet in Protocol tag.");
				}
				break;
			case 'd':
					if(current.getNodeName().equals("delay")) {
						try {
							delay = Integer.parseInt(DOMxmlParser.getTextNodeValue(current).trim());
						} catch(RuntimeException e) {
							throw new XMLParseException("delay tag malformattet in Protocol tag.");
						}
					}
				break;
			case 'i':
				if(current.getNodeName().equals("incomming")) {
					NodeList ingoingList = current.getChildNodes();
					int incommingSize = ingoingList.getLength();
					Node currentIncommingNode;
					for(int j = 0 ; j < incommingSize ; j++) {
						currentIncommingNode = ingoingList.item(j);
						switch(currentIncommingNode.getNodeName().charAt(0)) {
						case 't':
							incomming = Transmission.loadFromXMLElement(currentIncommingNode);
							break;
						}
					}
				} else if(current.getNodeName().equals("received")) {
					NodeList receivedList = current.getChildNodes();
					int receivedSize = receivedList.getLength();
					Node currentReceivedNode;
					for(int j = 0 ; j < receivedSize ; j++) {
						currentReceivedNode = receivedList.item(j);
						switch(currentReceivedNode.getNodeName().charAt(0)) {
						case 't':
							received = Transmission.loadFromXMLElement(currentReceivedNode);
							break;
						}
					}
				}
				break;
			case 'o':
				if(current.getNodeName().equals("outgoing")) {
					outgoing = new TreeSet<Transmission>();
					NodeList outgoingList = current.getChildNodes();
					int outgoingSize = outgoingList.getLength();
					Node currentOutgoingNode;
					for(int j = 0 ; j < outgoingSize ; j++) {
						currentOutgoingNode = outgoingList.item(j);
						switch(currentOutgoingNode.getNodeName().charAt(0)) {
						case 't':
							outgoing.add(Transmission.loadFromXMLElement(currentOutgoingNode));
							break;
						}
					}
				}
			case 's':
				if(current.getNodeName().equals("sent")) {
					sent = new ArrayList<Transmission>();
					NodeList sentList = current.getChildNodes();
					int sentSize = sentList.getLength();
					Node currentSentNode;
					for(int j = 0 ; j < sentSize ; j++) {
						currentSentNode = sentList.item(j);
						switch(currentSentNode.getNodeName().charAt(0)) {
						case 't':
							sent.add(Transmission.loadFromXMLElement(currentSentNode));
							break;
						}
					}
				}
			case 'w':
				try {
					if(current.getNodeName().equals("waitingForSensor")) {
						waitingForSensor = Integer.parseInt(DOMxmlParser.getTextNodeValue(current).trim());
					}
				} catch(RuntimeException e) {
					throw new XMLParseException("waitingForSensor tag malformattet in Protocol tag.");
				}
				break;
			}
		}
		toReturn = new Protocol(main);
		
		if(currentTick != null) {
			toReturn.currentTick = currentTick;
		}
		if(waitingForSensor != null){
			toReturn.waitingForSensor = waitingForSensor;
		}
		if(incomming != null) {
			toReturn.incomming = incomming;
		}
		if(received != null) {
			toReturn.received = received;
		}
		if(outgoing != null) {
			toReturn.outgoing = outgoing;
		}
		if(sent != null) {
			toReturn.sent = sent;
		}
		if(delay != null) {
			toReturn.delayNextTransmission = delay;
		} else throw new XMLParseException("Missing delay tag in protocol");
			
		return toReturn;
	}
	
	

	/**
	 * Returns the bit-flag for options for the current turn.
	 * @return A bit-flag for options and actions for the current turn.
	 */
	public int getCurrentTick() {
		return currentTick;
	}

	/**
	 * How long it will wait before transmitting its next transmissions.
	 * @return The delay measured in turns.
	 */
	public int getDelayNextTransmission() {
		return delayNextTransmission;
	}

	/**
	 * The transmission gotten this step. (May be null, if none was received).
	 * This transmission may be corrupted before round end.
	 * @return The incomming transmission.
	 */
	public Transmission getIncomming() {
		return incomming;
	}

	/**
	 * The incomming transmission becomes the received transmission in the end step if it was not corrupted. (May be null, if none was received).
	 * @return The received transmission
	 */
	public Transmission getReceived() {
		return received;
	}

	/**
	 * Get the list of outgoing transmissions
	 * @return The outgoing transmissions
	 */
	public TreeSet<Transmission> getOutgoing() {
		return outgoing;
	}

	/**
	 * Get the list of sent transmissions
	 * @return The sent transmissions (that needs 
	 */
	public ArrayList<Transmission> getSent() {
		return sent;
	}

	/**
	 * Fetch the id of the sensor it is waiting for, if any. 
	 * @return The id of the sensor it is waiting for or Sensor.INVALID_SENSOR_ID if it is not waiting.
	 */
	public int getWaitingForSensor() {
		return waitingForSensor;
	}

	public Element generateXMLElement(Document doc) {
		Element protocolElement = doc.createElement("protocol");
		Element delayElement = doc.createElement("delay");
		delayElement.appendChild(doc.createTextNode(String.valueOf(this.delayNextTransmission)));
		protocolElement.appendChild(delayElement);
		if(currentTick != 0) {
			Element currentTickElement = doc.createElement("currentTick");
			currentTickElement.appendChild(doc.createTextNode(String.valueOf(currentTick)));
			protocolElement.appendChild(currentTickElement);
		}
		if(waitingForSensor > Sensor.INVALID_SENSOR_ID) {
			Element waitingForElement = doc.createElement("waitingForSensor");
			waitingForElement.appendChild(doc.createTextNode(String.valueOf(waitingForSensor)));
			protocolElement.appendChild(waitingForElement);
		}
		if(incomming != null) {
			Element incommingElement = doc.createElement("incomming");
			incommingElement.appendChild(incomming.generateXMLElement(doc));
			protocolElement.appendChild(incommingElement);
		}
		if(received != null) {
			Element receivedElement = doc.createElement("received");
			receivedElement.appendChild(received.generateXMLElement(doc));			
			protocolElement.appendChild(receivedElement);
		}
		if(outgoing.size() > 0) {
			Element outgoingElement = doc.createElement("outgoing");
			for(Transmission list : outgoing) {
				outgoingElement.appendChild(list.generateXMLElement(doc));			
			}
			protocolElement.appendChild(outgoingElement);
		}
		return protocolElement;
	}

}
