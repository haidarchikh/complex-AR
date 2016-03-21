package d7002d.Laboration4.Events;

import d7002d.Laboration4.NetAddr;

/**
 * This class implements an event that send a Packet, currently the only fields
 * in the packet are who the sender is, the destination and a payload.
 */
public class Packet extends Data {
	private NetAddr _source;
	private NetAddr _destination;
	private Data _payload;

	public Packet(NetAddr from, NetAddr to, Data payload) {
		_source = from;
		_destination = to;
		_payload = payload;
	}

	public NetAddr getDst() {
		return _destination;
	}

	public Data getPayload() {
		return _payload;
	}

	public NetAddr getSrc() {
		return _source;
	}

	@Override
	public String toString() {
		if (null == getPayload())
			return String.format("[[[ src: %s | dst: %s | payload: %s ]]]", getSrc(), getDst(), "(null)");
		else
			return String.format("[[[ src: %s | dst: %s | payload: %s ]]]", getSrc(), getDst(), getPayload());
	}
}
