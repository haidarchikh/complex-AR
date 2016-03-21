package d7002d.laboration4.Connectables;

/**
 * This class represents a routing table entry by including the link connecting
 * to an interface as well as the node connected to the other side of the link.
 */
public class SwitchTableEntry extends TableEntry {

	SwitchTableEntry(Connectable link, Connectable node) {
		super(link, node);
	}

	public Connectable link() {
		return super.link();
	}

	public Connectable node() {
		return super.node();
	}
}
