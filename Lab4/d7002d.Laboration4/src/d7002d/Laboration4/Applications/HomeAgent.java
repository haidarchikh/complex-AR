package d7002d.Laboration4.Applications;

import d7002d.Laboration4.Configs;
import d7002d.Laboration4.EventHandle;
import d7002d.Laboration4.LinkLayerAddr;
import d7002d.Laboration4.NetAddr;
import d7002d.Laboration4.SimEngine;
import d7002d.Laboration4.SimEnt;
import d7002d.Laboration4.Utils;
import d7002d.Laboration4.Events.CareofPacket;
import d7002d.Laboration4.Events.ControlMsg;
import d7002d.Laboration4.Events.Data;
import d7002d.Laboration4.Events.Event;
import d7002d.Laboration4.Events.Frame;
import d7002d.Laboration4.Events.MobMsg;
import d7002d.Laboration4.Events.MobPkt;
import d7002d.Laboration4.Events.NbrData;
import d7002d.Laboration4.Events.Packet;
import d7002d.Laboration4.Events.Segment;
import d7002d.Laboration4.Events.TimerEventMsg;
import d7002d.laboration4.Connectables.NetworkInterface;

public class HomeAgent extends NetworkInterface implements Application {
	private static final String CHECK = "CHECK";
	public static final int DEFAULT_PORT = 8888;
	private NetAddr _MAhome;
	private NetAddr _MAaway;
	private double _lastContact;
	private String _password;
	private EventHandle _lastRefresh;
	
	public HomeAgent(String id, String password) {
		super(id + "_IF", null);
		super.bindSocket(DEFAULT_PORT, this);
		_password = password;
		_MAhome = null;
	}
	
	private void acknowledge(NetAddr from, Segment sgm) {
		MobMsg msg = MobMsg.getAck(_password);
		sendMessage(from, sgm.getPort(), sgm.getSeq(), msg);
	}
	
	@Override
	public String getName() {
		return "HomeAgent";
	}
	
	protected void handleFrame(Frame frame) {
		Data payload = frame.getPayload();
		if (null != _MAhome && frame.getDst().equals(LinkLayerAddr.makeMulticast(_MAhome))) {
			if (payload instanceof ControlMsg) {
				handleCtrlMsg((ControlMsg) payload);
			} else if (payload instanceof Packet) {
				handlePacket((Packet) payload);
			} else {
				super.handleFrame(frame);
			}
		} else
			super.handleFrame(frame);
	}

	protected void handlePacket(Packet packet) {
		NetAddr dst = packet.getDst();
		if (null != _MAhome && dst.equals(_MAhome) && null != _MAaway) {
			Utils.logMessage(getId(), getName(), String.format("Intercepting and forwarding packet for %s (%s).", _MAhome, _MAaway));
			MobPkt pkt = new MobPkt(getRealNetAddr(), _MAaway, _password, packet);
			sendPacket(pkt);
		} else {
			super.handlePacket(packet);
		}
	}

	private void handleRefresh(NetAddr from, Segment sgm, MobMsg msg) {
		/*
		 * If the refresh message comes from an address in the same
		 * network, disable the tunneling functionality. Otherwise refresh the contact with the mobile node.
		 */
		if (getRealNetAddr().inSameNet(from) && null != _lastRefresh) {
			SimEngine.instance().deregister(_lastRefresh);
			_MAaway = null;
			acknowledge(from, sgm);
		} else {
			if (null != _lastRefresh)
				SimEngine.instance().deregister(_lastRefresh);
			
			_lastRefresh = send(this, new TimerEventMsg(CHECK), Configs.MOB_TIMEOUT);
			_lastContact = SimEngine.getTime();
			if (null == _MAaway || !_MAaway.equals(msg.getAway())) {
				/*
				 * If the mobile agent address hasn't been set, or the refresh comes from an address different
				 * from the old value, update the value and advertise to the neighbors.
				 */
				Utils.logMessage(getId(), getName(), String.format("Setting mobile agent's away address to %s.", msg.getAway()));
				_MAaway = msg.getAway();
				if (!getRealNetAddr().inSameNet(_MAaway))
					unsolNbrAdv();
				acknowledge(from, sgm);
			}
		}
	}

	private void handleRegister(NetAddr from, Segment sgm, MobMsg msg) {
		_MAhome = msg.getHome();
		_MAaway = msg.getAway();
		Utils.logMessage(getId(), getName(), String.format("Mobile node with home at %s, registering; currently at %s!", _MAhome, _MAaway));
		if (!_MAaway.inSameNet(getRealNetAddr())) {
			advertiseMA();
		}
		if (null != _lastRefresh)
			SimEngine.instance().deregister(_lastRefresh);
		_lastRefresh = send(this, new TimerEventMsg(CHECK), Configs.MOB_ACK_TIME);
		_lastContact = SimEngine.getTime();
		acknowledge(from, sgm);
	}
	private void advertiseMA() {
		ControlMsg cm1 = new ControlMsg(ControlMsg.NBR_AD, _MAhome, NetAddr.MULTICAST_NODES, new NbrData(isRouter(), getLinkAddr()));
		ControlMsg cm2 = new ControlMsg(ControlMsg.NBR_AD, getRealNetAddr(), NetAddr.MULTICAST_NODES, new NbrData(isRouter(), getLinkAddr()));
		sendPacket(cm1);
		sendPacket(cm2);
	}

	@Override
	public void handleSegment(NetAddr from, NetAddr to, Segment segment) {
		if (segment.getPayload() instanceof MobMsg) {
			MobMsg msg = (MobMsg) segment.getPayload();
			if (msg.getPassw().equals(_password)) {
				if (msg.getType() == MobMsg.REGISTER) {
					handleRegister(from, segment, msg);
				} else if (msg.getType() == MobMsg.REFRESH) {
					handleRefresh(from, segment, msg);
				}
			} else {
				Utils.logMessage(getId(), "Received invalid mobile message.");
			}
		}
	}

	protected void handleNbrSol(ControlMsg cm) {
		/*
		 * The home agent is keeping watch over the mobile node's home address; if another interface duplicates it, respond to a 
		 * neighbor solicitation on behalf of the mobile node. If another node wants its link-layer address, also respond on its
		 * behalf.
		 */
		if (null != _MAhome && null != _MAaway) {
			if (cm.getSrc().isUnspecified() || cm.getDst().equals(_MAhome)) {
				Utils.logMessage(getId(),
						String.format("Received a neighbor solicitation from %s!", cm.getSrc()));
				Utils.logMessage(getId(),
						String.format("Responding to neighbor solicitation from %s...", cm.getSrc()));
				NbrData data = new NbrData(isRouter(), getLinkAddr());
				ControlMsg ncm = new ControlMsg(ControlMsg.NBR_AD, _MAhome, NetAddr.MULTICAST_NODES, data);
				sendPacket(ncm);
			} else {
				super.handleNbrSol(cm);
			}
		} else {
			super.handleNbrSol(cm);
		}
	}

	@Override
	public void recv(SimEnt src, Event event) {
		if (event instanceof TimerEventMsg) {
			TimerEventMsg msg = (TimerEventMsg) event;
			if (msg.getMsg().equals(CHECK)) {
				if (SimEngine.getTime() > _lastContact+Configs.MOB_TIMEOUT) {
					;//TODO: request refresh
				}
			}
			else {
				super.recv(src, event);
			}
		} else if (event instanceof Frame) {
			Frame frame = (Frame) event;
			if (frame.getPayload() instanceof MobPkt) {
				// MobPkts come from the mobile node, to the network.
				MobPkt mpkt = (MobPkt) frame.getPayload();
				if (null != _MAaway &&
						mpkt.getSrc().equals(_MAaway) &&
						mpkt.getDst().equals(getRealNetAddr()) &&
						mpkt.getPassw().equals(_password)) {
					Packet pkt = mpkt.getPacket();
					CareofPacket cpkt = new CareofPacket(pkt.getSrc(), pkt.getDst(), getRealNetAddr(), pkt.getPayload());
					sendPacket(cpkt);
				}
			} else if (frame.getPayload() instanceof CareofPacket) {
				/*
				 * CareofPkts come from the larger network, to be tunneled to the mobile node, wherever it may be.
				 */
				CareofPacket pkt = (CareofPacket) frame.getPayload();
				if (null != _MAaway && null != _MAhome && pkt.getDst().equals(_MAhome) && pkt.getDst().equals(getRealNetAddr())) {
					Packet newPacket = new Packet(getRealNetAddr(), _MAaway, pkt.getPayload());
					sendPacket(newPacket);
				} else {
					System.out.println("Error #66980");
					super.recv(src, event);
				}
			} else {
				super.recv(src, event);
			}
		} else {
			System.out.println("Error #1890");
			super.recv(src, event);
		}
	}
}
