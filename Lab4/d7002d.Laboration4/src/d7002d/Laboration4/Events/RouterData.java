package d7002d.Laboration4.Events;

import d7002d.Laboration4.LinkLayerAddr;

public class RouterData extends Data {
	private LinkLayerAddr _address;

	public RouterData(LinkLayerAddr address) {
		_address = address;
	}

	public LinkLayerAddr getData() {
		return _address;
	}

	@Override
	public String toString() {
		return String.format("<<< linklayeraddress: %s >>>", _address.getAddr());
	}
}