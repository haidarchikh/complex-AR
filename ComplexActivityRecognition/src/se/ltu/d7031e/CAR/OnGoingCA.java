package se.ltu.d7031e.CAR;

import java.util.Hashtable;

public class OnGoingCA {
	
	private ComplexActivity mCA;
	private String mName;
	private long   mTimeCounter;
	private double mWieght;
	private Hashtable<String,AtomicActivity>  mActivityList = new Hashtable<>();
	private Hashtable<String,ContextAttribute> mContextList = new Hashtable<>();
	
	public OnGoingCA(ComplexActivity mCA){
		this.mCA = mCA;
		this.setmTimeCounter(mCA.getmLifespan());
		this.mName = mCA.getmName();
	}
	public void NewAtomicActivity(AtomicActivity mActivity){
		if(mCA.isAtomicActivity(mActivity)&&!mActivityList.containsKey(mActivity.getmName())){
			mActivity.setmWeight(mCA.GetActivityWieght(mActivity));
			mActivityList.put(mActivity.getmName(),mActivity);
			mWieght += mActivity.getmWeight()/2;
		}
	}
	public void NewContextAttribute(ContextAttribute mContext){
		if(mCA.isContextAttribute(mContext)&&!mContextList.containsKey(mContext.getmName())){
			mContext.setmWeight(mCA.GetContextWieght(mContext));
			mContextList.put(mContext.getmName(),mContext);
			mWieght += mContext.getmWeight()/2;
		}
	}
	public boolean Fulfilled(){
		return(	CoreAtomicActivityFullfilled()&&
				CoreContextAttributeFulfilled()&&
				EndAtomicActivityFulfilled() &&
				EndContextAttributeFulfilled() &&
				ThresholdFulfilled());
	}
	private boolean CoreAtomicActivityFullfilled(){
		return mActivityList.keySet().containsAll(mCA.getmCoreActivities().keySet());
	}
	private boolean CoreContextAttributeFulfilled(){
		return mContextList.keySet().containsAll(mCA.getmCoreContext().keySet());
	}
	private boolean EndAtomicActivityFulfilled(){
		return mActivityList.keySet().containsAll(mCA.getmEndActivities().keySet());
	}
	private boolean EndContextAttributeFulfilled(){
		return mContextList.keySet().containsAll(mCA.getmEndContext().keySet());
	}
	private boolean ThresholdFulfilled(){
		return mWieght >= mCA.getmThreshold();
	}
	public boolean isAlive() {
		return mTimeCounter > System.currentTimeMillis();
	}
	public void setmTimeCounter(long mTimeCounter) {
		mTimeCounter *= 1000L;
		mTimeCounter += System.currentTimeMillis();
		this.mTimeCounter = mTimeCounter;
	}
	public String GetFulfilledActivities(){
		return mActivityList.keySet().toString();
	}
	public String GetFulfilledContext(){
		return mContextList.keySet().toString();
	}
	public Hashtable<String,AtomicActivity> getmActivityList() {
		return mActivityList;
	}
	public void setmActivityList(Hashtable<String,AtomicActivity> mActivityList) {
		this.mActivityList = mActivityList;
	}
	public Hashtable<String,ContextAttribute> getmContextList() {
		return mContextList;
	}
	public void setmContextList(Hashtable<String,ContextAttribute> mContextList) {
		this.mContextList = mContextList;
	}
	public ComplexActivity getmCA() {
		return mCA;
	}
	public void setmCA(ComplexActivity mCA) {
		this.mCA = mCA;
	}
	public double getmWieght(){
		return mWieght;
	}
	public String getmName() {
		return mName;
	}
	public void setmName(String mName) {
		this.mName = mName;
	}
}