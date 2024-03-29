package test;

import junit.framework.TestCase;
import nodes.Sensor;
import transmissions.Data;
import transmissions.Protocol;
import transmissions.Transmission;

/**
 * Testcase for the Protocol class.
 * @author Niels Thykier
 * @author Morten Soerensen
 */
public class ProtocolTest extends TestCase {
	
	/**
	 * The protocol object to test.
	 */
	private Protocol protocol;
	/**
	 * A sensor in the test.
	 */
	private Sensor sensor;
	/**
	 * Another sensor in the test.
	 */
	private Sensor sensor2;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		sensor2 = Sensor.newInstance();
		sensor = Sensor.newInstance();
		sensor2.addLinkToSensor(sensor);
		sensor.addLinkToSensor(sensor2);
		protocol = sensor.getProtocol();
	}
	
	
	/**
	 * Tests the method AddTransmissionToSend.
	 */
	public void testAddTransmissionToSend() {
		Transmission trans = new Transmission(1, 2, Data.generateData(new Object()));
		protocol.addTransmissionToSend(trans);
		assertTrue(protocol.getOutgoing().contains(trans));
	}


	/**
	 * A positive test of Receive
	 */
	public void testReceivePositive() {
		Transmission trans = new Transmission(sensor.id, sensor2.id, sensor.id
				, Data.generateData(new Object()));
		protocol.receive(Transmission.generateSendRequest(sensor.id, sensor2.id));
		protocol.receive(trans);
		assertTrue(protocol.getIncomming().equals(trans));
	}
	
	/**
	 * A negative test of Receive
	 */
	public void testReceiveNegative() {
		Transmission trans = new Transmission(sensor.id, sensor2.id, sensor.id
				, Data.generateData(new Object()));
		protocol.receive(Transmission.generateSendRequest(sensor.id, sensor2.id));
		protocol.receive(trans);
		protocol.receive(trans);
		assertTrue(protocol.getIncomming().getMessageType() == Data.TYPE_GARBAGE);
	}

	/**
	 * Test of the method Transmit
	 */
	public void testTransmit() {
		Transmission trans = new Transmission(sensor2.id, sensor.id, sensor.id
				, Data.generateData(new Object()));
		protocol.transmit(trans);
		assertTrue(sensor2.getProtocol().getIncomming().equals(trans));
	}

	
	/**
	 * Test of the method EndStep
	 */
	public void testEndStep() {
		Transmission trans = new Transmission(sensor.id, sensor2.id, sensor.id
				, Data.generateData(new Object()));
		protocol.receive(Transmission.generateSendRequest(sensor.id, sensor2.id));
		protocol.receive(trans);
		protocol.endStep();
		assertTrue(protocol.getReceived().equals(trans));
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		this.protocol = null;
		this.sensor = null;
		this.sensor2 = null;
	}

}
