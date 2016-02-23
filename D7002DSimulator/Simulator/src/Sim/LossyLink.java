package Sim;

import java.util.Random;

// This class implements a link without any loss, jitter or delay

public class LossyLink extends Link{
	Random _rand = new Random();
	private int _droppingProbability = 0;
	private int _mDelayRange  = 0;
	private int _mPakectCount = 0;
	private int _dropCount = 0;
	private int _mDelay = 0;
	public LossyLink(int identity, int droppingProbability , int mDelayRange)
	{
		super(identity);
		_droppingProbability = droppingProbability;
		_mDelayRange = mDelayRange;
	}
	// Called when a message enters the link
	public void recv(SimEnt src, Event ev)
	{
		if (ev instanceof Message){
			if(drop()){
				_dropCount ++;
				_mPakectCount++;
				System.out.println("Link: "+ _mIdentity +" dropped msg with seq: "+ ((Message) ev).seq()+
				" from node: " + ((Message) ev).source().networkId()+"." + ((Message) ev).source().nodeId()+
				" to node: " + ((Message) ev).destination().networkId()+"." + ((Message) ev).destination().nodeId()+
				" at time " + SimEngine.getTime()+
				", defined dropping probability: "+_droppingProbability+
				"%, actual dropping probability :"+getActualDroppingProbability()+"%"
				);
				System.out.println();
			}else{
				_mDelay = getDelay();
				_mPakectCount++;
				System.out.println("Link: "+ _mIdentity +" passes msg through with seq: "+ ((Message) ev).seq()+
						" from node: " + ((Message) ev).source().networkId()+"." + ((Message) ev).source().nodeId()+
						" to node: " + ((Message) ev).destination().networkId()+"." + ((Message) ev).destination().nodeId()+
						" at time " + SimEngine.getTime()+
						", added delay : "+_mDelay);
				}
				if (src == _connectorA)
				{
					send(_connectorB, ev, _mDelay);
					
				}
				else
				{
					send(_connectorA, ev, _mDelay);
				}
			}
		}
	// return a random delay within the maximum range specified at initialization time
	private int getDelay(){
		return _rand.nextInt(_mDelayRange);
	}
	// return true if the random number is less that the specified probability at initialization time
	private boolean drop(){
		return _rand.nextInt(100) < _droppingProbability;
	}
	// Calculate actual dropping probability
	private double getActualDroppingProbability(){
		return ((double)_dropCount / (double) _mPakectCount) * 100;
	}
}