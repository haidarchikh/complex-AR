package d7002d.Laboration4;

/**
 * A class representing link-layer addresses, e.g. MAC addresses in Ethernet.
 * 
 * 
 */
public class LinkLayerAddr {
	/**
	 * Length of link layer addresses, e.g. 98B81A.
	 */
	private static final int LINK_ADDR_LENGTH = 5;
	/**
	 * The pattern of broadcast addresses.
	 */
	private final static String PREFIX_BROADCAST = new String(new char[LINK_ADDR_LENGTH / 2]).replace("\0", "f");
	/**
	 * The general broadcast address.
	 */
	public final static LinkLayerAddr BROADCAST = new LinkLayerAddr(new String(new char[LINK_ADDR_LENGTH]).replace("\0", "f"));

	/**
	 * Give a link-layer object, made based on some string representation.
	 * 
	 * @param addr
	 *            The address to "make into" a link-layer address.
	 * @return The link-layer address.
	 */
	public static LinkLayerAddr getLinkLayerAddress(String addr) {
		
		if (null == addr || addr.equals(""))
			return new LinkLayerAddr();
		else {
			while (addr.length() < LINK_ADDR_LENGTH)
				addr = "0" + addr;
			LinkLayerAddr linkLayerAddress = new LinkLayerAddr(addr);
		
			if (linkLayerAddress.equals(BROADCAST))
				return new LinkLayerAddr();
			else
				return linkLayerAddress;
		}
	}

	/**
	 * Give a multicast representation of a link-layer address.
	 * 
	 * @param llAddr
	 *            The address to "convert" to multicast.
	 * @return The multicast representation.
	 */
	public static LinkLayerAddr getMulticast(LinkLayerAddr llAddr) {
		if (llAddr.getAddr().startsWith(PREFIX_BROADCAST))
			return llAddr;
		else {
			String unicastAddress = llAddr.getAddr();
			String multicast = PREFIX_BROADCAST+ unicastAddress.substring(PREFIX_BROADCAST.length());
			return new LinkLayerAddr(multicast);
		}
	}

	/**
	 * Give a link-layer multicast representatior of a network address.
	 * 
	 * @param addr
	 *            The address to "convert" to multicast.
	 * @return The multicast representation.
	 */
	public static LinkLayerAddr makeMulticast(NetAddr addr) {
		LinkLayerAddr linkLayerAddress = getLinkLayerAddress(String.valueOf(addr.nodeId())); // create the link layer address based on the nodeId.
		linkLayerAddress = getMulticast(linkLayerAddress);
		return linkLayerAddress;
	}

	private String _address;

	private LinkLayerAddr() {
		_address = Utils.randomHex(LINK_ADDR_LENGTH);// use an hexadecimal number.
		
		while (_address.equals(BROADCAST.getAddr()))
			_address = Utils.randomHex(LINK_ADDR_LENGTH);
	}

	private LinkLayerAddr(String address) {
		_address = address;
	}

	@Override
	public boolean equals(Object otherAddress) {
		if (otherAddress instanceof LinkLayerAddr)
			return (this.getAddr().equals(((LinkLayerAddr) otherAddress).getAddr()));
		else
			return false;
	}

	/**
	 * Get the link-layer address in text form.
	 * 
	 * @return The address in text form.
	 */
	public String getAddr() {
		if (this == LinkLayerAddr.BROADCAST)
			return "BROADCAST";
		else
			return _address;
	}

	/**
	 * A function that returns the link-layer address in machine readable form.
	 * 
	 * @return The address.
	 */
	private String getRealAddr() {
		return _address;
	}

	@Override
	public int hashCode() {
		Long value = Long.parseLong(getRealAddr(), 16);
		return value.hashCode();
	}

	/**
	 * Determine whether the address is a multicast address or not, based on its
	 * prefix.
	 * 
	 * @return True if the address is multicast, false otherwise.
	 */
	public boolean isMulticast() {
		if (getRealAddr().startsWith(PREFIX_BROADCAST))
			return true;
		else
			return false;
	}

	public String toString() {
		return this.getAddr();
	}
}
