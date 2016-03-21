package d7002d.laboration4.Connectables;

import java.util.ArrayList;

import d7002d.Laboration4.SimEngine;
import d7002d.Laboration4.SimEnt;
import d7002d.Laboration4.Utils;
import d7002d.Laboration4.Events.Data;
import d7002d.Laboration4.Events.Event;

/**
 * This class implements a link without any loss, jitter or delay.
 */
public class Link extends SimEnt implements Connectable {
	protected String _id;
	protected Connectable _connectorA = null;
	protected Connectable _connectorB = null;

	public Link(String id) {
		super();
		if (null == id || id.equals(""))
			_id = Utils.randomString(6);
		else
			_id = id;
	}

	/**
	 * Connects the link to some simulation entity like a node, switch, router
	 * etc. Will not replace any current connection.
	 * 
	 * @param entity
	 */
	@Override
	public void connect(Connectable entity) {
		if (_connectorA == entity || _connectorB == entity)
			return;
		else if (null == _connectorA) {
			_connectorA = entity;
			entity.connect(this);
		}
		else if (null == _connectorB) {
			_connectorB = entity;
			entity.connect(this);
		}
	}

	public void connect(Connectable entityA, Connectable entityB) {
		if (entityA == _connectorA || entityA == _connectorB)
			return;
		if (entityB == _connectorA || entityB == _connectorB)
			return;
		if (null == _connectorA) {
			_connectorA = entityA;
			entityA.connect(this);
		}
		if (null == _connectorB) {
			_connectorB = entityB;
			entityB.connect(this);
		}
	}

	@Override
	public void disconnect(Connectable entity) {
		if (_connectorA == entity) {
			Connectable tmp = _connectorA;
			_connectorA = null;
			tmp.disconnect(this);
		} else if (_connectorB == entity) {
			Connectable tmp = _connectorB;
			_connectorB = null;
			tmp.disconnect(this);
		}
	}

	@Override
	public void disconnectAll() {
		if (null != _connectorA) {
			Connectable tmp = _connectorA;
			_connectorA = null;
			tmp.disconnect(this);
		}
		if (null != _connectorB) {
			Connectable tmp = _connectorB;
			_connectorB = null;
			tmp.disconnect(this);
		}
	}

	public ArrayList<Connectable> getConnected() {
		ArrayList<Connectable> connected = new ArrayList<Connectable>();
		if (null != _connectorA)
			connected.add(_connectorA);
		if (null != _connectorB)
			connected.add(_connectorB);
		return connected;
	}

	@Override
	public String getId() {
		return _id;
	}

	@Override
	public boolean isConnected(Connectable entity) {
		return ((_connectorA == entity && entity.isConnected(this)) || (_connectorB == entity && entity.isConnected(this)));
	}

	public void recv(SimEnt source, Event event) {
		// Called when a message enters the link.
		if (event instanceof Data) {
			if (null == _connectorA || null == _connectorB) {
				Utils.logMessage(_id, String.format("Link received data from %s,  and dropped because  had nowhere to send it.", ((Connectable) source).getId()));
				return;
			}
			Connectable destination;
			if (source == _connectorA)
				destination = _connectorB;
			else
				destination = _connectorA;
			Utils.logMessage(_id,
					String.format("Link received data from %s, passes it through to %s.", ((Connectable) source).getId(), destination.getId()));
			send((SimEnt) destination, event, SimEngine.NOW);
		}
	}
}