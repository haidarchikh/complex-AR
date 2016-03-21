package d7002d.Laboration4.Applications;

import d7002d.Laboration4.NetAddr;
import d7002d.Laboration4.SimEnt;
import d7002d.Laboration4.Events.Event;
import d7002d.Laboration4.Events.Packet;
import d7002d.Laboration4.Events.Segment;
import d7002d.Laboration4.Events.TimerEvent;
import d7002d.laboration4.Connectables.NetworkInterface;

/**
 * Base class for traffic generators. Contains default values that may be
 * overridden/overwritten.
 * 
 * 
 */
public abstract class TrafficGeneratorBase extends SimEnt implements
		Application {
	protected int _numberOfMessages = 10;
	protected int _messageStartSequence = 0;
	protected int _messageSequence = 0;
	protected double _interstreamGap = 0.0;
	protected double _intermessageGap = 5.0;
	protected double _standardDeviationScale = 1.0;
	protected int _numberOfMessagesSent = 0;
	protected boolean _sending = false;
	protected NetAddr _destination = null;
	protected int _destinationPort = 4096;
	protected NetworkInterface _interface = null;

	public TrafficGeneratorBase(NetworkInterface networkInterface) {
		_interface = networkInterface;
	}

	public abstract double getDelay();

	public void handleSegment(NetAddr from, NetAddr to, Segment segment) {
		// Traffic generators needn't handle incoming segments.
	}

	public void recv(SimEnt source, Event event) {
		if (_sending && event instanceof TimerEvent) {
			Segment segment = new Segment(_destinationPort, _messageSequence++,
					null);
			_interface.sendSegment(_destination, segment);
			_numberOfMessagesSent += 1;
			if (_numberOfMessagesSent < _numberOfMessages)
				send(this, new TimerEvent(), getDelay());
			else
				_sending = false;
		}
	}

	protected void resume() {
		if (!_sending) {
			_sending = true;
			send(this, new TimerEvent(), _interstreamGap);
		}
	}

	/**
	 * The method to call when initiating traffic generation.
	 * 
	 * @param destination
	 *            The destination of the traffic.
	 * @param port
	 *            The destination port to send traffic to.
	 */
	protected void start(NetAddr destination, int port) {
		if (!_sending) {
			_destination = destination;
			_messageSequence = _messageStartSequence;
			_destinationPort = port;
			_sending = true;
			send(this, new TimerEvent(), _interstreamGap);
		}
	}

	/**
	 * Stop traffic generation.
	 */
	protected void stop() {
		_sending = false;
	}
}
