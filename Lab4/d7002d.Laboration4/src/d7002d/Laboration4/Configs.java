package d7002d.Laboration4;

/**
 * Defines shared and time related settings for the simulator. There are other
 * settings in-context, e.g. how default timings for traffic generators are in
 * the traffic generator class(es).
 * 
 * 
 */
public class Configs {
	/**
	 * Default entity ID length when generating random IDs.
	 */
	public static final int ID_LENGTH = 6;
	/**
	 * Timeout for a mobile agent waiting for acknowledgement.
	 */
	public static final double MOB_ACK_TIME = 10;
	/**
	 * The interval between a mobile agent's refresh messages.
	 */
	public static final double MOB_REFRESH = 25;
	/**
	 * How long a home agent is willing to wait for a mobile agent to refresh
	 * its position.
	 */
	public static final double MOB_TIMEOUT = 50;
	/**
	 * How long to wait for a neighbor to respond to a neighbor solicitation
	 * before concluding non-existent or unreachable.
	 */
	public static final int N_SOL_WAIT = 10;
	/**
	 * How many units of time to wait for a neighbor to respond when doing
	 * duplicate address detection.
	 */
	public static final int DAD_TIMING = N_SOL_WAIT;
	/**
	 * How many times to retry duplicate address detection before concluding
	 * that the address is unused (assuming no response).
	 * 
	 * Currently unimplemented.
	 */
	@Deprecated
	public static final int DAD_RESEND = 2;
	/**
	 * Minimum time for routers to wait between router advertisements,
	 * unsolicited or not.
	 */
	public static final int R_ADV_MIN = 10;
	/**
	 * Time for routers to wait between unsolicited router advertisements.
	 */
	public static final int R_ADV_INTERVAL = 50;
	/**
	 * How long to wait for a router's advertisement before sending out a router
	 * solicitation.
	 */
	public static final int ROUTER_WAIT = 10;
	/**
	 * How many characters to use when reporting time, including decimal point
	 * and decimals.
	 */
	public static final int TIME_CHARS = 12;
	/**
	 * How many decimals to use when reporting time.
	 */
	public static final int TIME_DECIMALS = 3;
	/**
	 * Whether or not to print verbosely.
	 * 
	 * Currently unimplemented.
	 */
	@Deprecated
	public static final boolean VERBOSE = true;
}
