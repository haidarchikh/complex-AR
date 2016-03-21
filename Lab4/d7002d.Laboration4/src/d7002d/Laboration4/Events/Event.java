package d7002d.Laboration4.Events;

import d7002d.Laboration4.SimEnt;

/**
 * This interface is to be used for messages sent between Simulation Entities
 * like packets and timing events calling itself etc. The method entering is
 * called when the event triggers.
 */
public interface Event {
	public void entering(SimEnt locale);
}
