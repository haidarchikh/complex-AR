package d7002d.Laboration4.Applications;

import d7002d.Laboration4.NetAddr;
import d7002d.Laboration4.Events.Segment;

public interface Application {
	public abstract String getName();
	public abstract void handleSegment(NetAddr from, NetAddr to, Segment segment);
}
