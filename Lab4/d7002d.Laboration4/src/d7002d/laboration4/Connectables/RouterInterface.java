package d7002d.laboration4.Connectables;

import d7002d.Laboration4.Configs;
import d7002d.Laboration4.EventHandle;
import d7002d.Laboration4.LinkLayerAddr;
import d7002d.Laboration4.NetAddr;
import d7002d.Laboration4.SimEngine;
import d7002d.Laboration4.SimEnt;
import d7002d.Laboration4.Utils;
import d7002d.Laboration4.Events.ControlMsg;
import d7002d.Laboration4.Events.Data;
import d7002d.Laboration4.Events.Event;
import d7002d.Laboration4.Events.Frame;
import d7002d.Laboration4.Events.NbrData;
import d7002d.Laboration4.Events.Packet;
import d7002d.Laboration4.Events.RouterData;
import d7002d.Laboration4.Events.TimerEventMsg;

public class RouterInterface extends NetworkInterface {
	public static final String ADVERTISEMENT = "ADVERTISEMENT";
	private Router _router;
	private double _lastAdvertisement = 0;
	private EventHandle _nextAdvertisement;
	
	public RouterInterface(Router router, String id, NetAddr networkAddress) {
		super(id, networkAddress);
		_router = router;
		if (getRealNetAddr().isGlobal()) {
			_nextAdvertisement = send(this, new TimerEventMsg(ADVERTISEMENT), Configs.R_ADV_MIN);
		}
	}
	
	public void handlePacket(Packet pkt) {
		NetAddr dst = pkt.getDst();
		if (getRealNetAddr().inSameNet(dst)) {
			if (isCareof(dst)) {
				// An entity is listed as a care-of address for this destination.
				if (null != getCareof(dst)) {
					// And it's a valid entity.
					
				} else {
					// And it's an unvalid entity that has yet to show itself.
					
				}
			}
		}
	}

	public void recv(SimEnt src, Event event) {
		if (event instanceof TimerEventMsg) {
			TimerEventMsg msg = (TimerEventMsg) event;
			if (msg.getMsg().equals(ADVERTISEMENT)) {
				advertiseRouter();
			} else {
				super.recv(src, event);
			}
		} else {
			super.recv(src, event);
		}
	}

	public void autoconfigure() {}
	
	protected void handleFrame(Frame frame) {
		Data payload = frame.getPayload();
		if (frame.getDst().equals(getLinkAddr())
				|| frame.getDst().equals(LinkLayerAddr.BROADCAST)
				|| frame.getDst().equals(
						LinkLayerAddr.makeMulticast(getRealNetAddr()))) {
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
				_router.handlePacket(packet);
			} else {
				Utils.logMessage(getId(),
						"Received frame with unhandled payload.");
			}
		} else
			Utils.logMessage(getId(),
					String.format("Received a frame meant for someone else. (It was addressed to %s instead of %s.)", frame.getDst(), getLinkAddr()));
	}

	protected void handleRouterSol(ControlMsg cm) {
		advertiseRouter();
	}
	
	private void advertiseRouter() {
		if (_lastAdvertisement > 0 && SimEngine.getTime() < _lastAdvertisement + Configs.R_ADV_MIN) {
			double nextTime = _lastAdvertisement + Configs.R_ADV_MIN - SimEngine.getTime();
			_nextAdvertisement = send(this, new TimerEventMsg(ADVERTISEMENT), nextTime);
		} else {
			RouterData data = new RouterData(getLinkAddr());
			ControlMsg controlMessage = new ControlMsg(ControlMsg.RTR_AD, getRealNetAddr(), NetAddr.MULTICAST_NODES, data);
			sendPacket(controlMessage);
			_lastAdvertisement = SimEngine.getTime();
			if (null != _nextAdvertisement)
				SimEngine.instance().deregister(_nextAdvertisement);
			_nextAdvertisement = send(this, new TimerEventMsg(ADVERTISEMENT), Configs.R_ADV_INTERVAL);
		}
	}

	protected boolean isRouter() {
		return true;
	}
}
