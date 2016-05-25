package se.ltu.thesis.haidar.datagenerator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONObject;

import se.ltu.thesis.haidar.agent.CloudWorld;

public class StateUpdater {
	public static final int COUNT_ALL = 1000;
	public static final int COUNT = 100;

	// Delay
	public static final double CONSTANT_DELAY = 10000;
	public static final double DELAY_VARIANCE = 10;
	public static final double ROUND_DELAY_TO = 10;
	
	public static final double PARETO_SHAPE = 5;
	
	// Throughput
	public static final double CONSTANT_THROUGHPUT = 1;
	public static final double THROUGHPUT_VARIANCE = 5;
	public static final double ROUND_THROUGHPUT_TO = 10;

	public static final String SAMPLE_COUNT 	= "sample count";
	
	public static final String FILE_PATH_DATA1	= "datasets/data1/data1.json";
	
	public static final String FILE_PATH_DATA2	= "datasets/data2/data2.json";
	
	public static final String FILE_PATH_DATA3	= "datasets/data3/data3.json";
	
	

	private Map<Integer, JSONObject> mData = new TreeMap<>();

	private DataGenerator D_N1_C1;
	private DataGenerator D_N1_C2;
	private DataGenerator D_N1_C3;

	private DataGenerator D_N2_C1;
	private DataGenerator D_N2_C2;
	private DataGenerator D_N2_C3;

	private DataGenerator T_N1_C1;
	private DataGenerator T_N1_C2;
	private DataGenerator T_N1_C3;

	private DataGenerator T_N2_C1;
	private DataGenerator T_N2_C2;
	private DataGenerator T_N2_C3;

	private JSONObject mJSON;

	
	
	
	public static void main(String[] args){
		
		StateUpdater mUpdater = new StateUpdater();
		mUpdater.initiateGenerators();
		mUpdater.dataSet3ToGenerators();
		
		mUpdater.dumpToFile(FILE_PATH_DATA3);
	}

	private void dumpToFile(String path) {
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

		JSONObject mJSON = null;
		try {
			for (int i = 0; i < COUNT_ALL; i++) {
				mJSON = getDataSample(i);
				out.write(mJSON.toString());
				out.write("\n");
			}

			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Map<Integer, JSONObject> loadDataFromFile(String path){
		JSONObject	 	mJSON 	= null;
		String 			line	= null;
		BufferedReader 	br		= null;
		
		try {
			br 		= new BufferedReader(new FileReader(path));
			line 	= br.readLine();
			
			while(line != null){
				mJSON = new JSONObject(line);
				mData.put(mJSON.getInt(SAMPLE_COUNT), mJSON);
				line = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mData;
	}

	private JSONObject getDataSample(int sampleNum) {
		mJSON = new JSONObject();
		mJSON.put(SAMPLE_COUNT, sampleNum);
		mJSON.put(CloudWorld.D_N1_C1, D_N1_C1.getData()[sampleNum]);
		mJSON.put(CloudWorld.D_N1_C2, D_N1_C2.getData()[sampleNum]);
		mJSON.put(CloudWorld.D_N1_C3, D_N1_C3.getData()[sampleNum]);

		mJSON.put(CloudWorld.D_N2_C1, D_N2_C1.getData()[sampleNum]);
		mJSON.put(CloudWorld.D_N2_C2, D_N2_C2.getData()[sampleNum]);
		mJSON.put(CloudWorld.D_N2_C3, D_N2_C3.getData()[sampleNum]);

		mJSON.put(CloudWorld.T_N1_C1, T_N1_C1.getData()[sampleNum]);
		mJSON.put(CloudWorld.T_N1_C2, T_N1_C2.getData()[sampleNum]);
		mJSON.put(CloudWorld.T_N1_C3, T_N1_C3.getData()[sampleNum]);

		mJSON.put(CloudWorld.T_N2_C1, T_N2_C1.getData()[sampleNum]);
		mJSON.put(CloudWorld.T_N2_C2, T_N2_C2.getData()[sampleNum]);
		mJSON.put(CloudWorld.T_N2_C3, T_N2_C3.getData()[sampleNum]);
		return mJSON;
	}

	public StateUpdater() {
		
	}
	public void initiateGenerators(){
		D_N1_C1 = new DataGenerator();
		D_N1_C2 = new DataGenerator();
		D_N1_C3 = new DataGenerator();

		D_N2_C1 = new DataGenerator();
		D_N2_C2 = new DataGenerator();
		D_N2_C3 = new DataGenerator();

		T_N1_C1 = new DataGenerator();
		T_N1_C2 = new DataGenerator();
		T_N1_C3 = new DataGenerator();

		T_N2_C1 = new DataGenerator();
		T_N2_C2 = new DataGenerator();
		T_N2_C3 = new DataGenerator();
		
	}
	

	private void addConstantDelay(DataGenerator mG) {
		mG.addGaussianPlan(COUNT_ALL, CONSTANT_DELAY, 0, ROUND_DELAY_TO);
	}

	private void addConstantThroughput(DataGenerator mG) {
		mG.addGaussianPlan(COUNT_ALL, CONSTANT_THROUGHPUT, 0,
				ROUND_THROUGHPUT_TO);
	}
	private void dataSet3ToGenerators() {
		// Count , Mean , Variance , RoundTO
		// D_N1_C1
		D_N1_C1.addParetoPlan(COUNT, 40, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C1.addParetoPlan(COUNT, 40, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C1.addParetoPlan(COUNT, 300, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C1.addParetoPlan(COUNT, 300, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C1.addParetoPlan(COUNT, 300, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C1.addParetoPlan(COUNT, 300, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C1.addParetoPlan(COUNT, 300, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C1.addParetoPlan(COUNT, 300, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C1.addParetoPlan(COUNT, 300, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C1.addParetoPlan(COUNT, 300, PARETO_SHAPE, ROUND_DELAY_TO);
		// D_N1_C2
		D_N1_C2.addParetoPlan(COUNT, 300, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C2.addParetoPlan(COUNT, 300, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C2.addParetoPlan(COUNT, 40, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C2.addParetoPlan(COUNT, 40, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C2.addParetoPlan(COUNT, 300, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C2.addParetoPlan(COUNT, 300, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C2.addParetoPlan(COUNT, 300, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C2.addParetoPlan(COUNT, 300, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C2.addParetoPlan(COUNT, 300, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C2.addParetoPlan(COUNT, 300, PARETO_SHAPE, ROUND_DELAY_TO);


		
		D_N2_C1.addParetoPlan(COUNT, 300, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N2_C1.addParetoPlan(COUNT, 300, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N2_C1.addParetoPlan(COUNT, 300, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N2_C1.addParetoPlan(COUNT, 300, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N2_C1.addParetoPlan(COUNT, 40, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N2_C1.addParetoPlan(COUNT, 40, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N2_C1.addParetoPlan(COUNT, 300, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N2_C1.addParetoPlan(COUNT, 300, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N2_C1.addParetoPlan(COUNT, 300, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N2_C1.addParetoPlan(COUNT, 300, PARETO_SHAPE, ROUND_DELAY_TO);
		// D_N2_C2
		D_N2_C2.addParetoPlan(COUNT, 300, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N2_C2.addParetoPlan(COUNT, 300, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N2_C2.addParetoPlan(COUNT, 300, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N2_C2.addParetoPlan(COUNT, 300, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N2_C2.addParetoPlan(COUNT, 300, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N2_C2.addParetoPlan(COUNT, 300, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N2_C2.addParetoPlan(COUNT, 40, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N2_C2.addParetoPlan(COUNT, 40, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N2_C2.addParetoPlan(COUNT, 300, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N2_C2.addParetoPlan(COUNT, 40, PARETO_SHAPE, ROUND_DELAY_TO);

		
		addConstantDelay(D_N1_C3);		
		addConstantDelay(D_N2_C3);
		// ////////////////////////////////////////////////////////////
		// //////////////////////// Throughput ////////////////////////
		// ////////////////////////////////////////////////////////////

		// D_N1_C1
		T_N1_C1.addGaussianPlan(COUNT, 50, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C1.addGaussianPlan(COUNT, 50, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C1.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE, ROUND_THROUGHPUT_TO);
		T_N1_C1.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C1.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C1.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C1.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C1.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C1.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C1.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		// T_N1_C2
		T_N1_C2.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C2.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C2.addGaussianPlan(COUNT, 50, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C2.addGaussianPlan(COUNT, 50, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C2.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C2.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE, ROUND_THROUGHPUT_TO);
		T_N1_C2.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C2.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C2.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C2.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C2.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);

		// D_N2_C1
		T_N2_C1.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N2_C1.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N2_C1.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE, ROUND_THROUGHPUT_TO);
		T_N2_C1.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N2_C1.addGaussianPlan(COUNT, 50, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N2_C1.addGaussianPlan(COUNT, 50, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N2_C1.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N2_C1.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N2_C1.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N2_C1.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		// T_N2_C2
		T_N2_C2.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N2_C2.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N2_C2.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N2_C2.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N2_C2.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N2_C2.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE, ROUND_THROUGHPUT_TO);
		T_N2_C2.addGaussianPlan(COUNT, 50, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N2_C2.addGaussianPlan(COUNT, 50, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N2_C2.addGaussianPlan(COUNT, 50, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N2_C2.addGaussianPlan(COUNT, 50, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N2_C2.addGaussianPlan(COUNT, 50, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);		
		
		addConstantThroughput(T_N1_C3);
		addConstantThroughput(T_N2_C3);
	}

	private void dataSet1ToGenerators() {
		// Count , Mean , Variance , RoundTO
		// D_N1_C1
		D_N1_C1.addParetoPlan(COUNT, 40, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C1.addParetoPlan(COUNT, 40, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C1.addParetoPlan(COUNT, 40, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C1.addParetoPlan(COUNT, 40, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C1.addParetoPlan(COUNT, 300, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C1.addParetoPlan(COUNT, 300, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C1.addParetoPlan(COUNT, 300, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C1.addParetoPlan(COUNT, 300, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C1.addParetoPlan(COUNT, 300, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C1.addParetoPlan(COUNT, 300, PARETO_SHAPE, ROUND_DELAY_TO);
		// D_N1_C2
		D_N1_C2.addParetoPlan(COUNT, 200, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C2.addParetoPlan(COUNT, 200, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C2.addParetoPlan(COUNT, 200, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C2.addParetoPlan(COUNT, 200, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C2.addParetoPlan(COUNT, 200, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C2.addParetoPlan(COUNT, 50, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C2.addParetoPlan(COUNT, 50, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C2.addParetoPlan(COUNT, 50, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C2.addParetoPlan(COUNT, 50, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C2.addParetoPlan(COUNT, 50, PARETO_SHAPE, ROUND_DELAY_TO);

		// D_N1_C3
		addConstantDelay(D_N1_C3);

		addConstantDelay(D_N2_C1);
		addConstantDelay(D_N2_C2);
		addConstantDelay(D_N2_C3);
		// ////////////////////////////////////////////////////////////
		// //////////////////////// Throughput ////////////////////////
		// ////////////////////////////////////////////////////////////

		// D_N1_C1
		T_N1_C1.addParetoPlan(COUNT, 50, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C1.addParetoPlan(COUNT, 50, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C1.addParetoPlan(COUNT, 50, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C1.addParetoPlan(COUNT, 50, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C1.addParetoPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C1.addParetoPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C1.addParetoPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C1.addParetoPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C1.addParetoPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C1.addParetoPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		// T_N1_C2
		T_N1_C2.addParetoPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C2.addParetoPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C2.addParetoPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C2.addParetoPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C2.addParetoPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C2.addParetoPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C2.addParetoPlan(COUNT, 50, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C2.addParetoPlan(COUNT, 50, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C2.addParetoPlan(COUNT, 50, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C2.addParetoPlan(COUNT, 50, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C2.addParetoPlan(COUNT, 50, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);

		// addConstantThroughput(T_N1_C1);
		// addConstantThroughput(T_N1_C2);
		addConstantThroughput(T_N1_C3);

		addConstantThroughput(T_N2_C1);
		addConstantThroughput(T_N2_C2);
		addConstantThroughput(T_N2_C3);
	}
	
	private void dataSet2ToGenerators() {
		// Count , Mean , Variance , RoundTO
		// D_N1_C1
		D_N1_C1.addParetoPlan(COUNT, 40, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C1.addParetoPlan(COUNT, 40, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C1.addParetoPlan(COUNT, 40, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C1.addParetoPlan(COUNT, 40, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C1.addParetoPlan(COUNT, 300, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C1.addParetoPlan(COUNT, 300, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C1.addParetoPlan(COUNT, 300, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C1.addParetoPlan(COUNT, 300, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C1.addParetoPlan(COUNT, 300, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C1.addParetoPlan(COUNT, 300, PARETO_SHAPE, ROUND_DELAY_TO);
		// D_N1_C2
		D_N1_C2.addParetoPlan(COUNT, 200, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C2.addParetoPlan(COUNT, 200, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C2.addParetoPlan(COUNT, 200, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C2.addParetoPlan(COUNT, 200, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C2.addParetoPlan(COUNT, 200, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C2.addParetoPlan(COUNT, 50, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C2.addParetoPlan(COUNT, 50, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C2.addParetoPlan(COUNT, 50, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C2.addParetoPlan(COUNT, 50, PARETO_SHAPE, ROUND_DELAY_TO);
		D_N1_C2.addParetoPlan(COUNT, 50, PARETO_SHAPE, ROUND_DELAY_TO);

		// D_N1_C3
		addConstantDelay(D_N1_C3);

		addConstantDelay(D_N2_C1);
		addConstantDelay(D_N2_C2);
		addConstantDelay(D_N2_C3);
		// ////////////////////////////////////////////////////////////
		// //////////////////////// Throughput ////////////////////////
		// ////////////////////////////////////////////////////////////

		// D_N1_C1
		T_N1_C1.addGaussianPlan(COUNT, 50, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C1.addGaussianPlan(COUNT, 50, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C1.addGaussianPlan(COUNT, 50, THROUGHPUT_VARIANCE, ROUND_THROUGHPUT_TO);
		T_N1_C1.addGaussianPlan(COUNT, 50, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C1.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C1.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C1.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C1.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C1.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C1.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		// T_N1_C2
		T_N1_C2.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C2.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C2.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C2.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C2.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C2.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE, ROUND_THROUGHPUT_TO);
		T_N1_C2.addGaussianPlan(COUNT, 50, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C2.addGaussianPlan(COUNT, 50, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C2.addGaussianPlan(COUNT, 50, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C2.addGaussianPlan(COUNT, 50, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C2.addGaussianPlan(COUNT, 50, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);

		// addConstantThroughput(T_N1_C1);
		// addConstantThroughput(T_N1_C2);
		addConstantThroughput(T_N1_C3);

		addConstantThroughput(T_N2_C1);
		addConstantThroughput(T_N2_C2);
		addConstantThroughput(T_N2_C3);
	}

	private void setupUpdaterAgentTestGaussian() {
		// Count , Mean , Variance , RoundTO
		// D_N1_C1
		D_N1_C1.addGaussianPlan(COUNT, 40, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addGaussianPlan(COUNT, 40, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addGaussianPlan(COUNT, 40, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addGaussianPlan(COUNT, 40, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addGaussianPlan(COUNT, 300, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addGaussianPlan(COUNT, 300, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addGaussianPlan(COUNT, 300, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addGaussianPlan(COUNT, 300, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addGaussianPlan(COUNT, 300, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addGaussianPlan(COUNT, 300, DELAY_VARIANCE, ROUND_DELAY_TO);
		// D_N1_C2
		D_N1_C2.addGaussianPlan(COUNT, 200, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addGaussianPlan(COUNT, 200, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addGaussianPlan(COUNT, 200, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addGaussianPlan(COUNT, 200, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addGaussianPlan(COUNT, 200, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addGaussianPlan(COUNT, 50, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addGaussianPlan(COUNT, 50, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addGaussianPlan(COUNT, 50, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addGaussianPlan(COUNT, 50, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addGaussianPlan(COUNT, 50, DELAY_VARIANCE, ROUND_DELAY_TO);

		// D_N1_C3
		addConstantDelay(D_N1_C3);

		addConstantDelay(D_N2_C1);
		addConstantDelay(D_N2_C2);
		addConstantDelay(D_N2_C3);
		// ////////////////////////////////////////////////////////////
		// //////////////////////// Throughput ////////////////////////
		// ////////////////////////////////////////////////////////////

		// D_N1_C1
		T_N1_C1.addGaussianPlan(COUNT, 50, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C1.addGaussianPlan(COUNT, 50, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C1.addGaussianPlan(COUNT, 50, THROUGHPUT_VARIANCE, ROUND_THROUGHPUT_TO);
		T_N1_C1.addGaussianPlan(COUNT, 50, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C1.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C1.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C1.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C1.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C1.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C1.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		// T_N1_C2
		T_N1_C2.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C2.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C2.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C2.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C2.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C2.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE, ROUND_THROUGHPUT_TO);
		T_N1_C2.addGaussianPlan(COUNT, 50, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C2.addGaussianPlan(COUNT, 50, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C2.addGaussianPlan(COUNT, 50, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C2.addGaussianPlan(COUNT, 50, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C2.addGaussianPlan(COUNT, 50, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);

		// addConstantThroughput(T_N1_C1);
		// addConstantThroughput(T_N1_C2);
		addConstantThroughput(T_N1_C3);

		addConstantThroughput(T_N2_C1);
		addConstantThroughput(T_N2_C2);
		addConstantThroughput(T_N2_C3);
	}

	private void setupConstatntUpdater() {
		// Count , Mean , Variance , RoundTO
		// D_N1_C1
		D_N1_C1.addGaussianPlan(COUNT, 200, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addGaussianPlan(COUNT, 200, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addGaussianPlan(COUNT, 200, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addGaussianPlan(COUNT, 200, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addGaussianPlan(COUNT, 200, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addGaussianPlan(COUNT, 200, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addGaussianPlan(COUNT, 200, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addGaussianPlan(COUNT, 200, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addGaussianPlan(COUNT, 200, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addGaussianPlan(COUNT, 200, DELAY_VARIANCE, ROUND_DELAY_TO);
		// D_N1_C2
		D_N1_C2.addGaussianPlan(COUNT, 500, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addGaussianPlan(COUNT, 500, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addGaussianPlan(COUNT, 500, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addGaussianPlan(COUNT, 500, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addGaussianPlan(COUNT, 500, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addGaussianPlan(COUNT, 500, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addGaussianPlan(COUNT, 500, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addGaussianPlan(COUNT, 500, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addGaussianPlan(COUNT, 500, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addGaussianPlan(COUNT, 500, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addGaussianPlan(COUNT, 500, DELAY_VARIANCE, ROUND_DELAY_TO);

		// D_N1_C3
		addConstantDelay(D_N1_C3);

		addConstantDelay(D_N2_C1);
		addConstantDelay(D_N2_C2);
		addConstantDelay(D_N2_C3);
		// ////////////////////////////////////////////////////////////
		// //////////////////////// Throughput ////////////////////////
		// ////////////////////////////////////////////////////////////

		addConstantThroughput(T_N1_C1);
		addConstantThroughput(T_N1_C2);
		addConstantThroughput(T_N1_C3);

		addConstantThroughput(T_N2_C1);
		addConstantThroughput(T_N2_C2);
		addConstantThroughput(T_N2_C3);
	}

}