package se.ltu.d7031e.CAR;

public class AtomicActivity {
	
	private String mName;
	private double mWeight;
	
	public AtomicActivity(String mName){
		this.mName = mName;
	}
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