package d7002d.Laboration4.Events;

import d7002d.Laboration4.NetAddr;

/**
 * 
 * 
 * @author bambanza
 *
 */
public class MobMsg extends Message {
	public static final int ACK = 0;
	public static final int REGISTER = 1;
	public static final int REFRESH = 2;
	
	public static MobMsg getAck(String password) {
		return new MobMsg(ACK, null, null, password);
	}
	private int _type;
	private NetAddr _homeAddr;
	private NetAddr _awayAddr;
    private String _password;
	
    public MobMsg(int type, NetAddr homeAddr, NetAddr awayAddr, String password) {
		super(null);
		_type = type;
		_homeAddr = homeAddr;
		_awayAddr = awayAddr;
		_password = password;
	}
	public NetAddr getHome() {
		return _homeAddr;
	}
	public NetAddr getAway() {
		return _awayAddr;
	}
	public String getPassw() {
		return _password;
	}
	public int getType() {
		return _type;
	}
	private String getTypeString() {
		if (getType() == ACK)
			return "ACK";
		else if (getType() == REGISTER)
			return "REGISTER";
		else if (getType() == REFRESH)
			return "REFRESH";
		else
			return "???";
	}
	public String toString() {
		if (null == getHome() || null == getAway())
			return String.format("⟨⟨⟨ type: %s | pw: %s ⟩⟩⟩", getTypeString(), getPassw());
		else
			return String.format("⟨⟨⟨ type: %s | home: %s | away: %s | pw: %s ⟩⟩⟩", getTypeString(), getHome(), getAway(), getPassw());
	}
}
