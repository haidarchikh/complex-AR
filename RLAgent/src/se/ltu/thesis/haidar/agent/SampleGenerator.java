package se.ltu.thesis.haidar.agent;

import java.util.Iterator;
import java.util.TreeMap;
import java.util.Map.Entry;

import cern.jet.random.Normal;
import cern.jet.random.Poisson;
import cern.jet.random.engine.DRand;
import cern.jet.random.engine.RandomEngine;

public class SampleGenerator {
	
	private TreeMap<Integer,Integer> mStatistcs = new TreeMap<>();
	private int mSampelCount;
	
	private double mLambda;
	private double mMean;
	private double mVariance;
	
	private RandomEngine 	mEngine;
	private Normal 			mNormal;
	private Poisson 		mPoisson;
	
	private boolean itIsGaussain = false;
	private int roundTo;
	
	public SampleGenerator(double mMean , double mVariance, int roundTo){
		this.mMean 		= mMean;
		this.mVariance 	= mVariance;
		this.roundTo 	= roundTo;
		mEngine = new DRand();
		mNormal = new Normal(this.mMean, this.mVariance, mEngine);
		itIsGaussain = true;
		
	}
	public SampleGenerator(double mLambda){
		this.mLambda = mLambda;
		mEngine = new DRand();
		mPoisson = new Poisson(this.mLambda, mEngine);
	}
	public int getGaussianSample(){
		int sample = 0;
		double gaussian = mNormal.nextDouble();
		sample = (int)round(gaussian);
		appendToStatistics(sample);
		mSampelCount++;
		return sample;
	}
	private double round(double sample){
		return Math.floor((sample + roundTo/2) / roundTo) * roundTo;
	}
	public int getPoissonSample(){
		int res = 0;
		double gaussian = mPoisson.nextInt();
		res = (int) Math.round(gaussian);
		appendToStatistics(res);
		mSampelCount++;
		return res;
	}
	public void printStatistics(){
		if(itIsGaussain){
			System.out.println("Gaussian distribution");
			System.out.println("Produced :"+ mSampelCount +" samples, mean value : "+ mMean 
					+", variance : " + mVariance+", round to : "+ roundTo);
		}else{
			System.out.println("Poisson distribution");
			System.out.println("Produced :"+ mSampelCount +" samples, lambda : "+ mLambda);	
		}
		Iterator<Entry<Integer, Integer>> mIterator = mStatistcs.entrySet().iterator();
		while(mIterator.hasNext()){
			 Entry<Integer, Integer> mEntry = mIterator.next();
			 int sample = mEntry.getKey();
			 int occurrence = mEntry.getValue();
			 double percentage = (double) occurrence /(double) mSampelCount;
			 percentage *=100;
			 System.out.println("sample : "+String.format("%02d", sample)
					 +", Occurrence : "+ String.format("%02d", occurrence)
					 +", Percentage : "+String.format("%.3f", percentage)+"%");
			 }
		}
	private void appendToStatistics(int sample){
		if(mStatistcs.containsKey(sample)){
			int occurrence = mStatistcs.get(sample);
			occurrence++;
			mStatistcs.put(sample, occurrence);	
		}else{
			mStatistcs.put(sample, 1);
		} 
	}

	public static void getGaussianExample(double mean, double variance,int roundTo, int samples){
		SampleGenerator mG = new SampleGenerator(mean, variance, roundTo);
		for (int i = 0 ; i < samples ; i++ ){
			mG.getGaussianSample();
		}
		mG.printStatistics();
	}
	public static void getPoissonExample(double lambda, int samples){
		SampleGenerator mG = new SampleGenerator(lambda);
		for (int i = 0 ; i < samples ; i++ ){
			mG.getPoissonSample();
		}
		mG.printStatistics();
	}
	
	public static void main (String[] args){
		getGaussianExample(400, 20, 10,1000);
	}
}