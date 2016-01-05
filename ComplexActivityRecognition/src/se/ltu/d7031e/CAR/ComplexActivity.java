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
	
	public ComplexActivity (String mName){
		this.mName = mName;
	}
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
	public boolean isCoreAtomicActivity(AtomicActivity mActivity){
			return mCoreActivities.containsKey(mActivity.getmName());
	}
	public boolean isCoreContextAttribute(ContextAttribute mContext){ 
			return mCoreContext.containsKey(mContext.getmName());
	}
	public boolean isStartAtomicActivity(AtomicActivity mActivity){
			return mStartActivities.containsKey(mActivity.getmName());
	}
	public boolean isStartContextAttribute(ContextAttribute mContext){ 
			return mStartContext.containsKey(mContext.getmName());
	}
	public boolean isEndAtomicActivity(AtomicActivity mActivity){
			return mEndActivities.containsKey(mActivity.getmName());
	}
	public boolean isEndContextAttribute(ContextAttribute mContext){ 
			return mEndContext.containsKey(mContext.getmName());
	}
	public double getmThreshold() {
		return mThreshold;
	}
	public void setmThreshold(double mThreshold) {
		this.mThreshold = mThreshold;
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
	public Hashtable<String,AtomicActivity> getmStartActivities() {
		return mStartActivities;
	}
	public void setmStartActivities(Hashtable<String,AtomicActivity> mStartActivities) {
		this.mStartActivities = mStartActivities;
	}
	public Hashtable<String,AtomicActivity> getmCoreActivities() {
		return mCoreActivities;
	}
	public void setmCoreActivities(Hashtable<String,AtomicActivity> mCoreActivities) {
		this.mCoreActivities = mCoreActivities;
	}
	public Hashtable<String,AtomicActivity> getmEndActivities() {
		return mEndActivities;
	}
	public void setmEndActivities(Hashtable<String,AtomicActivity> mEndActivities) {
		this.mEndActivities = mEndActivities;
	}
	public Hashtable<String,AtomicActivity> getmActivities() {
		return mActivities;
	}
	public void setmActivities(Hashtable<String,AtomicActivity> mActivities) {
		this.mActivities = mActivities;
	}
	public Hashtable<String,ContextAttribute> getmStartContext() {
		return mStartContext;
	}
	public void setmStartContext(Hashtable<String,ContextAttribute> mStartContext) {
		this.mStartContext = mStartContext;
	}
	public Hashtable<String,ContextAttribute> getmCoreContext() {
		return mCoreContext;
	}
	public void setmCoreContext(Hashtable<String,ContextAttribute> mCoreContext) {
		this.mCoreContext = mCoreContext;
	}
	public Hashtable<String,ContextAttribute> getmEndContext() {
		return mEndContext;
	}
	public void setmEndContext(Hashtable<String,ContextAttribute> mEndContext) {
		this.mEndContext = mEndContext;
	}
	public Hashtable<String,ContextAttribute> getmContexts() {
		return mContexts;
	}
	public void setmContexts(Hashtable<String,ContextAttribute> mContext) {
		this.mContexts = mContext;
	}
}