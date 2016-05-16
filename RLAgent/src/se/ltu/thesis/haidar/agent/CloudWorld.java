package se.ltu.thesis.haidar.agent;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

import se.ltu.thesis.haidar.datagenerator.StateUpdaterForVI;
import burlap.oomdp.auxiliary.DomainGenerator;
import burlap.oomdp.core.Attribute;
import burlap.oomdp.core.Attribute.AttributeType;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.ObjectClass;
import burlap.oomdp.core.TerminalFunction;
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
import burlap.oomdp.visualizer.ObjectPainter;
import burlap.oomdp.visualizer.StateRenderLayer;
import burlap.oomdp.visualizer.StaticPainter;
import burlap.oomdp.visualizer.Visualizer;

// Interface DomainGenerator is a BURLAP convention when we want to implement our own domain
public class CloudWorld implements DomainGenerator{
	
	public static final String C1 = "_cloud_1";
	public static final String C2 = "_cloud_2";
	public static final String C3 = "_cloud_3";
	
	public static final String N1 = "WiFI";
	public static final String N2 = "4G";
	
	public static final String SPACE = "_";
	
	public static final String DELAY = "delay_";
	public static final String THROUGHPUT = "throughput_";
	
	public static final String MIGRATE = "migrate";
	public static final String TO = "_to_";
	
	public static final String CURRENT_NETWORK 	= "current_network";
	public static final String CURRENT_CLOUD 	= "current_cloud";
	
	public static final String NETWORK 	= "network";
	public static final String CLOUD 	= "cloud";
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

	
	public static final String MIGRATE_TO_N1_C1 = MIGRATE + TO + N1 + C1;
	public static final String MIGRATE_TO_N1_C2 = MIGRATE + TO + N1 + C2;
	public static final String MIGRATE_TO_N1_C3 = MIGRATE + TO + N1 + C3;
	
	public static final String MIGRATE_TO_N2_C1 = MIGRATE + TO + N2 + C1;
	public static final String MIGRATE_TO_N2_C2 = MIGRATE + TO + N2 + C2;
	public static final String MIGRATE_TO_N2_C3 = MIGRATE + TO + N2 + C3;
	
	public static final boolean DEBUG = false;
	// My agent 
	public static final String CLASSAGENT = "agent";
	
	protected int [][] map = new int[][]{
			{0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0},
			{1,0,2,0,3,0,4,0,5,0,6},
	};
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
		
		
		new Migrate(MIGRATE_TO_N1_C1, domain, N1 ,C1);
		new Migrate(MIGRATE_TO_N1_C2, domain, N1 ,C2);
		//new Migrate(MIGRATE_TO_N1_C3, domain, N1 ,C3);
		
		//new Migrate(MIGRATE_TO_N2_C1, domain, N2 ,C1);
		//new Migrate(MIGRATE_TO_N2_C2, domain, N2 ,C2);
		//new Migrate(MIGRATE_TO_N2_C3, domain, N2 ,C3);
		
		return domain;
	}
	
	public static State getInitialState(Domain domain){
		State s = new MutableState();
		ObjectInstance agent = new MutableObjectInstance(domain.getObjectClass(CLASSAGENT), "agent0");

		agent.setValue(D_N1_C1, 11);
		agent.setValue(D_N1_C2, 12);
		agent.setValue(D_N1_C3, 12);

		agent.setValue(D_N2_C1, 21);
		agent.setValue(D_N2_C2, 22);
		agent.setValue(D_N2_C3, 23);
		
		
		agent.setValue(T_N1_C1, 9911);
		agent.setValue(T_N1_C2, 9912);
		agent.setValue(T_N1_C3, 9912);

		agent.setValue(T_N2_C1, 9921);
		agent.setValue(T_N2_C2, 9922);
		agent.setValue(T_N2_C3, 9923);

		agent.setValue(CURRENT_CLOUD  , C1);
		agent.setValue(CURRENT_NETWORK, N1);

		s.addObject(agent);
		
		return s;
	}
	
	public static State getPolicyTestState(Domain domain){
		State s = new MutableState();
		ObjectInstance agent = new MutableObjectInstance(domain.getObjectClass(CLASSAGENT), "agent0");
		
		agent.setValue(D_N1_C1, 220);
		agent.setValue(D_N1_C2, 520);
		agent.setValue(D_N1_C3, 10000);

		agent.setValue(D_N2_C1, 10000);
		agent.setValue(D_N2_C2, 10000);
		agent.setValue(D_N2_C3, 10000);
		
		
		agent.setValue(T_N1_C1, 1);
		agent.setValue(T_N1_C2, 1);
		agent.setValue(T_N1_C3, 1);

		agent.setValue(T_N2_C1, 1);
		agent.setValue(T_N2_C2, 1);
		agent.setValue(T_N2_C3, 1);

		
		agent.setValue(CURRENT_CLOUD  , C1);
		agent.setValue(CURRENT_NETWORK, N1);

		s.addObject(agent);
		
		return s;
	}
	
	protected class Migrate extends SimpleAction implements FullActionModel{
		private String mTargetNetwork;
		private String mTargetCloud;
		
		// the constructor takes a string name "cloud", the action will migrate the
		// application to this cloud
		public Migrate(String actionName, Domain domain, String mTargetNetwork, String mTargetCloud){
			super(actionName, domain);
			this.mTargetNetwork = mTargetNetwork;
			this.mTargetCloud 	= mTargetCloud;
		}

		@Override
		protected State performActionHelper(State s,
				GroundedAction groundedAction) {
			ObjectInstance agent	= s.getFirstObjectOfClass(CLASSAGENT);
			
			String currentNetwork 	= agent.getStringValForAttribute(CURRENT_NETWORK);
			String currentCloud 	= agent.getStringValForAttribute(CURRENT_CLOUD);
			
			// It's not necessary to do this, but for future improvement it will help
			// The idea is, not to do a migrate action if I'm using the right cloud 
			
			if(!mTargetNetwork.equals(currentNetwork)){
				agent.setValue(CURRENT_NETWORK	, mTargetNetwork);
			}
			if(!mTargetCloud.equals(currentCloud)){
				agent.setValue(CURRENT_CLOUD	, mTargetCloud);
			}
			return s;
		}
		
		@Override
		public List<TransitionProbability> getTransitions(State s,
				GroundedAction groundedAction) {
			
			//get agent and current network/cloud
			ObjectInstance agent = s.getFirstObjectOfClass(CLASSAGENT);
			/*
			String currentNetwork 	= agent.getStringValForAttribute(CURRENT_NETWORK);
			String currentCloud 	= agent.getStringValForAttribute(CURRENT_CLOUD);
			*/
			List<TransitionProbability> tps = new ArrayList<TransitionProbability>(1);
			//TransitionProbability noChangeTransition = null;
			
			
			// what I have to do here is return two states, one with each action
			// To do this I have to update my state from the data then change the 
			// current network and cloud and send is back
			
			StateUpdaterForVI mUpdater = StateUpdaterForVI.getInstance();
			State ns = mUpdater.getNetxStateValues(s);
			
			// MIGRATE_TO_N1_C1
			State temp1 = ns.copy();
			agent = temp1.getFirstObjectOfClass(CLASSAGENT);
			agent.setValue(CURRENT_NETWORK	, N1);
			agent.setValue(CURRENT_CLOUD	, C1);
			tps.add(new TransitionProbability(temp1, 0.5));
			
			// MIGRATE_TO_N1_C1
			State temp2 = ns.copy();
			agent = temp2.getFirstObjectOfClass(CLASSAGENT);
			agent.setValue(CURRENT_NETWORK	, N1);
			agent.setValue(CURRENT_CLOUD	, C2);
			tps.add(new TransitionProbability(temp2, 0.5));
			
			/*
			for(int i = 0; i < this.directionProbs.length; i++){
				int [] newPos = this.moveResult(curX, curY, i);
				if(newPos[0] != curX || newPos[1] != curY){
					//new possible outcome
					State ns = s.copy();
					ObjectInstance nagent = ns.getFirstObjectOfClass(CLASSAGENT);
					nagent.setValue(ATTX, newPos[0]);
					nagent.setValue(ATTY, newPos[1]);

					//create transition probability object and add to our list of outcomes
					tps.add(new TransitionProbability(ns, this.directionProbs[i]));
				}
				else{
					//this direction didn't lead anywhere new
					//if there are existing possible directions
					//that wouldn't lead anywhere, aggregate with them
					if(noChangeTransition != null){
						noChangeTransition.p += this.directionProbs[i];
					}
					else{
						//otherwise create this new state and transition
						noChangeTransition = new TransitionProbability(s.copy(),
								this.directionProbs[i]);
						tps.add(noChangeTransition);
					}
				}
			}

		*/
			return tps;
		}
	}
	
	public static class Terminal implements TerminalFunction{
		
		@Override
		public boolean isTerminal(State s) {
			
			ObjectInstance agent = s.getFirstObjectOfClass(CloudWorld.CLASSAGENT);
			int value = agent.getIntValForAttribute(CloudWorld.D_N1_C1);
			if(value == -1){
				return true;
			}
			return false;
		}
	}
		

	public static class Reward implements RewardFunction{
		// D : Delay
		private int min_D;
		private int max_D;
		
		// T : Throughput
		private int min_T;
		private int max_T;
		
		private double weight;
		
		private int nigative_Reward;
		/**
		 * @param min_D The minimum delay required by an application
		 * @param max_D The maximum delay required by an application
		 * @param min_T The minimum throughput required by an application
		 * @param max_T The maximum throughput required by an application
		 * */
		public Reward(int min_D, int max_D, int min_T, int max_T,int nigative_Reward, double weight){
			this.min_D 	= min_D;
			this.max_D 	= max_D;
			this.min_T 	= min_T;
			this.max_T 	= max_T;
			this.weight = weight;
			
			this.nigative_Reward = nigative_Reward;
		}
		@Override
		// s and a are the state and action in from the previous time epochs, spirme is the currnet state
		// so we can have the reward for the tuples  (state action state^prime) 
		public double reward(State s, GroundedAction a, State sprime) {
			
			ObjectInstance agent = sprime.getFirstObjectOfClass(CLASSAGENT);
			
			String mCurrentNetwork	= agent.getStringValForAttribute(CURRENT_NETWORK);
			String mCurrentCloud	= agent.getStringValForAttribute(CURRENT_CLOUD);
			
			String mDelay 		= DELAY 		+ mCurrentNetwork + mCurrentCloud;
			String mThroughput 	= THROUGHPUT 	+ mCurrentNetwork + mCurrentCloud;
			
			int delay 		= agent.getIntValForAttribute(mDelay);
			int throughput 	= agent.getIntValForAttribute(mThroughput);
			
			double delayReward 		= calcDelayReward(delay);
			double throughputReward = calcThroughputReward(throughput);
			
			double reward = (weight) * delayReward + (1.0 - weight ) * throughputReward;

			
			if(DEBUG){
				System.out.println("REWARD___"+ reward);
			}
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
				return nigative_Reward;
			}
			// if the delay is between minimum and maximum
			if(min_D < delay && delay < max_D){
				return (double)(max_D - delay)/(max_D - min_D);
			}
			return nigative_Reward;
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
	public class WallPainter implements StaticPainter{

		@Override
		public void paint(Graphics2D g2, State s, float cWidth, float cHeight) {
			
			// for the laptop screen (without the frame is funny)
			cWidth 	-=100;
			cHeight -=100;
			//walls will be filled in black
			g2.setColor(Color.BLACK);
			
			//set up floats for the width and height of our domain
			float fWidth 	= CloudWorld.this.map.length;
			float fHeight 	= CloudWorld.this.map[0].length;
			
			//determine the width of a single cell on our canvas 
			//such that the whole map can be painted
			float width = cWidth / fWidth;
			float height = cHeight / fHeight;
			
			
			//pass through each cell of our map and if it's a wall, paint a black 
			//rectangle on our cavas of dimension widthxheight
			for(int i = 0; i < CloudWorld.this.map.length; i++){
				for(int j = 0; j < CloudWorld.this.map[0].length; j++){
					
					//is there a wall here?
					if(CloudWorld.this.map[i][j] >= 1){
						
						//left corrdinate of cell on our canvas
						float rx = i*width;
						
						//top coordinate of cell on our canvas
						//coordinate system adjustment because the java canvas
						//origin is in the top left instead of the bottom right
						float ry = cHeight - height - j*height;
						
						int loc = CloudWorld.this.map[i][j];
						switch (loc){
						case 6 :  g2.drawString(N1+ SPACE +C1, ry, rx);
				        break;
						case 5 :  g2.drawString(N1+ SPACE +C2, ry, rx);
				        break;
						case 4 :  g2.drawString(N1+ SPACE +C3, ry, rx);
				        break;
				        
						case 3 :  g2.drawString(N2+ SPACE +C1, ry, rx);
				        break;
						case 2 :  g2.drawString(N2+ SPACE +C2, ry, rx);
				        break;
						case 1 :  g2.drawString(N2+ SPACE +C3, ry, rx);
				        break;
						}
						// I fliped the x and y to make it work!!
						//paint the rectangle
						//g2.drawString(N1+C1, ry, rx);
						//g2.fill(new Rectangle2D.Float(ry, rx, width, height));
					}
				}
			}	
		}
	}
	public class AgentPainter implements ObjectPainter{

		@Override
		public void paintObject(Graphics2D g2, State s, ObjectInstance ob,
				float cWidth, float cHeight) {
			
			// for the laptop screen (without the frame is funny)
			cWidth 	-=100;
			cHeight -=100;
			
			//agent will be filled in gray
			g2.setColor(Color.GRAY);
			
			//set up floats for the width and height of our domain
			float fWidth  = CloudWorld.this.map.length;
			float fHeight = CloudWorld.this.map[0].length;
			
			//determine the width of a single cell on our canvas 
			//such that the whole map can be painted
			float width  = cWidth / fWidth;
			float height = cHeight / fHeight;
			
			String mCurrentNetwork	= ob.getStringValForAttribute(CURRENT_NETWORK);
			String mCurrentCloud	= ob.getStringValForAttribute(CURRENT_CLOUD);
			
			//left coordinate of cell on our canvas
			int ax = agentLocation(mCurrentNetwork, mCurrentCloud);
			float rx = ax*width;
			
			//top coordinate of cell on our canvas
			//coordinate system adjustment because the java canvas 
			//origin is in the top left instead of the bottom right
			float ry = cHeight - height - 2*height;
		
			//paint the rectangle
			g2.fill(new Ellipse2D.Float(rx, ry, width , height));
			
			String D_N1_C1 = String.valueOf(ob.getIntValForAttribute(CloudWorld.D_N1_C1));
			String D_N1_C2 = String.valueOf(ob.getIntValForAttribute(CloudWorld.D_N1_C2));
			String D_N1_C3 = String.valueOf(ob.getIntValForAttribute(CloudWorld.D_N1_C3));
			
			String D_N2_C1 = String.valueOf(ob.getIntValForAttribute(CloudWorld.D_N2_C1));
			String D_N2_C2 = String.valueOf(ob.getIntValForAttribute(CloudWorld.D_N2_C2));
			String D_N2_C3 = String.valueOf(ob.getIntValForAttribute(CloudWorld.D_N2_C3));
			
			String T_N1_C1 = String.valueOf(ob.getIntValForAttribute(CloudWorld.T_N1_C1));
			String T_N1_C2 = String.valueOf(ob.getIntValForAttribute(CloudWorld.T_N1_C2));
			String T_N1_C3 = String.valueOf(ob.getIntValForAttribute(CloudWorld.T_N1_C3));
			
			String T_N2_C1 = String.valueOf(ob.getIntValForAttribute(CloudWorld.T_N2_C1));
			String T_N2_C2 = String.valueOf(ob.getIntValForAttribute(CloudWorld.T_N2_C2));
			String T_N2_C3 = String.valueOf(ob.getIntValForAttribute(CloudWorld.T_N2_C3));
			
			for(int i = 0; i < CloudWorld.this.map.length; i++){
				for(int j = 0; j < CloudWorld.this.map[0].length; j++){
					
					//is there a wall here?
					if(CloudWorld.this.map[i][j] >= 1){
						
						
						//left corrdinate of cell on our canvas
						float x = (i-3)*width;
						float xt = (i-4)*width;
						//top coordinate of cell on our canvas
						//coordinate system adjustment because the java canvas
						//origin is in the top left instead of the bottom right
						float y = cHeight - height - (j) * height;
						
						g2.drawString("Delay : ", y , x);
						g2.drawString("Throu : ", y , xt);
						
						y+= 45;
						
						int loc = CloudWorld.this.map[i][j];
						switch (loc){
						case 6 :  
							g2.drawString(D_N1_C1, y, x);
							g2.drawString(T_N1_C1, y, xt);
				        break;
						case 5 :  
							g2.drawString(D_N1_C2, y, x);
							g2.drawString(T_N1_C2, y, xt);
				        break;
						case 4 :  
							g2.drawString(D_N1_C3, y, x);
							g2.drawString(T_N1_C3, y, xt);
				        break;
				        
						case 3 :  
							g2.drawString(D_N2_C1, y, x);
							g2.drawString(T_N2_C1, y, xt);
				        break;
						case 2 :  
							g2.drawString(D_N2_C2, y, x);
							g2.drawString(T_N2_C2, y, xt);
				        break;
						case 1 :  
							g2.drawString(D_N2_C3, y, x);
							g2.drawString(T_N2_C3, y, xt);
				        break;
						}
						// I fliped the x and y to make it work!!
						//paint the rectangle
						//g2.drawString(N1+C1, ry, rx);
						//g2.fill(new Rectangle2D.Float(ry, rx, width, height));
					}
				}
			}
		}
	}
	public StateRenderLayer getStateRenderLayer(){
		StateRenderLayer rl = new StateRenderLayer();
		rl.addStaticPainter(new WallPainter());
		rl.addObjectClassPainter(CLASSAGENT, new AgentPainter());
		return rl;
	}

	public Visualizer getVisualizer(){
		return new Visualizer(this.getStateRenderLayer());
	}
	public static int agentLocation(String currentNetwork , String currentCloud){
		String location = currentNetwork + currentCloud;
		int loc = 0;
		switch (location){
		case N1+C1 :  loc = 0;
        break;
		case N1+C2 :  loc = 2;
        break;
		case N1+C3 :  loc = 4;
        break;
		case N2+C1 :  loc = 6;
        break;
		case N2+C2 :  loc = 8;
        break;
		case N2+C3 :  loc = 10;
        break;
		}
		return loc;
	}
}