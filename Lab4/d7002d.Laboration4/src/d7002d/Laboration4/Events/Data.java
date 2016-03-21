package d7002d.Laboration4.Events;

import d7002d.Laboration4.SimEnt;


/**
 * Something representing some probably binary data.
 * 
 * 
 */
public abstract class Data implements Event {
	public void entering(SimEnt locale) {
	}
	public abstract String toString();
}
