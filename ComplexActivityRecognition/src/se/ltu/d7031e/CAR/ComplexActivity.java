package se.ltu.d7031e.CAR;

import java.util.Hashtable;


public class ComplexActivity {
	
	private String mName;
	private double mThreshold;
	private long   mLifespan;
	private Hashtable<String,AtomicActivity>   mStartActivities  = new Hashtable<>();
	private Hashtable<String,AtomicActivity>   mCoreActivities   = new Hashtable<>();
	private Hashtable<String,AtomicActivity>   mEndActivities    = new Hashtable<>();
	private Hashtable<String,AtomicActivity>   mActivities       = new Hashtable<>();
	private Hashtable<String,ContextAttribute> mStartContext     = new Hashtable<>();
	private Hashtable<String,ContextAttribute> mCoreContext      = new Hashtable<>();
	private Hashtable<String,ContextAttribute> mEndContext       = new Hashtable<>();
	private Hashtable<String,ContextAttribute> mContexts         = new Hashtable<>();
	/**
	 * Creates Complex Activity object with the given name
	 * @param mName the Atomic Activity name.
	 * */
	public ComplexActivity (String mName){
		this.mName = mName;
	}
	/**
	 * Adds an Atomic Activity to this Complex Activity 
	 * Activities list.
	 * @param mActivity Atomic Activity to be added
	 * */
	public void addActivity(AtomicActivity mActivity){
		mActivities.put(mActivity.getmName(), mActivity);
	}
	public void addStartActivity(AtomicActivity mActivity){
		mStartActivities.put(mActivity.getmName(), mActivity);
	}
	public void addEndActivity(AtomicActivity mActivity){
		mEndActivities.put(mActivity.getmName(), mActivity);
	}
	public void addCoreActivity(AtomicActivity mActivity){
		mCoreActivities.put(mActivity.getmName(), mActivity);
	}
	public void addContext(ContextAttribute mContext){
		mContexts.put(mContext.getmName(), mContext);
	}
	public void addStartContext(ContextAttribute mContext){
		mStartContext.put(mContext.getmName(), mContext);
	}
	public void addEndContext(ContextAttribute mContext){
		mEndContext.put(mContext.getmName(), mContext);
	}
	public void addCoreContext(ContextAttribute mContext){
		mCoreContext.put(mContext.getmName(), mContext);
	}
	public double GetActivityWieght(AtomicActivity mActivity){
		return mActivities.get(mActivity.getmName()).getmWeight();
	}
	public double GetContextWieght(ContextAttribute mContext){
		return mContexts.get(mContext.getmName()).getmWeight();
	}
	public boolean isAtomicActivity(AtomicActivity mActivity){
		return mActivities.containsKey(mActivity.getmName());
	}
	public boolean isContextAttribute(ContextAttribute mContext){
		return mContexts.containsKey(mContext.getmName());
	}
	public boolean isStartAtomicActivity(AtomicActivity mActivity){
			return mStartActivities.containsKey(mActivity.getmName());
	}
	public boolean isStartContextAttribute(ContextAttribute mContext){ 
			return mStartContext.containsKey(mContext.getmName());
	}
	public Hashtable<String,AtomicActivity> getmCoreActivities() {
		return mCoreActivities;
	}
	public Hashtable<String,AtomicActivity> getmEndActivities() {
		return mEndActivities;
	}
	public Hashtable<String,ContextAttribute> getmCoreContext() {
		return mCoreContext;
	}
	public Hashtable<String,ContextAttribute> getmEndContext() {
		return mEndContext;
	}
	public void setmThreshold(double mThreshold) {
		this.mThreshold = mThreshold;
	}
	public double getmThreshold() {
		return mThreshold;
	}
	public String getmName() {
		return mName;
	}
	public void setmName(String mName) {
		this.mName = mName;
	}
	public long getmLifespan() {
		return mLifespan;
	}
	public void setmLifespan(long mLifespan) {
		this.mLifespan = mLifespan;
	}
}