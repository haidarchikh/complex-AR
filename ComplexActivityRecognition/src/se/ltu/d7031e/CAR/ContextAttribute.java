package se.ltu.d7031e.CAR;

public class ContextAttribute {
	
	private String mName;
	private double mWeight;

	/**
	 * Creates an Context Attribute object with the given name
	 * @param mName the Context Attribute name.
	 * */
	public ContextAttribute(String mName){
		this.mName = mName;
	}
	/**
	 * Creates an Context Attribute object with the given name/weight
	 * @param mName the Context Attribute name.
	 * @param mWeight the Context Attribute weight. 
	 * */
	public ContextAttribute (String mName, double mWeight){
		this.mName 		= mName;
		this.mWeight 	= mWeight;
	}
	/**
	 * @return the Context Attribute name
	 * */
	public String getmName() {
		return mName;
	}
	/**
	 * Sets the Context Attribute name
	 * @param mName the contexts's name
	 * */
	public void setmName(String mName) {
		this.mName = mName;
	}
	/**
	 * Returns the Context Attribute weight.
	 * @return a double value between 0 and 1
	 * */
	public double getmWeight() {
		return mWeight;
	}
	/**
	 * Sets the Context Attribute weight.
	 * @param mWeight a double value between 0 and 1
	 * */
	public void setmWeight(double mWeight) {
		this.mWeight = mWeight;
	}
}
