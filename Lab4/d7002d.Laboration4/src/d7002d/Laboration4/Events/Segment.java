package d7002d.Laboration4.Events;


public class Segment extends Data {
	public static int incSeq(int sequence) {
		if (sequence < 0 || sequence+1 > Segment.MAX_SEQ)
			return 0;
		else
			return sequence+1;
	}
	private int _port;
	private int _sequence = 0;
	private Data _payload;
	public static final int MAX_PORT = 0xffff;
	
	public static final int MAX_SEQ = 0xffff;
	
	public static int decSeq(int sequence) {
		if (sequence < 1)
			return Segment.MAX_SEQ;
		else
			return sequence-1;
	}

	public Segment(int port, int sequence, Data payload) {
		_port = port;
		_sequence = sequence;
		_payload = payload;
	}
	
	public Data getPayload() {
		return _payload;
	}

	public int getPort() {
		return _port;
	}
	
	public int getSeq() {
		return _sequence;
	}
	@Override
	public String toString() {
		if (null == _payload)
			return String.format("((( port: %d | seq: %d | payload: %s )))", getPort(), getSeq(), "(null)");
		else
			return String.format("((( port: %d | seq: %d | payload: %s )))", getPort(), getSeq(), getPayload());
	}
}
