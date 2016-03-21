package d7002d.Laboration4.Events;

/**
 * This class does implement a ping message,
 * @author bambanza
 *
 */
public class PingMsg extends Message {
	public static final PingMsg PING = new PingMsg(new StringData("Ping?"));
	public static final PingMsg PONG = new PingMsg(new StringData("Pong!"));

	private PingMsg(StringData payload) {
		super(payload);
	}

	public boolean isPing() {
		return (this == PING);
	}

	public boolean isPong() {
		return (this == PONG);
	}
}
