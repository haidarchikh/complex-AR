package se.ltu.thesis.haidar.agent;

import cern.jet.random.Normal;
import cern.jet.random.engine.DRand;
import cern.jet.random.engine.RandomEngine;
import burlap.behavior.learningrate.LearningRate;
import burlap.oomdp.core.AbstractGroundedAction;
import burlap.oomdp.core.states.State;


public class MyLearningRate implements LearningRate {
	private double mInitialLearningRate;
	
	private RandomEngine		mEngine;
	private Normal				mNormal;
	private StateActionOccurrenceMapper 	mStateActionMapper;
	public static final double 	INITIAL_LEARNING_RATE = 1.0;
	
	public MyLearningRate() {
		

		this.mInitialLearningRate 	= INITIAL_LEARNING_RATE;
		this.mStateActionMapper 	= new StateActionOccurrenceMapper();
		this.mEngine 				= new DRand();
		this.mNormal 				= new Normal(3, 0.5, mEngine);
	}
	
	@Override
	public double peekAtLearningRate(State s, AbstractGroundedAction ga) {
		return this.mInitialLearningRate;
	}

	@Override
	public double pollLearningRate(int agentTime, State s,
			AbstractGroundedAction ga) {
		double mLearningRate = 0;
		mLearningRate = mInitialLearningRate - mNormal.cdf(mStateActionMapper.getOccurrence(s, ga));
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