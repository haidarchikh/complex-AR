package d7002d.Laboration4.Applications;

import java.util.HashMap;

import d7002d.Laboration4.Configs;
import d7002d.Laboration4.EventHandle;
import d7002d.Laboration4.NetAddr;
import d7002d.Laboration4.SimEngine;
import d7002d.Laboration4.SimEnt;
import d7002d.Laboration4.Utils;
import d7002d.Laboration4.Events.Event;
import d7002d.Laboration4.Events.PingMsg;
import d7002d.Laboration4.Events.Segment;
import d7002d.Laboration4.Events.TimerEvent;
import d7002d.laboration4.Connectables.NetworkInterface;

public class Ping extends SimEnt implements Application {
	private static final double DEFAULT_INTERVAL = 5.0;
	protected NetworkInterface _interface = null;
	protected int _destinationPort = 3000;
	private HashMap<NetAddr, HashMap<Integer, Double[]>> _outstandingPings;
	private NetAddr _pingee;
	private EventHandle _nextPing;
	private int _donePings;
	private int _maxPings;
	private double _pingInterval;
	
	public Ping(NetworkInterface networkInterface) {
		_interface = networkInterface;
		_interface.bindSocket(_destinationPort, this);
		_outstandingPings = new HashMap<NetAddr, HashMap<Integer, Double[]>>();
	}
	
	@Override
	public String getName() {
		return "Ping";
	}
	
	private Double getRTT(NetAddr address, int sequence) {
		HashMap<Integer, Double[]> pingrecord = _outstandingPings.get(address);
		if (null != pingrecord) {
			Double[] timestamp = pingrecord.get(sequence);
			if (null != timestamp && null != timestamp[1]) {
				return (timestamp[1]-timestamp[0]);
			}
		}
		return null;
	}
	
	@Override
	public void handleSegment(NetAddr from, NetAddr to, Segment segment) {
		
		if (null != from && null != to && null != segment && segment instanceof Segment && segment.getPayload() instanceof PingMsg) {
			PingMsg msg = (PingMsg) segment.getPayload();
			if (msg.isPing())
				pong(from, segment.getSeq());
			
			else if (msg.isPong()) {
				updateRecord(from, Segment.decSeq(segment.getSeq()), SimEngine.getTime());
				Double rtt = getRTT(from, Segment.decSeq(segment.getSeq()));
				
				if (null != rtt) {
					Utils.logMessage(_interface.getId(), getName(), String.format("Finished a ping that took %." + Configs.TIME_DECIMALS + "f units of time.", rtt));
				}
			}
		}
	}
	
	private void insertRecord(NetAddr address, int sequence, double pingTime) {
		Double[] timestamp = {pingTime, null};
		HashMap<Integer, Double[]> pingrecord = new HashMap<Integer, Double[]>();
		
		pingrecord.put(sequence, timestamp);
		_outstandingPings.put(address, pingrecord);
	}
	
	public void ping(NetAddr address) {
		Utils.logMessage(_interface.getId(), getName(), "Sending a ping.");
		int seq = Utils.randomNumber(Segment.MAX_SEQ);
		_interface.sendMessage(address, _destinationPort, seq, PingMsg.PING);
		insertRecord(address, seq, SimEngine.getTime());
	}

	public void pings(Integer pings, NetAddr address) {
		pings(pings, address, DEFAULT_INTERVAL);
	}

	public void pings(Integer pings, NetAddr address, double interval) {
		if (null != _nextPing) {
			SimEngine.instance().deregister(_nextPing);
			_nextPing = null;
		}
		_pingee = address;
		_maxPings = pings;
		_pingInterval = interval;
		_nextPing = send(this, new TimerEvent(), SimEngine.NOW);
	}
	
	public void pings(NetAddr address) {
		pings(Integer.MAX_VALUE, address, DEFAULT_INTERVAL);
	}
	
	public void pings(NetAddr address, double interval) {
		pings(Integer.MAX_VALUE, address, interval);
	}
	
	private void pong(NetAddr address, int sequence) {
		Utils.logMessage(_interface.getId(), getName(), "Replying to a ping.");
		_interface.sendMessage(address, _destinationPort, Segment.incSeq(sequence), PingMsg.PONG);
	}
	
	@Override
	public void recv(SimEnt source, Event event) {
		if (event instanceof TimerEvent) {
			if (_donePings <= _maxPings) {
				ping(_pingee);
				_donePings += 1;
				_nextPing = send(this, new TimerEvent(), _pingInterval);
			}
		}
	}
	
	public void stop() {
		if (null != _nextPing) {
			SimEngine.instance().deregister(_nextPing);
			_nextPing = null;
		}
	}

	private void updateRecord(NetAddr address, int sequence, double pongTime) {
		HashMap<Integer, Double[]> pingrecord = _outstandingPings.get(address);
		if (null != pingrecord) {
			Double[] timestamp = pingrecord.get(sequence);
			if (null != timestamp) {
				Double[] newTimestamp = {timestamp[0], pongTime};
				pingrecord.put(sequence, newTimestamp);
			}
		}
	}
}
