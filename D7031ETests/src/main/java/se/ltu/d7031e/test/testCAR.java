package se.ltu.d7031e.test;

import se.ltu.d7031e.CAR.CAR;

public class testCAR {
	
	public static String mRabbit_IP = Consts.LOCALHOST ;
	
	public static void main(String[] args) {	
		
		Mockup mMockup     = new Mockup();
		CAR     mAlgorithm = new CAR();
		mMockup.setRunning(true);
		mAlgorithm.setRunning(true);
		
		mAlgorithm.setmInQ(mMockup.getmOutQ());
		
		mAlgorithm.start();
		mMockup .start();
	}
	
}
