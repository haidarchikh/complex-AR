package d7002d.Laboration4;

import java.util.ArrayList;
import java.util.HashMap;

import d7002d.Laboration4.Events.Data;

/**
 * Create a traffic sink object for keeping statistics on received messages at
 * some node or such.
 * 
 * 
 */
public class TrafficSink {
	private class Row {
		private double _arrivalTime;
		private Data _payload;

		Row(double arrivalTime, Data payload) {
			_arrivalTime = arrivalTime;
			_payload = payload;
		}

		Data getPayload() {
			return _payload;
		}

		double getTime() {
			return _arrivalTime;
		}
	}

	/**
	 * A traffic sink statistics object for calculating statistics on received
	 * messages.
	 * 
	 */
	public class Statistics {
		/**
		 * Calculate the average message gap time from the messages received
		 * from some node.
		 * 
		 * @param from
		 *            The origin of the messages to analyze.
		 * @return The average message gap time.
		 */
		public Double averageMessageGap(NetAddr from) {
			ArrayList<Row> messageList = _sourceMap.get(from.dotAddress());
			if (null == messageList)
				return Double.NaN;
			else {
				double totalGapTimes = 0;
				for (int i = 1; i < messageList.size(); i++)
					totalGapTimes += messageList.get(i).getTime()
							- messageList.get(i - 1).getTime();
				return totalGapTimes / (messageList.size() - 1);
			}
		}

		/**
		 * Count the number of messages received from some node.
		 * 
		 * @param from
		 *            The origin of the messages to count.
		 * @return The number of messages received.
		 */
		public int messageCount(NetAddr from) {
			ArrayList<Row> messageList = _sourceMap.get(from.dotAddress());
			if (null == messageList)
				return 0;
			else
				return messageList.size();
		}

		/**
		 * Calculate the number of messages received per unit time from some
		 * node.
		 * 
		 * @param from
		 *            The origin of the messages to analyze.
		 * @return The number of messages per unit time received.
		 */
		public Double messagesPerTimeUnit(NetAddr from) {
			ArrayList<Row> messageList = _sourceMap.get(from.dotAddress());
			if (null == messageList)
				return Double.NaN;
			else {
				double totalTime = messageList.get(messageList.size() - 1)
						.getTime() - messageList.get(0).getTime();
				return messageList.size() / totalTime;
			}
		}
	}

	private HashMap<String, ArrayList<Row>> _sourceMap;
	private Statistics _statistics;

	/**
	 * Create a blank TrafficSink object.
	 */
	public TrafficSink() {
		_sourceMap = new HashMap<String, ArrayList<Row>>();
		_statistics = new Statistics();
	}

	/**
	 * Get the statistics object for the traffic sink.
	 * 
	 * @return The statistics object.
	 */
	public Statistics getStats() {
		return _statistics;
	}

	/**
	 * Log a message for statistical purposes.
	 * 
	 * @param from
	 *            The origin of the message.
	 * @param arrivalTime
	 *            The time the message arrived.
	 * @param sequence
	 *            The message sequence number.
	 */
	public void logMessage(NetAddr from, double arrivalTime, Data payload) {
		Row newRow = new Row(arrivalTime, payload);
		ArrayList<Row> messageList = _sourceMap.get(from.dotAddress());
		if (null == messageList) {
			messageList = new ArrayList<Row>();
			messageList.add(newRow);
			_sourceMap.put(from.dotAddress(), messageList);
		} else {
			messageList.add(newRow);
		}
	}
}
