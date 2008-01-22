package test;

import nodes.Sensor;
import transmissions.NetworkData;
import transmissions.Protocol;
import transmissions.Transmission;
import junit.framework.TestCase;

/**
 * @author Niels Thykier
 */
public class ProtocolTest extends TestCase {
	
	private Protocol protocol;
	private Sensor sensor;
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
	 * 
	 */
	public void testReceive() {
		
		fail("Not yet implemented");
	}

	/**
	 * 
	 */
	public void testTransmit() {
		protocol.transmit(new Transmission(sensor2.id,sensor.id,sensor.id,new NetworkData(0, sensor.id)));
		protocol.endStep();
		System.out.println(sensor.id);
		System.out.println(sensor2.getNearestTerminal());
		assertTrue(sensor2.getNearestTerminal() == sensor.id);
	}

	/**
	 * 
	 */
	public void testAddTransmissionToSend() {
		fail("Not yet implemented");
	}

	/**
	 * 
	 */
	public void testGetCurrentTick() {
		protocol.receive(Transmission.generateSendRequest(sensor.id, sensor2.id));
		protocol.transmit(Transmission.generateCorruptTransmission());
		protocol.receive(Transmission.generateSendRequest(sensor.id, sensor2.id));
		protocol.transmit(new Transmission(sensor2.id,sensor.id,sensor.id,new NetworkData(0,sensor.id)));
		protocol.endStep();
		protocol.step();
		System.out.println(protocol.getCurrentTick());
		assertTrue(protocol.getCurrentTick() == 4);
	}

	/**
	 * 
	 */
	public void testGetDelayNextTransmission() {
		fail("Not yet implemented");
	}

	/**
	 * 
	 */
	public void testGetIncomming() {
		fail("Not yet implemented");
	}

	/**
	 * 
	 */
	public void testGetIngoing() {
		fail("Not yet implemented");
	}

	/**
	 * 
	 */
	public void testGetOutgoing() {
		fail("Not yet implemented");
	}

	/**
	 * 
	 */
	public void testGetSent() {
		fail("Not yet implemented");
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		this.protocol = null;
		this.sensor = null;
		this.sensor2 = null;
	}

}
