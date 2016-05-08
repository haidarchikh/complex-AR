package se.ltu.thesis.haidar.agent;

import java.awt.*;
import java.util.List;

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
import burlap.oomdp.singleagent.common.VisualActionObserver;
import burlap.oomdp.singleagent.environment.Environment;
import burlap.oomdp.singleagent.environment.EnvironmentServer;
import burlap.oomdp.statehashing.HashableStateFactory;
import burlap.oomdp.statehashing.SimpleHashableStateFactory;
import burlap.oomdp.visualizer.Visualizer;


public class Main {

	public static final int MIN_D = 20;
	public static final int MAX_D = 70;
	
	public static final int MIN_T = 40;
	public static final int MAX_T = 100;
	
	public static final double REWARD_WEIGHT = 0.85;
	
	public static final double 	DISSCOUNT_FACTOR 	= 0.1;
	public static final double 	INITIAL_Q_VALUE		= 0.0;
	public static final double 	LEARNING_RATE 		= 0.9;
	
	public static final int		STEPS_IN_EPISODE 	= 1000;
	public static final double 	EPSILON 			= 0.9;
	
	public static final double 	DELTA_TERMINATION 	= 4.0;
	public static final int		NUM_PLAN_EPISODE 	= 10000;
	
	private CloudWorld 			 mCloudWorld;
	private Domain 		 		 domain;
	private RewardFunction 		 rf;
	private TerminalFunction 	 tf;
	private State 				 initialState;
	private HashableStateFactory hashingFactory;
	private Environment 		 env;
	//private StateConditionTest goalCondition;
	
	
	public Main(){
		mCloudWorld = new CloudWorld();
		domain = mCloudWorld.generateDomain();
		rf = new CloudWorld.Reward(MIN_D , MAX_D , MIN_T , MAX_T, REWARD_WEIGHT);
		tf = new NullTermination();
		//tf = new CloudWorld.Terminal();
		initialState = CloudWorld.getInitialState(domain);
		
		hashingFactory = new SimpleHashableStateFactory();
		//I'm using my environment
		env = new MyEnve(domain, rf, tf, initialState);
		
		
		
	}
	
	public void valueIteration(String outputPath){
		
		Planner planner = new ValueIteration(domain, rf, tf, 0.99, hashingFactory, 0.1, 10);
		Policy p = planner.planFromState(initialState);
		p.evaluateBehavior(initialState, rf, tf).writeToFile(outputPath + "vi");
		
	}
	
	public void QLearning(String outputPath){
		
		LearningAgent agent = new MyQLearning(domain, DISSCOUNT_FACTOR, hashingFactory, INITIAL_Q_VALUE, LEARNING_RATE);
		
		((MyQLearning) agent).setLearningPolicy(new EpsilonGreedy((QFunction) agent, EPSILON));
		
		//run learning for 50 episodes
		for(int i = 0; i < 50; i++){
			EpisodeAnalysis ea = agent.runLearningEpisode(env , STEPS_IN_EPISODE);
			ea.writeToFile(outputPath + "ql_" + i);
			
			System.out.println(i + ": " + ea.maxTimeStep());
			env.resetEnvironment();
			
			// To get a greedy policy for each episode
			
			//GreedyQPolicy mPolicy = new GreedyQPolicy((QFunction) agent);
			//env.resetEnvironment();
			//EpisodeAnalysis eaP = mPolicy.evaluateBehavior(env,900);
			//env.resetEnvironment();
			//eaP.writeToFile(outputPath + "ql_p" + i);
		}
	}
	public void QPlaning(String outputPath){
		LearningAgent agent = new MyQLearning(domain, DISSCOUNT_FACTOR, hashingFactory, INITIAL_Q_VALUE, LEARNING_RATE);
		((MyQLearning) agent).setLearningPolicy(new EpsilonGreedy((QFunction) agent, EPSILON));
		
		
		((MyQLearning) agent).setMaxPlaningEpisodeSize(STEPS_IN_EPISODE);
		((MyQLearning) agent).setMaxQChangeForPlanningTerminaiton(DELTA_TERMINATION);
		((MyQLearning) agent).initializeForPlanning(rf, tf, NUM_PLAN_EPISODE);
		
		GreedyQPolicy mPolicy = ((MyQLearning)agent).planFromState(initialState);
		//AbstractGroundedAction mAction = mPolicy1.getAction(CloudWorld.getPolicyTestState(domain));
		//System.out.println(mAction.actionName());

		
		env.resetEnvironment();
		EpisodeAnalysis eaP = mPolicy.evaluateBehavior(env, STEPS_IN_EPISODE);
		//mPolicy1.evaluateBehavior(env, STEPS_IN_EPISODE).writeToFile(outputPath + "ql_p");
		env.resetEnvironment();
		eaP.writeToFile(outputPath + "ql_p");
		//manualValueFunctionVis((ValueFunction) agent, mPolicy1);
	}
	public void SARASLearning(String outputPath){
		
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
				return new MyQLearning(domain, DISSCOUNT_FACTOR , hashingFactory, INITIAL_Q_VALUE, LEARNING_RATE);
			}
		};

		LearningAgentFactory sarsaLearningFactory = new LearningAgentFactory() {
			@Override
			public String getAgentName() {
				return "SARSA";
			}

			@Override
			public LearningAgent generateAgent() {
				return new SarsaLam(domain, DISSCOUNT_FACTOR , hashingFactory, 0.3, 0.5 , 0.9);
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
	public void manualValueFunctionVis(ValueFunction valueFunction, Policy p){

		List<State> allStates = StateReachability.getReachableStates(initialState, 
							(SADomain)domain, hashingFactory);

		//define color function
		LandmarkColorBlendInterpolation rb = new LandmarkColorBlendInterpolation();
		rb.addNextLandMark(0., Color.RED);
		rb.addNextLandMark(1., Color.BLUE);

		//define a 2D painter of state values, specifying which attributes correspond 
		//to the x and y coordinates of the canvas
		StateValuePainter2D svp = new StateValuePainter2D(rb);
		svp.setXYAttByObjectClass(CloudWorld.CLASSAGENT, CloudWorld.CURRENT_NETWORK,
				CloudWorld.CLASSAGENT, CloudWorld.CURRENT_CLOUD);

		//create our ValueFunctionVisualizer that paints for all states
		//using the ValueFunction source and the state value painter we defined
		ValueFunctionVisualizerGUI gui = new ValueFunctionVisualizerGUI(allStates, svp, valueFunction);

		//define a policy painter that uses arrow glyphs for each of the grid world actions
		PolicyGlyphPainter2D spp = new PolicyGlyphPainter2D();
		spp.setXYAttByObjectClass(CloudWorld.CLASSAGENT, CloudWorld.CURRENT_NETWORK,
				CloudWorld.CLASSAGENT, CloudWorld.CURRENT_CLOUD);
		
		spp.setActionNameGlyphPainter(CloudWorld.MIGRATE_TO_N1_C1, new ArrowActionGlyph(0));
		spp.setActionNameGlyphPainter(CloudWorld.MIGRATE_TO_N1_C2, new ArrowActionGlyph(1));
		//spp.setActionNameGlyphPainter(GridWorldDomain.ACTIONEAST, new ArrowActionGlyph(2));
		//spp.setActionNameGlyphPainter(GridWorldDomain.ACTIONWEST, new ArrowActionGlyph(3));
		spp.setRenderStyle(PolicyGlyphPainter2D.PolicyGlyphRenderStyle.DISTSCALED);

		//add our policy renderer to it
		gui.setSpp(spp);
		gui.setPolicy(p);

		//set the background color for places where states are not rendered to grey
		gui.setBgColor(Color.GRAY);

		//start it
		gui.initGUI();
	}
	
	public static void main(String[] args) {
	
		Main ex = new Main();
		
		/*VisualActionObserver observer = new VisualActionObserver(ex.domain, 
				ex.mCloudWorld.getVisualizer());
		observer.initGUI();
		EnvironmentServer nv = new EnvironmentServer(ex.env, observer);
		//((SADomain)domain).addActionObserverForAllAction(observer);
		*/
		
		String outputPathQl 	= "output/ql/";
		String outputPathSARAS 	= "output/saras/";
		String outputPathVI 	= "output/vi/";
		
		
		//ex.QLearning(outputPathQl);
		ex.QPlaning(outputPathQl);
		//ex.SARASLearningExample(outputPathSARAS);
		//ex.experimentAndPlotter();
		//ex.valueIteration(outputPathVI);
		
		Visualizer v = ex.mCloudWorld.getVisualizer();
		//VisualExplorer exp = new VisualExplorer(ex.domain, ex.env, v);
		//exp.initGUI();
		new EpisodeSequenceVisualizer(v, ex.domain, outputPathQl);
	}	
}