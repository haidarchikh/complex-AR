package d7002d.Laboration4.RunTest;

import d7002d.Laboration4.NetAddr;
import d7002d.Laboration4.SimEngine;
import d7002d.Laboration4.Utils;
import d7002d.laboration4.Connectables.Link;
import d7002d.laboration4.Connectables.LossyLink;
import d7002d.laboration4.Connectables.NetworkInterface;
import d7002d.laboration4.Connectables.Router;


public class HostRouterTest {
	
	public static void hostRouterTest() {
		// Testing a host connecting to a router, doing stateless
		// autoconfiguration.
		final NetworkInterface host = new NetworkInterface("HOST", null);
		final Router router = new Router("ROUTER", 2);
		router.assignAddress(0, NetAddr.makeNetAddr(10, 1));
		final Link link = new LossyLink("LINK", 0.05, 0.5, 0.05);
		link.connect(host, router.getPort(0));

		Utils.delayedAction(new Utils.Action() {
			@Override
			public void action() {host.autoconfigure();}}, 10);

		Utils.delayedAction(new Utils.Action() {
			@Override
			public void action() {
				SimEngine.instance().stop();
			}
		}, 200);

		// Start the simulation engine and off we go!
		Thread t = new Thread(SimEngine.instance());

		t.start();
		try {
			t.join();
			// Utils.statsPrinter(host1.getStatistics(), host1address,
			// host2address);
			// Utils.statsPrinter(host2.getStatistics(), host2address,
			// host1address);
		} catch (Exception e) {
			e.printStackTrace();
			System.out
					.println("The motor seems to have a problem, time for service?");
		}
	}
}
