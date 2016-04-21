package se.ltu.thesis.haidar.agent;

import java.util.List;

import burlap.oomdp.auxiliary.DomainGenerator;
import burlap.oomdp.core.Attribute;
import burlap.oomdp.core.Attribute.AttributeType;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.ObjectClass;
import burlap.oomdp.core.TransitionProbability;
import burlap.oomdp.core.objects.MutableObjectInstance;
import burlap.oomdp.core.objects.ObjectInstance;
import burlap.oomdp.core.states.MutableState;
import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.FullActionModel;
import burlap.oomdp.singleagent.GroundedAction;
import burlap.oomdp.singleagent.RewardFunction;
import burlap.oomdp.singleagent.SADomain;
import burlap.oomdp.singleagent.common.SimpleAction;

// Interface DomainGenerator is a BURLAP convention when we want to implement our own domain
public class CloudWorld implements DomainGenerator{
	
	public static final String C1 = " cloud 1 ";
	public static final String C2 = " cloud 2 ";
	public static final String C3 = " cloud 3 ";
	
	public static final String N1 = " WiFI ";
	public static final String N2 = " 4G ";
	
	public static final String DELAY = " delay ";
	public static final String THROUGHPUT = " throughput ";
	
	public static final String MIGRATE = " migrate ";
	public static final String HANDOFF = " handoff ";
	public static final String TO = " to ";
	
	public static final String CURRENT_NETWORK 	= "current network";
	public static final String CURRENT_CLOUD 	= "current cloud";

	// Cloud 1 - WiFI
	public static final String D_N1_C1	= DELAY + N1 + C1;
	public static final String T_N1_C1	= THROUGHPUT + N1 + C1;	
	// Cloud 2 - WiFI
	public static final String D_N1_C2	= DELAY + N1 + C2;
	public static final String T_N1_C2	= THROUGHPUT + N1 + C2;	
	// Cloud 3 - WiFI
	public static final String D_N1_C3	= DELAY + N1 + C3;
	public static final String T_N1_C3	= THROUGHPUT + N1 + C3;	

	
	// Cloud 1 - 4G
	public static final String D_N2_C1	= DELAY + N2 + C1;
	public static final String T_N2_C1	= THROUGHPUT + N2 + C1;	
	// Cloud 2 - 4G
	public static final String D_N2_C2	= DELAY + N2 + C2;
	public static final String T_N2_C2	= THROUGHPUT + N2 + C2;	
	// Cloud 3 - 4G
	public static final String D_N2_C3	= DELAY + N2 + C3;
	public static final String T_N2_C3	= THROUGHPUT + N2 + C3;


	// Actions
	public static final String HANDOFF_TO_N1 = HANDOFF + TO + N1;
	public static final String HANDOFF_TO_N2 = HANDOFF + TO + N2;
	
	public static final String MIGRATE_TO_C1 = MIGRATE + TO + C1;
	public static final String MIGRATE_TO_C2 = MIGRATE + TO + C2;
	public static final String MIGRATE_TO_C3 = MIGRATE + TO + C3;
	
	// My agent 
	public static final String CLASSAGENT = "agent";
	
	
	@Override
	public Domain generateDomain() {
		
		
		// a Single Agent Domain (SADomain)
		SADomain domain = new SADomain();
		
		ObjectClass mAgentClass = new ObjectClass(domain, CLASSAGENT);
		
		
		// WiFI 
		Attribute mD_N1_C1 = new Attribute(domain, D_N1_C1, AttributeType.INT);
		Attribute mT_N1_C1 = new Attribute(domain, T_N1_C1, AttributeType.INT);
		mD_N1_C1.setLims(0, 1000);
		mT_N1_C1.setLims(0, 1000);
		mAgentClass.addAttribute(mD_N1_C1);
		mAgentClass.addAttribute(mT_N1_C1);
		
		Attribute mD_N1_C2 = new Attribute(domain, D_N1_C2, AttributeType.INT);
		Attribute mT_N1_C2 = new Attribute(domain, T_N1_C2, AttributeType.INT);
		mD_N1_C2.setLims(0, 1000);
		mT_N1_C2.setLims(0, 1000);
		mAgentClass.addAttribute(mD_N1_C2);
		mAgentClass.addAttribute(mT_N1_C2);
		
		Attribute mD_N1_C3 = new Attribute(domain, D_N1_C3, AttributeType.INT);
		Attribute mT_N1_C3 = new Attribute(domain, T_N1_C3, AttributeType.INT);
		mD_N1_C3.setLims(0, 1000);
		mT_N1_C3.setLims(0, 1000);
		mAgentClass.addAttribute(mD_N1_C3);
		mAgentClass.addAttribute(mT_N1_C3);
		
		
		// 4G 
		Attribute mD_N2_C1 = new Attribute(domain, D_N2_C1, AttributeType.INT);
		Attribute mT_N2_C1 = new Attribute(domain, T_N2_C1, AttributeType.INT);
		mD_N2_C1.setLims(0, 1000);
		mT_N2_C1.setLims(0, 1000);
		mAgentClass.addAttribute(mD_N2_C1);
		mAgentClass.addAttribute(mT_N2_C1);
		
		Attribute mD_N2_C2 = new Attribute(domain, D_N2_C2, AttributeType.INT);
		Attribute mT_N2_C2 = new Attribute(domain, T_N2_C2, AttributeType.INT);
		mD_N2_C2.setLims(0, 1000);
		mT_N2_C2.setLims(0, 1000);
		mAgentClass.addAttribute(mD_N2_C2);
		mAgentClass.addAttribute(mT_N2_C2);
		
		Attribute mD_N2_C3 = new Attribute(domain, D_N2_C3, AttributeType.INT);
		Attribute mT_N2_C3 = new Attribute(domain, T_N2_C3, AttributeType.INT);
		mD_N2_C3.setLims(0, 1000);
		mT_N2_C3.setLims(0, 1000);
		mAgentClass.addAttribute(mD_N2_C3);
		mAgentClass.addAttribute(mT_N2_C3);
		
		
		// Current network and cloud
		Attribute mC = new Attribute(domain, CURRENT_CLOUD, AttributeType.STRING);
		Attribute mN = new Attribute(domain, CURRENT_NETWORK, AttributeType.STRING);
		mAgentClass.addAttribute(mC);
		mAgentClass.addAttribute(mN);
		

		new Handoff(HANDOFF_TO_N1, domain, 1);
		new Handoff(HANDOFF_TO_N2, domain, 2);
		
		new Migrate(MIGRATE_TO_C1, domain, 1);
		new Migrate(MIGRATE_TO_C2, domain, 2);
		new Migrate(MIGRATE_TO_C3, domain, 3);
		
		return domain;
	}
	
	public static State getInitialState(Domain domain){
		State s = new MutableState();
		ObjectInstance agent = new MutableObjectInstance(domain.getObjectClass(CLASSAGENT), "agent0");
		
		agent.setValue(D_N2_C1, 0);
		agent.setValue(T_N2_C1, 0);
		
		agent.setValue(D_N2_C2, 0);
		agent.setValue(T_N2_C1, 0);
		
		agent.setValue(D_N2_C3, 0);
		agent.setValue(T_N2_C1, 0);
		
		agent.setValue(D_N1_C1, 0);
		agent.setValue(T_N1_C1, 0);
		
		agent.setValue(D_N1_C2, 0);
		agent.setValue(T_N1_C1, 0);
		
		agent.setValue(D_N1_C3, 0);
		agent.setValue(T_N1_C1, 0);
		
		agent.setValue(CURRENT_CLOUD  , C1);
		agent.setValue(CURRENT_NETWORK, N1);

		s.addObject(agent);
		
		return s;
	}
	
	protected class Handoff extends SimpleAction implements FullActionModel{
		
		//1: WiFI; 2: 4G;
		public Handoff(String actionName, Domain domain, int network){
			super(actionName, domain);
		}
		
		@Override
		public List<TransitionProbability> getTransitions(State s,
				GroundedAction groundedAction) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected State performActionHelper(State s,
				GroundedAction groundedAction) {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	protected class Migrate extends SimpleAction implements FullActionModel{
		
		//1: Cloud 1; 2: Cloud 2; 3: Cloud 3;
		public Migrate(String actionName, Domain domain, int cloud){
			super(actionName, domain);
		}
		
		@Override
		public List<TransitionProbability> getTransitions(State s,
				GroundedAction groundedAction) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected State performActionHelper(State s,
				GroundedAction groundedAction) {
			// TODO Auto-generated method stub
			return null;
		}
	}
	public static class Reward implements RewardFunction{
		// D : Delay
		private int min_D;
		private int max_D;
		
		// T : Throughput
		private int min_T;
		private int max_T;
		
		/**
		 * @param min_D The minimum delay required by an application
		 * @param max_D The maximum delay required by an application
		 * @param min_T The minimum throughput required by an application
		 * @param max_T The maximum throughput required by an application
		 * */
		public Reward(int min_D, int max_D, int min_T, int max_T){
			this.min_D = min_D;
			this.max_D = max_D;
			this.min_T = min_T;
			this.max_T = max_T;
		}
		@Override
		public double reward(State s, GroundedAction a, State sprime) {
			
			ObjectInstance agent = s.getFirstObjectOfClass(CLASSAGENT);
			
			String mCurrentNetwork	= agent.getStringValForAttribute(CURRENT_NETWORK);
			String mCurrentCloud	= agent.getStringValForAttribute(CURRENT_CLOUD);
			
			String mDelay 		= DELAY + mCurrentNetwork + mCurrentCloud;
			String mThroughput 	= THROUGHPUT + mCurrentNetwork + mCurrentCloud;
			
			int delay 		= agent.getIntValForAttribute(mDelay);
			int throughput 	= agent.getIntValForAttribute(mThroughput);
			
			double delayReward 		= calcDelayReward(delay);
			double throughputReward = calcThroughputReward(throughput);
			
			double reward = delayReward + throughputReward;
			//TODO fix the weighting
			return reward;
		}
		// calculate the delay reward 
		private double calcDelayReward(int delay){
			// if the delay is smaller than the minimum
			if(delay <= min_D){
				return 1;
			}
			// if the delay is bigger than the maximum allowed delay
			if(delay >= max_D){
				return 0;
			}
			// if the delay is between minimum and maximum
			if(min_D < delay && delay < max_D){
				return (double)(max_D - delay)/(max_D - min_D);
			}
			return 0;
		}
		
		// calculate the throughput reward
		private double calcThroughputReward(int throughput){
			// if the throughput is smaller than the minimum
			if(throughput <= min_T){
				return 0;
			}
			// if the throughput is bigger than the maximum 
			if(throughput >= max_T){
				return 1;
			}
			// if the delay is between minimum and maximum
			if(min_T < throughput && throughput < max_T){
				return (double)(max_D - throughput)/(max_D - min_D);
			}
			return 0;
		}
	}
}