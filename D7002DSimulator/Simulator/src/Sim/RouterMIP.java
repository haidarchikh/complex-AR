package Sim;

public class RouterMIP extends SimEnt {
	private RouteTableEntry [] _routingTable;
	private int _interfaces;
	private int _now=0;
	private NetworkAddr _id;

	// When created, number of interfaces are defined
	
	RouterMIP(int network, int node, int interfaces)
	{
		_routingTable = new RouteTableEntry[interfaces];
		_interfaces=interfaces;
		_id = new NetworkAddr(network, node);
	}
	
	// This method connects links to the router and also informs the 
	// router of the host connects to the other end of the link
	
	public void connectInterface(int interfaceNumber, SimEnt link, SimEnt node)
	{
		if (interfaceNumber<_interfaces)
		{
			Node tempNode = new Node(((Node)node)._id.networkId(),((Node)node)._id.nodeId());
			_routingTable[interfaceNumber] = new RouteTableEntry(link, tempNode);
			Node temp = (Node) node;
			System.out.println("Router connected node "
			+temp._id.networkId()+"."+temp._id.nodeId()+" to interface "+interfaceNumber);
		}
		else
			System.out.println("Trying to connect to port not in router");
		
		((Link) link).setConnector(this);
	}

	// This method searches for an entry in the routing table that matches
	// the network number in the destination field of a messages. The link
	// represents that network number is returned
	
	private SimEnt getInterface(int networkAddress)
	{
		SimEnt routerInterface=null;
		for(int i=0; i<_interfaces; i++)
			if (_routingTable[i] != null)
			{
				if (((Node) _routingTable[i].node()).getAddr().networkId() == networkAddress)
				{
					routerInterface = _routingTable[i].link();
				}
			}
		return routerInterface;
	}
	
	
	// When messages are received at the router this method is called
	
	public void recv(SimEnt source, Event event)
	{
		if (event instanceof Message)
		{
			System.out.println("Router handles packet with seq: " + ((Message) event).seq()+
					" from node: "+((Message) event).source().networkId()+"." + ((Message) event).source().nodeId()
					+" to node "+((Message) event).destination().networkId()+"."+((Message) event).destination().nodeId());
			SimEnt sendNext = getInterface(((Message) event).destination().networkId());
			System.out.println("Router sends to node: " + ((Message) event).destination().networkId()+"." + ((Message) event).destination().nodeId());		
			send (sendNext, event, _now);
	
		}
		if(event instanceof RoutingUpdateEvent){
			System.out.println("Routing Update : Router got a routing update");
			RoutingUpdateEvent update = (RoutingUpdateEvent) event;
			// Look if the node is connected to this router, update the table then broadcast
			if(getInterface(update.oldNode().getAddr().networkId())!=null){
				// delete old routing entry
				deleteOldEntry(update.oldNode());
				// Create new Entry
				connectInterface(update.newInterface(), update.newLink(), update.newNode());
				// broadcastUpdate
				broadcastUpdate(update);
			}else{
				// if node is not connected to this router, just broadcast
				broadcastUpdate(update);
			}
		}
	}
	private void broadcastUpdate(RoutingUpdateEvent update){
		for(int i=0; i<_interfaces; i++)
			if (_routingTable[i] != null)
			{
					 send(_routingTable[i].link(),update,0);
			}
		System.out.println("Routing Update : Router broadcast a routing update, Node "
			+update.oldNode()._id.networkId()+"."+update.oldNode()._id.nodeId()
			+" to "+update.newNode()._id.networkId()+"."+update.newNode()._id.nodeId());
	}
	// delete the old node address
	private void deleteOldEntry(Node oldNode){
		int networkId = oldNode.getAddr().networkId();
		int nodeId = oldNode.getAddr().nodeId();
		for(int i=0; i<_interfaces; i++)
			if (_routingTable[i] != null)
			{
				if (((Node) _routingTable[i].node()).getAddr().networkId() == networkId 
						&& ((Node) _routingTable[i].node()).getAddr().nodeId() == nodeId)
				{
					_routingTable[i] = null;
				}
		   }
		System.out.println("Routing Update : Router deleted node "+networkId+"."+nodeId+" from routing table");
	}
}
