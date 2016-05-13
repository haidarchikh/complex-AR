package se.ltu.thesis.haidar.agent;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math3.distribution.ParetoDistribution;

import cern.jet.random.Normal;
import cern.jet.random.engine.DRand;
import cern.jet.random.engine.RandomEngine;
import burlap.behavior.learningrate.LearningRate;
import burlap.oomdp.core.AbstractGroundedAction;
import burlap.oomdp.core.states.State;
import burlap.oomdp.statehashing.HashableState;
import burlap.oomdp.statehashing.HashableStateFactory;
import burlap.oomdp.statehashing.SimpleHashableStateFactory;

public class MyLearningRate implements LearningRate {

	private HashableStateFactory hashingFactory;
	private Map<HashableState, Map<String, Integer>> mStateMap;
	private double mInitialLearningRate;
	
	private RandomEngine	mEngine;
	private Normal			mNormal;
	private ParetoDistribution mPareto;
	private ExploreFunction mExploreFunction;
	public static final double INITIAL_LEARNING_RATE = 1.0;

	public MyLearningRate() {
		hashingFactory = new SimpleHashableStateFactory();
		mStateMap = new HashMap<>();

		this.mInitialLearningRate = INITIAL_LEARNING_RATE;
		
		this.mPareto = new ParetoDistribution(10, 10);
		this.mExploreFunction = new ExploreFunction();
		this.mEngine = new DRand();
		this.mNormal = new Normal(10, 3, mEngine);
	}

	@Override
	public double peekAtLearningRate(State s, AbstractGroundedAction ga) {
		return this.mInitialLearningRate;
	}

	@Override
	public double pollLearningRate(int agentTime, State s,
			AbstractGroundedAction ga) {
		HashableState mState = hashingFactory.hashState(s);
		double mLearningRate = 0;
		mLearningRate = mInitialLearningRate - mNormal.cdf(mExploreFunction.getOccurrence(s, ga));
		return mLearningRate;
	}
	@Override
	public double peekAtLearningRate(int featureId) {
		return this.mInitialLearningRate;
	}

	@Override
	public double pollLearningRate(int agentTime, int featureId) {
		return this.mInitialLearningRate;
	}

	@Override
	public void resetDecay() {
		// TODO Auto-generated method stub

	}

}