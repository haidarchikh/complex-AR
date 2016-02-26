package Sim;

// This class implements a link without any loss, jitter or delay

public class Link extends SimEnt{
	protected SimEnt _connectorA=null;
	protected SimEnt _connectorB=null;
	protected int _now=0;
	protected int _mIdentity;
	
	public Link(int identety)
	{
		super();
		_mIdentity = identety;
	}
	
	// Connects the link to some simulation entity like
	// a node, switch, router etc.
	
	public void setConnector(SimEnt connectTo)
	{
		if (_connectorA == null) 
			_connectorA=connectTo;
		else
			_connectorB=connectTo;
	}

	// Called when a message enters the link
	public void recv(SimEnt src, Event ev)
	{
		if (ev instanceof Message)
		{
			System.out.println("Link: "+ _mIdentity +" passes msg through with seq: "+ ((Message) ev).seq()+
					" from node: " + ((Message) ev).source().networkId()+"." + ((Message) ev).source().nodeId()+
					" to node: " + ((Message) ev).destination().networkId()+"." + ((Message) ev).destination().nodeId()
					+ " at time " + SimEngine.getTime());
			}
			if (src == _connectorA)
			{
				send(_connectorB, ev, _now);
			}
			else
			{
				send(_connectorA, ev, _now);
			}
		}
	}