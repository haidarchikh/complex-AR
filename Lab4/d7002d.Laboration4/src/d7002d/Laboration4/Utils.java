package d7002d.Laboration4;

import java.util.Random;

import d7002d.Laboration4.Events.Event;
import d7002d.Laboration4.Events.TimerEvent;
import d7002d.Laboration4.TrafficSink.Statistics;

/**
 * A collection of helper utilities, for printing and generating data.
 * 
 */
public class Utils {
	/**
	 * An interface for creating ad-hoc "statement collections" for running at
	 * different points in the simulation.
	 * 
	 * 
	 */
	public interface Action {
		/**
		 * This code will be run at some specified point in time.
		 */
		public void action();
	}

	/**
	 * This is used for having dynamic (or rather configuration file based)
	 * number formatting for time.
	 */
	private static String NUMBERFORMAT = "[%"+ String.format("%d.%d", Configs.TIME_CHARS, Configs.TIME_DECIMALS)+ "f]";

	private static Random RANDOM = new Random();

	/**
	 * A class for creating ad-hoc "statement collections" for running at
	 * different points in the simulation; an example could be having a line to
	 * stop some sending entity at some point t.
	 * 
	 * @param action
	 *            The "statement collection" to run at the specified time.
	 * @param time
	 *            The time to run the action function at.
	 */
	public static void delayedAction(Action action, double time) {
		class Delayer extends SimEnt {
			private Action _action;

			Delayer(Action action) {
				_action = action;
			}

			@Override
			public void recv(SimEnt source, Event event) {
				if (event instanceof TimerEvent)
					_action.action();
			}
		}
		Delayer delayer = new Delayer(action);
		delayer.send(delayer, new TimerEvent(), time);
	}

	/**
	 * Use this for printing a formatted output, presumably when working with
	 * packets.
	 * 
	 * @param from
	 *            The originating address of the treated packets.
	 * @param to
	 *            The destined address of the treated packets.
	 * @param message
	 *            Some text describing the treatment.
	 */
	public static void logMessage(NetAddr from, NetAddr to, String message) {
		String output = String.format(NUMBERFORMAT + "{%s->%s} %s",SimEngine.getTime(), from.dotAddress(), to.dotAddress(),
				message);
		System.out.println(output);
	}

	/**
	 * Use this for printing a formatted output consisting of only a timestamp
	 * and a message.
	 * 
	 * @param message
	 *            The message to print.
	 */
	public static void logMessage(String message) {
		System.out.println(String.format(NUMBERFORMAT + " %s",
				SimEngine.getTime(), message));
	}

	/**
	 * Use this for printing an identified formatted output, presumably when
	 * working with packets.
	 * 
	 * @param id
	 *            Some identifier for the process.
	 * @param from
	 *            The originating address of the treated packets.
	 * @param to
	 *            to The destined address of the treated packets.
	 * @param message
	 *            Some text describing the treatment.
	 */
	public static void logMessage(String id, NetAddr from, NetAddr to,
			String message) {
		String output = String.format(NUMBERFORMAT + "[%s][%s->%s] %s",
				SimEngine.getTime(), id, from.dotAddress(), to.dotAddress(),
				message);
		System.out.println(output);
	}

	/**
	 * Use this for printing an identified formatted output.
	 * 
	 * @param id
	 *            Some identifier for the process to print.
	 * @param message
	 *            Some text describing the process.
	 */
	public static void logMessage(String id, String message) {
		System.out.println(String.format(NUMBERFORMAT + "[%s] %s",
				SimEngine.getTime(), id, message));
	}

	/**
	 * Use this for printing an identified formatted output when working with
	 * applications.
	 * 
	 * @param id
	 *            Some identifier for the process to print.
	 * @param app
	 *            The identifier for the application.
	 * @param message
	 *            Some text describing the process.
	 */
	public static void logMessage(String id, String app, String message) {
		System.out.println(String.format(NUMBERFORMAT + "[%s][%s] %s",
				SimEngine.getTime(), id, app, message));
	}

	/**
	 * Generate a random decimal integer of a certain length (i.e. number of
	 * digits).
	 * 
	 * @param digits
	 *            The number of digits to generate.
	 * @return A randomly generated integer.
	 */
	public static int randomDigits(int digits) {
		String string = randomString("0123456789", digits);
		while (digits > 1 && string.startsWith("0"))
			string = randomString("0123456789", digits);
		return Integer.parseInt(string);
	}

	/**
	 * Generate a random hexadecimal number of a certain length (i.e. number of
	 * digits).
	 * 
	 * @param length
	 *            The number of digits (characters) to generate.
	 * @return A randomly generated hexadecimal number.
	 */
	public static String randomHex(int length) {
		return randomString("abcdef0123456789", length);
	}

	/**
	 * Generate a random decimal integer of a maximal size.
	 * 
	 * @param max
	 *            The biggest number to possibly generate.
	 * @return A randomly generated integer.
	 */
	public static int randomNumber(int max) {
		return RANDOM.nextInt(max);
	}

	/**
	 * Generate a random sequence of alpha-numerical characters and underscore.
	 * 
	 * @param length
	 *            The number of characters to generate.
	 * @return A randomly generated string of alpha-numerical characters.
	 */
	public static String randomString(int length) {
		return randomString("abcdefghijklmnopqrstuvwxyz0123456789_", length);
	}

	/**
	 * Generate a random sequence of supplied characters.
	 * 
	 * @param input
	 *            The possible characters to select from.
	 * @param length
	 *            The number of characters to generate.
	 * @return A randomly generated string of characters.
	 */
	public static String randomString(String input, int length) {
		char[] chars = input.toCharArray();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			char c = chars[RANDOM.nextInt(chars.length)];
			sb.append(c);
		}
		String output = sb.toString();
		return output;
	}

	/**
	 * A method for printing network statistics, presumably after a traffic
	 * generator run.
	 * 
	 * @param statistics
	 *            A statistics object.
	 * @param to
	 *            The traffic originator to regard.
	 * @param from
	 *            The traffic destination to regard.
	 */
	public static void statsPrinter(Statistics statistics, NetAddr to,
			NetAddr from) {
		String report;
		if (statistics.messageCount(from) == 1)
			report = String.format("[%s->%s] There was 1 message",
					from.dotAddress(), to.dotAddress());
		else
			report = String.format("[%s->%s] There were %d messages",
					from.dotAddress(), to.dotAddress(),
					statistics.messageCount(from));
		if (!statistics.averageMessageGap(from).isInfinite()
				&& !statistics.averageMessageGap(from).isNaN())
			report += String.format(" with average message gap %f",
					statistics.averageMessageGap(from));
		if (!statistics.messagesPerTimeUnit(from).isInfinite()
				&& !statistics.averageMessageGap(from).isNaN())
			report += String.format(
					", with a rate of %f messages per time unit",
					statistics.messagesPerTimeUnit(from));
		report += ".";
		System.out.println(report);
	}
}
