package se.ltu.thesis.haidar.agent;

import java.util.Iterator;
import java.util.Random;
import java.util.TreeMap;
import java.util.Map.Entry;



public class GaussianGenerator {
	private Random rand = new Random();
	private double mMean;
	private TreeMap<Integer,Integer> mStatistcs = new TreeMap<>();
	private int mSampelCount;
	public static final double GAUSSIAN_LOWER_LIMIT = -3.0;
	public static final double GAUSSIAN_UPPER_LIMIT = 3.0;
	public GaussianGenerator(double mMean) {
		this.mMean = mMean;
	}
	public int getSample(){
		int res = 0;
		double gaussian = rand.nextGaussian();
		// Around 99 % of the random numbers are going to be {-3 ,3}
		if(gaussian <= GAUSSIAN_LOWER_LIMIT ){gaussian = GAUSSIAN_LOWER_LIMIT ;}
		if(gaussian >= GAUSSIAN_UPPER_LIMIT ){gaussian = GAUSSIAN_UPPER_LIMIT ;}
		// Make the range {-0.5,0.5}
		gaussian /= GAUSSIAN_UPPER_LIMIT*2;
		// Make the expected range the mean and extend the range
		gaussian = (mMean * gaussian)+ mMean ;
		res = (int) Math.round(gaussian);
		appendToStatistics(res);
		mSampelCount++;
		return res;
	}
	public void printStatistics(){
		System.out.println("Produced :"+ mSampelCount +" samples, mean value : "+ mMean);
		Iterator<Entry<Integer, Integer>> mIterator = mStatistcs.entrySet().iterator();
		while(mIterator.hasNext()){
			 Entry<Integer, Integer> mEntry = mIterator.next();
			 int sample = mEntry.getKey();
			 int occurrence = mEntry.getValue();
			 double percentage = (double) occurrence /(double) mSampelCount;
			 percentage *=100;
			 System.out.println("sample : "+String.format("%02d", sample)
					 +", Occurrence : "+ String.format("%02d", occurrence)
					 +", Percentage : "+percentage+"%");
			 }
		}
	private void appendToStatistics(int delay){
		if(mStatistcs.containsKey(delay)){
			int occurrence = mStatistcs.get(delay);
			occurrence++;
			mStatistcs.put(delay, occurrence);	
		}else{
			mStatistcs.put(delay, 1);
		} 
	}
	public static void main (String[] args){
		
		GaussianGenerator mG = new GaussianGenerator(1000);
		for (int i = 0 ; i < 10000000 ; i++ ){
			mG.getSample();
		}
		mG.printStatistics();
	}
}
