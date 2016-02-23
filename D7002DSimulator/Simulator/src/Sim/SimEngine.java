package Sim;

import java.util.Iterator;
import java.util.TreeMap;
import java.util.Map.Entry;

// This class implements the simulation engine
// As long as there are events in the queue, the simulaiton 
// will run. When empty, the engine stops

public final class SimEngine implements Runnable {

	private static SimEngine _instance;
	private final TreeMap<SimTimeSlot,EventHandle> _simTimeTree = new TreeMap<>();
	private boolean _quit = false;
	private static double _simTime = 0;
	
	
	// This method is called to when scheduling an event for some target. Examples of events are messages,
	// timer events etc.
	
	public EventHandle register(SimEnt registrator, SimEnt target, Event event, double delayedExecution)
	{
		double scheduleForTime = getTime() + delayedExecution;
		EventHandle handle = new EventHandle(registrator, target, event, new SimTimeSlot(scheduleForTime));
		_simTimeTree.put(handle._simSlot, handle);
		return handle;
	}
	
	public static double getTime()
	{
		return _simTime;
	}
	
	// To erase a scheduled event, this method can be used
	
	public void deregister (EventHandle handle) 
	{
	
		_simTimeTree.remove(handle._simSlot);
	}
	
	// To force a stop of the motor, even when events are still
	// present in the event list. This method can be used
	
	public void stop()
	{
		_quit = true;
	}
	
	// To empty all events in the queue and restart the engine
	// this method can be used. You however need to add a new 
	// event directly otherwise the engine will stop due to no events 
	
	public void reset()
	{
		_simTimeTree.clear();
		_simTime = 0;
		_quit = false;
	}
	
	
	// We can only have one engine in the simulator so this method
	// sees to that. In other words we have implemented a singleton
	
	public static SimEngine instance() 
	{
		if (_instance==null) 
		{
			_instance = new SimEngine();
		}
		return _instance;
		
	}	
	
	// This is the motor itself, is fetches events from the event list as long as there 
	// still are events present or until the stop method has been called
	
	public void run()
	{	
		EventHandle handleToNextEvent	=null;
		SimTimeSlot nextEventToExecute	=null;
		
		do
		{
			if (_simTimeTree.size() == 0) 
				_quit=true;
			else
			{
				nextEventToExecute = (SimTimeSlot) _simTimeTree.firstKey();
				handleToNextEvent  = (EventHandle) _simTimeTree.get(nextEventToExecute);
				_simTime           = nextEventToExecute._msek;
				handleToNextEvent._event.entering(handleToNextEvent._target);
				try{handleToNextEvent._target.recv(handleToNextEvent._registrator, handleToNextEvent._event);}
				catch(Exception e ){
					// Cheating, don't print the exception :D. Nothing has happened
					}
				deregister(handleToNextEvent);
				chckMoving();
			}
		} while (!_quit);
		reset();
	}
	private Node node;
	private boolean moved = false;
	public void movingNode(Node node){
		this.node = node;
	}
	private void chckMoving(){
		if(SimEngine.getTime() > 20 && !moved){
			node.move(3, new NetworkAddr(4, 6));
			moved = true;
		}
	}
	/* Deprecated
	public void updateScheduler(NetworkAddr oldAddr, NetworkAddr newAddr){
		Iterator<Entry<SimTimeSlot, EventHandle>> mIterator = _simTimeTree.entrySet().iterator();
		while(mIterator.hasNext()){
			SimEnt mEnt = mIterator.next().getValue()._target;
			if(mEnt instanceof Node){
				Node mNode = (Node) mEnt;
				if(mNode._id.networkId() == oldAddr.networkId() && mNode._id.nodeId() == oldAddr.nodeId()){
					mNode._id = newAddr;
				}
			}
		}
	}
	*/
}