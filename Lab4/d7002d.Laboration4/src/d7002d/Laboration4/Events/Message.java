package d7002d.Laboration4.Events;


public class Message extends Data {
	private Data _payload;
	
	public Message(Data payload) {
		_payload = payload;
	}
	
	public Data getPayload() {
		return _payload;
	}

	@Override
	public String toString() {
		if (null == getPayload())
			return "(null)";
		else
			return getPayload().toString();
	}
}
