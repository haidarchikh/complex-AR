package d7002d.Laboration4.Events;

public class StringData extends Data {
	private String _string;

	public StringData(String string) {
		_string = string;
	}

	public String getData() {
		return _string;
	}

	@Override
	public String toString() {
		return String.format("<<< string: \"%s\" >>>", _string);
	}
}