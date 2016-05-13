package se.ltu.thesis.haidar.agent;

import org.apache.commons.math3.distribution.ParetoDistribution;

import cern.jet.random.Normal;
import cern.jet.random.engine.DRand;
import cern.jet.random.engine.RandomEngine;

public class Test {
	private RandomEngine	mEngine;
	private Normal			mNormal;
	
	
	private ParetoDistribution mPareto;
	
	
	public Test(){
		mEngine = new DRand();
		mNormal = new Normal(10, 10, mEngine);
		
		mPareto = new ParetoDistribution(100, 5);
	}
	public static void Main(String[] args){
		Test x = new Test();
		x.mNormal.nextDouble();
		//x.mPareto.
	}
}
