package d7002d.Laboration4.Events;

import d7002d.Laboration4.NetAddr;

/**
 * This class implements an event that send a Packet, currently the only fields
 * in the packet are who the sender is, the destination and a payload.
 */
public class MobPkt extends Packet {
	private NetAddr _source;
	private NetAddr _destination;
	private String _password;
	private Packet _packet;

	public MobPkt(NetAddr from, NetAddr to, String password, Packet packet) {
		super(from, to, null);
		_source = from;
		_destination = to;
		_password = password;
		_packet = packet;
	}

	public NetAddr getDst() {
		return _destination;
	}

	public Packet getPacket() {
		return _packet;
	}

	public String getPassw() {
		return _password;
	}

	public NetAddr getSrc() {
		return _source;
	}
	
	public Data getPayload() {
		return _packet;
	}

	@Override
	public String toString() {
		if (null == getPacket())
			return String.format("[[[ ma: %s | ha: %s | pw: %s | pkt: %s ]]]", getSrc(), getDst(), getPassw(), "(null)");
		else
			return String.format("[[[ ma: %s | ha: %s | pw: %s | pkt: %s ]]]", getSrc(), getDst(), getPassw(), getPacket());
	}
}
