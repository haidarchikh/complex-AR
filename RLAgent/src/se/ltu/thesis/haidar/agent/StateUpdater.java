package se.ltu.thesis.haidar.agent;

public class StateUpdater {
	
	
	public static final double COUNT_ALL 			= 100;
	public static final double COUNT 				= 10;
	
	public static final double CONSTANT_DELAY 		= 10000;
	public static final double CONSTANT_THROUGHPUT 	= 1;
	public static final int ROUND_DELAY_TO 			= 10;
	public static final int ROUND_THROUGHPUT_TO 	= 1;

	
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
	
	public StateUpdater(boolean print){
		D_N1_C1 = new DataSetGenerator(print);
		D_N1_C2 = new DataSetGenerator(print);
		D_N1_C3 = new DataSetGenerator(print);
		
		D_N2_C1 = new DataSetGenerator(print);
		D_N2_C2 = new DataSetGenerator(print);
		D_N2_C3 = new DataSetGenerator(print);
		
		T_N1_C1 = new DataSetGenerator(print);
		T_N1_C2 = new DataSetGenerator(print);
		T_N1_C3 = new DataSetGenerator(print);
		
		T_N2_C1 = new DataSetGenerator(print);
		T_N2_C2 = new DataSetGenerator(print);
		T_N2_C3 = new DataSetGenerator(print);
		
		setupUpdater();
		
	}
	private void setupUpdater(){
		// Count , Mean , Variance , RoundTO
		// D_N1_C1
		D_N1_C1.addDataPlan(COUNT, 100, 10, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 100, 10, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 100, 10, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 100, 10, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 100, 10, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 100, 10, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 100, 10, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 100, 10, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 100, 10, ROUND_DELAY_TO);
		D_N1_C1.addDataPlan(COUNT, 100, 10, ROUND_DELAY_TO);
		// D_N1_C2
		D_N1_C2.addDataPlan(COUNT, 500, 10, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 500, 10, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 500, 10, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 500, 10, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 500, 10, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 500, 10, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 500, 10, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 500, 10, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 500, 10, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 500, 10, ROUND_DELAY_TO);
		D_N1_C2.addDataPlan(COUNT, 500, 10, ROUND_DELAY_TO);
		
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
	private void addConstantDelay(DataSetGenerator mG){
		mG.addDataPlan(COUNT_ALL, CONSTANT_DELAY, 0, ROUND_DELAY_TO);
	}
	private void addConstantThroughput(DataSetGenerator mG){
		mG.addDataPlan(COUNT_ALL, CONSTANT_THROUGHPUT, 0, ROUND_THROUGHPUT_TO);
	}

	public DataSetGenerator getD_N1_C1() {
		return D_N1_C1;
	}

	public DataSetGenerator getD_N1_C2() {
		return D_N1_C2;
	}

	public DataSetGenerator getD_N1_C3() {
		return D_N1_C3;
	}

	public DataSetGenerator getD_N2_C1() {
		return D_N2_C1;
	}

	public DataSetGenerator getD_N2_C2() {
		return D_N2_C2;
	}

	public DataSetGenerator getD_N2_C3() {
		return D_N2_C3;
	}

	public DataSetGenerator getT_N1_C1() {
		return T_N1_C1;
	}

	public DataSetGenerator getT_N1_C2() {
		return T_N1_C2;
	}

	public DataSetGenerator getT_N1_C3() {
		return T_N1_C3;
	}

	public DataSetGenerator getT_N2_C1() {
		return T_N2_C1;
	}

	public DataSetGenerator getT_N2_C2() {
		return T_N2_C2;
	}

	public DataSetGenerator getT_N2_C3() {
		return T_N2_C3;
	}	
}