package Sim;

public class MobileNode extends Node{
	/**
	 * This class is MN class, each MN has an identity to authenticate with the HA and 
	 * a Network address which hold the MN's home agent address
	 * */
	private NetworkAddr mHA;
	private int mIdentity;
	public MobileNode(int network, int node, int mIdentity , NetworkAddr mHA) {
		super(network, node);
		this.mIdentity 	= mIdentity;
		this.mHA 		= mHA;
	}
	public void recv(SimEnt src, Event ev)
	{
		if (ev instanceof ICMP)
		{
			System.out.println("Node "+_id.networkId()+ "." + _id.nodeId() +
					" receives message with seq: "+((Message) ev).seq() +
					" at time "+SimEngine.getTime());
			if(ev instanceof ICMPRouterAdvertisement)
			{
				// Look at the router advertisement, if the router has a FA send a binding update
				ICMPRouterAdvertisement mRA = (ICMPRouterAdvertisement) ev;
				// we set the network node's network address here
				
			}
			if(ev instanceof ICMPBindingAck)
			{
				System.out.println("MN received a binding ACK, everything is working just fine :D");
			}
		}
	}
	public void SendRouterSolicitation(){
		// I assume (0,0) is the layer 3 broadcast address
		ICMPRouterSolicitation RC = new ICMPRouterSolicitation(_id,new NetworkAddr(0, 0)); 
		send(_peer,RC,0);
	}
	public NetworkAddr getmHA() {
		return mHA;
	}
	public void setmHA(NetworkAddr mHA) {
		this.mHA = mHA;
	}
	public int getmIdentity() {
		return mIdentity;
	}
	public void setmIdentity(int mIdentity) {
		this.mIdentity = mIdentity;
	}
}