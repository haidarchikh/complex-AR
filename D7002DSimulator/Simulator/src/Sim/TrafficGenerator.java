package Sim;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;


public class TrafficGenerator extends Node {
	
	protected int _sentmsg;
	protected int _seq;
	
	protected int _stopSendingAfter; //messages
	protected int _expectedDelay; //time between messages
	
	private TreeMap<Integer,Integer> mStatistcs = new TreeMap<>();
	
	public TrafficGenerator (int network, int node)
	{
		super(network,node);
	}
	public void StartSending(int network, int node, int number, int expectedDelay, int startSeq)
	{
		_stopSendingAfter = number;
		_expectedDelay = expectedDelay;
		_toNetwork = network;
		_toHost = node;
		_seq = startSeq;
		send(this, new TimerEvent(),0);	
	}
	
//**********************************************************************************	
	
	// This method is called upon that an event destined for this node triggers.
	
	public final void recv(SimEnt src, Event ev)
	{
		if(ev instanceof RoutingUpdateEvent){
			// Look if this node is sending to the mobile node, update address
			RoutingUpdateEvent update = (RoutingUpdateEvent) ev;
			handleRoutingUpdate(update);
		}
		if (ev instanceof TimerEvent)
		{			
			if (_stopSendingAfter > _sentmsg)
			{
				_sentmsg++;
				send(_peer, new Message(_id, new NetworkAddr(_toNetwork, _toHost),_seq),0);
				int mDelay = getTimeInterval();
				appendToStatistics(mDelay);
				send(this, new TimerEvent(),mDelay);
				System.out.println("Node "+_id.networkId()+ "." + _id.nodeId() +
						" sent message with seq: "+_seq +" to node "+_toNetwork+"."+_toHost+" at time "+SimEngine.getTime());
				_seq++;
			}else{printStatistics();}
		}
	}
	protected int getTimeInterval(){
		return _expectedDelay;
	}
	protected void appendToStatistics(int delay){
		if(mStatistcs.containsKey(delay)){
			int occurrence = mStatistcs.get(delay);
			occurrence++;
			mStatistcs.put(delay, occurrence);	
		}else{
			mStatistcs.put(delay, 1);
		} 
	}
	protected void printStatistics(){
		System.out.println("Sent messages :"+_sentmsg+", mean value : "+_expectedDelay);
		Iterator<Entry<Integer, Integer>> mIterator = mStatistcs.entrySet().iterator();
		while(mIterator.hasNext()){
			 Entry<Integer, Integer> mEntry = mIterator.next();
			 int delay = mEntry.getKey();
			 int occurrence = mEntry.getValue();
			 double percentage = (double) occurrence /(double)_sentmsg;
			 percentage *=100;
			 System.out.println("Delay : "+String.format("%02d", delay)
					 +", Occurrence : "+ String.format("%02d", occurrence)
					 +", Percentage : "+percentage+"%");
			 }
		}
}