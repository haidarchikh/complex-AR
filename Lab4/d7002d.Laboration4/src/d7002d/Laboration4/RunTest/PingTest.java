package d7002d.Laboration4.RunTest;

import d7002d.Laboration4.NetAddr;
import d7002d.Laboration4.SimEngine;
import d7002d.Laboration4.Utils;
import d7002d.Laboration4.Applications.Ping;
import d7002d.Laboration4.Utils.Action;
import d7002d.laboration4.Connectables.Link;
import d7002d.laboration4.Connectables.LossyLink;
import d7002d.laboration4.Connectables.NetworkInterface;
import d7002d.laboration4.Connectables.Router;

public class PingTest {

	public static void pingTest() {
		// Create two end hosts that will be communicating via the routers.
		final NetworkInterface mobileHost = new NetworkInterface("MOBILEHOST",null);
		final NetworkInterface staticHost = new NetworkInterface("STATICHOST",null);

		// Create addressed routers.
		final Router routerA = new Router("ROUTER_A", 3);
		routerA.assignAddress(0, NetAddr.makeNetAddr(10, 1));
		routerA.assignAddress(1, NetAddr.makeNetAddr(20, 1));
		routerA.assignAddress(2, NetAddr.makeNetAddr(30, 1));

		final Router routerB = new Router("ROUTER_B", 3);
		routerB.assignAddress(0, NetAddr.makeNetAddr(50, 2));
		routerB.assignAddress(1, NetAddr.makeNetAddr(40, 2));
		routerB.assignAddress(2, NetAddr.makeNetAddr(30, 2));

		// Create links.
		final Link mobileLink = new LossyLink("MOBILELINK", 0.01, 0.5, 0.05);
		final Link staticLink = new LossyLink("STATICLINK", 0.01, 0.5, 0.05);
		final Link routerLink = new LossyLink("ROUTERLINK", 0.001, 0.05, 0.005);

		// Connections.
		mobileLink.connect(mobileHost, routerA.getPort(0));
		routerLink.connect(routerA.getPort(2), routerB.getPort(2));
		staticLink.connect(staticHost, routerB.getPort(0));

		routerA.createRouteTEMPORARY(50, 2);
		routerA.createRouteTEMPORARY(40, 2);
		routerB.createRouteTEMPORARY(20, 2);
		routerB.createRouteTEMPORARY(10, 2);
		
		routerA.getPort(2).createRouterTEMPORARY(routerB.getPort(2).getLinkAddr());
		routerB.getPort(2).createRouterTEMPORARY(routerA.getPort(2).getLinkAddr());

		// Create applications.
		final Ping mobilePing = new Ping(mobileHost);
		final Ping staticPing = new Ping(staticHost);

		Utils.delayedAction(new Utils.Action() {
			@Override
			public void action() {
				mobileHost.autoconfigure();
				staticHost.autoconfigure();
			}
		}, 10);

		Utils.delayedAction(new Utils.Action() {
			@Override
			public void action() {
				mobilePing.pings(10, staticHost.getNetAddr(), 16);
				staticPing.pings(10, mobileHost.getNetAddr(), 14);
			}
		}, 60);

		Utils.delayedAction(new Utils.Action() {
			@Override
			public void action() {
				routerA.disconnect(mobileLink);
				mobileHost.unassignAddr();
			}
		}, 100);

		Utils.delayedAction(new Utils.Action() {
			@Override
			public void action() {
				// mobilePing.stop();
			}
		}, 120);

		Utils.delayedAction(new Utils.Action() {
			@Override
			public void action() {
				routerA.connect(1, mobileLink);
				mobileHost.autoconfigure();
			}
		}, 140);

		Utils.delayedAction(new Utils.Action() {
			@Override
			public void action() {
				mobilePing.pings(10, staticHost.getNetAddr(), 16);
				// staticPing.pings(10, mobileHost.getNetAddressExternal(), 14);
			}
		}, 160);

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
