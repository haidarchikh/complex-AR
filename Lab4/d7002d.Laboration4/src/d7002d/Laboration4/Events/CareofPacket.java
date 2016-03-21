package d7002d.Laboration4.Events;

import d7002d.Laboration4.NetAddr;

public class CareofPacket extends Packet {
	private NetAddr _careof;

	public CareofPacket(NetAddr from, NetAddr to, NetAddr careof, Data payload) {
		super(from, to, payload);
		_careof = careof;
	}
	public NetAddr getCareof() {
		return _careof;
	}
}
