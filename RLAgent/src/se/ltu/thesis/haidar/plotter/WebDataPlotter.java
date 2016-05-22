package se.ltu.thesis.haidar.plotter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;

import org.json.JSONArray;
import org.json.JSONObject;

import se.ltu.thesis.haidar.database.SqlStatistics;
import se.ltu.thesis.haidar.database.SqlStatistics.Reward;
import se.ltu.thesis.haidar.database.SqlStatistics.Tuple;

public class WebDataPlotter {
	
	public static final String X = " Learning Rate ";
	public static final String Y = " Discount Factor ";
	public static final String Z = " Reward ";
	
	

	public static final String NEW_LINE = "\n";
	public static final String EPISODE = "Episode";
	public static final String COMMA = ",";
	
	public static final String HEADER = "\""+ X + "\", \""+ Y + "\", \""+ Z + "\", \" "+EPISODE+" \" ";
	
	private SqlStatistics mS;
	
	public static final String FILE_PATH_DATA2	= "datasets/data2/plots/epsilon_";
	public WebDataPlotter(){
		mS = new SqlStatistics();
	}
	public static void main(String[] args){
		WebDataPlotter mP = new WebDataPlotter();
		mP.mS.connect();
		//mP.getData();
		
		int mTestID 	= 18;
		int mRewardStep = 10;
		
		
		double mStep = 0.5;
		for (double mEpsilon = 0;mEpsilon <= 1;	mEpsilon += mStep) {
			mEpsilon = Math.round(mEpsilon*100.0)/100.0;
			StringBuilder mBuilder = mP.getDataCSV(mTestID,mStep,mRewardStep ,mEpsilon);
			String path = FILE_PATH_DATA2+mEpsilon;
			mP.writeToFile(path, mBuilder);
		}
		
		mP.mS.disconnect();
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
				
				String temp = "";
				int episodeNum = 0;
				for(int i = 0 ; i< reward.length ; i = i + mRewardStep){
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
	
	
	
	/* http://almende.github.io/chap-links-library/js/graph3d/playground/
	private void getData(){
		BigDecimal mStep = new BigDecimal("0.1");
		BigDecimal mEpsilon = new BigDecimal("0.8");
		
		int mTestID = 11;
		System.out.println(HEADER);
        for (BigDecimal mLearningRate 	= BigDecimal.ZERO;
				mLearningRate.compareTo(BigDecimal.ONE) <= 0;
				mLearningRate = mLearningRate.add(mStep)) {
			
			for (BigDecimal mDiscountFactor 	= BigDecimal.ZERO; 
					mDiscountFactor.compareTo(BigDecimal.ONE)<= 0;
					mDiscountFactor = mDiscountFactor.add(mStep)) {
				double reward[] = getReward(mTestID, mEpsilon.doubleValue(),
						mLearningRate.doubleValue(), mDiscountFactor.doubleValue());
				
				System.out.println(mLearningRate+" , "+ mDiscountFactor+" , "+ reward[10]);
				
			}
		}
	}
	*/
}
