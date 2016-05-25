package se.ltu.thesis.haidar.database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.json.JSONObject;

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
	public static final String GREEDY_REWARD 	= "greedy_reward";
	public static final String EPISODE_NUMBER 	= "episode_number";
	
	
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
			double mDisscountFactor,Double[] mReward,Double[] mGreedyReward){
		PreparedStatement mStatement = null;
		try {
			mStatement = mConnection.prepareStatement(INSERT_INTO_TUPLE, Statement.RETURN_GENERATED_KEYS);
			mStatement.setNull	(1, java.sql.Types.NULL	);
			mStatement.setInt	(2, mTestID				);
			mStatement.setDouble(3, mEpsilon);
			mStatement.setDouble(4, mLearningRate);
			mStatement.setDouble(5, mDisscountFactor);
			mStatement.executeUpdate();
			
			
			ResultSet rs = mStatement.getGeneratedKeys();
			rs.next();
			int mTupleID = rs.getInt(1);
			insertReward(mTupleID, mReward, mGreedyReward);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static final String INSERT_INTO_REWARD = "INSERT INTO "+REWARD_TABLE
			+"("+ REWARD_ID+","+ TUPLE_ID+","+TEST_ID+","+
			EPISODE_NUMBER+","+REWARD	+","+GREEDY_REWARD+") VALUES(?,?,?,?,?,?)";
	public void insertReward(int mTupleID, Double[] mReward, Double[] mGreedyReward){
		PreparedStatement mStatement = null;
		try {
			mStatement = mConnection.prepareStatement(INSERT_INTO_REWARD);
			for(int i =0; i <mReward.length ; i++){
				mStatement.setNull	( 1	, java.sql.Types.NULL);
				mStatement.setInt	( 2 , mTupleID);
				mStatement.setInt	( 3 , mTestID);
				mStatement.setInt	( 4 , i);
				mStatement.setDouble( 5 , Math.round(mReward[i] * 1000000.0) / 1000000.0);
				mStatement.setDouble( 6 , Math.round(mGreedyReward[i] * 1000000.0) / 1000000.0);
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
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new Tuple(mMap);
	}
	public static final String QUERY_GET_TUPLE_BY_ID = "SELECT * FROM "+TUPLE_TABLE
			+" WHERE "+TUPLE_ID+"=? ";
	public Tuple getTuple(int tuple_id){
		PreparedStatement mStatement = null;
		ResultSet mRes = null;
		Map<String, Double> mMap = null;
		try {

			mStatement = mConnection.prepareStatement(QUERY_GET_TUPLE_BY_ID);
			mStatement.setInt	(1, tuple_id);
			
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
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new Tuple(mMap);
	}

	public static final String QUERY_GET_REWARD = "SELECT * "+" FROM "+REWARD_TABLE
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
				double mGreedyReward 	= mRes.getDouble(GREEDY_REWARD);
				mMap.put(mEpisode ,	mGreedyReward);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new Reward(mMap);
	}
	public List<Tuple> getGreedy(SqlStatistics mS) throws SQLException{
		String query = loadQueryFromFile("./queries/1.sql");
		PreparedStatement mStatement = null;
		ResultSet mRes = null;
		List<Tuple> mTupleList = new ArrayList<Tuple>();
		mStatement = mConnection.prepareStatement(query);
		
		mRes = mStatement.executeQuery();
		
		while(mRes.next()){
			int mTestID 			= mRes.getInt(TEST_ID);
			int mTuple_id 			= mRes.getInt(TUPLE_ID);
			int mEpisode 			= mRes.getInt(EPISODE_NUMBER);
			double mGreedyReward 	= mRes.getDouble(GREEDY_REWARD);
			double mReward		 	= mRes.getDouble(REWARD);
			
			Tuple mTuple = mS.getTuple(mTuple_id);
			mTuple.setEpisodeTillGreedy(mEpisode);
			mTuple.setGreedyReward(mGreedyReward);
			mTupleList.add(mTuple);
		}
		return mTupleList;
		
	}
	public static String loadQueryFromFile(String path){
		StringBuilder mBuilder = new StringBuilder();
		String 			line	= null;
		BufferedReader 	br		= null;
		
		try {
			br 		= new BufferedReader(new FileReader(path));
			line 	= br.readLine();
			
			while(line != null){
				mBuilder.append(line);
				line = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mBuilder.toString();
	}
	public static void main(String[] args){
		SqlStatistics mS = new SqlStatistics();
		mS.connect();
		System.out.println(mS.getTuple(37777));;
		try {
			mS.getGreedy(mS);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		mS.disconnect();
	}
	public class Tuple{
		
		private static final String TUPLE_ID 		= "tuple_id";
		private static final String EPSILON 		= "epsilon";
		private static final String LEARNING_RATE	= "learning_rate";
		private static final String DISCOUNT_FACTOR	= "discount_factor";
		private static final String GREEDY_REWARD	= "greedy_reward";
		private static final String EPISODE_NUMBER 	= "episode_number";
		
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
		public double getEpsilon(){
			return mTupleMap.get(EPSILON).doubleValue();
		}
		public double getLearningRate(){
			return mTupleMap.get(LEARNING_RATE).doubleValue();
		}
		public double getDiscountFactor(){
			return mTupleMap.get(DISCOUNT_FACTOR).doubleValue();
		}
		public double getGreedyReward(){
			return mTupleMap.get(GREEDY_REWARD).doubleValue();
		}
		public double getEpisodeTillGready(){
			return mTupleMap.get(EPISODE_NUMBER).doubleValue();
		}
		public void setGreedyReward(double greedyReward){
			mTupleMap.put(GREEDY_REWARD, greedyReward);
		}
		public void setEpisodeTillGreedy(double episodeNum){
			mTupleMap.put(EPISODE_NUMBER, episodeNum);
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
			return mRewardArray[episode];
		}
		private void createArray(){
			mRewardArray = new double[mRewardMap.size()];
			
			Iterator<Entry<Integer, Double>> mIterator = mRewardMap.entrySet()
					.iterator();
			while (mIterator.hasNext()) {
				Entry<Integer, Double> mEntry = mIterator.next();
				// the map is Tree map
				mRewardArray[mEntry.getKey()] = mEntry.getValue();
			}
		}
		public String toString(){
			return mRewardMap.toString();
		}
	}
}