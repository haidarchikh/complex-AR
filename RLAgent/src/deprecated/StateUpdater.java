package deprecated;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;

import org.json.JSONObject;

import se.ltu.thesis.haidar.agent.CloudWorld;

public class StateUpdater {
	
	
	public static final double COUNT_ALL 			= 1000;
	public static final double COUNT 				= 100;
	
	// Delay
	public static final double	CONSTANT_DELAY 			= 10000;
	public static final int 	DELAY_VARIANCE 			= 10;
	public static final int 	ROUND_DELAY_TO 			= 10;
	
	// Throughput
	public static final double 	CONSTANT_THROUGHPUT 	= 1;
	public static final int 	THROUGHPUT_VARIANCE 	= 10;
	public static final int 	ROUND_THROUGHPUT_TO 	= 10;

	public static final String 	SAMPLE_COUNT 			= "sample count";
	public static final String 	FILE_PATH				= "outputData/samples.json";
	
	private static final boolean PRINT = false;
	
	private Hashtable<Integer,JSONObject> mTable = new Hashtable<>();
	
	private DataSetGenerator D_N1_C1 ;
	private DataSetGenerator D_N1_C2 ;
	private DataSetGenerator D_N1_C3 ;
	
	private DataSetGenerator D_N2_C1 ;
	private DataSetGenerator D_N2_C2 ;
	private DataSetGenerator D_N2_C3 ;
	
	private DataSetGenerator T_N1_C1 ;
	private DataSetGenerator T_N1_C2 ;
	private DataSetGenerator T_N1_C3 ;
	
	private DataSetGenerator T_N2_C1 ;
	private DataSetGenerator T_N2_C2 ;
	private DataSetGenerator T_N2_C3 ;
	
	private JSONObject mJSON;
	
	public StateUpdater(){
		D_N1_C1 = new DataSetGenerator(PRINT);
		D_N1_C2 = new DataSetGenerator(PRINT);
		D_N1_C3 = new DataSetGenerator(PRINT);
		
		D_N2_C1 = new DataSetGenerator(PRINT);
		D_N2_C2 = new DataSetGenerator(PRINT);
		D_N2_C3 = new DataSetGenerator(PRINT);
		
		T_N1_C1 = new DataSetGenerator(PRINT);
		T_N1_C2 = new DataSetGenerator(PRINT);
		T_N1_C3 = new DataSetGenerator(PRINT);
		
		T_N2_C1 = new DataSetGenerator(PRINT);
		T_N2_C2 = new DataSetGenerator(PRINT);
		T_N2_C3 = new DataSetGenerator(PRINT);
		
		//setupConstatntUpdater();
		//setupUpdater1();
		setupUpdaterAgentTest();
	}
	private void setupUpdaterAgentTest(){
		// Count , Mean , Variance , RoundTO
		// D_N1_C1
		D_N1_C1.addDataPlan(COUNT, 40 , DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 40 , DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 40 , DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 40 , DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 300, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 300, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 300, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 300, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 300, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 300, DELAY_VARIANCE, ROUND_DELAY_TO);
		// D_N1_C2
		D_N1_C2.addDataPlan(COUNT, 200, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 200, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 200, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 200, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 200, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 50 , DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 50 , DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 50 , DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 50 , DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 50 , DELAY_VARIANCE, ROUND_DELAY_TO);
		
		// D_N1_C3
		addConstantDelay(D_N1_C3);
		
		addConstantDelay(D_N2_C1);
		addConstantDelay(D_N2_C2);
		addConstantDelay(D_N2_C3);
		//////////////////////////////////////////////////////////////
		////////////////////////// Throughput ////////////////////////
		//////////////////////////////////////////////////////////////

		// D_N1_C1
		T_N1_C1.addDataPlan(COUNT, 50, THROUGHPUT_VARIANCE, ROUND_THROUGHPUT_TO);
		T_N1_C1.addDataPlan(COUNT, 50, THROUGHPUT_VARIANCE, ROUND_THROUGHPUT_TO);
		T_N1_C1.addDataPlan(COUNT, 50, THROUGHPUT_VARIANCE, ROUND_THROUGHPUT_TO);
		T_N1_C1.addDataPlan(COUNT, 50, THROUGHPUT_VARIANCE, ROUND_THROUGHPUT_TO);
		T_N1_C1.addDataPlan(COUNT, 20, THROUGHPUT_VARIANCE, ROUND_THROUGHPUT_TO);
		T_N1_C1.addDataPlan(COUNT, 20, THROUGHPUT_VARIANCE, ROUND_THROUGHPUT_TO);
		T_N1_C1.addDataPlan(COUNT, 20, THROUGHPUT_VARIANCE, ROUND_THROUGHPUT_TO);
		T_N1_C1.addDataPlan(COUNT, 20, THROUGHPUT_VARIANCE, ROUND_THROUGHPUT_TO);
		T_N1_C1.addDataPlan(COUNT, 20, THROUGHPUT_VARIANCE, ROUND_THROUGHPUT_TO);
		T_N1_C1.addDataPlan(COUNT, 20, THROUGHPUT_VARIANCE, ROUND_THROUGHPUT_TO);
		// T_N1_C2
		T_N1_C2.addDataPlan(COUNT, 20, THROUGHPUT_VARIANCE, ROUND_THROUGHPUT_TO);
		T_N1_C2.addDataPlan(COUNT, 20, THROUGHPUT_VARIANCE, ROUND_THROUGHPUT_TO);
		T_N1_C2.addDataPlan(COUNT, 20, THROUGHPUT_VARIANCE, ROUND_THROUGHPUT_TO);
		T_N1_C2.addDataPlan(COUNT, 20, THROUGHPUT_VARIANCE, ROUND_THROUGHPUT_TO);
		T_N1_C2.addDataPlan(COUNT, 20, THROUGHPUT_VARIANCE, ROUND_THROUGHPUT_TO);
		T_N1_C2.addDataPlan(COUNT, 20, THROUGHPUT_VARIANCE, ROUND_THROUGHPUT_TO);
		T_N1_C2.addDataPlan(COUNT, 50, THROUGHPUT_VARIANCE, ROUND_THROUGHPUT_TO);
		T_N1_C2.addDataPlan(COUNT, 50, THROUGHPUT_VARIANCE, ROUND_THROUGHPUT_TO);
		T_N1_C2.addDataPlan(COUNT, 50, THROUGHPUT_VARIANCE, ROUND_THROUGHPUT_TO);
		T_N1_C2.addDataPlan(COUNT, 50, THROUGHPUT_VARIANCE, ROUND_THROUGHPUT_TO);
		T_N1_C2.addDataPlan(COUNT, 50, THROUGHPUT_VARIANCE, ROUND_THROUGHPUT_TO);
		
		//addConstantThroughput(T_N1_C1);
		//addConstantThroughput(T_N1_C2);
		addConstantThroughput(T_N1_C3);
		
		addConstantThroughput(T_N2_C1);
		addConstantThroughput(T_N2_C2);
		addConstantThroughput(T_N2_C3);
	}
	private void setupUpdater3(){
		// Count , Mean , Variance , RoundTO
		// D_N1_C1
		D_N1_C1.addDataPlan(COUNT, 40 , DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 40 , DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 40 , DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 40 , DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 40 , DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 300, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 300, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 300, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 300, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 300, DELAY_VARIANCE, ROUND_DELAY_TO);
		// D_N1_C2
		D_N1_C2.addDataPlan(COUNT, 200, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 200, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 200, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 200, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 200, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 40 , DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 40 , DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 40 , DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 40 , DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 40 , DELAY_VARIANCE, ROUND_DELAY_TO);
		
		// D_N1_C3
		addConstantDelay(D_N1_C3);
		
		addConstantDelay(D_N2_C1);
		addConstantDelay(D_N2_C2);
		addConstantDelay(D_N2_C3);
		//////////////////////////////////////////////////////////////
		////////////////////////// Throughput ////////////////////////
		//////////////////////////////////////////////////////////////

		// D_N1_C1
		T_N1_C1.addDataPlan(COUNT, 50, THROUGHPUT_VARIANCE, ROUND_THROUGHPUT_TO);
		T_N1_C1.addDataPlan(COUNT, 50, THROUGHPUT_VARIANCE, ROUND_THROUGHPUT_TO);
		T_N1_C1.addDataPlan(COUNT, 50, THROUGHPUT_VARIANCE, ROUND_THROUGHPUT_TO);
		T_N1_C1.addDataPlan(COUNT, 50, THROUGHPUT_VARIANCE, ROUND_THROUGHPUT_TO);
		T_N1_C1.addDataPlan(COUNT, 50, THROUGHPUT_VARIANCE, ROUND_THROUGHPUT_TO);
		T_N1_C1.addDataPlan(COUNT, 20, THROUGHPUT_VARIANCE, ROUND_THROUGHPUT_TO);
		T_N1_C1.addDataPlan(COUNT, 20, THROUGHPUT_VARIANCE, ROUND_THROUGHPUT_TO);
		T_N1_C1.addDataPlan(COUNT, 20, THROUGHPUT_VARIANCE, ROUND_THROUGHPUT_TO);
		T_N1_C1.addDataPlan(COUNT, 20, THROUGHPUT_VARIANCE, ROUND_THROUGHPUT_TO);
		T_N1_C1.addDataPlan(COUNT, 20, THROUGHPUT_VARIANCE, ROUND_THROUGHPUT_TO);
		// T_N1_C2
		T_N1_C2.addDataPlan(COUNT, 15, THROUGHPUT_VARIANCE, ROUND_THROUGHPUT_TO);
		T_N1_C2.addDataPlan(COUNT, 15, THROUGHPUT_VARIANCE, ROUND_THROUGHPUT_TO);
		T_N1_C2.addDataPlan(COUNT, 15, THROUGHPUT_VARIANCE, ROUND_THROUGHPUT_TO);
		T_N1_C2.addDataPlan(COUNT, 15, THROUGHPUT_VARIANCE, ROUND_THROUGHPUT_TO);
		T_N1_C2.addDataPlan(COUNT, 15, THROUGHPUT_VARIANCE, ROUND_THROUGHPUT_TO);
		T_N1_C2.addDataPlan(COUNT, 15, THROUGHPUT_VARIANCE, ROUND_THROUGHPUT_TO);
		T_N1_C2.addDataPlan(COUNT, 60, THROUGHPUT_VARIANCE, ROUND_THROUGHPUT_TO);
		T_N1_C2.addDataPlan(COUNT, 60, THROUGHPUT_VARIANCE, ROUND_THROUGHPUT_TO);
		T_N1_C2.addDataPlan(COUNT, 60, THROUGHPUT_VARIANCE, ROUND_THROUGHPUT_TO);
		T_N1_C2.addDataPlan(COUNT, 60, THROUGHPUT_VARIANCE, ROUND_THROUGHPUT_TO);
		T_N1_C2.addDataPlan(COUNT, 60, THROUGHPUT_VARIANCE, ROUND_THROUGHPUT_TO);
		
		//addConstantThroughput(T_N1_C1);
		//addConstantThroughput(T_N1_C2);
		addConstantThroughput(T_N1_C3);
		
		addConstantThroughput(T_N2_C1);
		addConstantThroughput(T_N2_C2);
		addConstantThroughput(T_N2_C3);
	}
	private void setupUpdater2(){
		// Count , Mean , Variance , RoundTO
		// D_N1_C1
		D_N1_C1.addDataPlan(COUNT, 70 , DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 70 , DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 70 , DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 70 , DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 70 , DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 300, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 300, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 300, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 300, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 300, DELAY_VARIANCE, ROUND_DELAY_TO);
		// D_N1_C2
		D_N1_C2.addDataPlan(COUNT, 300, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 300, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 300, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 300, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 300, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 70 , DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 70 , DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 70 , DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 70 , DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 70 , DELAY_VARIANCE, ROUND_DELAY_TO);
		
		// D_N1_C3
		addConstantDelay(D_N1_C3);
		
		addConstantDelay(D_N2_C1);
		addConstantDelay(D_N2_C2);
		addConstantDelay(D_N2_C3);
		//////////////////////////////////////////////////////////////
		////////////////////////// Throughput ////////////////////////
		//////////////////////////////////////////////////////////////
		
		addConstantThroughput(T_N1_C1);
		addConstantThroughput(T_N1_C2);
		addConstantThroughput(T_N1_C3);
		
		addConstantThroughput(T_N2_C1);
		addConstantThroughput(T_N2_C2);
		addConstantThroughput(T_N2_C3);
	}
	
	private void setupUpdater1(){
		// Count , Mean , Variance , RoundTO
		// D_N1_C1
		D_N1_C1.addDataPlan(COUNT, 500, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 500, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 400, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 400, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 500, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 100, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 500, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 150, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 250, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 500, DELAY_VARIANCE, ROUND_DELAY_TO);
		// D_N1_C2
		D_N1_C2.addDataPlan(COUNT, 150, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 200, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 500, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 180, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 500, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 500, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 500, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 160, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 500, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 500, DELAY_VARIANCE, ROUND_DELAY_TO);
		
		// D_N1_C3
		addConstantDelay(D_N1_C3);
		
		addConstantDelay(D_N2_C1);
		addConstantDelay(D_N2_C2);
		addConstantDelay(D_N2_C3);
		//////////////////////////////////////////////////////////////
		////////////////////////// Throughput ////////////////////////
		//////////////////////////////////////////////////////////////
		
		addConstantThroughput(T_N1_C1);
		addConstantThroughput(T_N1_C2);
		addConstantThroughput(T_N1_C3);
		
		addConstantThroughput(T_N2_C1);
		addConstantThroughput(T_N2_C2);
		addConstantThroughput(T_N2_C3);
	}
	
	private void setupConstatntUpdater(){
		// Count , Mean , Variance , RoundTO
		// D_N1_C1
		D_N1_C1.addDataPlan(COUNT, 200, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 200, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 200, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 200, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 200, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 200, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 200, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 200, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 200, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 200, DELAY_VARIANCE, ROUND_DELAY_TO);
		// D_N1_C2
		D_N1_C2.addDataPlan(COUNT, 500, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 500, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 500, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 500, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 500, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 500, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 500, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 500, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 500, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 500, DELAY_VARIANCE, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 500, DELAY_VARIANCE, ROUND_DELAY_TO);
		
		// D_N1_C3
		addConstantDelay(D_N1_C3);
		
		addConstantDelay(D_N2_C1);
		addConstantDelay(D_N2_C2);
		addConstantDelay(D_N2_C3);
		//////////////////////////////////////////////////////////////
		////////////////////////// Throughput ////////////////////////
		//////////////////////////////////////////////////////////////
		
		addConstantThroughput(T_N1_C1);
		addConstantThroughput(T_N1_C2);
		addConstantThroughput(T_N1_C3);
		
		addConstantThroughput(T_N2_C1);
		addConstantThroughput(T_N2_C2);
		addConstantThroughput(T_N2_C3);
	}
	// Write the data plan to a file
	private void writeToFile(String path){
		File f = (new File(path)).getParentFile();
		if(f != null){
			f.mkdirs();
		}
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(path));
		} catch (IOException e) {e.printStackTrace();}
		
		JSONObject mJSON = null ;
		do{
			mJSON = getDataSample();
			try {
				out.write(mJSON.toString());
				out.write("\n");
			} catch (IOException e) {e.printStackTrace();}
			
		} while(mJSON.getInt(CloudWorld.D_N1_C1) != -1);
		
		try {
			out.close();
		} catch (IOException e) {e.printStackTrace();}
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
				mTable.put(mJSON.getInt(SAMPLE_COUNT), mJSON);
				line = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public Hashtable<Integer, JSONObject> getDataTable() {
		return mTable;
	}
	public JSONObject getDataSample(int datasample){
		return mTable.get(datasample);
	}
	private void addConstantDelay(DataSetGenerator mG){
		mG.addDataPlan(COUNT_ALL, CONSTANT_DELAY, 0, ROUND_DELAY_TO);
	}
	private void addConstantThroughput(DataSetGenerator mG){
		mG.addDataPlan(COUNT_ALL, CONSTANT_THROUGHPUT, 0, ROUND_THROUGHPUT_TO);
	}
	
	// ugly fix
	private int sampleCount = 0;
	private JSONObject getDataSample(){
		mJSON = new JSONObject();
		mJSON.put(SAMPLE_COUNT, sampleCount);

		mJSON.put(CloudWorld.D_N1_C1, D_N1_C1.getSample());
		mJSON.put(CloudWorld.D_N1_C2, D_N1_C2.getSample());
		mJSON.put(CloudWorld.D_N1_C3, D_N1_C3.getSample());
		
		mJSON.put(CloudWorld.D_N2_C1, D_N2_C1.getSample());
		mJSON.put(CloudWorld.D_N2_C2, D_N2_C2.getSample());
		mJSON.put(CloudWorld.D_N2_C3, D_N2_C3.getSample());
		
		
		mJSON.put(CloudWorld.T_N1_C1, T_N1_C1.getSample());
		mJSON.put(CloudWorld.T_N1_C2, T_N1_C2.getSample());
		mJSON.put(CloudWorld.T_N1_C3, T_N1_C3.getSample());
		
		mJSON.put(CloudWorld.T_N2_C1, T_N2_C1.getSample());
		mJSON.put(CloudWorld.T_N2_C2, T_N2_C2.getSample());
		mJSON.put(CloudWorld.T_N2_C3, T_N2_C3.getSample());
		
		sampleCount++;
		return mJSON;
	}
	public static void main(String[] args){
		
		StateUpdater mUpdater = new StateUpdater();
		mUpdater.writeToFile(FILE_PATH);
	}
}