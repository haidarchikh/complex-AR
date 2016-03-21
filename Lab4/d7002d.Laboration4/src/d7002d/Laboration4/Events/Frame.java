package d7002d.Laboration4.Events;

import d7002d.Laboration4.LinkLayerAddr;
import d7002d.Laboration4.SimEnt;

public class Frame extends Data implements Event {
	private LinkLayerAddr _source;
	private LinkLayerAddr _destination;
	private Data _payload;

	public Frame(LinkLayerAddr source, LinkLayerAddr destination,Data payload) {
		_source = source;
		_destination = destination;
		_payload = payload;
	}

	public LinkLayerAddr getDst() {
		return _destination;
	}

	public Data getPayload() {
		return _payload;
	}

	public LinkLayerAddr getSrc() {
		return _source;
	}

	@Override
	public String toString() {
		if (null == _payload)
			return String.format("{{{ src: %s | dst: %s | payload: %s }}}", getSrc(), getDst(), "(null)");
		else
			return String.format("{{{ src: %s | dst: %s | payload: %s }}}", getSrc(), getDst(), getPayload());
	}
}
