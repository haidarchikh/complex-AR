package se.ltu.thesis.haidar.datagenerator;

import java.util.Iterator;
import java.util.Random;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.distribution.ParetoDistribution;

import cern.jet.random.Normal;
import cern.jet.random.engine.DRand;
import cern.jet.random.engine.RandomEngine;

public class DataGenerator {

	
	private double[] mData;
	private SampleGenerator mG;
	private Statistics mStatistics;


	public void addGaussianPlan(int mCount, double mMean, double mVariance,
			double roundTo) {
		mG = new GaussianGenerator(mCount, mMean, mVariance, roundTo);
		double[] data = mG.getData();
		mData = (double[])ArrayUtils.addAll(mData, data);
	}
	public double[] getData(){
		return mData;
	}
	
	public void addParetoPlan(int mCount, double mScale, double mShape,
			double roundTo) {
		mG = new ParetoGenerator(mCount, mScale, mShape, roundTo);
		double[] data = mG.getData();
		mData = (double[])ArrayUtils.addAll(mData, data);
	}
	/*
	public void addParetoPlan(int mCount, double mScale, double mShape,double inverce,
			double roundTo) {
		mG = new ParetoGenerator(mCount, mScale, mShape,inverce, roundTo);
		double[] data = mG.getData();
		mData = (double[])ArrayUtils.addAll(mData, data);
	}
	*/
	public SampleGenerator getGenerator(){
		return mG;
	}
	

	// Delay
	public static final double CONSTANT_DELAY = 10000;
	public static final double DELAY_VARIANCE = 10;
	public static final double ROUND_DELAY_TO = 10;
	
	public static final double PARETO_SHAPE = 5;
	
	// Throughput
	public static final double CONSTANT_THROUGHPUT = 1;
	public static final double THROUGHPUT_VARIANCE = 5;
	public static final double ROUND_THROUGHPUT_TO = 10;
	
	
	public static void main(String[] args){
		DataGenerator T_N2_C2 = new DataGenerator();
		//mG.addGaussianPlan(100, 100, 10, 10);
		//mG.addGaussianPlan(100, 2000, 10, 10);
		
		T_N2_C2.addGaussianPlan(100, 20, 5,	10);
		T_N2_C2.addGaussianPlan(100, 20, 5,	10);
		T_N2_C2.addGaussianPlan(100, 20, 5, 10);
		T_N2_C2.addGaussianPlan(100, 20, 5,	10);
		T_N2_C2.addGaussianPlan(100, 20, 5,	10);
		T_N2_C2.addGaussianPlan(100, 50, 5,	10);
		T_N2_C2.addGaussianPlan(100, 50, 5,	10);
		T_N2_C2.addGaussianPlan(100, 50, 5,	10);
		T_N2_C2.addGaussianPlan(100, 50, 5,	10);
		T_N2_C2.addGaussianPlan(100, 50, 5,	10);
		
		T_N2_C2.printStatistics();
	}
	
	
	
	
	
	
	
	public DataGenerator() {}
	public void printStatistics(){
		mStatistics = new Statistics(mData);
		mStatistics.printStatistics();
	}
	// It returns {sampleValue, Occurrence}
	public TreeMap<Double, Integer> getStatistics(){
		mStatistics = new Statistics(mData);
		return mStatistics.getStatistics();
	}
	
	public abstract class SampleGenerator {

		protected double[] mData;
		protected int mSampleCount;
		protected double roundTo;

		public SampleGenerator(int mSampleCount, double roundTo) {
			this.mSampleCount = mSampleCount;
			this.mData = new double[mSampleCount];
			this.roundTo = roundTo;
		}

		protected abstract double getSample();
		
		public final double[] getData() {
			int count = 0;
				while (mSampleCount > count) {
					double sample = getSample();
					// Don't round if the rounding value is -1
					if(roundTo != -1){
						sample = round(sample, roundTo);
						}
					mData[count] = sample;
					count++;
				}
				return mData;
		}
		// rounds up and down. 1.4 to 1 and 1.6 to 2
		public final double round(double sample, double roundTo) {
			if(roundTo % 1 == 0){
				// round to an integer
				return Math.round((sample + roundTo / 2) / roundTo) * roundTo;
			} else{
				// round to a double
				return roundTo * Math.floor(sample / roundTo);
			}
		}
	}
	public class ParetoGenerator extends SampleGenerator{

		private ParetoDistribution mPareto;
		
		public ParetoGenerator(int mSampleCount,double mScale, double mShape,double mInverseCumAccuracy, double roundTo) {
			super(mSampleCount, roundTo);
			mPareto = new ParetoDistribution(mScale, mShape, mInverseCumAccuracy);
		}
		public ParetoGenerator(int mSampleCount,double mScale, double mShape,double roundTo) {
			super(mSampleCount, roundTo);
			mPareto = new ParetoDistribution(mScale, mShape);
			//this(mSampleCount,mScale,mShape,ParetoDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY,roundTo);
		}
		public ParetoDistribution getGenerator(){
			return mPareto;
		}

		@Override
		protected double getSample() {
			return mPareto.sample();
		}
		
	}
	public class GaussianGenerator extends SampleGenerator {
		
		private double mMean;
		private double mVariance;
		
		private RandomEngine	mEngine;
		private Normal			mNormal;
		
		public GaussianGenerator(int mSampleCount, double mMean , double mVariance, double roundTo) {
			super(mSampleCount, roundTo);
			this.mMean 		= mMean;
			this.mVariance 	= mVariance;
			
			mEngine = new DRand();
			mNormal = new Normal(this.mMean, this.mVariance, mEngine);
		}
		
		private Random mRandom = new Random();
		@Override
		protected double getSample() {
			double gaussian = 0;
			
			// Increase randomness
			do{gaussian = mNormal.nextDouble();}
			while (mRandom.nextInt(10) < 7);
			
			// If the sample is < 0 . return 0
			if(gaussian <0){gaussian = 0 ;}
			
			return gaussian;
		}
		public Normal getGenerator(){
			return mNormal;
		}
	}
	
	private class Statistics {
		
		private TreeMap<Double, Integer> mStatistics = new TreeMap<>();
		private int mSampelCount;
		
		public Statistics(double[]mData){
			for(double i : mData){
				appendToStatistics(i);
			}
		}
		public TreeMap<Double, Integer> getStatistics(){
			return mStatistics;
		} 
		public void printStatistics() {
			
			Iterator<Entry<Double, Integer>> mIterator = mStatistics.entrySet()
					.iterator();
			while (mIterator.hasNext()) {
				Entry<Double, Integer> mEntry = mIterator.next();
				double sample = mEntry.getKey();
				int occurrence = mEntry.getValue();
				double percentage = (double) occurrence / (double) mSampelCount;
				percentage *= 100;
				System.out.println("sample : " + String.format("%.3f", sample)
						+ ", Occurrence : " + String.format("%03d", occurrence)
						+ ", Percentage : " + String.format("%.3f", percentage)
						+ "%");
			}
		}
		private void appendToStatistics(double sample) {
			mSampelCount++;
			if (mStatistics.containsKey(sample)) {
				int occurrence = mStatistics.get(sample);
				occurrence++;
				mStatistics.put(sample, occurrence);
			} else {
				mStatistics.put(sample, 1);
			}
		}
	}
}