package se.ltu.thesis.haidar.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

public class SqlStatistics {

	private Connection mConnection;
	private int mTestID;
	
	public static final String URL 		= "jdbc:mysql://localhost:3306/rlAgent";
	public static final String USER 	= "haidar";
	public static final String PASSWORD = "haidar12";
	
	public static final String TABLE_NAME 	= "rlAgent2";
	
	public static final String TUPLE_TABLE 	= "tuples";
	public static final String REWARD_TABLE = "rewards";
	public static final String TEST_TABLE 	= "tests";
	
	public static final String TEST_ID 		= "test_id";
	public static final String TUPLE_ID 	= "tuple_id";
	public static final String REWARD_ID 	= "reward_id";
	
	public static final String STEP 			= "step_size";
	public static final String EPSILON 			= "epsilon";
	public static final String LEARNING_RATE	= "learning_rate";
	public static final String DISCOUNT_FACTOR	= "discount_factor";
	public static final String REWARD 			= "reward";
	public static final String EPISODE_NUMBER 	= "episode_number";
	
	public static final int TEST_ID_ZERO_FIVE = 7;
	public SqlStatistics(){
	}
	
	public void connect() {
		try {
			mConnection = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void disconnect() {
		if (mConnection != null) {
			try {
				mConnection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	
	
	public static final String INSERT_INTO_TEST = "INSERT INTO "+TEST_TABLE+ "(" +TEST_ID+"," +STEP+") VALUES(?,?)";
	public void mNewTest(double mStep){
		PreparedStatement mStatement = null;
		try {
			mStatement = mConnection.prepareStatement(INSERT_INTO_TEST,Statement.RETURN_GENERATED_KEYS);
			mStatement.setNull(1, java.sql.Types.NULL);
			mStatement.setDouble(2, mStep);
			mStatement.executeUpdate();
			
			ResultSet rs = mStatement.getGeneratedKeys();
			rs.next();
			mTestID = rs.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static final String INSERT_INTO_TUPLE = "INSERT INTO "
			+TUPLE_TABLE
			+"("
			+ TUPLE_ID
			+ ","
			+ TEST_ID
			+ ","
			+ EPSILON
			+ ","
			+ LEARNING_RATE
			+ ","
			+ DISCOUNT_FACTOR
			+ ") VALUES(?,?,?,?,?)";
	
	public void insertNewTuple(double mEpsilon, double mLearningRate,
			double mDisscountFactor,double[] mReward){
		PreparedStatement mStatement = null;
		try {
			mStatement = mConnection.prepareStatement(INSERT_INTO_TUPLE, Statement.RETURN_GENERATED_KEYS);
			mStatement.setNull	(1, java.sql.Types.NULL	);
			mStatement.setInt	(2, mTestID				);
			mStatement.setDouble(3, mEpsilon			);
			mStatement.setDouble(4, mLearningRate		);
			mStatement.setDouble(5, mDisscountFactor	);
			mStatement.executeUpdate();
			
			
			ResultSet rs = mStatement.getGeneratedKeys();
			rs.next();
			int mTupleID = rs.getInt(1);
			insertReward(mTupleID, mReward);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static final String INSERT_INTO_REWARD = "INSERT INTO "+REWARD_TABLE
			+"("+ REWARD_ID+","+ TUPLE_ID+","+TEST_ID+","+EPISODE_NUMBER+","+REWARD
			+ ") VALUES(?,?,?,?,?)";
	public void insertReward(int mTupleID, double[] mReward){
		PreparedStatement mStatement = null;
		try {
			mStatement = mConnection.prepareStatement(INSERT_INTO_REWARD);
			for(int i =0; i <mReward.length ; i++){
				mStatement.setNull	( 1	, java.sql.Types.NULL);
				mStatement.setInt	( 2 , mTupleID);
				mStatement.setInt	( 3 , mTestID);
				mStatement.setInt	( 4 , i+1);
				mStatement.setDouble( 5 , mReward[i]);
				mStatement.addBatch();
			}
			mStatement.executeBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static final String QUERY_GET_TUPLE = "SELECT * FROM "+TUPLE_TABLE
			+" WHERE "+TEST_ID+"=? and "+EPSILON+"=? and "+LEARNING_RATE+"=? and "+DISCOUNT_FACTOR+"=?";
	public Tuple getTuple(int test_id, double epsilon ,double learning, double discount){
		PreparedStatement mStatement = null;
		ResultSet mRes = null;
		Map<String, Double> mMap = null;
		try {

			mStatement = mConnection.prepareStatement(QUERY_GET_TUPLE);
			mStatement.setInt	(1, test_id);
			mStatement.setDouble(2, epsilon);
			mStatement.setDouble(3, learning);
			mStatement.setDouble(4, discount);
			
			mRes = mStatement.executeQuery();
			
			mMap = new HashMap<>();
			
			while(mRes.next()){
				int mTuple_id 			= mRes.getInt(TUPLE_ID);
				int mTest_id 			= mRes.getInt(TEST_ID);
				double mEpsilon 		= mRes.getDouble(EPSILON);
				double mLearningRate 	= mRes.getDouble(LEARNING_RATE);
				double mDiscountFactor 	= mRes.getDouble(DISCOUNT_FACTOR);
				
				

				mMap.put(TUPLE_ID,			(double) mTuple_id);
				mMap.put(TEST_ID,			(double) mTest_id);
				mMap.put(EPSILON, 			mEpsilon);
				mMap.put(LEARNING_RATE, 	mLearningRate);
				mMap.put(DISCOUNT_FACTOR, 	mDiscountFactor);
				System.out.println(mMap.toString());
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new Tuple(mMap);
	}

	public static final String QUERY_GET_REWARD = "SELECT "+EPISODE_NUMBER+","+REWARD
			+" FROM "+REWARD_TABLE
			+" WHERE "+TEST_ID +"=?"+" and "+TUPLE_ID 	+"=?";
	public Reward getReward(int test_id, int tuple_id){
		PreparedStatement mStatement = null;
		ResultSet mRes = null;
		Map<Integer, Double> mMap = null;
		try {
			mStatement = mConnection.prepareStatement(QUERY_GET_REWARD);
			mStatement.setInt(1, test_id);
			mStatement.setInt(2, tuple_id);
			mRes = mStatement.executeQuery();
			
			mMap = new TreeMap<>();
			while(mRes.next()){
				int mEpisode 			= mRes.getInt(EPISODE_NUMBER);
				double mReward		 	= mRes.getDouble(REWARD);
				mMap.put(mEpisode ,	mReward);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new Reward(mMap);
	}
	public static final String QUERY_GET_MAX_REWARD = "SELECT  MAX("+REWARD+") FROM "+ REWARD_TABLE;
	public Reward getMaxReward(){
		PreparedStatement mStatement = null;
		ResultSet mRes = null;
		Map<Integer, Double> mMap = null;
		try {
			mStatement = mConnection.prepareStatement(QUERY_GET_MAX_REWARD);
			mRes = mStatement.executeQuery();
			
			mMap = new TreeMap<>();
			while(mRes.next()){
				int mEpisode 			= mRes.getInt(EPISODE_NUMBER);
				double mReward		 	= mRes.getDouble(REWARD);
				mMap.put(mEpisode ,	mReward);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new Reward(mMap);
	}
	public static void main(String[] args){
		SqlStatistics mS = new SqlStatistics();
		mS.connect();
		Tuple mTuple = mS.getTuple(7, 0.6, 0.35, 0.5);
		System.out.println(mTuple.toString());
		int tuple_id = mTuple.getID();
		
		System.out.println(mS.getReward(TEST_ID_ZERO_FIVE, tuple_id));
		System.out.println(mS.getMaxReward().toString());
		mS.disconnect();
	}
	public class Tuple{
		private static final String TUPLE_ID 		= "tuple_id";
		private static final String EPSILON 		= "epsilon";
		private static final String LEARNING_RATE	= "learning_rate";
		private static final String DISCOUNT_FACTOR	= "discount_factor";
		
		private Map<String, Double> mTupleMap;
		
		public Tuple(Map<String, Double> mTupleMap){
			this.mTupleMap = mTupleMap;
		}
		public String getName(){
			String str = "{"+mTupleMap.get(EPSILON)+","
		+mTupleMap.get(LEARNING_RATE)+","+mTupleMap.get(DISCOUNT_FACTOR)+"}";
			return str;
		}
		public int getID(){
			return (int) mTupleMap.get(TUPLE_ID).doubleValue();
		}
		public Map<String, Double> getMap(){
			return mTupleMap;
		}
		@Override
		public String toString(){
			return mTupleMap.toString();
		}
	}
	public class Reward{
		private Map<Integer, Double> mRewardMap;
		private double[] mRewardArray;
		
		public Reward(Map<Integer, Double> mRewardMap){
			this.mRewardMap = mRewardMap;
			createArray();
		}
		public double[] getArray(){
			return mRewardArray;
		}
		public Map<Integer, Double> getMap(){
			return mRewardMap;
		}
		public double getReward(int episode){
			return mRewardArray[episode-1];
		}
		private void createArray(){
			mRewardArray = new double[mRewardMap.size()];
			
			Iterator<Entry<Integer, Double>> mIterator = mRewardMap.entrySet()
					.iterator();
			while (mIterator.hasNext()) {
				Entry<Integer, Double> mEntry = mIterator.next();
				// the keys in the map represent the episode number and they start from 1
				// the map is Tree map
				mRewardArray[mEntry.getKey()-1] = mEntry.getValue();
			}
		}
		public String toString(){
			return mRewardMap.toString();
		}
	}
}