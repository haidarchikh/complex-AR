package d7002d.Laboration4.Events;

import d7002d.Laboration4.SimEnt;

/**
 * This class is used to schedule timer events when for instance calling the
 * recv method in SimEnt classes due to other reasons than that a message has
 * arrived.
 */
public class TimerEvent implements Event {
	public void entering(SimEnt locale) {
	}
}
