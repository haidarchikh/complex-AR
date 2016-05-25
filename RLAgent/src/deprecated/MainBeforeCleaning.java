package deprecated;

import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

import se.ltu.thesis.haidar.agent.CloudWorld;
import se.ltu.thesis.haidar.agent.MyEnve;
import se.ltu.thesis.haidar.agent.MyEpsilonGreedy;
import se.ltu.thesis.haidar.agent.MyLearningRate;
import se.ltu.thesis.haidar.agent.MyQLearning;
import se.ltu.thesis.haidar.agent.CloudWorld.Reward;
import se.ltu.thesis.haidar.database.SqlStatistics;
import burlap.behavior.learningrate.LearningRate;
import burlap.behavior.policy.EpsilonGreedy;
import burlap.behavior.policy.GreedyQPolicy;
import burlap.behavior.policy.Policy;
import burlap.behavior.singleagent.EpisodeAnalysis;
import burlap.behavior.singleagent.auxiliary.EpisodeSequenceVisualizer;
import burlap.behavior.singleagent.auxiliary.StateReachability;
import burlap.behavior.singleagent.auxiliary.performance.LearningAlgorithmExperimenter;
import burlap.behavior.singleagent.auxiliary.performance.PerformanceMetric;
import burlap.behavior.singleagent.auxiliary.performance.TrialMode;
import burlap.behavior.singleagent.auxiliary.valuefunctionvis.ValueFunctionVisualizerGUI;
import burlap.behavior.singleagent.auxiliary.valuefunctionvis.common.ArrowActionGlyph;
import burlap.behavior.singleagent.auxiliary.valuefunctionvis.common.LandmarkColorBlendInterpolation;
import burlap.behavior.singleagent.auxiliary.valuefunctionvis.common.PolicyGlyphPainter2D;
import burlap.behavior.singleagent.auxiliary.valuefunctionvis.common.StateValuePainter2D;
import burlap.behavior.singleagent.learning.LearningAgent;
import burlap.behavior.singleagent.learning.LearningAgentFactory;
import burlap.behavior.singleagent.learning.tdmethods.SarsaLam;
import burlap.behavior.singleagent.planning.Planner;
import burlap.behavior.singleagent.planning.stochastic.valueiteration.ValueIteration;
import burlap.behavior.valuefunction.QFunction;
import burlap.behavior.valuefunction.ValueFunction;
import burlap.oomdp.auxiliary.common.NullTermination;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.TerminalFunction;
import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.RewardFunction;
import burlap.oomdp.singleagent.SADomain;
import burlap.oomdp.singleagent.environment.Environment;
import burlap.oomdp.statehashing.HashableStateFactory;
import burlap.oomdp.statehashing.SimpleHashableStateFactory;
import burlap.oomdp.visualizer.Visualizer;

public class MainBeforeCleaning {

	public static final int MIN_D = 20;
	public static final int MAX_D = 90;

	public static final int MIN_T = 10000;
	public static final int MAX_T = 10000;

	public static final int NIGATIVE_REWARD = 0;

	public static final double REWARD_WEIGHT = 0.9;

	public static final double DISSCOUNT_FACTOR = 0.1;
	public static final double INITIAL_Q_VALUE = 0.0;
	public static final double LEARNING_RATE = 0.9;

	public static final int STEPS_IN_EPISODE = 1000;
	public static final double EPSILON = 0.6;

	public static final double DELTA_TERMINATION = 0.001;
	public static final int NUM_PLAN_EPISODE = 10000;

	private CloudWorld mCloudWorld;
	private Domain domain;
	private RewardFunction rf;
	private TerminalFunction tf;
	private State initialState;
	private HashableStateFactory hashingFactory;
	private Environment env;

	// private StateConditionTest goalCondition;

	public MainBeforeCleaning() {
		mCloudWorld = new CloudWorld();
		domain = mCloudWorld.generateDomain();
		rf = new CloudWorld.Reward(MIN_D, MAX_D, MIN_T, MAX_T, NIGATIVE_REWARD,
				REWARD_WEIGHT);
		tf = new NullTermination();
		// tf = new CloudWorld.Terminal();
		initialState = CloudWorld.getInitialState(domain);

		hashingFactory = new SimpleHashableStateFactory();
		// I'm using my environment
		env = new MyEnve(domain, rf, tf, initialState);

	}
	public void QLearning(String outputPath) {
		
		LearningAgent agent = new MyQLearning(domain, DISSCOUNT_FACTOR,
		hashingFactory, INITIAL_Q_VALUE, LEARNING_RATE);
	 
		((MyQLearning) agent).setLearningPolicy(new EpsilonGreedy((QFunction) agent, EPSILON));
		
		LearningRate mLearnig = new MyLearningRate(); 
		//((MyQLearning) agent).setLearningRateFunction(mLearnig);
			  
		//run learning for 50 episodes 
		for(int i = 0; i < 100; i++){
		EpisodeAnalysis ea = agent.runLearningEpisode(env , STEPS_IN_EPISODE);
		
		List<Double> reward = ea.rewardSequence; 
		double sum = 0; 
		for (double r: reward){sum += r;} 
		
		System.out.println("Episode : "+i+", cumulative reward :"+ sum);
		
		ea.writeToFile(outputPath + "ql_" + i);
		env.resetEnvironment();
		}
		
		// To get a greedy policy form this leaning run
		GreedyQPolicy mPolicy = new GreedyQPolicy((QFunction) agent); 
		EpisodeAnalysis eaP = mPolicy.evaluateBehavior(env, STEPS_IN_EPISODE); 
		env.resetEnvironment();
		eaP.writeToFile(outputPath + "ql_greedyPolicy");
	}
	
	public void QLearningTODatabase() {
		
		int mMaxEpisodes = 20;
		BigDecimal mStep = new BigDecimal("0.05");

		SqlStatistics mS = new SqlStatistics();
		
		mS.connect();
		mS.mNewTest(mStep.doubleValue());
		
		for (BigDecimal mEpsilon = BigDecimal.ZERO;
				mEpsilon.compareTo(BigDecimal.ONE) <= 0; 
				mEpsilon = mEpsilon.add(mStep)) {
			
			for (BigDecimal mLearningRate 	= BigDecimal.ZERO;
					mLearningRate.compareTo(BigDecimal.ONE) <= 0;
					mLearningRate = mLearningRate.add(mStep)) {
				
				for (BigDecimal mDiscountFactor 	= BigDecimal.ZERO; 
						mDiscountFactor.compareTo(BigDecimal.ONE)<= 0;
						mDiscountFactor = mDiscountFactor.add(mStep)) {
			
					System.out.println("Epsilon : "	+mEpsilon
							+", Learning rate : "+mLearningRate
							+", Discount factor : "	+mDiscountFactor);		
					
					// setting up the agent
					MyQLearning agent = new MyQLearning(domain, mDiscountFactor.doubleValue(), 
							hashingFactory, INITIAL_Q_VALUE,mLearningRate.doubleValue());
					
					// adding a greedy policy to the agent	
					agent.setLearningPolicy(new EpsilonGreedy(agent, mEpsilon.doubleValue()));
					
					Double[] mReward = new Double[mMaxEpisodes];
					
					// running learning episodes
					for (int i = 0; i < mMaxEpisodes; i++) {
						EpisodeAnalysis ea = agent.runLearningEpisode(env,STEPS_IN_EPISODE);
						
						// get the sum of reward for this episode
						double sum = 0;
						List<Double> reward = ea.rewardSequence;
						for (double r : reward) { sum += r;}
						mReward[i]= sum;
						
						// reset the environment for the next episode
						env.resetEnvironment();
					}
					// push to database
					mS.insertNewTuple(mEpsilon.doubleValue(),mLearningRate.doubleValue(),
									mDiscountFactor.doubleValue(),mReward);
				}
			}
		}
		// disconnect from the database
		mS.disconnect();
	}

	public void QPlaning(String outputPath) {

		MyQLearning agent = new MyQLearning(domain, DISSCOUNT_FACTOR,
				hashingFactory, INITIAL_Q_VALUE, LEARNING_RATE);

		LearningRate mLearnig = new MyLearningRate();
		// ((MyQLearning) agent).setLearningRateFunction(mLearnig);

		agent.setLearningPolicy(new MyEpsilonGreedy(agent, EPSILON, false));

		agent.setMaxPlaningEpisodeSize(STEPS_IN_EPISODE);
		agent.setMaxQChangeForPlanningTerminaiton(DELTA_TERMINATION);
		agent.initializeForPlanning(rf, tf, NUM_PLAN_EPISODE);
		
		GreedyQPolicy mPolicy = agent.planFromState(initialState);
		
		// mPolicy.getDeterministicPolicy(initialState);

		env.resetEnvironment();
		EpisodeAnalysis eaP = mPolicy.evaluateBehavior(env, STEPS_IN_EPISODE);

		env.resetEnvironment();
		eaP.writeToFile(outputPath + "ql_p");
		// manualValueFunctionVis((ValueFunction) agent, mPolicy1);
	}

	public void valueIteration(String outputPath) {

		Planner planner = new ValueIteration(domain, rf, tf, 0.99,
				hashingFactory, 0.1, 10);
		Policy p = planner.planFromState(initialState);
		p.evaluateBehavior(initialState, rf, tf).writeToFile(outputPath + "vi");

	}
	public void SARASLearning(String outputPath) {

		/*
		 * LearningAgent agent = new SarsaLam(domain, DISSCOUNT_FACTOR ,
		 * hashingFactory, INITIAL_Q_VALUE, LEARNING_RATE , 1.);
		 * 
		 * //run learning for 50 episodes for(int i = 0; i < 50; i++){
		 * EpisodeAnalysis ea = agent.runLearningEpisode(env);
		 * ea.writeToFile(outputPath + "saras_" + i); System.out.println(i +
		 * ": " + ea.maxTimeStep());
		 * 
		 * //reset environment for next learning episode env.resetEnvironment();
		 * }
		 */
	}

	public void experimentAndPlotter() {

		// different reward function for more interesting results

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
				return new MyQLearning(domain, DISSCOUNT_FACTOR,
						hashingFactory, INITIAL_Q_VALUE, LEARNING_RATE);
			}
		};

		LearningAgentFactory sarsaLearningFactory = new LearningAgentFactory() {
			@Override
			public String getAgentName() {
				return "SARSA";
			}

			@Override
			public LearningAgent generateAgent() {
				return new SarsaLam(domain, DISSCOUNT_FACTOR, hashingFactory,
						0.3, 0.5, 0.9);
			}
		};

		LearningAlgorithmExperimenter exp = new LearningAlgorithmExperimenter(
				env, 10, 100, qLearningFactory, sarsaLearningFactory);
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

	public void manualValueFunctionVis(ValueFunction valueFunction, Policy p) {

		List<State> allStates = StateReachability.getReachableStates(
				initialState, (SADomain) domain, hashingFactory);

		// define color function
		LandmarkColorBlendInterpolation rb = new LandmarkColorBlendInterpolation();
		rb.addNextLandMark(0., Color.RED);
		rb.addNextLandMark(1., Color.BLUE);

		// define a 2D painter of state values, specifying which attributes
		// correspond
		// to the x and y coordinates of the canvas
		StateValuePainter2D svp = new StateValuePainter2D(rb);
		svp.setXYAttByObjectClass(CloudWorld.CLASSAGENT,
				CloudWorld.CURRENT_NETWORK, CloudWorld.CLASSAGENT,
				CloudWorld.CURRENT_CLOUD);

		// create our ValueFunctionVisualizer that paints for all states
		// using the ValueFunction source and the state value painter we defined
		ValueFunctionVisualizerGUI gui = new ValueFunctionVisualizerGUI(
				allStates, svp, valueFunction);

		// define a policy painter that uses arrow glyphs for each of the grid
		// world actions
		PolicyGlyphPainter2D spp = new PolicyGlyphPainter2D();
		spp.setXYAttByObjectClass(CloudWorld.CLASSAGENT,
				CloudWorld.CURRENT_NETWORK, CloudWorld.CLASSAGENT,
				CloudWorld.CURRENT_CLOUD);

		spp.setActionNameGlyphPainter(CloudWorld.MIGRATE_TO_N1_C1,
				new ArrowActionGlyph(0));
		spp.setActionNameGlyphPainter(CloudWorld.MIGRATE_TO_N1_C2,
				new ArrowActionGlyph(1));
		// spp.setActionNameGlyphPainter(GridWorldDomain.ACTIONEAST, new
		// ArrowActionGlyph(2));
		// spp.setActionNameGlyphPainter(GridWorldDomain.ACTIONWEST, new
		// ArrowActionGlyph(3));
		spp.setRenderStyle(PolicyGlyphPainter2D.PolicyGlyphRenderStyle.DISTSCALED);

		// add our policy renderer to it
		gui.setSpp(spp);
		gui.setPolicy(p);

		// set the background color for places where states are not rendered to
		// grey
		gui.setBgColor(Color.GRAY);

		// start it
		gui.initGUI();
	}

	public static void main(String[] args) {

		MainBeforeCleaning ex = new MainBeforeCleaning();

		/*
		 * VisualActionObserver observer = new VisualActionObserver(ex.domain,
		 * ex.mCloudWorld.getVisualizer()); observer.initGUI();
		 * EnvironmentServer nv = new EnvironmentServer(ex.env, observer);
		 * //((SADomain)domain).addActionObserverForAllAction(observer);
		 */

		String outputPathQl = "output/ql/";
		String outputPathSARAS = "output/saras/";
		String outputPathVI = "output/vi/";
		
		//ex.QLearningTODatabase();
		//ex.QLearning(outputPathQl);
		// ex.QPlaning(outputPathQl);
		// ex.SARASLearningExample(outputPathSARAS);
		// ex.experimentAndPlotter();
		// ex.valueIteration(outputPathVI);

		
		Visualizer v = ex.mCloudWorld.getVisualizer();
		new EpisodeSequenceVisualizer(v, ex.domain, outputPathQl);
	}
}