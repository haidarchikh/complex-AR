package se.ltu.d7031e.CAR;

import java.util.Hashtable;

public class Situation {
	
	private String mName;
	private Hashtable<String,ComplexActivity> mCA = new Hashtable<>();
	
	public Situation(String mName){
		this.mName = mName;
	}
	public void addComplexActivity(ComplexActivity mComplexActivity){
		mCA.put(mComplexActivity.getmName(), mComplexActivity);
	}
	public String getmName() {
		return mName;
	}
	public void setmName(String mName) {
		this.mName = mName;
	}
	public Hashtable<String,ComplexActivity> getmCA() {
		return mCA;
	}
	public void setmCA(Hashtable<String,ComplexActivity> mCA) {
		this.mCA = mCA;
	}
}