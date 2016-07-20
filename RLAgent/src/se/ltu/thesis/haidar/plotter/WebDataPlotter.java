package se.ltu.thesis.haidar.plotter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import se.ltu.thesis.haidar.database.SqlStatistics;
import se.ltu.thesis.haidar.database.SqlStatistics.Reward;
import se.ltu.thesis.haidar.database.SqlStatistics.Tuple;

public class WebDataPlotter {
	
	public static final String X = " Learning Rate ";
	public static final String Y = " Discount Factor ";
	public static final String Z = " Episode ";
	
	

	public static final String NEW_LINE = "\n";
	public static final String EPISODE = "Episode";
	public static final String COMMA = ",";
	
	public static final String HEADER = "\""+ X + "\", \""+ Y + "\", \""+ Z + "\""+ "\", \""+EPISODE+"\"";
	
	private SqlStatistics mS;
	
	public static final String FILE_PATH_DATA2	= "datasets/data2/plots/greedyreward/epsilon_";
	public static final String FILE_PATH_DATA3	= "datasets/data3/plots/oracle/aggregatedByepsolin";
	public WebDataPlotter(){
		mS = new SqlStatistics();
	}
	public static void main(String[] args){
		WebDataPlotter mP = new WebDataPlotter();
		mP.mS.connect();
		//mP.getData();
		List<Tuple> mTupleList = null;
		try {
			mTupleList = mP.mS.getGreedy(mP.mS);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		double mStep = 0.1;
		StringBuilder mBuilder =mP.getDataCSVGreedy(mTupleList);
		String path = FILE_PATH_DATA3;
		mP.writeToFile(path, mBuilder);
		/*
		for (double mEpsilon = 0.1;mEpsilon <= 1;	mEpsilon += mStep) {
			mEpsilon = Math.round(mEpsilon*100.0)/100.0;
			StringBuilder mBuilder = mP.getDataCSVGreedy(mTupleList, mEpsilon);
			String path = FILE_PATH_DATA3+mEpsilon;
			mP.writeToFile(path, mBuilder);
		}
		
		
		/*
		int mTestID 	= 22;
		int mRewardStep = 1;
		double mStep = 0.1;
		for (double mEpsilon = 0;mEpsilon <= 1;	mEpsilon += mStep) {
			mEpsilon = Math.round(mEpsilon*100.0)/100.0;
			StringBuilder mBuilder = mP.getDataCSV(mTestID,mStep,mRewardStep ,mEpsilon);
			String path = FILE_PATH_DATA2+mEpsilon;
			mP.writeToFile(path, mBuilder);
		}
		*/
		mP.mS.disconnect();
	}

private StringBuilder getDataCSVOracleEpsilonTubles(List<Tuple> mTupleList){
		
		StringBuilder mBuilder = new StringBuilder();
		
		//mBuilder.append(HEADER);
		//mBuilder.append(NEW_LINE);
		int count = 0;
		Map<Map<Double,Double>,List<Double>>  mTuplesMap= new HashMap<>();
		for(int i = 0; i< mTupleList.size() ; i++){
		Tuple mTuple = mTupleList.get(i);
		
		double mEpsilon				= mTuple.getEpsilon();
		double mLearning 			= mTuple.getLearningRate();
		double mDiscount 			= mTuple.getDiscountFactor();
		
		double mGreedyReward 		= mTuple.getGreedyReward();
		double mEpisodeTillGreedy 	= mTuple.getEpisodeTillGready();
		
		
		if(mGreedyReward < 411.37983){
			
			Map<Double,Double>  mCurrentTuble= new HashMap<>();
			mCurrentTuble.put(mLearning, mDiscount);
			if(mTuplesMap.containsKey(mCurrentTuble)){
				List<Double> mCount = mTuplesMap.get(mCurrentTuble);
				mCount.add(mEpsilon);
				mTuplesMap.put(mCurrentTuble, mCount);
			} else{
				List<Double> mCount = new ArrayList<Double>();
				mCount.add(mEpsilon);
				mTuplesMap.put(mCurrentTuble, mCount);
			}
		}
		
		}
		System.out.println(mTuplesMap.size());
		Iterator<Map<Double, Double>> mEntrySet = mTuplesMap.keySet().iterator();
		while(mEntrySet.hasNext()){
			Map<Double, Double> entry = mEntrySet.next();
			List<Double> epsilons = mTuplesMap.get(entry);
			mBuilder
			.append(entry.toString())
			.append(COMMA)
			.append(epsilons.toString())
			.append(NEW_LINE);
		}
		
		System.out.println(mBuilder);
        return mBuilder;
	}
private StringBuilder getDataJSONAllOracleData(List<Tuple> mTupleList){
	
	StringBuilder mBuilder = new StringBuilder();
	
	//mBuilder.append(HEADER);
	//mBuilder.append(NEW_LINE);
	int count = 0;
	Map<Map<Double,Double>,List<Double>>  mTuplesMap= new HashMap<>();
	for(int i = 0; i< mTupleList.size() ; i++){
	Tuple mTuple = mTupleList.get(i);
	
	double mEpsilon				= mTuple.getEpsilon();
	double mLearning 			= mTuple.getLearningRate();
	double mDiscount 			= mTuple.getDiscountFactor();
	
	double mGreedyReward 		= mTuple.getGreedyReward();
	double mEpisodeTillGreedy 	= mTuple.getEpisodeTillGready();
	
	JSONObject mJSON = new JSONObject();
	mJSON.put(SqlStatistics.EPSILON, mEpsilon);
	mJSON.put(SqlStatistics.LEARNING_RATE, mLearning);
	mJSON.put(SqlStatistics.DISCOUNT_FACTOR, mDiscount);
	mJSON.put(SqlStatistics.GREEDY_REWARD, mGreedyReward);
	mJSON.put(SqlStatistics.EPISODE_NUMBER, mEpisodeTillGreedy);
	mBuilder.append(mJSON.toString()).append(NEW_LINE);
	}
    return mBuilder;
}
	
private StringBuilder getDataCSVGreedy(List<Tuple> mTupleList){
		
		StringBuilder mBuilder = new StringBuilder();
		
		mBuilder.append(HEADER);
		mBuilder.append(NEW_LINE);
		int count = 0;
		for(int i = 0; i< mTupleList.size() ; i++){
		Tuple mTuple = mTupleList.get(i);
		
		double mEpsilon				= mTuple.getEpsilon();
		double mLearning 			= mTuple.getLearningRate();
		double mDiscount 			= mTuple.getDiscountFactor();
		
		double mGreedyReward 		= mTuple.getGreedyReward();
		double mEpisodeTillGreedy 	= mTuple.getEpisodeTillGready();
		
		if(mGreedyReward >= 402.37983){
			
			count++;
			mBuilder
			//.append(mEpsilon)
			//.append(COMMA)
			.append(mLearning)
			.append(COMMA)
			.append(mDiscount)
			.append(COMMA)
			.append(mEpisodeTillGreedy)
			.append(COMMA)
			.append(" Epsilon = ")
			.append(mEpsilon)
			.append(NEW_LINE)
			;
		}
		
		}
        return mBuilder;
	}
	
private StringBuilder getDataCSV(int mTestID, double mStep , int mRewardStep, double mEpsilon){
	
	StringBuilder mBuilder = new StringBuilder();
	
	mBuilder.append(HEADER);
	mBuilder.append(NEW_LINE);
	
	for (double mLearning = 0;mLearning <= 1;	mLearning += mStep) {
		mLearning = Math.round(mLearning*100.0)/100.0;
		for (double mDiscount = 0;mDiscount <= 1;	mDiscount += mStep) {		
			mDiscount = Math.round(mDiscount*100.0)/100.0;
			
			// get the reward from the database
			double reward[] = getReward(mTestID, mEpsilon, mLearning, mDiscount);
			
			int episodeNum = 0;
			for(int i = 0 ; i< 10 ; i = i + mRewardStep){
				episodeNum = i;
				mBuilder
				.append(mLearning)
				.append(COMMA)
				.append(mDiscount)
				.append(COMMA)
				.append(reward[i])
				.append(COMMA)
				.append(EPISODE)
				.append(" ")
				.append(episodeNum)
				.append(NEW_LINE);
			}
		}
	}
    return mBuilder;
}

	private void writeToFile(String path, StringBuilder mBuilder) {
		File f = (new File(path)).getParentFile();
		if (f != null) {
			f.mkdirs();
		}
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			out.write(mBuilder.toString());
			out.write("\n");
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private double[] getReward(int test_id, double epsilon, double learning, double discount){
		System.out.println(test_id+", " + epsilon+", "  + learning+", "  + discount);
		Tuple mTuple 		= mS.getTuple(test_id, epsilon, learning, discount);
		int tuple_id 		= mTuple.getID();
		Reward mReward 		= mS.getReward(test_id, tuple_id);
		double[] reward 	= mReward.getArray();
		return reward;
	}
}
