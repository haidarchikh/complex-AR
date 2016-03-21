package d7002d.Laboration4.Events;

public class TimerEventMsg extends TimerEvent {
	private String _message;

	public TimerEventMsg(String message) {
		super();
		_message = message;
	}
	public String getMsg() {
		return _message;
	}
}
