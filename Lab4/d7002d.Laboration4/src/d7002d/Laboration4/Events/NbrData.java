package d7002d.Laboration4.Events;

import d7002d.Laboration4.LinkLayerAddr;
import d7002d.Laboration4.NetAddr;

public class NbrData extends Data {
	private boolean _routerFlag;
	private LinkLayerAddr _address;
	
	public NbrData(boolean routerFlag,
			LinkLayerAddr address) {
		_routerFlag = routerFlag;
		_address = address;
	}

	public boolean fromRouter() {
		return _routerFlag;
	}
	
	public LinkLayerAddr getData() {
		return _address;
	}

	@Override
	public String toString() {
		if (null == getData())
			return String.format("<<< R=%d >>>", fromRouter()?1:0);
		else
			return String.format("<<< lla: %s | R=%d >>>", getData(), fromRouter()?1:0);
	}
}