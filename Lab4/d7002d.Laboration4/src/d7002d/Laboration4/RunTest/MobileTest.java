package d7002d.Laboration4.RunTest;

import d7002d.Laboration4.NetAddr;
import d7002d.Laboration4.SimEngine;
import d7002d.Laboration4.Utils;
import d7002d.Laboration4.Applications.HomeAgent;
import d7002d.Laboration4.Applications.MobileAgent;
import d7002d.Laboration4.Applications.Ping;
import d7002d.laboration4.Connectables.Link;
import d7002d.laboration4.Connectables.LossyLink;
import d7002d.laboration4.Connectables.NetworkInterface;
import d7002d.laboration4.Connectables.Router;
import d7002d.laboration4.Connectables.Switch;

public class MobileTest {

	/**
	 * Run a mobile network test. The set-up is that there are three interconnected router: A, B and C. The 10.* network of router A
	 * is the home network of the test. In this network, there is 
	 */
	public static void mobileTest() {
		final String password = Utils.randomString(8);
		// Create two end hosts that will be communicating via the routers.
		final MobileAgent mn = new MobileAgent("MN", null);
		final HomeAgent hn = new HomeAgent("HA", password);
		final NetworkInterface cn = new NetworkInterface("CN", null);

		// Create addressed routers.
		final Router rtrA = new Router("RTR_A", 3);
		
		rtrA.assignAddress(0, NetAddr.makeNetAddr(10, 1));
		rtrA.assignAddress(1, NetAddr.makeNetAddr(110, 1));
		rtrA.assignAddress(2, NetAddr.makeNetAddr(101, 1));

		final Router rtrB = new Router("RTR_B", 3);
		rtrB.assignAddress(0, NetAddr.makeNetAddr(20, 2));
		rtrB.assignAddress(1, NetAddr.makeNetAddr(11, 2));
		rtrB.assignAddress(2, NetAddr.makeNetAddr(110, 2));

		final Router rtrC = new Router("RTR_C", 3);
		rtrC.assignAddress(0, NetAddr.makeNetAddr(30, 3));
		rtrC.assignAddress(1, NetAddr.makeNetAddr(11, 3));
		rtrC.assignAddress(2, NetAddr.makeNetAddr(101, 3));
		
		final Switch swich = new Switch("Switch", 3);

		// Create links.
		final Link ml = new LossyLink("ML", 0.001, 0.5, 0.05);
		final Link hl = new LossyLink("HL", 0.001, 0.5, 0.05);
		final Link cl = new LossyLink("CL", 0.001, 0.5, 0.05);
		final Link sl = new LossyLink("SL", 0.001, 0.5, 0.05);// Static Link
		
		final Link rlAB = new LossyLink("RLAB", 0.001, 0.05, 0.005);
		final Link rlAC = new LossyLink("RLAC", 0.001, 0.05, 0.005);
		final Link rlBC = new LossyLink("RLBC", 0.001, 0.05, 0.005);

		// Connect the home network.
		ml.connect(mn, swich.getPort(0));
		hl.connect(hn, swich.getPort(1));
		sl.connect(swich.getPort(2), rtrA.getPort(0));
		
		// Connect the corresponding network.
		cl.connect(cn, rtrB.getPort(0));
		
		// Connect the routers.
		rlAB.connect(rtrA.getPort(1), rtrB.getPort(2));
		rlAC.connect(rtrA.getPort(2), rtrC.getPort(2));
		rlBC.connect(rtrB.getPort(1), rtrC.getPort(1));

		rtrA.createRouteTEMPORARY(20, 1);
		rtrA.createRouteTEMPORARY(30, 2);
		rtrB.createRouteTEMPORARY(10, 2);
		rtrB.createRouteTEMPORARY(30, 1);
		rtrC.createRouteTEMPORARY(10, 2);
		rtrC.createRouteTEMPORARY(20, 1);
		
		rtrA.getPort(1).createRouterTEMPORARY(rtrB.getPort(2).getLinkAddr());
		rtrA.getPort(2).createRouterTEMPORARY(rtrC.getPort(2).getLinkAddr());
		rtrB.getPort(2).createRouterTEMPORARY(rtrA.getPort(1).getLinkAddr());
		rtrB.getPort(1).createRouterTEMPORARY(rtrC.getPort(1).getLinkAddr());
		rtrC.getPort(2).createRouterTEMPORARY(rtrA.getPort(2).getLinkAddr());
		rtrC.getPort(1).createRouterTEMPORARY(rtrB.getPort(1).getLinkAddr());

		// Create applications.
		final Ping mobilePing = new Ping(mn);
		final Ping staticPing = new Ping(cn);

		Utils.delayedAction(new Utils.Action() {
			@Override
			public void action() {
				mn.autoconfigure();
				hn.autoconfigure();
				cn.autoconfigure();
			}
		}, 10);

		Utils.delayedAction(new Utils.Action() {
			@Override
			public void action() {
			mobilePing.ping(cn.getNetAddr());
			}
		}, 30);

		Utils.delayedAction(new Utils.Action() {
			@Override
			public void action() {
				mn.setHA(hn.getNetAddr(), password);
			}
		}, 40);

		Utils.delayedAction(new Utils.Action() {
			@Override
			public void action() {
				mn.setHome(mn.getNetAddr());
				
				mobilePing.ping(cn.getNetAddr());
			}
		}, 60);

		Utils.delayedAction(new Utils.Action() {
			@Override
			public void action() {
				ml.disconnectAll();
				mn.unassignAddr();
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
				ml.connect(mn, rtrC.getPort(0));
				mn.autoconfigure();
			}
		}, 145);

		Utils.delayedAction(new Utils.Action() {
			@Override
			public void action() {
				mobilePing.ping(cn.getNetAddr());
			}
		}, 160);

		Utils.delayedAction(new Utils.Action() {
			@Override
			public void action() {
				staticPing.ping(mn.getNetAddr());
			}
		}, 170);

		Utils.delayedAction(new Utils.Action() {
			@Override
			public void action() {
				SimEngine.instance().stop();
			}
		}, 180);

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
			System.out.println("The motor seems to have a problem, time for service?");
		}
	}
}
