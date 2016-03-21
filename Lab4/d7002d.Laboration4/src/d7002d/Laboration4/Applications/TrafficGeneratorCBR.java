package d7002d.Laboration4.Applications;

import d7002d.laboration4.Connectables.NetworkInterface;

/**
 * Implements a constant bit rate traffic generator.
 * 
 */
public class TrafficGeneratorCBR extends TrafficGeneratorBase {

	/**
	 * Construct a CBR traffic generator with default values.
	 */
	public TrafficGeneratorCBR(NetworkInterface networkInterface) {
		super(networkInterface);
	}

	/**
	 * Construct a CBR traffic generator with specific values.
	 * 
	 * @param numberOfMessages
	 *            The amount of messages to send before stopping.
	 * @param intermessageGap
	 *            The time to wait after a message has been sent.
	 * @param messageStartSequence
	 *            The message sequence number to start with.
	 */
	public TrafficGeneratorCBR(NetworkInterface networkInterface, int numberOfMessages, double intermessageGap,
			int messageStartSequence) {
		super(networkInterface);
		this._numberOfMessages = numberOfMessages;
		this._intermessageGap = intermessageGap;
		this._messageStartSequence = messageStartSequence;
	}

	@Override
	public double getDelay() {
		// TODO Auto-generated method stub
		return _intermessageGap;
	}
	public String getName() {
		return "TrafficGeneratorCBR";
	}
}
