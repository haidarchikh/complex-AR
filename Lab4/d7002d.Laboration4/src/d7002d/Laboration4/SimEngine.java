package d7002d.Laboration4;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.TreeMap;

import d7002d.Laboration4.Events.Event;

/**
 * This class implements the simulation engine. As long as there are events in
 * the queue, the simulation will run. When empty, the engine stops.
 */
public final class SimEngine implements Runnable {
	private static SimEngine _instance;
	private final TreeMap<SimTimeSlot, EventHandle> _simTimeTree = new TreeMap<SimTimeSlot, EventHandle>();
	private boolean _quit = false;
	private static double _simTime = 0;
	public static final int NOW = 0;

	public static double getTime() {
		BigDecimal time = new BigDecimal(_simTime);
		int precision = 6;
		return time.round(new MathContext(precision)).doubleValue();
	}

	/**
	 * We can only have one engine in the simulator so this method sees to that.
	 * In other words we have implemented a singleton.
	 * 
	 * @return
	 */
	public static SimEngine instance() {
		if (_instance == null)
			_instance = new SimEngine();
		return _instance;
	}

	/**
	 * To erase a scheduled event, this method can be used.
	 * 
	 * @param handle
	 */
	public void deregister(EventHandle handle) {
		_simTimeTree.remove(handle._simSlot);
	}

	/**
	 * This method is called to when scheduling an event for some target.
	 * Examples of events are messages, timer events etc.
	 * 
	 * @param registrator
	 * @param target
	 * @param event
	 * @param delayedExecution
	 * @return
	 */
	public EventHandle register(SimEnt registrator, SimEnt target, Event event,
			double delayedExecution) {
		double scheduleForTime = getTime() + delayedExecution;
		EventHandle handle = new EventHandle(registrator, target, event,
				new SimTimeSlot(scheduleForTime));
		_simTimeTree.put(handle._simSlot, handle);
		return handle;
	}

	/**
	 * To empty all events in the queue and restart the engine this method can
	 * be used. You however need to add a new event directly otherwise the
	 * engine will stop due to no events
	 */
	public void reset() {
		_simTimeTree.clear();
		_simTime = 0;
		_quit = false;
	}

	public void run() {
		/*
		 * This is the motor itself, is fetches events from the event list as
		 * long as there still are events present or until the stop method has
		 * been called.
		 */
		EventHandle handleToNextEvent = null;
		SimTimeSlot nextEventToExecute = null;

		do {
			if (_simTimeTree.size() == 0)
				_quit = true;
			else {
				nextEventToExecute = (SimTimeSlot) _simTimeTree.firstKey();
				handleToNextEvent = (EventHandle) _simTimeTree
						.get(nextEventToExecute);
				_simTime = nextEventToExecute._msek;
				handleToNextEvent._event.entering(handleToNextEvent._target);
				handleToNextEvent._target.recv(handleToNextEvent._registrator,
						handleToNextEvent._event);
				deregister(handleToNextEvent);
			}
		} while (!_quit);
		reset();
	}

	/**
	 * To force a stop of the motor, even when events are still present in the
	 * event list, this method can be used.
	 */
	public void stop() {
		_quit = true;
	}
}