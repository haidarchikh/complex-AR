package se.ltu.thesis.haidar.agent;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONObject;

import burlap.oomdp.auxiliary.StateGenerator;
import burlap.oomdp.auxiliary.common.ConstantStateGenerator;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.TerminalFunction;
import burlap.oomdp.core.objects.ObjectInstance;
import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.GroundedAction;
import burlap.oomdp.singleagent.RewardFunction;
import burlap.oomdp.singleagent.environment.EnvironmentObserver;
import burlap.oomdp.singleagent.environment.EnvironmentOutcome;
import burlap.oomdp.singleagent.environment.EnvironmentServerInterface;
import burlap.oomdp.singleagent.environment.StateSettableEnvironment;
import burlap.oomdp.singleagent.environment.TaskSettableEnvironment;

public class MyEnve implements StateSettableEnvironment, TaskSettableEnvironment, EnvironmentServerInterface{

	public static final int DELAY_MEAN = 100; // ms
	public static final int THROUGHPUT_MEAN = 40; // Mbps
	
	// N1 : WiFI , N2 : 4G
	// C1 : Local, C2 : Cloud1 , C3 : Cloud2 
	private StateUpdater mUpdater;
	
	private boolean allowActionFromTerminalStates = false;
	
	protected Domain domain;

	protected RewardFunction rf;

	protected TerminalFunction tf;

	/**
	 * The state generator used to generate new states when the environment is reset with {@link #resetEnvironment()};
	 */
	protected StateGenerator stateGenerator;

	protected State curState;

	protected double lastReward = 0.;
	
	protected List<EnvironmentObserver> observers = new LinkedList<EnvironmentObserver>();
	
	private Hashtable<Integer, JSONObject> mDataTable;
	
	public MyEnve(Domain domain, RewardFunction rf, TerminalFunction tf, State initialState) {
		this.domain = domain;
		this.rf = rf;
		this.tf = tf;
		this.stateGenerator = new ConstantStateGenerator(initialState);
		this.curState = initialState;
		
		// for the state
		mUpdater = new StateUpdater();
		mUpdater.loadDataFromFile();
		mDataTable = mUpdater.getDataTable();
	}

	@Override
	public State getCurrentObservation() {
		return this.curState.copy();
	}
	

	@Override
	public EnvironmentOutcome executeAction(GroundedAction ga) {
		GroundedAction simGA = (GroundedAction)ga.copy();
		simGA.action = this.domain.getAction(ga.actionName());
		if(simGA.action == null){
			throw new RuntimeException("Cannot execute action " + ga.toString() + " in this SimulatedEnvironment because the action is to known in this Environment's domain");
		}

		for(EnvironmentObserver observer : this.observers){
			observer.observeEnvironmentActionInitiation(this.getCurrentObservation(), ga);
		}

		State nextState;
		if(this.allowActionFromTerminalStates || !this.isInTerminalState()) {
			nextState = simGA.executeIn(this.curState);
			///////////////////////////////////////////////////////////
			// here I can plug in my external data to the "nextState"//
			///////////////////////////////////////////////////////////
			nextState = getNetxStateValues(nextState);
			this.lastReward = this.rf.reward(this.curState, simGA, nextState);
		}
		else{
			nextState = this.curState;
			this.lastReward = 0.;
		}

		EnvironmentOutcome eo = new EnvironmentOutcome(this.curState.copy(), simGA, nextState.copy(), this.lastReward, this.tf.isTerminal(nextState));

		this.curState = nextState;

		for(EnvironmentObserver observer : this.observers){
			observer.observeEnvironmentInteraction(eo);
		}

		return eo;
	}
	
	@Override
	public void resetEnvironment() {
		this.lastReward = 0.;
		this.curState = stateGenerator.generateState();
		for(EnvironmentObserver observer : this.observers){
			observer.observeEnvironmentReset(this);
		}
		///////////////////////////////
		// here I can rest the input//
		//////////////////////////////
		timeEpoch = 0;
	}
	private int timeEpoch = 0;
	private JSONObject mState;
	private State getNetxStateValues(State nextState){
		State temp = nextState.copy();
		ObjectInstance agent = temp.getFirstObjectOfClass(CloudWorld.CLASSAGENT);
		//System.out.println(mDataTable);
		mState = mDataTable.get(timeEpoch);
		timeEpoch++;
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
		
		// To test without terminal state
		if(mState.getInt(CloudWorld.D_N1_C1)== -1){timeEpoch = 0;}
		return temp;
	}
	@Override
	public void setCurStateTo(State s) {
		if(this.stateGenerator == null){
		   this.stateGenerator = new ConstantStateGenerator(s);
		}
		this.curState = s;
	}
	
	@Override
	public boolean isInTerminalState() {
		return this.tf.isTerminal(this.curState);
	}
	
	@Override
	public double getLastReward() {
		return this.lastReward;
	}

	@Override
	public void addObservers(EnvironmentObserver... observers) {
		for(EnvironmentObserver o : observers){
			this.observers.add(o);
		}
	}
	@Override
	public List<EnvironmentObserver> getObservers() {
		return this.observers;
	}

	@Override
	public void clearAllObservers() {
		this.observers.clear();
		
	}

	@Override
	public void removeObservers(EnvironmentObserver... observers) {
		for(EnvironmentObserver o : observers){
			this.observers.remove(o);
		}
	}
	
	@Override
	public void setTf(TerminalFunction tf) {
		this.tf = tf;
	}

	@Override
	public void setRf(RewardFunction rf) {
		this.rf = rf;
	}

	@Override
	public TerminalFunction getTf() {
		return tf;
	}
	
	@Override
	public RewardFunction getRf() {
		return rf;
	}
	public Domain getDomain() {
		return domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}
}