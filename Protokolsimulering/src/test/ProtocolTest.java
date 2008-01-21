package test;

import nodes.Sensor;
import nodes.Sensor.SensorImplementation;
import transmissions.NetworkData;
import transmissions.Protocol;
import transmissions.Transmission;
import junit.framework.TestCase;

public class ProtocolTest extends TestCase {
	
	private Protocol protocol;
	private SensorImplementation sensor;
	private SensorImplementation sensor2;
	
	protected void setUp() throws Exception {
		super.setUp();
		sensor2 = Sensor.newInstance().getReal();
		sensor = Sensor.newInstance().getReal();
		sensor2.addLinkToSensor(sensor);
		sensor.addLinkToSensor(sensor2);
		protocol = sensor.getProtocol();
	}

	public void testReceive() {
		
		fail("Not yet implemented");
	}

	public void testTransmit() {
		protocol.transmit(new Transmission(sensor2.id,sensor.id,sensor.id,new NetworkData(0, sensor.id)));
		protocol.endStep();
		System.out.println(sensor.id);
		System.out.println(sensor2.getNearestTerminal());
		assertTrue(sensor2.getNearestTerminal() == sensor.id);
	}

	public void testAddTransmissionToSend() {
		fail("Not yet implemented");
	}

	public void testGetStatus() {
		protocol.receive(Transmission.generateSendRequest(sensor.id, sensor2.id));
		protocol.transmit(Transmission.generateCorruptTransmission());
		protocol.receive(Transmission.generateSendRequest(sensor.id, sensor2.id));
		protocol.transmit(new Transmission(sensor2.id,sensor.id,sensor.id,new NetworkData(0,sensor.id)));
		protocol.endStep();
		protocol.step();
		System.out.println(protocol.getStatus());
		assertTrue(protocol.getStatus() == 4);
	}

	public void testPrepare() {
		protocol.prepare();
		fail("Not yet implemented");
	}

	public void testStep() {
		fail("Not yet implemented");
	}

	public void testEndStep() {
		fail("Not yet implemented");
	}

	public void testGetCurrentTick() {
		fail("Not yet implemented");
	}

	public void testGetDelayNextTransmission() {
		fail("Not yet implemented");
	}

	public void testGetIncomming() {
		fail("Not yet implemented");
	}

	public void testGetIngoing() {
		fail("Not yet implemented");
	}

	public void testGetOutgoing() {
		fail("Not yet implemented");
	}

	public void testGetSent() {
		fail("Not yet implemented");
	}

	public void testGetWaitingForSensor() {
		fail("Not yet implemented");
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

}
