package se.ltu.thesis.haidar.agent;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import se.ltu.thesis.haidar.database.SqlStatistics;
import burlap.behavior.policy.EpsilonGreedy;
import burlap.behavior.policy.GreedyQPolicy;
import burlap.behavior.singleagent.EpisodeAnalysis;
import burlap.behavior.singleagent.auxiliary.EpisodeSequenceVisualizer;
import burlap.behavior.valuefunction.QFunction;
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

	public static final int MIN_D = 20;
	public static final int MAX_D = 100;

	public static final int MIN_T = 70;
	public static final int MAX_T = 100;

	public static final int 	NIGATIVE_REWARD = 0;
	public static final double 	REWARD_WEIGHT 	= 0.8;

	public static final double EPSILON 			= 0.4;
	public static final double LEARNING_RATE 	= 0.0;
	public static final double DISSCOUNT_FACTOR = 0.0;
	
	public static final double 	INITIAL_Q_VALUE 	= 0.0;
	public static final double 	DELTA_TERMINATION 	= 0.0;
	public static final int 	STEPS_IN_EPISODE 	= 1000;
	public static final int 	MAX_NUM_OF_EPISODE 	= 100;
	
	

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
		mRF 		= new CloudWorld.Reward(MIN_D, MAX_D, MIN_T, MAX_T, NIGATIVE_REWARD, REWARD_WEIGHT);
		//mRF 		= new UniformCostRF();
		mTF 		= new NullTermination();
		// mTF 		= new CloudWorld.Terminal();
		
		mInitialState 	= CloudWorld.getInitialState(mDomain);
		mHashingFactory = new SimpleHashableStateFactory();
		
		// I'm using my environment
		mEnv = new MyEnve(mDomain, mRF, mTF, mInitialState);

	}
	public void QLearning(String outputPath) {
		
		MyQLearning agent = new MyQLearning(mDomain, DISSCOUNT_FACTOR,
		mHashingFactory, INITIAL_Q_VALUE, LEARNING_RATE);
	 
		agent.setLearningPolicy(new MyEpsilonGreedy((QFunction) agent, EPSILON));
		
		MyLearningRate mLearnig = new MyLearningRate(); 
		//agent.setLearningRateFunction(mLearnig);
		
		int eCount = 0;
		do{
			mEnv.resetEnvironment();
			EpisodeAnalysis ea = agent.runLearningEpisode(mEnv, STEPS_IN_EPISODE);
			//ea.writeToFile(outputPath + "ql_" + eCount);
			eCount++;
			
			System.out.println("Episode :"+ eCount +", max Q Change :" + agent.getMaxQChangeInLastEpisode());
			
		}while(eCount < MAX_NUM_OF_EPISODE && agent.getMaxQChangeInLastEpisode() > DELTA_TERMINATION);
		
		// To get a greedy policy form this leaning run
		mEnv.resetEnvironment();
		GreedyQPolicy mPolicy = new GreedyQPolicy(agent); 
		EpisodeAnalysis eaP = mPolicy.evaluateBehavior(mEnv, STEPS_IN_EPISODE); 
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

		for (double mEpsilon = 0;mEpsilon <= 1;	mEpsilon += mStep) {
			mEpsilon = Math.round(mEpsilon*100.0)/100.0;
			for (double mLearning = 0;mLearning <= 1;	mLearning += mStep) {
				mLearning = Math.round(mLearning*100.0)/100.0;
				for (double mDiscount = 0;mDiscount <= 1;	mDiscount += mStep) {		
					mDiscount = Math.round(mDiscount*100.0)/100.0;
					// setting up the agent
					MyQLearning agent = new MyQLearning(mDomain, mDiscount, 
							mHashingFactory, INITIAL_Q_VALUE,mLearning);
					
					// adding a greedy policy to the agent	
					agent.setLearningPolicy(new EpsilonGreedy(agent, mEpsilon));
					
					
					List<Double> mRewardList = new ArrayList<>();
					int eCount = 0;
					do{
						mEnv.resetEnvironment();
						EpisodeAnalysis ea = agent.runLearningEpisode(mEnv, STEPS_IN_EPISODE);
						
						// get the sum of reward for this episode
						double sum = 0;
						List<Double> reward = ea.rewardSequence;
						for (double r : reward) { sum += r;}
						mRewardList.add(sum);
						
						eCount++;
						//System.out.println("Episode :"+ eCount +", max Q Change :" + agent.getMaxQChangeInLastEpisode());
					}while(eCount < MAX_NUM_OF_EPISODE /*&& agent.getMaxQChangeInLastEpisode() > DELTA_TERMINATION*/);
					
					Double[] mRewardArray = mRewardList.toArray(new Double[0]);
					
					System.out.println("Epsilon : "	+ mEpsilon
							+", Learning rate : "	+ mLearning
							+", Discount factor : "	+ mDiscount
							+", Stops after : "		+ mRewardArray.length);
					
					// push to database
					if(pushToDatabase){
						mS.insertNewTuple(mEpsilon,mLearning,
								mDiscount,mRewardArray);
					}
				}
			}
		}
		// disconnect from the database
		if(pushToDatabase){mS.disconnect();}
	}

	public static void main(String[] args) {

		Main ex = new Main();


		String outputPathQl 	= "output/ql/";
		String outputPathSARAS	= "output/saras/";
		String outputPathVI 	= "output/vi/";
		
		//ex.QLearningTODatabase(false);
		
		ex.QLearning(outputPathQl);
		

		Visualizer v = ex.mCloudWorld.getVisualizer();
		new EpisodeSequenceVisualizer(v, ex.mDomain, outputPathQl);
	}
}