package Sim;

// This class implements a node (host) it has an address, a peer that it communicates with
// and it count messages send and received.

public class Node extends SimEnt {
	protected NetworkAddr _id;
	protected SimEnt _peer;
	protected int _toNetwork;
	protected int _toHost;
	
	public Node (int network, int node)
	{
		super();
		_id = new NetworkAddr(network, node);
	}	
	
	
	// Sets the peer to communicate with. This node is single homed
	
	public void setPeer (SimEnt peer)
	{
		_peer = peer;
		
		if(_peer instanceof Link)
		{
			 ((Link) _peer).setConnector(this);
		}
	}
	
	
	public NetworkAddr getAddr()
	{
		return _id;
	}
	public void recv(SimEnt src, Event ev)
	{
		if (ev instanceof Message)
		{
			System.out.println("Node "+_id.networkId()+ "." + _id.nodeId() +
					" receives message with seq: "+((Message) ev).seq() +
					" at time "+SimEngine.getTime());
			}
		}
}
	


	
	/*
		
	// This method is called upon that an event destined for this node triggers.
	
	public void recv(SimEnt src, Event ev)
	{
		if (ev instanceof Message)
		{
			_mMessageCount++;
			if(_mMessageCount<3){
				calculateJitter();
				System.out.println("Node "+_id.networkId()+ "." + _id.nodeId() +
						" receives message with seq: "+((Message) ev).seq() +
						" at time "+SimEngine.getTime());
			}else{
				System.out.println("Node "+_id.networkId()+ "." + _id.nodeId() +
						" receives message with seq: "+((Message) ev).seq() +
						" at time "+SimEngine.getTime()+
						", Jitter = "+ calculateJitter() + " ms");
			}
		}
	}
	// Calculate Jitter
	private double _DeltaT1 = 0;
	private double _DeltaT2 = 0;
	private double _mT1;
	private double _mT2;
	private double _mT3;
	private double _mJitter;
	private int i = 1;
	private double _mJitterAve;
	private int _mMessageCount;
	//
	// The method execute the following formula 
	//    deltaT1 = T2 - T1
	//    deltaT2 = T3 - T2
	//    jitter  = | delataT1 - deltaT2 |
	// 
	protected double calculateJitter(){
		switch(i){
		case 1 : _mT1 = SimEngine.getTime(); i++;
		break;
		case 2 : _mT2 = SimEngine.getTime(); i++;
		_DeltaT1 = _mT2 - _mT1;
		break;
		case 3 : 
		_mT3 		= SimEngine.getTime(); i++;
		_DeltaT2 	= _mT3- _mT2;
		_mJitter 	= Math.abs(_DeltaT1 - _DeltaT2);
		_mJitterAve += _mJitter;
		_DeltaT1 	= _DeltaT2;
		_mT1 		= _mT3;
		break;
		case 4 : 
		_mT2 		= SimEngine.getTime();
		_DeltaT2	= _mT2 - _mT1;
		_mJitter 	= Math.abs(_DeltaT1 - _DeltaT2);
		_mJitterAve += _mJitter;
		_DeltaT1 	= _DeltaT2;
		_mT1 		= _mT2;
		break;
		}
		return _mJitter;
	}
	protected double getAverageJitter(){
		return _mJitterAve / (_mMessageCount - 2);
	}
	protected void move(int newInterface, NetworkAddr newAddr){
		Node oldMe = new Node(_id.networkId(),_id.nodeId());
		//SimEngine.instance().updateScheduler(_id, newAddr);
		_id = newAddr;
		RoutingUpdateEvent update = new RoutingUpdateEvent(oldMe,this,(Link)_peer, newInterface);
		send(_peer,update, 0);
		System.out.println("Routing Update : Node "+oldMe._id.networkId()+"."+oldMe._id.nodeId()+" moved to "+newAddr.networkId()+"."+newAddr.nodeId());
	}
	protected void handleRoutingUpdate(RoutingUpdateEvent update){
		if(update.oldNode().getAddr().networkId()==_toNetwork 
				&& update.oldNode().getAddr().nodeId()==_toHost){
			System.out.println("Routing Update : Node "+_id.networkId()+"."+_id.nodeId()+" got routing update node "
				+update.oldNode().getAddr().networkId()+"."
				+update.oldNode().getAddr().nodeId()+" moved to "
				+update.newNode().getAddr().networkId()+"."
				+update.newNode().getAddr().nodeId());
			_toNetwork = update.newNode().getAddr().networkId();
			_toHost = update.newNode().getAddr().nodeId();
		}
		}
		}
		*/