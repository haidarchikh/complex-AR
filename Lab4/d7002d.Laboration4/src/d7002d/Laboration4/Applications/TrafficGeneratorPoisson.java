package d7002d.Laboration4.Applications;

import java.util.Random;

import d7002d.laboration4.Connectables.NetworkInterface;

/**
 * Implements a traffic generator with an exponential distribution for intermessage
 * gaps (resulting in a Poisson distribution for the number of messages per unit of time).
 * 
 */
public class TrafficGeneratorPoisson extends TrafficGeneratorBase {
	private Random _random = null;

	/**
	 * Construct a traffic generator with default values for generating
	 * intermessage gaps with an exponential distribution.
	 */
	public TrafficGeneratorPoisson(NetworkInterface networkInterface) {
		super(networkInterface);
		_random = new Random();
	}

	/**
	 * Construct a traffic generator with specific values for generating
	 * intermessage gaps with an exponential distribution.
	 * 
	 * @param numberOfMessages
	 *            The amount of messages to send before stopping.
	 * @param meanIntermessageGap
	 *            The expected time to wait after a message has been sent.
	 * @param messageStartSequence
	 *            The message sequence number to start with.
	 */
	public TrafficGeneratorPoisson(NetworkInterface networkInterface, int numberOfMessages,
			double meanIntermessageGap, int messageStartSequence) {
		super(networkInterface);
		_numberOfMessages = numberOfMessages;
		_intermessageGap = meanIntermessageGap;
		_messageStartSequence = messageStartSequence;
		_random = new Random();
	}

	@Override
	public double getDelay() {
		double delay = -_intermessageGap * Math.log(_random.nextDouble());
		return (delay >= 0 ? delay : 0);
	}
	public String getName() {
		return "TrafficGeneratorPoisson";
	}
}
