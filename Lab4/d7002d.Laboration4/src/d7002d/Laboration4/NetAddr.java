package d7002d.Laboration4;

/**
 * This class represent the network address, it consist of a network identity
 * "_networkId" represented as an integer (if you want to link this to IP number
 * it can be compared to the network part of the IP address like 132.17.9.0).
 * Then _nodeId represent the host part.
 */
public class NetAddr {
	/**
	 * The biggest allowed network ID number.
	 */
	public static final int NETWORK_ID_MAX = 0xff;
	/**
	 * The biggest allowed node ID number.
	 */
	public static final int NODE_ID_MAX = 0xff;
	private final static Integer PREFIX_UNSPECIFIED = new Integer(0);
	private final static Integer PREFIX_LINKLOCAL = new Integer(NETWORK_ID_MAX - 1);
	private final static Integer PREFIX_MULTICAST = new Integer(NETWORK_ID_MAX - 2);
	/**
	 * A general object representing all unspecified addresses.
	 */
	public final static NetAddr UNSPECIFIED = new NetAddr(PREFIX_UNSPECIFIED, 0);
	/**
	 * A general object representing all localhost addresses.
	 */
	public final static NetAddr LOCALHOST = new NetAddr(PREFIX_UNSPECIFIED, 1);
	/**
	 * A general object representing all node addresses (i.e. multicasting).
	 */
	public final static NetAddr MULTICAST_NODES = new NetAddr(PREFIX_MULTICAST,
			1);
	/**
	 * A general object representing all router addresses (i.e. multicasting).
	 */
	public final static NetAddr MULTICAST_ROUTERS = new NetAddr(PREFIX_MULTICAST, 2);

	/**
	 * Generate a link-local address, given a network node ID.
	 * 
	 * @param nodeId
	 *            The node ID to use to generate the link-local address.
	 * @return The generated link-local address.
	 */
	public static NetAddr getLinkLocalAddress(int nodeId) {
		return makeNetAddr(PREFIX_LINKLOCAL, nodeId);
	}

	/**
	 * Generate a link-local address, given a network address.
	 * 
	 * @param netAddr
	 *            The network address to use to generate the link-local address.
	 * @return The generated link-local address.
	 */
	public static NetAddr makeLinkAddr(NetAddr netAddr) {
		return makeNetAddr(PREFIX_LINKLOCAL, netAddr.nodeId());
	}

	/**
	 * Return a NetworkAddress object. Will be an unspecified address if either
	 * network or host part is invalid.
	 * 
	 * @param network
	 *            The network part of the network address.
	 * @param node
	 *            The host part of the network address.
	 * @return A network address object.
	 */
	public static NetAddr makeNetAddr(Integer network, int node) {
		if (node <= 0 || node > NODE_ID_MAX)
			return UNSPECIFIED;
		if (null == network || network <= 0 || network > NETWORK_ID_MAX|| network.equals(PREFIX_UNSPECIFIED)
				|| network.equals(PREFIX_MULTICAST))
			network = PREFIX_UNSPECIFIED;
		else if (network.equals(PREFIX_LINKLOCAL))
			network = PREFIX_LINKLOCAL;
		else if (network.equals(PREFIX_MULTICAST))
			network = PREFIX_MULTICAST;
		return new NetAddr(network, node);
	}

	private Integer _networkId;

	private int _nodeId;

	private NetAddr(Integer network, int node) {
		_networkId = network;
		_nodeId = node;
	}

	/**
	 * Give a human-readable representation of the network address.
	 * 
	 * @return The human-readable representation.
	 */
	public String dotAddress() {
		if (this == NetAddr.UNSPECIFIED)
			return "UNSPECIFIED";
		else if (this == NetAddr.LOCALHOST)
			return "LOCALHOST";
		else if (this == MULTICAST_NODES)
			return "MULTICAST_NODES";
		else if (this == MULTICAST_ROUTERS)
			return "MULTICAST_ROUTERS";
		else if (PREFIX_LINKLOCAL.equals(this.netId()))
			return "LINKLOCAL." + nodeId();
		else if (PREFIX_MULTICAST.equals(this.netId()))
			return "MULTICAST." + nodeId();
		else
			return _networkId + "." + _nodeId;
	}

	@Override
	public boolean equals(Object otherAddress) {
		/*
		 * Two network addresses are equal if their network and node parts are
		 * equal.
		 */
		if (otherAddress instanceof NetAddr)
			return (this.netId() == ((NetAddr) otherAddress).netId() && this
					.nodeId() == ((NetAddr) otherAddress).nodeId());
		else
			return false;
	}

	@Override
	public int hashCode() {
		int hash = 17;
		hash = hash * 31 + netId();
		hash = hash * 31 + nodeId();
		return hash;
	}

	/**
	 * Determine whether two addresses are in the same network (i.e. have the
	 * same network ID).
	 * 
	 * @param other
	 *            The address to compare to.
	 * @return True if they are in the same network, false otherwise.
	 */
	public boolean inSameNet(NetAddr other) {
		return (this.netId() == other.netId());
	}

	/**
	 * Determine whether an address is globally valid or not.
	 * 
	 * @return True if the address is globally valid, false otherwise.
	 */
	public boolean isGlobal() {
		return (!isLinkLocal() && !isMulticast() && !isLocalhost() && !isUnspecified());
	}

	/**
	 * Determine whether an address is only valid for link-local communication
	 * or not.
	 * 
	 * @return True if the address only is link-locally valid, false otherwise.
	 */
	public boolean isLinkLocal() {
		return (PREFIX_LINKLOCAL.equals(netId()));
	}

	/**
	 * Determine whether an address is a localhost (loopback) address or not.
	 * 
	 * @return True if the address is loopback, false otherwise.
	 */
	public boolean isLocalhost() {
		return (this == LOCALHOST);
	}

	/**
	 * Determine whether an address is a form of multicast or not.
	 * 
	 * @return True if the address is in multicast form, false otherwise.
	 */
	public boolean isMulticast() {
		return (this == MULTICAST_NODES || this == MULTICAST_ROUTERS || PREFIX_MULTICAST.equals(netId()));
	}

	/**
	 * Determine whether an address is unspecified or not.
	 * 
	 * @return True if the address is unspecified, false otherwise.
	 */
	public boolean isUnspecified() {
		return (this == UNSPECIFIED);
	}

	/**
	 * Give the network ID part of a network address.
	 * 
	 * @return The network ID.
	 */
	public int netId() {
		return _networkId;
	}

	/**
	 * Give the host ID part of a network address.
	 * 
	 * @return The host ID.
	 */
	public int nodeId() {
		return _nodeId;
	}

	public String toString() {
		return this.dotAddress();
	}
}
