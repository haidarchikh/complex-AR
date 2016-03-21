package d7002d.Laboration4.Applications;

import java.util.Random;

import d7002d.laboration4.Connectables.NetworkInterface;

/**
 * Implements a traffic generator with normal distribution for intermessage
 * gaps.
 * 
 */
public class TrafficGeneratorNormal extends TrafficGeneratorBase {
	private Random _random = null;

	/**
	 * Construct a traffic generator with default values for generating
	 * intermessage gaps with a normal distribution.
	 */
	public TrafficGeneratorNormal(NetworkInterface networkInterface) {
		super(networkInterface);
		_random = new Random();
	}

	/**
	 * Construct a traffic generator with specific values for generating
	 * intermessage gaps with a normal distribution.
	 * 
	 * @param numberOfMessages
	 *            The amount of messages to send before stopping.
	 * @param meanIntermessageGap
	 *            The expected time to wait after a message has been sent.
	 * @param standardDeviation
	 *            The standard deviation from the expected time.
	 * @param messageStartSequence
	 *            The message sequence number to start with.
	 */
	public TrafficGeneratorNormal(NetworkInterface networkInterface, int numberOfMessages,
			double meanIntermessageGap, double standardDeviation,
			int messageStartSequence) {
		super(networkInterface);
		_numberOfMessages = numberOfMessages;
		_intermessageGap = meanIntermessageGap;
		_standardDeviationScale = standardDeviation;
		_messageStartSequence = messageStartSequence;
		_random = new Random();
	}

	@Override
	public double getDelay() {
		double delay = _intermessageGap + _random.nextGaussian()
				* _standardDeviationScale;
		return (delay >= 0 ? delay : 0);
	}
	public String getName() {
		return "TrafficGeneratorNormal";
	}
}
