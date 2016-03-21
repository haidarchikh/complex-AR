package d7002d.Laboration4;

import d7002d.Laboration4.Events.Event;

/**
 * All entities like, nodes, switch, router, link etc that handles events need
 * to inherit from this class
 */
public abstract class SimEnt {

	protected SimEnt() {
	}

	/**
	 * To be implemented in child classes if cleaning up is needed when the
	 * entity is killed.
	 * 
	 */
	protected void destructor() {
		// no op, can be added in child classes
	}

	/**
	 * Erases a scheduled event from the SimEngine.
	 * 
	 * @param handleToEvent
	 */
	protected final void eraseScheduledEvent(EventHandle handleToEvent) {
		SimEngine.instance().deregister(handleToEvent);
	}

	/**
	 * Called when erasing an entity like node or link etc. The SimEngine is
	 * called in case that de-registration of the entity is needed.
	 */
	protected final void kill() {
		this.destructor();
	}

	/**
	 * To be implemented in child classes acting on events/messages received.
	 * 
	 * @param source
	 * @param event
	 */
	public abstract void recv(SimEnt source, Event event);

	/**
	 * This method schedules a coming event in the SimEngine.
	 * 
	 * @param destination
	 * @param event
	 * @param delayExecution
	 * @return
	 */
	protected final EventHandle send(SimEnt destination, Event event,
			double delayExecution) {
		// this object is the registrator/source submitting the event
		return SimEngine.instance().register(this, destination, event,
				delayExecution);
	}
}
