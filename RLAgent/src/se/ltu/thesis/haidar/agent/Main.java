package se.ltu.thesis.haidar.agent;

import burlap.domain.singleagent.gridworld.GridWorldDomain;
import burlap.oomdp.auxiliary.stateconditiontest.StateConditionTest;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.TerminalFunction;
import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.RewardFunction;
import burlap.oomdp.singleagent.environment.Environment;
import burlap.oomdp.statehashing.HashableStateFactory;
import burlap.oomdp.statehashing.SimpleHashableStateFactory;


public class Main {
	private CloudWorld cw;
	private Domain domain;
	private RewardFunction rf;
	private TerminalFunction tf;
	private StateConditionTest goalCondition;
	private State initialState;
	private HashableStateFactory hashingFactory;
	private Environment env;
	
	public Main(){
		cw = new CloudWorld();
		domain = cw.generateDomain();
		rf =  new CloudWorld.Reward(0,0,0,0);
		initialState = CloudWorld.getInitialState(domain);
		hashingFactory = new SimpleHashableStateFactory();
		
		//env = new SimulatedEnvironment(
	}
	public static void main(String[] args){
		
	}
	
	
}
