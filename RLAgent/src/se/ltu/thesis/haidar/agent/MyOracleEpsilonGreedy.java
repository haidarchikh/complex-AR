package se.ltu.thesis.haidar.agent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import se.ltu.thesis.haidar.agent.CloudWorld.Migrate;
import se.ltu.thesis.haidar.datagenerator.StateUpdater;
import burlap.behavior.policy.Policy;
import burlap.behavior.policy.SolverDerivedPolicy;
import burlap.behavior.singleagent.MDPSolverInterface;
import burlap.behavior.valuefunction.QFunction;
import burlap.behavior.valuefunction.QValue;
import burlap.oomdp.core.AbstractGroundedAction;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.objects.ObjectInstance;
import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.Action;
import burlap.oomdp.singleagent.GroundedAction;
import burlap.oomdp.singleagent.RewardFunction;

/**
 * This class defines a an epsilon-greedy policy over Q-values and requires a QComputable valueFunction to be specified.
 * With probability epsilon the policy will return a random action (with uniform distribution over all possible action).
 * With probability 1 - epsilon the policy will return the greedy action. If multiple actions tie for the highest Q-value,
 * then one of the tied actions is randomly selected.
 * @author James MacGlashan
 *
 */
public class MyOracleEpsilonGreedy extends Policy implements SolverDerivedPolicy {

	
	private QFunction 				qplanner;
	private RewardFunction	mRF;
	
	private StateUpdater mUpdater;
	private Map<Integer, JSONObject> mData;
	
	private Domain 				mDomain;
	private CloudWorld 			mCloudWorld;
	
	private int timeEpoch;;
	
	public MyOracleEpsilonGreedy(QFunction planner) {
		mRF = new CloudWorld.Reward(Main.MIN_D, Main.MAX_D, Main.MIN_T, Main.MAX_T, Main.NIGATIVE_REWARD,
				Main.DELAY_WEIGHT, Main.TH_WEIGHT, Main.DAMPER_WEIGHT);
		
		
		mUpdater 	= new StateUpdater();
		mData 		= mUpdater.loadDataFromFile(StateUpdater.FILE_PATH_DATA3);
		mCloudWorld = new CloudWorld();
		mDomain = mCloudWorld.generateDomain();
		
		qplanner = planner;
	}

	@Override
	public void setSolver(MDPSolverInterface solver){
		
	}
	@Override
	public AbstractGroundedAction getAction(State s) {
		
		List<QValue> qValues = this.qplanner.getQs(s);
		AbstractGroundedAction action0 = qValues.get(0).a;
		AbstractGroundedAction action1 = qValues.get(1).a;
		AbstractGroundedAction action2 = qValues.get(2).a;
		AbstractGroundedAction action3 = qValues.get(3).a;
		

		// copy the current state
		State sPrime = s.copy();
		
		ObjectInstance agent = sPrime.getFirstObjectOfClass(CloudWorld.CLASSAGENT);
		
		// get the data of the next state
		JSONObject mState = mData.get(timeEpoch);
		timeEpoch++;
		if(timeEpoch == 1000){timeEpoch =0;}
		System.out.println(timeEpoch);
		agent.setValue(CloudWorld.D_N1_C1, mState.get(CloudWorld.D_N1_C1));
		agent.setValue(CloudWorld.D_N1_C2, mState.get(CloudWorld.D_N1_C2));

		agent.setValue(CloudWorld.D_N1_C3, mState.get(CloudWorld.D_N1_C3));
		
		agent.setValue(CloudWorld.D_N2_C1, mState.get(CloudWorld.D_N2_C1));
		agent.setValue(CloudWorld.D_N2_C2, mState.get(CloudWorld.D_N2_C2));
		agent.setValue(CloudWorld.D_N2_C3, mState.get(CloudWorld.D_N2_C3));
		
		
		agent.setValue(CloudWorld.T_N1_C1, mState.get(CloudWorld.T_N1_C1));
		agent.setValue(CloudWorld.T_N1_C2, mState.get(CloudWorld.T_N1_C2));

		agent.setValue(CloudWorld.T_N1_C3, mState.get(CloudWorld.T_N1_C3));
		
		agent.setValue(CloudWorld.T_N2_C1, mState.get(CloudWorld.T_N2_C1));
		agent.setValue(CloudWorld.T_N2_C2, mState.get(CloudWorld.T_N2_C2));
		agent.setValue(CloudWorld.T_N2_C3, mState.get(CloudWorld.T_N2_C3));	
		
		
		List<Double> mRewardList = new ArrayList<Double>();
		
		// action 0
		agent.setValue(CloudWorld.CURRENT_NETWORK	, CloudWorld.N1);
		agent.setValue(CloudWorld.CURRENT_CLOUD	    , CloudWorld.C1);
		mRewardList.add(mRF.reward(s, null, sPrime));
		
		// action 1
		agent.setValue(CloudWorld.CURRENT_NETWORK	, CloudWorld.N1);
		agent.setValue(CloudWorld.CURRENT_CLOUD	    , CloudWorld.C2);
		mRewardList.add(mRF.reward(s, null, sPrime));
		
		// action 2
		agent.setValue(CloudWorld.CURRENT_NETWORK	, CloudWorld.N2);
		agent.setValue(CloudWorld.CURRENT_CLOUD	    , CloudWorld.C1);
		mRewardList.add(mRF.reward(s, null, sPrime));
		
		// action 3
		agent.setValue(CloudWorld.CURRENT_NETWORK	, CloudWorld.N2);
		agent.setValue(CloudWorld.CURRENT_CLOUD	    , CloudWorld.C2);
		mRewardList.add(mRF.reward(s, null, sPrime));
		
		
		Double max  = Collections.max(mRewardList);
		int action = mRewardList.indexOf(max);
		System.out.println(action);
		switch (action){
		case 0 : return action0;
		case 1 : return action1;
		case 2 : return action2;
		case 3 : return action3;
		}
		
		
		return action3;
	}
	
	@Override
	public List<ActionProb> getActionDistributionForState(State s) {
		return null;
	}

	@Override
	public boolean isStochastic() {
		return true;
	}
	
	@Override
	public boolean isDefinedFor(State s) {
		return true; 
	}

}
