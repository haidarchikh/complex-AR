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

	public static final String SAMPLE_COUNT = "sample count";
	public static final String FILE_PATH = "outputData/samplesNew.json";

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
		mUpdater.writeToFile(FILE_PATH);
	}
	
	public Map<Integer, JSONObject> getData() {
		return mData;
	}

	private void writeToFile(String path) {
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
	public void loadDataFromFile(){
		JSONObject	 	mJSON 	= null;
		String 			line	= null;
		BufferedReader 	br		= null;
		
		try {
			br 		= new BufferedReader(new FileReader(FILE_PATH));
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
		
		setupUpdaterAgentTestPareto();
	}

	private void addConstantDelay(DataGenerator mG) {
		mG.addGaussianPlan(COUNT_ALL, CONSTANT_DELAY, 0, ROUND_DELAY_TO);
	}

	private void addConstantThroughput(DataGenerator mG) {
		mG.addGaussianPlan(COUNT_ALL, CONSTANT_THROUGHPUT, 0,
				ROUND_THROUGHPUT_TO);
	}
	private void setupUpdaterAgentTestPareto() {
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

	private void setupUpdater3() {
		// Count , Mean , Variance , RoundTO
		// D_N1_C1
		D_N1_C1.addGaussianPlan(COUNT, 40, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addGaussianPlan(COUNT, 40, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addGaussianPlan(COUNT, 40, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addGaussianPlan(COUNT, 40, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addGaussianPlan(COUNT, 40, DELAY_VARIANCE, ROUND_DELAY_TO);
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
		D_N1_C2.addGaussianPlan(COUNT, 40, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addGaussianPlan(COUNT, 40, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addGaussianPlan(COUNT, 40, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addGaussianPlan(COUNT, 40, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addGaussianPlan(COUNT, 40, DELAY_VARIANCE, ROUND_DELAY_TO);

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
		T_N1_C1.addGaussianPlan(COUNT, 50, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C1.addGaussianPlan(COUNT, 50, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C1.addGaussianPlan(COUNT, 50, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C1.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C1.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE, ROUND_THROUGHPUT_TO);
		T_N1_C1.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C1.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C1.addGaussianPlan(COUNT, 20, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		// T_N1_C2
		T_N1_C2.addGaussianPlan(COUNT, 15, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C2.addGaussianPlan(COUNT, 15, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C2.addGaussianPlan(COUNT, 15, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C2.addGaussianPlan(COUNT, 15, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C2.addGaussianPlan(COUNT, 15, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C2.addGaussianPlan(COUNT, 15, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C2.addGaussianPlan(COUNT, 60, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C2.addGaussianPlan(COUNT, 60, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C2.addGaussianPlan(COUNT, 60, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C2.addGaussianPlan(COUNT, 60, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);
		T_N1_C2.addGaussianPlan(COUNT, 60, THROUGHPUT_VARIANCE,	ROUND_THROUGHPUT_TO);

		// addConstantThroughput(T_N1_C1);
		// addConstantThroughput(T_N1_C2);
		addConstantThroughput(T_N1_C3);

		addConstantThroughput(T_N2_C1);
		addConstantThroughput(T_N2_C2);
		addConstantThroughput(T_N2_C3);
	}

	private void setupUpdater2() {
		// Count , Mean , Variance , RoundTO
		// D_N1_C1
		D_N1_C1.addGaussianPlan(COUNT, 70, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addGaussianPlan(COUNT, 70, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addGaussianPlan(COUNT, 70, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addGaussianPlan(COUNT, 70, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addGaussianPlan(COUNT, 70, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addGaussianPlan(COUNT, 300, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addGaussianPlan(COUNT, 300, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addGaussianPlan(COUNT, 300, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addGaussianPlan(COUNT, 300, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addGaussianPlan(COUNT, 300, DELAY_VARIANCE, ROUND_DELAY_TO);
		// D_N1_C2
		D_N1_C2.addGaussianPlan(COUNT, 300, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addGaussianPlan(COUNT, 300, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addGaussianPlan(COUNT, 300, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addGaussianPlan(COUNT, 300, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addGaussianPlan(COUNT, 300, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addGaussianPlan(COUNT, 70, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addGaussianPlan(COUNT, 70, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addGaussianPlan(COUNT, 70, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addGaussianPlan(COUNT, 70, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addGaussianPlan(COUNT, 70, DELAY_VARIANCE, ROUND_DELAY_TO);

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

	private void setupUpdater1() {
		// Count , Mean , Variance , RoundTO
		// D_N1_C1
		D_N1_C1.addGaussianPlan(COUNT, 500, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addGaussianPlan(COUNT, 500, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addGaussianPlan(COUNT, 400, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addGaussianPlan(COUNT, 400, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addGaussianPlan(COUNT, 500, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addGaussianPlan(COUNT, 100, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addGaussianPlan(COUNT, 500, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addGaussianPlan(COUNT, 150, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addGaussianPlan(COUNT, 250, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addGaussianPlan(COUNT, 500, DELAY_VARIANCE, ROUND_DELAY_TO);
		// D_N1_C2
		D_N1_C2.addGaussianPlan(COUNT, 150, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addGaussianPlan(COUNT, 200, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addGaussianPlan(COUNT, 500, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addGaussianPlan(COUNT, 180, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addGaussianPlan(COUNT, 500, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addGaussianPlan(COUNT, 500, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addGaussianPlan(COUNT, 500, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addGaussianPlan(COUNT, 160, DELAY_VARIANCE, ROUND_DELAY_TO);
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