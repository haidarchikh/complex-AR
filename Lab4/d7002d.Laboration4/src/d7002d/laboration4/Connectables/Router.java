package d7002d.laboration4.Connectables;

import java.util.HashMap;

import d7002d.Laboration4.Configs;
import d7002d.Laboration4.NetAddr;
import d7002d.Laboration4.SimEnt;
import d7002d.Laboration4.Utils;
import d7002d.Laboration4.Events.Event;
import d7002d.Laboration4.Events.Packet;


/**
 * This class implements a router.
 */
public class Router extends SimEnt {
	private String _id;
	private RouterInterface[] _interfaces;
	private HashMap<Integer, Integer> _routingTable;

	public Router(String id, int interfaces) {
		if (null == id || id.equals(""))
			_id = Utils.randomString(Configs.ID_LENGTH);
		else
			_id = id;
		_interfaces = new RouterInterface[interfaces];
		for (int i = 0; i < interfaces; i++)
			_interfaces[i] = new RouterInterface(this, String.format("%s_%d",_id, i), null);
		    _routingTable = new HashMap<Integer, Integer>();
	}

	public void createRouteTEMPORARY(int networkAddress, int routerPort) {
		changeRoutingTable(networkAddress, routerPort);
	}

	/**
	 * Assign a network address to a specific router interface.
	 * 
	 * @param port
	 *            The router port which the router interface uses.
	 * @param address
	 *            The address to assign to the router interface.
	 */
	public void assignAddress(int port, NetAddr address) {
		_interfaces[port].assignAddr(address);
		changeRoutingTable(address.netId(), port);
	}

	private void changeRoutingTable(int networkAddress, int routerPort) {
		Utils.logMessage(getId(), String.format(
				"Adding to routing table: %s @ port %d", networkAddress,
				routerPort));
		_routingTable.put(networkAddress, routerPort);
	}

	/**
	 * Connect a networking entity to the router, on the first free port.
	 * 
	 * @param entity
	 *            The entity to connect to the router.
	 */
	public void connect(Connectable entity) {
		for (RouterInterface ri : _interfaces)
			if (null == ri.getPeer()) {
				ri.connect(entity);
				return;
			}
	}

	/**
	 * Connect a networking entity to the router, on a specific port.
	 * 
	 * @param port
	 *            The router port to connect the entity to.
	 * @param entity
	 *            The entity to connect to the router.
	 */
	public void connect(int port, Connectable entity) {
		_interfaces[port].connect(entity);
	}

	/**
	 * Disconnect a networking entity from the router, disregarding on which
	 * port or even if it is connected.
	 * 
	 * @param entity
	 *            The entity to disconnect from the router.
	 */
	public void disconnect(Connectable entity) {
		for (RouterInterface ri : _interfaces)
			if (ri.getPeer() == entity)
				ri.disconnect(entity);
	}

	/**
	 * Disconnect a networking entity from the router, if it is connected to a
	 * specific port.
	 * 
	 * @param port
	 *            The router port to disconnect the entity from.
	 * @param entity
	 *            The entity to disconnect from the router.
	 */
	public void disconnect(int port, Connectable entity) {
		_interfaces[port].disconnect(entity);
	}

	/**
	 * Disconnect all networking entities from the router.
	 */
	public void disconnectAll() {
		for (RouterInterface ri : _interfaces)
			ri.disconnectAll();
	}

	public String getId() {
		return _id;
	}

	/**
	 * This method searches for an entry in the routing table that matches the
	 * network number in the destination field of a messages. The interface
	 * represents that network number is returned.
	 * 
	 * @param networkAddress
	 * @return
	 */
	private RouterInterface getInterface(int networkAddress) {
		Integer port = _routingTable.get(networkAddress);
		if (null == port)
			return null;
		else
			return _interfaces[port];
	}

	/**
	 * Give access to a router interface given a specific port.
	 * 
	 * @param port
	 *            The router port which the router interface uses.
	 * @return The router interface.
	 */
	public RouterInterface getPort(int port) {
		return _interfaces[port];
	}

	public void handlePacket(Packet packet) {
		NetAddr dst = packet.getDst();
		RouterInterface ri = getInterface(dst.netId());
		if (null != ri) {
			// There is a known route for the destination network.
			Utils.logMessage(_id, packet.getSrc(), packet.getDst(), "Routing packet...");
			ri.sendPacket(packet);
		} else {
			// There is no known route for the destination network.
			Utils.logMessage(_id, packet.getSrc(), packet.getDst(),
					"Dropped packet: no path.");
		}
	}

	/**
	 * Check whether any networking entity is connected to the router on any
	 * port.
	 * 
	 * @return <code>true</code> if any entity is connected on any port,
	 *         <code>false</code> otherwise.
	 */
	public boolean isConnected() {
		for (RouterInterface ri : _interfaces)
			if (null != ri.getPeer())
				return true;
		return false;
	}

	/**
	 * Check whether a specific networking entity is connected to the router on
	 * any port.
	 * 
	 * @param entity
	 *            The entity to check for connectedness.
	 * @return <code>true</code> if the entity is connected on any port,
	 *         <code>false</code> otherwise.
	 */
	public boolean isConnected(Connectable entity) {
		for (RouterInterface ri : _interfaces)
			if (ri.getPeer() == entity)
				return true;
		return false;
	}

	public void recv(SimEnt source, Event event) {
	}
}
