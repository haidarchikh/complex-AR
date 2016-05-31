package se.ltu.thesis.haidar.agent;

import java.util.HashMap;
import java.util.Map;

import burlap.oomdp.core.AbstractGroundedAction;
import burlap.oomdp.core.states.State;
import burlap.oomdp.statehashing.HashableState;
import burlap.oomdp.statehashing.HashableStateFactory;
import burlap.oomdp.statehashing.SimpleHashableStateFactory;

public class StateActionOccurrenceMapper {
	
	private HashableStateFactory 						hashingFactory;
	private Map<HashableState, Map<String, Integer>> 	mStateMap;
	
	public StateActionOccurrenceMapper(){
		hashingFactory	= new SimpleHashableStateFactory();
		mStateMap 		= new HashMap<>();
	}
	
	/**
	 * This method checks if an action is already taken from a given state
	 * @param s the state
	 * @param ga the action to be check if its already been taken from this state
	 * @return 0 if the action is not take
	 * @return X the number of the action been taken
	 * */
	public int getOccurrence(State s, AbstractGroundedAction ga){
		HashableState mState = hashingFactory.hashState(s);
		
		if (!mStateMap.containsKey(mState)) {
			// If state-action doesn't exist
			AddNewState(mState, ga);
			return 0;
		}
		
		// look how many times we've been in this state and took that action
		Map<String, Integer> mActionMap = mStateMap.get(mState);
		// If state-action already exist
		if (mActionMap.containsKey(ga.actionName())) {
			int occurrenceTime = IncrementActionOccurenceTime(mState, mActionMap, ga);
			return occurrenceTime;
			
			// else track the new state-action
		} else {AddNewAction(mState, mActionMap, ga); return 0;	}
	}
	
	private void AddNewState(HashableState mState, AbstractGroundedAction ga) {
		Map<String, Integer> mActionMap = new HashMap<>();
		// add the action to the map. 1 , it's the first time this action
		// has occurred
		mActionMap.put(ga.actionName(), 1);
		mStateMap.put(mState, mActionMap);
	}

	private void AddNewAction(HashableState mState,
			Map<String, Integer> mActionMap, AbstractGroundedAction ga) {
		mActionMap.put(ga.actionName(), 1);
		mStateMap.put(mState, mActionMap);
	}

	private int IncrementActionOccurenceTime(HashableState mState,
			Map<String, Integer> mActionMap, AbstractGroundedAction ga) {
		int occurrenceTime = mActionMap.get(ga.actionName());
		occurrenceTime++;
		mActionMap.put(ga.actionName(), occurrenceTime);
		mStateMap.put(mState, mActionMap);
		return occurrenceTime;
	}
}