package d7002d.laboration4.Connectables;

/**
 * Just a class that works like a table entry hosting a link connecting and the
 * node at the other end.
 */
public class TableEntry {
	private Connectable _link;
	private Connectable _node;

	TableEntry(Connectable link, Connectable node) {
		_link = link;
		_node = node;
	}

	protected Connectable link() {
		return _link;
	}

	protected Connectable node() {
		return _node;
	}
}
