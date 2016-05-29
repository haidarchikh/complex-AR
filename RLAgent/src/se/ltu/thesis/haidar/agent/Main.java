package se.ltu.thesis.haidar.agent;

import java.util.ArrayList;
import java.util.List;

import se.ltu.thesis.haidar.database.SqlStatistics;
import burlap.behavior.policy.GreedyQPolicy;
import burlap.behavior.singleagent.EpisodeAnalysis;
import burlap.behavior.singleagent.auxiliary.EpisodeSequenceVisualizer;
import burlap.oomdp.auxiliary.common.NullTermination;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.TerminalFunction;
import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.RewardFunction;
import burlap.oomdp.singleagent.environment.Environment;
import burlap.oomdp.statehashing.HashableStateFactory;
import burlap.oomdp.statehashing.SimpleHashableStateFactory;
import burlap.oomdp.visualizer.Visualizer;

public class Main {
	//402.379829999997
	public static final double DATASET_3_ORACLE_REWARD = 402.37983;

	public static final int MIN_D = 20;
	public static final int MAX_D = 100;

	public static final int MIN_T = 70;
	public static final int MAX_T = 100;

	
	public static final int 	NIGATIVE_REWARD 	= 0;
	public static final double 	DELAY_WEIGHT 		= 0.8;
	public static final double 	TH_WEIGHT 			= 0.19;
	public static final double 	DAMPER_WEIGHT 		= 0.01;

	public static final double 	EPSILON 			= 1.0;
	public static final double 	LEARNING_RATE 		= 1.0;
	public static final double 	DISSCOUNT_FACTOR 	= 0.1;
	
	public static final double 	INITIAL_Q_VALUE 	= 0.0;
	public static final double 	DELTA_TERMINATION 	= 0.01;
	public static final int 	STEPS_IN_EPISODE 	= 1000;
	public static final int 	MAX_NUM_OF_EPISODES	= 1000;
	
	

	private CloudWorld 				mCloudWorld;
	private Domain 					mDomain;
	private RewardFunction 			mRF;
	private TerminalFunction 		mTF;
	private State 					mInitialState;
	private HashableStateFactory 	mHashingFactory;
	private Environment 			mEnv;

	public Main() {
		mCloudWorld = new CloudWorld();
		mDomain 	= mCloudWorld.generateDomain();
		mRF 		= new CloudWorld.Reward(MIN_D, MAX_D, MIN_T, MAX_T, NIGATIVE_REWARD,
				DELAY_WEIGHT, TH_WEIGHT, DAMPER_WEIGHT);
		//mRF 		= new UniformCostRF();
		mTF 		= new NullTermination();
		// mTF 		= new CloudWorld.Terminal();
		
		mInitialState 	= CloudWorld.getInitialState(mDomain);
		mHashingFactory = new SimpleHashableStateFactory();
		
		// I'm using my environment
		mEnv = new MyEnve(mDomain, mRF, mTF, mInitialState);

	}
	public void QLearningMine(String outputPath) {
		
		MyQLearning agent = new MyQLearning(mDomain, DISSCOUNT_FACTOR,
		mHashingFactory, INITIAL_Q_VALUE, LEARNING_RATE);
	 
		agent.setLearningPolicy(new MyEpsilonGreedy(agent, EPSILON, false));
		
		MyLearningRate mLearnig = new MyLearningRate(); 
		agent.setLearningRateFunction(mLearnig);
		
		int eCount = 0;
		do{
			eCount++;
			mEnv.resetEnvironment();
			EpisodeAnalysis ea = agent.runLearningEpisode(mEnv, STEPS_IN_EPISODE);
			
			//ea.writeToFile(outputPath + "ql_" + eCount);
			
			mEnv.resetEnvironment();
			GreedyQPolicy mPolicy = new GreedyQPolicy(agent); 
			EpisodeAnalysis eaP = mPolicy.evaluateBehavior(mEnv, STEPS_IN_EPISODE);
			
			double rewardSum = getSum(eaP.rewardSequence);
			
			System.out.println("Episode :"+ eCount +", max Q Change :" + agent.getMaxQChangeInLastEpisode()+
					", greedy reward :" + rewardSum);
			
			if (rewardSum > 402.37){break;}
			
		}while(eCount < MAX_NUM_OF_EPISODES /*&& agent.getMaxQChangeInLastEpisode() > DELTA_TERMINATION*/);
		
		
		// To get a greedy policy form this leaning run
		mEnv.resetEnvironment();
		GreedyQPolicy mPolicy = new GreedyQPolicy(agent); 
		EpisodeAnalysis eaP = mPolicy.evaluateBehavior(mEnv, STEPS_IN_EPISODE);
		eaP.writeToFile(outputPath + "ql_greedyPolicy");
	}
	
	public void QLearning(String outputPath) {
		
		MyQLearning agent = new MyQLearning(mDomain, DISSCOUNT_FACTOR,
		mHashingFactory, INITIAL_Q_VALUE, LEARNING_RATE);
	 
		//agent.setLearningPolicy(new MyEpsilonGreedy(agent, EPSILON, true));
		agent.setLearningPolicy(new MyOracleEpsilonGreedy(agent));
		//MyLearningRate mLearnig = new MyLearningRate(); 
		//agent.setLearningRateFunction(mLearnig);
		
		int eCount = 0;
		do{
			mEnv.resetEnvironment();
			EpisodeAnalysis ea = agent.runLearningEpisode(mEnv, STEPS_IN_EPISODE);
			//ea.writeToFile(outputPath + "ql_" + eCount);
			eCount++;
			
			System.out.println("Episode :"+ eCount +", max Q Change :" + agent.getMaxQChangeInLastEpisode()+
					"reward :" + getSum(ea.rewardSequence));
			
		}while(eCount < MAX_NUM_OF_EPISODES && agent.getMaxQChangeInLastEpisode() > DELTA_TERMINATION);
		
		// To get a greedy policy form this leaning run
		mEnv.resetEnvironment();
		GreedyQPolicy mPolicy = new GreedyQPolicy(agent); 
		EpisodeAnalysis eaP = mPolicy.evaluateBehavior(mEnv, STEPS_IN_EPISODE);
		System.out.println(getSum(eaP.rewardSequence));
		mEnv.resetEnvironment();
		eaP.writeToFile(outputPath + "ql_greedyPolicy");
	}
	
	public void QLearningTODatabase(boolean pushToDatabase) {
		
		
		double mStep = 0.1;

		SqlStatistics mS = null;
		
		if(pushToDatabase){
			// connect to the database and set the new test
			mS = new SqlStatistics();
			mS.connect();
			mS.mNewTest(mStep);
		}

		List<Double> mRewardList = null;
		List<Double> mGreedyRewardList = null;
		for (double mEpsilon = 0;mEpsilon <= 1;mEpsilon += mStep) {
			for (double mLearning = 0;mLearning <= 1;mLearning += mStep) {
				for (double mDiscount = 0;mDiscount <= 1;mDiscount += mStep) {		
					
					mEpsilon 	= Math.round(mEpsilon *100.0)/100.0;
					mLearning 	= Math.round(mLearning*100.0)/100.0;
					mDiscount 	= Math.round(mDiscount*100.0)/100.0;
					
					// setting up the agent
					MyQLearning agent = 
							new MyQLearning(mDomain, mDiscount,mHashingFactory, INITIAL_Q_VALUE,mLearning);
					
					// adding a greedy policy to the agent	
					agent.setLearningPolicy(new MyEpsilonGreedy(agent, mEpsilon,true));
					
					
					mRewardList 			= new ArrayList<>();
					mGreedyRewardList 		= new ArrayList<>();
					
					int eCount = 0;
					do{
						// learn
						mEnv.resetEnvironment();
						EpisodeAnalysis ea = agent.runLearningEpisode(mEnv, STEPS_IN_EPISODE);
						mRewardList.add(getSum(ea.rewardSequence));
						
						// use the learning 
						mEnv.resetEnvironment();
						GreedyQPolicy mPolicy 	 = new GreedyQPolicy(agent); 
						EpisodeAnalysis eaP 	 = mPolicy.evaluateBehavior(mEnv, STEPS_IN_EPISODE);
						mGreedyRewardList.add(getSum(eaP.rewardSequence));
						
						eCount++;
					}while(eCount < MAX_NUM_OF_EPISODES);
					
					Double[] mRewardArray 		= mRewardList		.toArray(new Double[0]);
					Double[] mGreedyRewardArray = mGreedyRewardList	.toArray(new Double[0]);
					
					System.out.println("Epsilon : "	+ mEpsilon
							+", Learning rate : "	+ mLearning
							+", Discount factor : "	+ mDiscount
							+", Stops after : "		+ mRewardArray.length);
					
					// push to database
					if(pushToDatabase){mS.insertNewTuple(mEpsilon,mLearning,mDiscount,mRewardArray,mGreedyRewardArray);}
				}
			}
		}
		// disconnect from the database
		if(pushToDatabase){mS.disconnect();}
	}
	public static void main(String[] args) {

		Main ex = new Main();


		String outputPathQl 	= "output/ql/";
		
		//ex.QLearningTODatabase(false);
		
		//ex.QLearning(outputPathQl);
		
		ex.QLearningMine(outputPathQl);

		Visualizer v = ex.mCloudWorld.getVisualizer();
		new EpisodeSequenceVisualizer(v, ex.mDomain, outputPathQl);
	}
	
	
	private double getSum(List<Double> mArray){
		double sum = 0;
		for (double r : mArray) { sum += r;}
		return sum;
	}

}