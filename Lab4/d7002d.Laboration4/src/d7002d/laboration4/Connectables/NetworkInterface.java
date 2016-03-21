package d7002d.laboration4.Connectables;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import d7002d.Laboration4.Configs;
import d7002d.Laboration4.EventHandle;
import d7002d.Laboration4.LinkLayerAddr;
import d7002d.Laboration4.MapEntry;
import d7002d.Laboration4.NetAddr;
import d7002d.Laboration4.SimEngine;
import d7002d.Laboration4.SimEnt;
import d7002d.Laboration4.TrafficSink;
import d7002d.Laboration4.Utils;
import d7002d.Laboration4.Applications.Application;
import d7002d.Laboration4.Events.CareofPacket;
import d7002d.Laboration4.Events.ControlMsg;
import d7002d.Laboration4.Events.Data;
import d7002d.Laboration4.Events.Event;
import d7002d.Laboration4.Events.Frame;
import d7002d.Laboration4.Events.Message;
import d7002d.Laboration4.Events.NbrData;
import d7002d.Laboration4.Events.Packet;
import d7002d.Laboration4.Events.RouterData;
import d7002d.Laboration4.Events.Segment;
import d7002d.Laboration4.Events.TimerEvent;
import d7002d.Laboration4.Events.TimerEventMsg;
import d7002d.Laboration4.TrafficSink.Statistics;

/**
 * This class implements a network interface.
 */
public class NetworkInterface extends SimEnt implements Connectable {
	public static final String NO_DUPLICATE_ADDRESS = "NO_DUPLICATE_ADDRESS";
	public static final String SOLICIT_ROUTER = "SOLICIT_ROUTER";
	public static final String NO_NEIGHBOR = "NO_NEIGHBOR";
	private LinkLayerAddr _linkAddr;
	private String _id;
	private Connectable _peer;
	private NetAddr _netAddr;
	private HashMap<Integer, Application> _sockets;
	private TrafficSink _trafficSink = null;
	private HashMap<NetAddr, MapEntry<LinkLayerAddr, Double>> _neighbors;
	private HashMap<LinkLayerAddr, Double> _routers;
	private HashMap<NetAddr, NetAddr> _careofs;
	protected ConcurrentHashMap<NetAddr, MapEntry<Packet, Double>> _sendQueue;
	private EventHandle _autoconf;
	protected boolean _staticAddr;
	
	/**
	 * Construct a NetworkInterface object. If no valid identifier is given, a
	 * random one will be generated. If no valid network address is given, the
	 * network interface will acquire a loopback address.
	 * 
	 * @param id
	 *            The identifier for this network interface.
	 * @param addr
	 *            The network address for this network interface.
	 */
	public NetworkInterface(String id, NetAddr addr) {
		super();
		_linkAddr = LinkLayerAddr.getLinkLayerAddress(null);
		if (null == id || id.equals(""))
			_id = Utils.randomString(Configs.ID_LENGTH);
		else
			_id = id;
		if (null == addr) {
			_netAddr = NetAddr.UNSPECIFIED;
			_staticAddr = false;
		} else {
			_netAddr = addr;
			_staticAddr = true;
		}
		_sockets = new HashMap<Integer, Application>();
		_trafficSink = new TrafficSink();
		_neighbors = new HashMap<NetAddr, MapEntry<LinkLayerAddr, Double>>();
		_routers = new HashMap<LinkLayerAddr, Double>();
		_careofs = new HashMap<NetAddr, NetAddr>();
		_sendQueue = new ConcurrentHashMap<NetAddr, MapEntry<Packet, Double>>();
		Utils.logMessage(getId(), String.format("Link layer address is %s.", getLinkAddr()));
	}
	
	protected void addCareof(NetAddr absentAddr, NetAddr careofAddr) {
		Utils.logMessage(
				getId(),
				String.format("Adding care-of address: %s takes care of %s.",careofAddr, absentAddr));
		_careofs.put(absentAddr, careofAddr);
		pruneQueue();
	}
	
	protected void addNeighbor(NetAddr netAddr, LinkLayerAddr linkAddr) {
		/*
		 * Don't add link-local addresses.
		 */
		if (netAddr.isLinkLocal())
			return;
		else {
			Utils.logMessage(getId(),String.format("Adding neighbor: %s = %s",netAddr, linkAddr));
			_neighbors.put(netAddr, new MapEntry<LinkLayerAddr, Double>(linkAddr, SimEngine.getTime()));
		}
		pruneQueue();
	}

	private void addQueue(NetAddr dst, Packet pkt, double time) {
		Utils.logMessage(
				getId(),String.format("Queueing packet to %s.",dst));
		_sendQueue.put(dst, new MapEntry<Packet, Double>(pkt, time));
	}

	protected void addQueue(Packet pkt) {
		_sendQueue.put(pkt.getDst(), new MapEntry<Packet, Double>(pkt, SimEngine.getTime()));
	}

	public void createRouterTEMPORARY(LinkLayerAddr addr) {
		Utils.logMessage( getId(), String.format("Adding router: %s", addr));
		_routers.put(addr, SimEngine.getTime());
	}
	
	public void assignAddr(NetAddr addr) {
		if (null == addr) {
			_netAddr = NetAddr.UNSPECIFIED;
			_staticAddr = false;
		} else {
			_netAddr = addr;
			if (!addr.isLinkLocal())
				_staticAddr = true;
		}
		Utils.logMessage(
				getId(),
				String.format("Network address set to %s.",_netAddr.dotAddress()));
	}
	
	public void autoconfigure() {
		
		_staticAddr = false;
		
		if (null != _autoconf)
		   SimEngine.instance().deregister(_autoconf);
		   unassignAddr();
	
		_neighbors = new HashMap<NetAddr, MapEntry<LinkLayerAddr, Double>>();
		_routers = new HashMap<LinkLayerAddr, Double>();
		_careofs = new HashMap<NetAddr, NetAddr>();
		_sendQueue = new ConcurrentHashMap<NetAddr, MapEntry<Packet, Double>>();
		
		NetAddr netAddr = NetAddr.getLinkLocalAddress(Utils.randomNumber(NetAddr.NODE_ID_MAX));
		
		while (netAddr == NetAddr.UNSPECIFIED)
		netAddr = NetAddr.getLinkLocalAddress(Utils.randomNumber(NetAddr.NODE_ID_MAX));
		assignAddr(netAddr);
		solicitNeighbor(getRealNetAddr());
		
		_autoconf = send(this, new TimerEventMsg(NO_DUPLICATE_ADDRESS),
				Configs.DAD_TIMING);
	}
	
	/**
	 * If an application is to listen to some application layer port, it must
	 * bind to the port on the network interface using this method.
	 * 
	 * @param port
	 *            The port to bind to.
	 * @param app
	 *            The application to call when segments arrive on this interface
	 *            and port.
	 */
	public void bindSocket(int port, Application app) {
		/*
		 * If invalid request (bad port or application), don't bind. Else if
		 * socket not already bound, bind. Else don't try.
		 */
		if (port <= 0 || port > Segment.MAX_PORT || null == app)
			return;
		
		else if (null == _sockets.get(port)) {
			_sockets.put(port, app);
			Utils.logMessage(
					getId(),
					String.format("Application %s bound to port %d.",
							app.getName(), port));
		}
	}
	
	@Override
	public void connect(Connectable entity) {
		if (null == _peer) {
			_peer = entity;
			Utils.logMessage(getId(),
					String.format("Connected %s.", entity.getId()));
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
			// TODO: Evaluate whether entities should call disconnect on entities that are disconnected from.
		}
	}

	protected NetAddr getCareof(NetAddr absentAddr) {
		return _careofs.get(absentAddr);
	}

	/**
	 * Get the identifier of this network interface.
	 * 
	 * @return The identifier.
	 */
	public String getId() {
		return _id;
	}

	public LinkLayerAddr getLinkAddr() {
		return _linkAddr;
	}

	protected LinkLayerAddr getNeighbor(NetAddr neighbor) {
		MapEntry<LinkLayerAddr, Double> me = _neighbors.get(neighbor);
		if (null != me)
			return me.data();
		else
			return null;
	}

	/**
	 * Get the network address of this network interface.
	 * 
	 * @return The network address.
	 */
	public NetAddr getNetAddr() {
		return _netAddr;
	}

	/**
	 * Get the connected peer of this network interface.
	 * 
	 * @return The connected peer, or null if none.
	 */
	public Connectable getPeer() {
		return _peer;
	}

	/**
	 * Get the real network address of this network interface,
	 * in case the other function returns a fake.
	 * 
	 * @return The network address.
	 */
	protected NetAddr getRealNetAddr() {
		return _netAddr;
	}

	/**
	 * Get the statistics object associated with this network interface.
	 * 
	 * @return The statistics object.
	 */
	public Statistics getStats() {
		return _trafficSink.getStats();
	}

	protected void handleCtrlMsg(ControlMsg cm) {
		/*
		 * A non-router node handles a control message if it is unicast to the
		 * node's address, multicast to all nodes, or multicast to all nodes
		 * with the same node ID as the node in question.
		 */
		NetAddr dst = cm.getDst();
		
		if (dst.equals(getRealNetAddr())
				|| NetAddr.MULTICAST_NODES.equals(dst)
				|| (isRouter() && NetAddr.MULTICAST_ROUTERS.equals(dst))
				|| (dst.isLinkLocal() && getRealNetAddr().nodeId() == dst.nodeId())) {
			if (cm.getType() == ControlMsg.RTR_AD) {
				if (cm.getPayload() instanceof RouterData) {
					handleRouterAd(cm);
				} else
					Utils.logMessage(getId(),
							"Received an invalid router advertisement.");
			} else if (isRouter() && cm.getType() == ControlMsg.RTR_SOL) {
				if ((cm.getSrc().isUnspecified() && null == cm.getPayload()) || (!cm.getSrc().isUnspecified() && 
						cm.getPayload() instanceof RouterData)) {
					handleRouterSol(cm);
				}
				else
					Utils.logMessage(getId(), "Received an invalid router solicitation.");
			} else if (cm.getType() == ControlMsg.NBR_AD) {
				if (cm.getPayload() instanceof NbrData) {
					handleNbrAd(cm);
				} else {
					Utils.logMessage(getId(),
							"Received an invalid neighbor advertisement.");
				}
			} else if (cm.getType() == ControlMsg.NBR_SOL) {
				if (cm.getPayload() instanceof NbrData) {
					handleNbrSol(cm);
				} else
					Utils.logMessage(getId(),
							"Received an invalid neighbor solicitation.");
			} else
				Utils.logMessage(getId(),
						"Received an unhandled control message.");
		} else
			Utils.logMessage(getId(),
					"Received a control message meant for someone else.");
	}

	protected void handleFrame(Frame frame) {
		Data payload = frame.getPayload();
		if (frame.getDst().equals(getLinkAddr())
				|| frame.getDst().equals(LinkLayerAddr.BROADCAST)
				|| frame.getDst().equals(LinkLayerAddr.makeMulticast(getRealNetAddr()))) {
			/*
			 * Here, the frame is accepted because it either is addressed
			 * specifically for this interface, or it is addressed to all
			 * interfaces, or it is addressed to the link layer multicast
			 * version of this interface's link layer address.
			 */
			if (payload instanceof ControlMsg) {
				handleCtrlMsg((ControlMsg) payload);
			} else if (payload instanceof Packet) {
				Packet packet = (Packet) payload;
				handlePacket(packet);
			} else
				Utils.logMessage(getId(),
						"Received frame with unhandled payload.");
		} else
			Utils.logMessage(getId(),
					String.format("Received a frame meant for someone else. (It was addressed to %s instead of %s.)", frame.getDst(), getLinkAddr()));
	}

	private void handleNbrAd(ControlMsg cm) {
		/*
		 * Having received a neighbor advertisement, was it solicited by this
		 * interface? If so, was it because this network interface is in an
		 * autoconfiguration state, or because this interface is still looking
		 * for the link layer address of some network address?
		 */
		Utils.logMessage(getId(),
				"Received a neighbor advertisement!");
		NbrData nd = (NbrData) cm.getPayload();
		if (getRealNetAddr().isLinkLocal()) {
			/*
			 * Apparently, a neighbor has replied to this network interface's
			 * neighbor solicitation in the autoconfiguration process; that is,
			 * address duplication has been detected. Therefore, restart the
			 * autoconfiguration process (unless the address is to be static).
			 */
			if (!_staticAddr)
				autoconfigure();
		} else {
			addNeighbor(cm.getSrc(), nd.getData());
		}
	}

	protected void handleNbrSol(ControlMsg cm) {
		Utils.logMessage(getId(),
				String.format("Received a neighbor solicitation from %s!", cm.getSrc()));
		NbrData nd = (NbrData) cm.getPayload();
		
		if (cm.getSrc().equals(NetAddr.UNSPECIFIED)
				&& !nd.getData().equals(getLinkAddr())) {
			/*
			 * Another interface was doing a Duplicate Address Detection, and it
			 * was indeed a duplicate and "should not be used (by either node)"
			 * according to RFC 4862.
			 */
			NbrData data = new NbrData(isRouter(), getLinkAddr());
			ControlMsg ncm = new ControlMsg(ControlMsg.NBR_AD, getRealNetAddr(),
					NetAddr.makeLinkAddr(getRealNetAddr()), data);
			Frame frame = new Frame(getLinkAddr(), nd.getData(), cm);
			sendFrame(frame);
			if (!_staticAddr)
				autoconfigure();
		} else {
			if (!cm.getSrc().isLinkLocal() && !nd.getData().isMulticast())
				addNeighbor(cm.getSrc(), nd.getData());
			NbrData data = new NbrData(isRouter(), getLinkAddr());
			ControlMsg ncm = new ControlMsg(ControlMsg.NBR_AD, getRealNetAddr(), cm.getSrc(),
					data);
			sendPacket(ncm);
		}
	}

	protected void handlePacket(Packet packet) {
		NetAddr dst = packet.getDst();
		if (dst.equals(getRealNetAddr()) || dst.equals(NetAddr.MULTICAST_NODES) || (isRouter() && dst.equals(NetAddr.MULTICAST_ROUTERS))
				|| (dst.isLinkLocal() && dst.nodeId() == getRealNetAddr().nodeId())) {
			_trafficSink.logMessage(packet.getSrc(), SimEngine.getTime(), packet.getPayload());
			if (packet.getPayload() instanceof Segment) {
				handleSegment(packet.getSrc(), packet.getDst(), (Segment) packet.getPayload());
			} else {
				Utils.logMessage(getId(), packet.getSrc(), packet.getDst(),
						"Received packet with unhandled payload.");
			}
		} else {
			Utils.logMessage(getId(),
					String.format("Received a packet meant for someone else. (It was addressed to %s instead of %s.)", dst, getRealNetAddr()));
		}
	}

	protected void handleSegment(NetAddr from, NetAddr to, Segment segment) {
		if (null == _sockets.get(segment.getPort()))
			Utils.logMessage(
					getId(),
					from,
					to,
					String.format(
							"Received segment with sequence %d on unlistened port %d.",
							segment.getSeq(), segment.getPort()));
		else {
			Utils.logMessage(
					getId(),
					from,
					to,
					String.format(
							"Received segment with sequence %d on port %d.",
							segment.getSeq(), segment.getPort()));
			Application app = _sockets.get(segment.getPort());
			app.handleSegment(from, to, segment);
		}
	}

	private void handleRouterAd(ControlMsg cm) {
		/*
		 * If the router wasn't already known, it is now. And if the interface
		 * is still in the autoconfiguration process, the router's network ID is
		 * used to make a real network address.
		 */
		Utils.logMessage(getId(),
				String.format("Received a router advertisement from %s!", cm.getSrc()));
		RouterData rd = (RouterData) cm.getPayload();
		LinkLayerAddr linkAddr = rd.getData();
		NetAddr netAddr = cm.getSrc();
		if (!linkAddr.isMulticast()) {
			createRouterTEMPORARY(linkAddr);
			addNeighbor(netAddr, linkAddr);
			if (null != _autoconf) {
				SimEngine.instance().deregister(_autoconf);
				_autoconf = null;
				NetAddr newNetAddr = NetAddr.makeNetAddr(
						netAddr.netId(), getRealNetAddr().nodeId());
				assignAddr(newNetAddr);
			}
		}
	}
	
	protected void handleRouterSol(ControlMsg cm) {
		Utils.logMessage(getId(),
				String.format("Received a router solicitation from %s! DETTA ÄR NETWINTERFACE", cm.getSrc()));
	}

	protected boolean isCareof(NetAddr absentAddr) {
		return _careofs.containsKey(absentAddr);
	}

	@Override
	public boolean isConnected(Connectable entity) {
		return (_peer == entity);
	}

	private boolean isNeighbor(NetAddr neighbor) {
		return _neighbors.containsKey(neighbor);
	}

	protected boolean isRouter() {
		return false;
	}
	
	protected void pruneQueue() {
		for (Entry<NetAddr, MapEntry<Packet, Double>> entry : _sendQueue.entrySet()) {
			if (SimEngine.getTime() > entry.getValue().time() + Configs.N_SOL_WAIT) {
				remQueue(entry.getKey());
			} else {
				Packet pkt = entry.getValue().data();
				NetAddr dst = pkt.getDst();
				if (isCareof(dst) && null != getCareof(dst)) {
					dst = getCareof(dst);
				}
				if (isNeighbor(dst) && null != getNeighbor(dst)) {
					sendPacket(new Packet(pkt.getSrc(), dst, pkt.getPayload()));
					remQueue(entry.getKey());
				}
			}
		}
	}
	
	@Override
	public void recv(SimEnt src, Event event) {
		if (event instanceof TimerEventMsg) {
			TimerEventMsg msg = (TimerEventMsg) event;
			if (msg.getMsg().equals(NO_DUPLICATE_ADDRESS)) {
				/*
				 * Here, an autoconfiguring network interface has waited a while
				 * for a neighbor advertisement but gotten nothing. Therefore,
				 * it may continue the process.
				 */
				solicitRouter();
			} else if (msg.getMsg().equals(SOLICIT_ROUTER)) {
				/*
				 * Here, an autoconfiguring network interface has waited a while
				 * for a router advertisement but gotten nothing. Not much to do
				 * about it, though.
				 */
			} else if (msg.getMsg().equals(NO_NEIGHBOR)) {
				pruneQueue();
			} else {
				//Utils.logMessage("Error #5429");// TODO:?
			}
		} else if (event instanceof TimerEvent) {
			;// Network interfaces are not to handle TimerEvents.
		} else if (event instanceof Frame) {
			Utils.logMessage(getId(), "RECEIVED FRAME: " + (Frame) event);
			handleFrame((Frame) event);
		} else {
			Utils.logMessage(getId(), "Received garbage data.");
		}
	}

	protected NetAddr remCareof(NetAddr absentAddr) {
		Utils.logMessage(
				getId(),
				String.format("Removing care-of address: %s is no longer taken care of.",absentAddr));
		        return _careofs.remove(absentAddr);
	}

	protected Packet remQueue(NetAddr addr) {
		if (_sendQueue.containsKey(addr)) {
			MapEntry<Packet, Double> me = _sendQueue.remove(addr);
			return me.data();
		}
		else
			return null;
	}

	private void routePacket(Packet packet) {
		/*
		 * Send it to the router.
		 */
		if (_routers.isEmpty())
			Utils.logMessage(getId(), "No router to forward packet to. The packet was: " + packet);
		else {
			LinkLayerAddr router = _routers.keySet().iterator().next();
			Frame frame = new Frame(getLinkAddr(), router, packet);
			sendFrame(frame);
		}
	}
	
	protected void sendFrame(Frame frame) {
		Utils.logMessage(getId(), "SENDING FRAME: " + frame);
		if (null == getPeer())
			Utils.logMessage(getId(),
					"Was supposed to send a frame, but was not connected to anything.");
		else
			send((SimEnt) _peer, frame, SimEngine.NOW);
	}
	
	public void sendMessage(NetAddr rcpt, int port, int seq, Message msg) {
		Segment sgm = new Segment(port, seq, msg);
		sendSegment(rcpt, sgm);
	}

	protected void sendPacket(Packet packet) {
		NetAddr dst = packet.getDst();
		if (dst.isMulticast()) {
			/*
			 * If the destination is a multicast address, just send it out as
			 * broadcast. (Although it should actually be sent with a multicast
			 * link layer address.)
			 */
			Frame frame = new Frame(getLinkAddr(), LinkLayerAddr.BROADCAST, packet);
			sendFrame(frame);
		} else if (dst.isLinkLocal()) {
			/*
			 * If the network destination is a link-local address, then make the
			 * link layer destination a multicast address based on the
			 * link-local address.
			 */
			Frame frame = new Frame(getLinkAddr(),
					LinkLayerAddr.getMulticast(LinkLayerAddr.makeMulticast(dst)), packet);
			sendFrame(frame);
		} else if (dst.isLocalhost()) {
			/*
			 * If the destination is the localhost, don't even send it out; just
			 * handle the packet directly.
			 */
			handlePacket(packet);
		} else if (dst.isUnspecified())
			Utils.logMessage(getId(),
					"Will not send packet to unspecified address.");
		else if (!dst.inSameNet(getRealNetAddr())) {
			/*
			 * The destination is on another network; determine the route.
			 */
			routePacket(packet);
		} else {
			/*
			 * The destination is not some special kind of address or on another network; it is likely
			 * just another network interface on this local link.
			 */
			if (_neighbors.containsKey(dst)) {
				/*
				 * The destination network address has been seen in some sense
				 * before.
				 */
				if (null != _neighbors.get(dst).data()) {
					/*
					 * There is a complete entry in the neighbor cache (and no care-of) for the address; therefore, that
					 * older entry will be used.
					 */
					LinkLayerAddr linkDst = _neighbors.get(dst).data();
					Frame frame = new Frame(getLinkAddr(), linkDst, packet);
					sendFrame(frame);
				} else if (null == _neighbors.get(dst).data()) {
					/*
					 * The network destination has an "incomplete" entry in the interface's neighbor cache.
					 * Therefore, we might as well discard the packet.
					 */
					System.out.println("Error #99492");
				} else {
					System.out.println("Error #30028");
				}
			} else {
				/*
				 * This interface has no recollection of having sent anything to this network
				 * destination before. Therefore, put the data in queue and do a
				 * neighbor solicitation to find its link layer address.
				 */
				addNeighbor(dst, null);
				addQueue(dst, packet, SimEngine.getTime());
				solicitNeighbor(dst);
				send(this, new TimerEventMsg(NO_NEIGHBOR), Configs.N_SOL_WAIT);
			}
		}
	}

	public void sendSegment(NetAddr rcpt, Segment sgm) {
		Packet pkt = new Packet(getNetAddr(), rcpt, sgm);
		sendPacket(pkt);
	}

	/**
	 * Set the identifier of this network interface to a new value.
	 * 
	 * @param newID
	 *            The new identifier.
	 */
	public void setID(String newID) {
		_id = newID;
	}

	private void solicitNeighbor(NetAddr netAddr) {
		/*
		 * If this network interface needs to solicit a neighbor, it is either a
		 * part of the autoconfiguration process, or it's to find another
		 * network interface's link layer address.
		 */
		NbrData data = new NbrData(false, getLinkAddr());
		ControlMsg msg;
		Frame frame;
		if (getRealNetAddr().isLinkLocal()) {
			msg = new ControlMsg(ControlMsg.NBR_SOL, NetAddr.UNSPECIFIED,
					getRealNetAddr(), data);
			frame = new Frame(getLinkAddr(),
					LinkLayerAddr.makeMulticast(getRealNetAddr()), msg);
		} else {
			msg = new ControlMsg(ControlMsg.NBR_SOL, getRealNetAddr(), netAddr, data);
			frame = new Frame(getLinkAddr(), LinkLayerAddr.makeMulticast(netAddr),
					msg);
		}
		sendFrame(frame);
	}

	private void solicitRouter() {
		/*
		 * Is this part of an autoconfiguration process?
		 */
		ControlMsg msg;
		if (null != _autoconf) {
			msg = new ControlMsg(ControlMsg.RTR_SOL, NetAddr.UNSPECIFIED,
					NetAddr.MULTICAST_ROUTERS, null);
			_autoconf = send(this, new TimerEventMsg(SOLICIT_ROUTER),
					Configs.ROUTER_WAIT);
		} else {
			RouterData data = new RouterData(getLinkAddr());
			msg = new ControlMsg(ControlMsg.RTR_SOL, getRealNetAddr(),
					NetAddr.MULTICAST_ROUTERS, data);
		}
		sendPacket(msg);
	}

	public void unassignAddr() {
		_netAddr = NetAddr.UNSPECIFIED;
	}

	/**
	 * Unbind an application from listening to a port.
	 * 
	 * @param port
	 *            The port to unbind.
	 */
	public void unbindSocket(int port) {
		// TODO: Test if socket busy?
		Application app = _sockets.remove(port);
		if (null != app)
			Utils.logMessage(
					getId(),
					String.format("Application %s unbound from port %d.",
							app.getName(), port));
	}

	public void unsolNbrAdv() {
		if (!getRealNetAddr().isLinkLocal()) {
			NbrData data = new NbrData(isRouter(), getLinkAddr());
			ControlMsg cm = new ControlMsg(ControlMsg.NBR_AD, getRealNetAddr(), NetAddr.MULTICAST_NODES, data);
			sendPacket(cm);
		}
	}
}
