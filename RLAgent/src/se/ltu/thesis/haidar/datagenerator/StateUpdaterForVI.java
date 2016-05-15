package se.ltu.thesis.haidar.datagenerator;

import java.util.Hashtable;

import org.json.JSONObject;

import se.ltu.thesis.haidar.agent.CloudWorld;
import deprecated.StateUpdater;
import burlap.oomdp.core.objects.ObjectInstance;
import burlap.oomdp.core.states.State;

public class StateUpdaterForVI {
	  private static StateUpdaterForVI instance = null;
	  
	  private Hashtable<Integer, JSONObject> mDataTable;
	  private StateUpdater mUpdater;
	  protected StateUpdaterForVI() {
		  	
		  	mUpdater = new StateUpdater();
			mUpdater.loadDataFromFile();
			mDataTable = mUpdater.getDataTable();
	  }
	  
	  public static StateUpdaterForVI getInstance() {
	     if(instance == null) {
	        instance = new StateUpdaterForVI();
	     }
	     return instance;
	  }
	  
	private int timeEpoch = 0;
	private JSONObject mState;
	
	public State getNetxStateValues(State nextState){
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
}
