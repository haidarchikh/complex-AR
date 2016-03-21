package d7002d.laboration4.Connectables;

import java.util.Random;

import d7002d.Laboration4.SimEngine;
import d7002d.Laboration4.SimEnt;
import d7002d.Laboration4.Utils;
import d7002d.Laboration4.Events.Data;
import d7002d.Laboration4.Events.Event;

/**
 * This class implements a link with packet loss, jitter and/or delay. Jitter
 * follows a gaussian (normal) distribution.
 * 
 */
public class LossyLink extends Link {
	private double _packetLossProbability = 0;
	private double _standardDeviation = 0;
	private double _packetDelay = 0;
	private Random _random = null;

	/**
	 * Creates a LossyLink object with some probability of packet loss, a
	 * maximum amount of gaussian jitter, and some delay.
	 * 
	 * @param probability
	 *            The probability of a packet being lost, from 0 to 1.
	 * @param delay
	 *            Constant delay, in time units.
	 * @param standardDeviation
	 *            The standard deviation from the delay.
	 */
	public LossyLink(String id, double probability, double delay,
			double standardDeviation) {
		super(id);
		_packetLossProbability = probability;
		if (_packetLossProbability < 0)
			_packetLossProbability = 0;
		else if (_packetLossProbability > 1)
			_packetLossProbability = 1;
		/*
		 * Ensure that delay is positive, unless it's a time traveling link.
		 */
		_standardDeviation = standardDeviation;
		_packetDelay = delay;
		if (_packetDelay < 0)
			_packetDelay = 0;
		_random = new Random();
	}

	/**
	 * Creates a LossyLink object with some probability of packet loss, a
	 * maximum amount of gaussian jitter, and some delay.
	 * 
	 * @param tries
	 *            The average number of tries necessary to incur one packet
	 *            loss.
	 * @param delay
	 *            Constant delay, in time units.
	 * @param standardDeviation
	 *            The standard deviation from the delay.
	 */
	public LossyLink(String id, int tries, double delay,
			double standardDeviation) {
		super(id);
		if (tries <= 0)
			_packetLossProbability = 1;
		else
			_packetLossProbability = 1.0 / tries;
		/*
		 * Ensure that delay is positive, unless it's a time traveling link.
		 */
		_standardDeviation = standardDeviation;
		_packetDelay = delay;
		if (_packetDelay < 0)
			_packetDelay = 0;
		_random = new Random();
	}

	public void recv(SimEnt source, Event event) {
		Random random = new Random();
		if (event instanceof Data) {
			if (random.nextDouble() <= _packetLossProbability) {
				Utils.logMessage(_id, String.format(
						"LossyLink received data from %s, but it was lost.",
						((Connectable) source).getId()));
			} else {
				double delay = _packetDelay + _random.nextGaussian()
						* _standardDeviation;
				if (null == _connectorA || null == _connectorB) {
					Utils.logMessage(
							_id,
							String.format(
									"LossyLink received data from %s, but had nowhere to send it.",
									((Connectable) source).getId()));
					return;
				}
				Connectable destination;
				if (source == _connectorA)
					destination = _connectorB;
				else
					destination = _connectorA;
				Utils.logMessage(
						_id,
						String.format("LossyLink received data from %s, passes it through to %s.",
								((Connectable) source).getId(),
								destination.getId()));
				send((SimEnt) destination, event, SimEngine.NOW
						+ (delay >= 0 ? delay : 0));
			}
		}
	}
}