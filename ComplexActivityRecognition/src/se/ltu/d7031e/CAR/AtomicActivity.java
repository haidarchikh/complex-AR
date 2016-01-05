package se.ltu.d7031e.CAR;

public class AtomicActivity {
	
	private String mName;
	private double mWeight;
	/**
	 * Creates an Atomic Activity object with the given name
	 * @param mName the Atomic Activity name.
	 * */
	public AtomicActivity(String mName){
		this.mName = mName;
	}
	/**
	 * Creates an Atomic Activity object with the given name/weight
	 * @param mName the Atomic Activity name.
	 * @param mWeight the Atomic Activity weight. 
	 * */
	public AtomicActivity(String mName , double mWeight){
		this.mName		= mName;
		this.mWeight	= mWeight;
	}
	public String getmName() {
		return mName;
	}
	public void setmName(String mName) {
		this.mName = mName;
	}
	public double getmWeight() {
		return mWeight;
	}
	public void setmWeight(double mWeight) {
		this.mWeight = mWeight;
	}
}