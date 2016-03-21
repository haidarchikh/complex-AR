package d7002d.Laboration4.Events;

import d7002d.Laboration4.NetAddr;

/**
 * This class is used to send control messages,
 *
 * @author bambanza
 * 
 */
public class ControlMsg extends Packet {
	public static final int RTR_SOL = 133;
	public static final int RTR_AD = 134;
	public static final int NBR_SOL = 135;
	public static final int NBR_AD = 136;
	public static final int CO_AD = 999;
	private int _type;

	public ControlMsg(int type, NetAddr from,NetAddr to, Data payload) {
		super(from, to, payload);
		_type = type;
	}

	public int getType() {
		return _type;
	}

	public String getTypeString() {
		if (_type == RTR_SOL)
			return "R_SOLICITATION";
		else if (_type == RTR_AD)
				return "R_ADVERTISEMENT";
		else if (_type == NBR_SOL)
			return "N_SOLICITATION";
		else if (_type == NBR_AD)
			return "N_ADVERTISEMENT";
		return "UNKNOWN";
	}
	
	@Override
	public String toString() {
		if (null == getPayload())
			return String.format("[[[ type: %s | src: %s | dst: %s | payload: %s ]]]",getTypeString(), getSrc(), getDst(), "(null)");
		else
			return String.format("[[[ type: %s | src: %s | dst: %s | payload: %s ]]]",getTypeString(), getSrc(), getDst(), getPayload());
	}
}
