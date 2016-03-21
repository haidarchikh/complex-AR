package d7002d.Laboration4;

/**
 * A rudimentary class for use in hash maps when keys must map to pairs of values.
 * 
 *
 * @param <A> A thing.
 * @param <B> Another thing.
 */
public class MapEntry<A, B> {
	private A _a;
	private B _b;
	public MapEntry(A a, B b) {
		_a = a;
		_b = b;
	}
	public A data() {
		return _a;
	}
	public B time() {
		return _b;
	}
	public String toString() {
		String a = (null == _a)?"null":_a.toString();
		String b = (null == _b)?"null":_b.toString();
		return "(" + a + ", " + b + ")";
	}
}