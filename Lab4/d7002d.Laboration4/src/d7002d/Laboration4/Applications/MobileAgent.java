package d7002d.Laboration4.Applications;

import d7002d.Laboration4.Configs;
import d7002d.Laboration4.EventHandle;
import d7002d.Laboration4.NetAddr;
import d7002d.Laboration4.SimEngine;
import d7002d.Laboration4.SimEnt;
import d7002d.Laboration4.Utils;
import d7002d.Laboration4.Events.ControlMsg;
import d7002d.Laboration4.Events.Event;
import d7002d.Laboration4.Events.Frame;
import d7002d.Laboration4.Events.MobMsg;
import d7002d.Laboration4.Events.MobPkt;
import d7002d.Laboration4.Events.Packet;
import d7002d.Laboration4.Events.Segment;
import d7002d.Laboration4.Events.TimerEventMsg;
import d7002d.laboration4.Connectables.NetworkInterface;

public class MobileAgent extends NetworkInterface implements Application {
	private enum State { UNREGISTERED, REGISTERED, TIMEDOUT }
	public static final String REGISTER = "REGISTER";
	public static final String REFRESH = "REFRESH";
	public static final String CHECK = "CHECK";
	private NetAddr _homeAgent;
	private String _password;
	private int _lastSeq;
	private EventHandle _resend;
	private double _lastAction;
	private NetAddr _homeAddr;
	private State _state;
	
	public MobileAgent(String id, NetAddr addr) {
		super(id, addr);
		super.bindSocket(HomeAgent.DEFAULT_PORT, this);
		
		_lastSeq = Utils.randomNumber(Segment.MAX_SEQ);
		_state = State.UNREGISTERED;
	}

	public void assignAddr(NetAddr addr) {
		if (null != addr && addr.isGlobal()) {
			super.assignAddr(addr);
			sendRegister();
		}
		else
			super.assignAddr(addr);
	}
	
	private void check() {
		if (_state == State.UNREGISTERED) {
			if (null == _homeAgent) {
				Utils.logMessage(getId(), getName(), "Cannot register, no home agent set. Use setHA(addr, passw).");
			}
			else if (null == getHome()) {
				Utils.logMessage(getId(), getName(), "Cannot register, home address not known. Use setHome(addr).");
			} else {
				sendRegister();
			}
		} else if (_state == State.REGISTERED) {
			if (SimEngine.getTime() > _lastAction + Configs.MOB_REFRESH) {
				if (null != _resend)
					SimEngine.instance().deregister(_resend);
				sendRefresh();
			} else if (null != _resend) {
				SimEngine.instance().deregister(_resend);
				double nextTime = Math.abs(Configs.MOB_REFRESH - (SimEngine.getTime() - _lastAction));
				_resend = send(this, new TimerEventMsg(REFRESH), nextTime);
				_lastAction = SimEngine.getTime();
			}
		}
	}
	
	public NetAddr getHome() {
		return _homeAddr;
	}

	@Override
	public String getName() {
		return "MobileAgent";
	}
	
	public NetAddr getNetAddr() {
		if (null != _homeAddr && !getRealNetAddr().inSameNet(_homeAddr))
			return _homeAddr;
		else
			return getRealNetAddr();
	}

	protected void handlePacket(Packet packet) {
		NetAddr dst = packet.getDst();
		if (dst.isGlobal() && getRealNetAddr().isGlobal() && (dst.equals(getRealNetAddr()) || dst.equals(getHome()))) {
			if (packet.getPayload() instanceof Packet) {
				handlePacket((Packet) packet.getPayload()); 
			} else if (packet.getPayload() instanceof Segment) {
				super.handleSegment(packet.getSrc(), packet.getDst(), (Segment) packet.getPayload());
			} else {
				super.handlePacket(packet);
			}
		} else {
			super.handlePacket(packet);
		}
	}
	
	@Override
	public void handleSegment(NetAddr from, NetAddr to, Segment segment) {
		if (from.equals(_homeAgent) && to.equals(getRealNetAddr()) && segment.getPayload() instanceof MobMsg) {
			MobMsg msg = (MobMsg) segment.getPayload();
			if (msg.getType() == MobMsg.ACK) {
				if (null != _resend && _resend._event instanceof TimerEventMsg) {
					TimerEventMsg tmsg = (TimerEventMsg) _resend._event;
					
					if (tmsg.getMsg().equals(REGISTER)) {
						/*
						 * Ack'd for registration; move on to refresh state.
						 */
						Utils.logMessage(getId(), getName(), "Registration acknowledged!");
						_state = State.REGISTERED;
						if (null != _resend)
							SimEngine.instance().deregister(_resend);
						double nextTime = Math.abs(Configs.MOB_REFRESH - (SimEngine.getTime() - _lastAction));
						_resend = send(this, new TimerEventMsg(REFRESH), nextTime);
						_lastAction = SimEngine.getTime();
					} else if (tmsg.getMsg().equals(REFRESH)) {
						/*
						 * Ack'd for refresh; just keep on refreshing.
						 */
						Utils.logMessage(getId(), getName(), "Refresh acknowledged.");
						if (null != _resend)
							SimEngine.instance().deregister(_resend);
						double nextTime = Math.abs(Configs.MOB_REFRESH - (SimEngine.getTime() - _lastAction));
						_resend = send(this, new TimerEventMsg(REFRESH), nextTime);
						_lastAction = SimEngine.getTime();
					}
				}
			} else if (msg.getType() == MobMsg.REFRESH) {
				/*
				 * This means that the refresh has timed out; resend a refresh immediately.
				 */
				if (null != _resend)
					SimEngine.instance().deregister(_resend);
				sendRefresh();
			} else if (msg.getType() == MobMsg.REGISTER) {
				/*
				 * The home agent requests a registration; send immediately.
				 */
				_state = State.UNREGISTERED;
				if (null != _resend)
					SimEngine.instance().deregister(_resend);
				sendRegister();
			}
		}
	}
	
	@Override
	public void recv(SimEnt src, Event event) {
		if (event instanceof TimerEventMsg) {
			TimerEventMsg msg = (TimerEventMsg) event;
			if (msg.getMsg().equals(REFRESH)) {
				sendRefresh();
			} else if (msg.getMsg().equals(REGISTER)) {
				sendRegister();
			} else if (msg.getMsg().equals(CHECK)) {
				check();
			} else {
				super.recv(src, event);
			}
		} else if (event instanceof Frame) {
			Frame frame = (Frame) event;
			if (frame.getPayload() instanceof MobPkt) {
				MobPkt mpkt = (MobPkt) frame.getPayload();
				if (mpkt.getSrc().equals(_homeAgent) && mpkt.getDst().equals(getRealNetAddr()) && mpkt.getPassw().equals(_password)) {
					recv(this, mpkt.getPacket());
				} else {
					super.recv(src, event);
				}
			} else {
				super.recv(src, event);
			}
		} else if (event instanceof Packet) {
			handlePacket((Packet) event);
		} else {
			super.recv(src, event);
		}
	}
	
	protected void sendPacket(Packet packet) {
		/*
		 * If there is no home agent set, or the mobile nod is in the same network as the home agent, send the packet directly.
		 * Otherwise, tunnel the packet through the home agent.
		 */
		if (packet instanceof ControlMsg) {
			super.sendPacket(packet);
		}
		else if (getRealNetAddr().isGlobal() && null != _homeAgent && !packet.getDst().equals(_homeAgent) && !getRealNetAddr().inSameNet(_homeAgent)) {
			Utils.logMessage(getId(), getName(), String.format("Packet addressed to %s going through home agent at %s first.", packet.getDst(), _homeAgent));
			MobPkt mpkt = new MobPkt(getRealNetAddr(), _homeAgent, _password, packet);
			super.sendPacket(mpkt);
		} else {
			super.sendPacket(packet);
		}
	}

	private void sendRefresh() {
		/*
		 * Don't send refreshes without a valid network address.
		 */
		if (getRealNetAddr().isGlobal() && null != _homeAgent) {
			MobMsg msg = new MobMsg(MobMsg.REFRESH, getNetAddr(), getRealNetAddr(), _password);
			Segment sgm = new Segment(HomeAgent.DEFAULT_PORT, _lastSeq, msg);
			Packet pkt = new Packet(getRealNetAddr(), _homeAgent, sgm);
			sendPacket(pkt);
			if (null != _resend)
				SimEngine.instance().deregister(_resend);
			_resend = send(this, new TimerEventMsg(REFRESH), Configs.MOB_ACK_TIME);
			_lastAction = SimEngine.getTime();
		}
		send(this, new TimerEventMsg(CHECK), Configs.MOB_ACK_TIME);
	}
	
	private void sendRegister() {
		/*
		 * Don't send registrations without a valid network address.
		 */
		if (getRealNetAddr().isGlobal() && null != _homeAgent) {
			if (null == getHome() && getRealNetAddr().inSameNet(_homeAgent)) {
				setHome(getRealNetAddr());
			} else if (null == getHome() && !getRealNetAddr().inSameNet(_homeAgent)) {
				Utils.logMessage(getId(), getName(), "Cannot register, home address not known. Use setHome(addr).");
				return;
			}
			Utils.logMessage(getId(), getName(), String.format("Sending registration to home agent at %s.", _homeAgent));
			MobMsg msg = new MobMsg(MobMsg.REGISTER, getHome(), getRealNetAddr(), _password);
			Segment sgm = new Segment(HomeAgent.DEFAULT_PORT, _lastSeq, msg);
			sendSegment(_homeAgent, sgm);
			if (null != _resend)
				SimEngine.instance().deregister(_resend);
			_resend = send(this, new TimerEventMsg(REGISTER), Configs.MOB_ACK_TIME);
			_lastAction = SimEngine.getTime();
		}
		send(this, new TimerEventMsg(CHECK), Configs.MOB_ACK_TIME);
	}
	

	public void setHA(NetAddr agent, String password) {
		Utils.logMessage(getId(), getName(), String.format("Setting home agent to address %s.", agent));
		_homeAgent = agent;
		_password = password;
		if (null == _homeAddr) {
			sendRegister();
		}
	}

	public void setHome(NetAddr addr) {
		_homeAddr = addr;
	}
}
