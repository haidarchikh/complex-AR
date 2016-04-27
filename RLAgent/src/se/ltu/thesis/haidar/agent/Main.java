package se.ltu.thesis.haidar.agent;

import burlap.behavior.singleagent.EpisodeAnalysis;
import burlap.behavior.singleagent.auxiliary.performance.LearningAlgorithmExperimenter;
import burlap.behavior.singleagent.auxiliary.performance.PerformanceMetric;
import burlap.behavior.singleagent.auxiliary.performance.TrialMode;
import burlap.behavior.singleagent.learning.LearningAgent;
import burlap.behavior.singleagent.learning.LearningAgentFactory;
import burlap.behavior.singleagent.learning.tdmethods.QLearning;
import burlap.behavior.singleagent.learning.tdmethods.SarsaLam;
import burlap.oomdp.auxiliary.common.NullTermination;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.TerminalFunction;
import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.RewardFunction;
import burlap.oomdp.singleagent.environment.Environment;
import burlap.oomdp.statehashing.HashableStateFactory;
import burlap.oomdp.statehashing.SimpleHashableStateFactory;

public class Main {

	public static final int MIN_D = 150;
	public static final int MAX_D = 250;
	
	public static final int MIN_T = 90;
	public static final int MAX_T = 100;
	
	public static final double DISSCOUNT_FACTOR = 0.95;
	public static final double INITIAL_Q_VALUE 	= 0.0;
	public static final double LEARNING_RATE 	= 1.;
	
	public static final double EPSILON = 0.5;
	public static final int mTermination = 1000;
	
	private CloudWorld 			 mCloudWorld;
	private Domain 				 domain;
	private RewardFunction 		 rf;
	private TerminalFunction 	 tf;
	private State 				 initialState;
	private HashableStateFactory hashingFactory;
	private Environment 		 env;
	//private StateConditionTest goalCondition;
	public Main(){
		mCloudWorld = new CloudWorld();
		domain = mCloudWorld.generateDomain();
		rf = new CloudWorld.Reward(MIN_D , MAX_D , MIN_T , MAX_T);
		//tf = new NullTermination();
		tf = new CloudWorld.Terminal(mTermination);
		initialState = CloudWorld.getInitialState(domain);
		
		hashingFactory = new SimpleHashableStateFactory();
		//I'm using my environment
		env = new MyEnve(domain, rf, tf, initialState);
	}
	
	public void QLearningExample(String outputPath){
		
		LearningAgent agent = new QLearning(domain, DISSCOUNT_FACTOR , hashingFactory, INITIAL_Q_VALUE , LEARNING_RATE);

		//run learning for 50 episodes
		for(int i = 0; i < 50; i++){
			EpisodeAnalysis ea = agent.runLearningEpisode(env);

			ea.writeToFile(outputPath + "ql_" + i);
			System.out.println(i + ": " + ea.maxTimeStep());

			//reset environment for next learning episode
			env.resetEnvironment();
		}
		
	}
	public void SARASLearningExample(String outputPath){
		
		LearningAgent agent = new SarsaLam(domain, DISSCOUNT_FACTOR , hashingFactory, INITIAL_Q_VALUE, LEARNING_RATE , 1.);

		//run learning for 50 episodes
		for(int i = 0; i < 50; i++){
			EpisodeAnalysis ea = agent.runLearningEpisode(env);

			ea.writeToFile(outputPath + "saras_" + i);
			System.out.println(i + ": " + ea.maxTimeStep());

			//reset environment for next learning episode
			env.resetEnvironment();
		}
		
	}
	
	public void experimentAndPlotter(){

		//different reward function for more interesting results

		/**
		 * Create factories for Q-learning agent and SARSA agent to compare
		 */
		LearningAgentFactory qLearningFactory = new LearningAgentFactory() {
			@Override
			public String getAgentName() {
				return "Q-Learning";
			}

			@Override
			public LearningAgent generateAgent() {
				return new QLearning(domain, DISSCOUNT_FACTOR , hashingFactory, INITIAL_Q_VALUE, LEARNING_RATE);
			}
		};

		LearningAgentFactory sarsaLearningFactory = new LearningAgentFactory() {
			@Override
			public String getAgentName() {
				return "SARSA";
			}

			@Override
			public LearningAgent generateAgent() {
				return new SarsaLam(domain, DISSCOUNT_FACTOR , hashingFactory, 0.3, LEARNING_RATE , 1.);
			}
		};

		LearningAlgorithmExperimenter exp = new LearningAlgorithmExperimenter(env, 10, 100, 
												qLearningFactory, sarsaLearningFactory);
		exp.setUpPlottingConfiguration(500, 250, 2, 1000,
				TrialMode.MOSTRECENTANDAVERAGE,
				PerformanceMetric.CUMULATIVEREWARDPERSTEP,
				PerformanceMetric.CUMULTAIVEREWARDPEREPISODE,
				PerformanceMetric.AVERAGEEPISODEREWARD,
				PerformanceMetric.MEDIANEPISODEREWARD,
				PerformanceMetric.CUMULATIVESTEPSPEREPISODE,
				PerformanceMetric.STEPSPEREPISODE);

		exp.startExperiment();
		exp.writeStepAndEpisodeDataToCSV("expData");

	}
	public static void main(String[] args) {
	
		Main example = new Main();
		String outputPathQl = "output/ql/";
		String outputPathSARAS = "output/saras/";
		//example.QLearningExample(outputPathQl);
		//example.SARASLearningExample(outputPathSARAS);
		example.experimentAndPlotter();
			
		//example.visualize(outputPath);
	}	
}