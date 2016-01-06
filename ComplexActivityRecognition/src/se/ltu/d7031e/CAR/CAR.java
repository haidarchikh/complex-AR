package se.ltu.d7031e.CAR;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;

import org.json.JSONObject;
	
public class CAR extends Thread{

	private JSONObject mEvent;
	private Situation  mSituation;
	private BlockingQueue<JSONObject> mInQ;
	private Hashtable<String, OnGoingCA> mOnGoingCAList = new Hashtable<>();
	private boolean situationChanged = false;
	private boolean running = false;
	@Override
	public void run() {
		while(running){
			try{
				mEvent = mInQ.take();
				checkCurrentSituation();
				deleteDeadCA();
				checkForNewCA(mEvent);
				passNewEvent(mEvent);
				checkIfAnyCAFulfilled();
			}catch (InterruptedException e) {
				System.out.println("-----------------INTERRUP-------------------");}	
		}
	}
	private void passNewEvent(JSONObject mEvent){
		if(mEvent.has(Consts.ACTIVITY)){
			newAtomicActivity(new AtomicActivity((mEvent.getString(Consts.ACTIVITY))));
		}else if(mEvent.has(Consts.CONTEXT)){
			newContextAttribute(new ContextAttribute(mEvent.getString(Consts.CONTEXT)));
		}
	}
	private void checkForNewCA(JSONObject mEvent){
		if(mEvent.has(Consts.ACTIVITY)){
			checkForNewCAA(new AtomicActivity((mEvent.getString(Consts.ACTIVITY))));
		}else if(mEvent.has(Consts.CONTEXT)){
			checkForNewCAC(new ContextAttribute(mEvent.getString(Consts.CONTEXT)));
		}
	}
	private void deleteDeadCA(){
		Iterator<Entry<String, OnGoingCA>> mIterator = mOnGoingCAList.entrySet().iterator();
		while(mIterator.hasNext()){
			 OnGoingCA mOnGoingCA = mIterator.next().getValue();
			 if(!mOnGoingCA.isAlive()){
				 System.out.println("Comlex Activity deleted (Exceded Time Range): "+ mOnGoingCA.getmName());
				 mIterator.remove(); 
			 }
		}
	}
	private void newAtomicActivity(AtomicActivity mActivity){
		Iterator<Entry<String, OnGoingCA>> mIterator = mOnGoingCAList.entrySet().iterator();
		while(mIterator.hasNext()){ 
			mIterator.next().getValue().NewAtomicActivity(mActivity);
			}
	}
	private void newContextAttribute(ContextAttribute mContext){
		Iterator<Entry<String, OnGoingCA>> mIterator = mOnGoingCAList.entrySet().iterator();
		while(mIterator.hasNext()){	
			mIterator.next().getValue().NewContextAttribute(mContext);
			}
	}
	private void checkCurrentSituation(){
		mSituation = CAFactory.atHome();
		if(situationChanged){deleteNonSituationCA();}
	}
	private void checkForNewCAA(AtomicActivity mActivity){
		Iterator<Entry<String, ComplexActivity>> mSituationCAListIterator = mSituation.getmCA().entrySet().iterator();
		while(mSituationCAListIterator.hasNext()){
			ComplexActivity mSituationCA = mSituationCAListIterator.next().getValue();
			if(mSituationCA.isStartAtomicActivity(mActivity) && !mOnGoingCAList.containsKey(mSituationCA.getmName())){
				addComplexActivitytoOngoingCALisht(mSituationCA);
			}
		}
	}
	private void checkForNewCAC(ContextAttribute mContext){
		Iterator<Entry<String, ComplexActivity>> mSituationCAListIterator = mSituation.getmCA().entrySet().iterator();
		while(mSituationCAListIterator.hasNext()){
			ComplexActivity mSituationCA = mSituationCAListIterator.next().getValue();
			if(mSituationCA.isStartContextAttribute(mContext) && !mOnGoingCAList.containsKey(mSituationCA.getmName())){
				addComplexActivitytoOngoingCALisht(mSituationCA);
			}
		}
	}
	private void addComplexActivitytoOngoingCALisht(ComplexActivity mCA){
		mOnGoingCAList.put(mCA.getmName(),new OnGoingCA(mCA));
		System.out.println("Detected a biggining of a Complex Activity :" + mCA.getmName());
	}
	private void checkIfAnyCAFulfilled(){
		Iterator<Entry<String, OnGoingCA>> mIterator = mOnGoingCAList.entrySet().iterator();
		while(mIterator.hasNext()){
			OnGoingCA mOnGoingCA = mIterator.next().getValue();
			if(mOnGoingCA.Fulfilled()){
				System.out.println("--------- Complex Activity fulfilled and removed --------- ");
				System.out.println("Name   :" + mOnGoingCA.getmCA().getmName());
				System.out.println("Wieght :" + mOnGoingCA.getmWieght());
				mIterator.remove();
			}
		}
	}
	private void deleteNonSituationCA(){
		Iterator<Entry<String, OnGoingCA>> mIterator = mOnGoingCAList.entrySet().iterator();
		while(mIterator.hasNext()){
			if(!mSituation.getmCA().containsKey(mIterator.next().getValue().getmName())){
				mIterator.remove();
			}
		}
	}
	public void setRunning(boolean running){
		this.running = running;
		if(!running){this.interrupt();}
	}
	public void setmInQ(BlockingQueue<JSONObject> mInQ) {
		this.mInQ = mInQ;
	}
}