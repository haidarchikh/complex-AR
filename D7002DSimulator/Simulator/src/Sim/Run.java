package Sim;

// An example of how to build a topology and starting the simulation engine

public class Run {
	public static void main (String [] args)
	{
 		//Creates two links
 		Link link1 = new Link(1);
		Link link2 = new Link(2);
	//	Link LossyLink1 = new LossyLink( 1, 10, 15);
		Link link3 = new Link(3);
		// Create two end hosts that will be
		// communicating via the router
		Node host1 = new Node(1,1);
		Node host2 = new Node(2,1);
		Node host3 = new Node(3,1);
		
		// Constant Bit rate (CBR) generator
		TrafficGenerator mCBR = new TrafficGenerator(1, 1);
		// Poisson Traffic Generator (PTC)
 		TrafficGenerator mPTG = new TrafficGeneratorPoisson(1, 1);
 		// Normal Traffic Generator (NTG)
		TrafficGenerator mNTG = new TrafficGeneratorNormal(3, 1);

		//Connect links to hosts
		mNTG .setPeer(link1);
		host2.setPeer(link2);
		mPTG .setPeer(link3);

		// Creates as router and connect
		// links to it. Information about 
		// the host connected to the other
		// side of the link is also provided
		// Note. A switch is created in same way using the Switch class
		Router routeNode1 = new Router(4);
		
		routeNode1.connectInterface(0, link1, mNTG);
		routeNode1.connectInterface(1, link2, host2);
		routeNode1.connectInterface(2, link3, mPTG);
		// Generate some traffic
		// host1 will send 3 messages with time interval 5 to network 2, node 1. Sequence starts with number 1
		
		mNTG.StartSending(2, 1, 10, 10, 1);
		mPTG.StartSending(2, 1, 50, 2, 100);
		SimEngine.instance().movingNode(host2);
		
		//host2.move(3, new NetworkAddr(4, 6));
		// Start the simulation engine and of we go!
		Thread t=new Thread(SimEngine.instance());
		t.start();
		try
		{
			t.join();
		}
		catch (Exception e)
		{
			System.out.println("The motor seems to have a problem, time for service?");
		}		
	}
}