package se.ltu.thesis.haidar.database;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

public class Statistics {
	public static final String STEP 			= "step_size";
	public static final String EPSILON 			= "epsilon";
	public static final String LEARNING_RATE	= "learning_rate";
	public static final String DISCOUNT_FACTOR	= "discount_factor";
	public static final String REWARD 			= "reward";
	
	public static final String FILE_PATH ="output/db2";
	
	BufferedWriter out;
	
	public Statistics(){
		
		File f = (new File(FILE_PATH)).getParentFile();
		if (f != null) {
			f.mkdirs();
		}
		try {
			out = new BufferedWriter(new FileWriter(FILE_PATH));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void append(double mStep ,double mEpsilon, double mLearningRate, double mDiscountFactor, double[] mReward){
		JSONObject mJSON = new JSONObject();
		mJSON.put(STEP,				mStep);
		mJSON.put(EPSILON, 			mEpsilon);
		mJSON.put(LEARNING_RATE, 	mLearningRate);
		mJSON.put(DISCOUNT_FACTOR,  mDiscountFactor);
		mJSON.put(REWARD, new JSONArray(mReward));
		writeToFile(mJSON);
	}
	private void writeToFile(JSONObject mJSON) {
		
		try {
			out.write(mJSON.toString());
			out.write("\n");
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	public void close(){
		try {
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}