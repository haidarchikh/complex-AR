package d7002d.laboration4.Connectables;

import java.util.HashMap;

import d7002d.Laboration4.Configs;
import d7002d.Laboration4.LinkLayerAddr;
import d7002d.Laboration4.SimEngine;
import d7002d.Laboration4.SimEnt;
import d7002d.Laboration4.Utils;
import d7002d.Laboration4.Events.Event;
import d7002d.Laboration4.Events.Frame;
import d7002d.Laboration4.Events.Packet;

/**
 * This class implements a simple switch.
 */
public class Switch implements Connectable {
	private class SwitchPort extends SimEnt implements Connectable {
		private String _id;
		private Connectable _peer;
		private int _port;
		private Switch _switch;
		
		public SwitchPort(Switch switchingHub, String id, int port) {
			_switch = switchingHub;
			_id = id;
			_port = port;
		}

		@Override
		public void connect(Connectable entity) {
			if (null == _peer) {
				_peer = entity;
				Utils.logMessage(getId(),String.format("Connected %s.", entity.getId()));
				_peer.connect(this);
			}
		}

		@Override
		public void disconnect(Connectable entity) {
			if (_peer == entity) {
				disconnectAll();
			}
		}
		@Override
		public void disconnectAll() {
			if (null != _peer) {
				Connectable tmp = _peer;
				_peer = null;
				Utils.logMessage(getId(),
						String.format("Disconnected %s.", tmp.getId()));
				tmp.disconnect(this);
			}
		}

		@Override
		public String getId() {
			return _id;
		}

		public Connectable getPeer() {
			return _peer;
		}

		public int getPort() {
			return _port;
		}
		
		@Override
		public boolean isConnected(Connectable entity) {
			return (_peer == entity);
		}

		@Override
		public void recv(SimEnt source, Event event) {
			if (event instanceof Frame) {
				Utils.logMessage(getId(), "RECEIVED FRAME: " + (Frame) event);
				_switch.handleFrame(_port, (Frame) event);
			} else
				Utils.logMessage(getId(), "Received garbage data.");
		}
		
		public void sendFrame(Frame frame) {
			if (null == _peer)
				Utils.logMessage(getId(),
						"Was supposed to send a frame, but was not connected to anything.");
			else {
				Utils.logMessage(getId(), "SEND FRAME: " + frame);
				send((SimEnt) _peer, frame, SimEngine.NOW);
			}
		}
		
	}
	private SwitchPort[] _ports;
	private HashMap<LinkLayerAddr, Integer> _switchTable;
	private String _id;

	/**
	 * When creating the switch, the number of ports must be specified.
	 * 
	 * @param ports
	 */
	public Switch(String id, int ports) {
		if (null == id || id.equals(""))
			_id = Utils.randomString(Configs.ID_LENGTH);
		else
			_id = id;
		_ports = new SwitchPort[ports];
		for (int i = 0; i < ports; i++)
			_ports[i] = new SwitchPort(this, String.format("%s_%d", _id, i), i);
		_switchTable = new HashMap<LinkLayerAddr, Integer>();
	}

	
	@Override
	public void connect(Connectable entity) {
		for (SwitchPort sp : _ports)
			if (null == sp.getPeer()) {
				sp.connect(entity);
				return;
			}
	}
	
	public void connect(int port, Connectable entity) {
		_ports[port].connect(entity);
	}

	@Override
	public void disconnect(Connectable entity) {
		for (SwitchPort sp : _ports)
			if (sp.getPeer() == entity) {
				sp.disconnect(entity);
				return;
			}
	}

	@Override
	public void disconnectAll() {
		for (SwitchPort sp : _ports)
			sp.disconnectAll();
	}

	@Override
	public String getId() {
		return _id;
	}

	public SwitchPort getPort(int port) {
		return _ports[port];
	}
	
	public void handleFrame(int port, Frame frame) {
		_switchTable.put(frame.getSrc(), port);
		if (frame.getDst().isMulticast()) {
			for (SwitchPort sp : _ports) { 
				if (sp.getPort() != port)
					sp.sendFrame(frame);
			}
		} else {
			Integer to = _switchTable.get(frame.getDst());
			if (null != to)
				_ports[to].sendFrame(frame);
			else
				Utils.logMessage(getId(),"Was supposed to send a frame, but did not know the link layer address of the destination.");
		}
	}
	
	@Override
	public boolean isConnected(Connectable entity) {
		for (SwitchPort sp : _ports)
			if (null != sp.getPeer())
				return true;
		return false;
	}
}
