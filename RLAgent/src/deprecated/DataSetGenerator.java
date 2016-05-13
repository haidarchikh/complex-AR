package deprecated;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeMap;
import java.util.Map.Entry;

public class DataSetGenerator {
	
	// This will contain the statistics for each data set
	private TreeMap<Integer,Integer> mStatistcs = new TreeMap<>();
	
	// This will contain the statistics for all the generated data
	private TreeMap<Integer,Integer> mOverallStatistcs = new TreeMap<>();
	
	private Queue<double[]> mQ;
	private SampleGenerator mGenerator;
	
	// [0] : count, [1] : mean, [2] : variance
	private double[] mCurrentTuple;
	
	private double mCount;
	private double mMean;
	private double mVariance;
	private int    mRoundToMultipleOf;
	
	// set to true of you want to print statistics
	private boolean printStatistics;
	
	private int mOverallSampleCount;
	private int mCurrentDataSetCount;
	
	// ugly fix
	private boolean firstRun = true;
	
	public DataSetGenerator(boolean printStatistics){
		mQ = new LinkedList<>();
		this.printStatistics = printStatistics;
	}
	
	public void addDataPlan(double count, double mean, double variance,int roundToMultipleOf){
		double[] temp = {count, mean, variance, roundToMultipleOf};
		mQ.add(temp);
	}
	
	public int getSample(){
		
		if(firstRun){
			setupNextTuple();
			firstRun=false;
			}
		if (mCount == 0){
			printLastDataSetStatistics();
			RestLastDataSetStatistics();
			if(mQ.isEmpty()){
				printOverallDataStatistics();
				return -1;
				}
			setupNextTuple();
			}
		mCount--;
		mOverallSampleCount++;
		
		int res = mGenerator.getGaussianSample();
		addToStatistics(res);
		return res;
	}

	
	private void setupNextTuple(){
		mCurrentTuple 		= mQ.poll();
		mCount 				= mCurrentTuple[0];
		mMean 				= mCurrentTuple[1];
		mVariance 			= mCurrentTuple[2];
		mRoundToMultipleOf	= (int) mCurrentTuple[3];
		
		mGenerator = new SampleGenerator(mMean, mVariance,mRoundToMultipleOf);
		
		// nasty
		mCurrentDataSetCount = (int) mCount;
	}
	private void RestLastDataSetStatistics(){
		mStatistcs = new TreeMap<>();
	}
	
	public void printLastDataSetStatistics(){
		if(printStatistics){
			printTree(mStatistcs, mCurrentDataSetCount);
		}
	}
	public void printOverallDataStatistics(){
		if(printStatistics){
			printTree(mOverallStatistcs, mOverallSampleCount);
		}
	}
	
	private void addToStatistics(int sample){
		// don't add to statistics if we are not going to print any
		if(printStatistics){
			addToTreeMap(sample, mStatistcs);
			addToTreeMap(sample, mOverallStatistcs);
		}
	}
	
	private void addToTreeMap(int sample,TreeMap <Integer,Integer> mTree){
		if(mTree.containsKey(sample)){
			int occurrence = mTree.get(sample);
			occurrence++;
			mTree.put(sample, occurrence);	
		}else{
			mTree.put(sample, 1);
		}
	}
	
	private void printTree(TreeMap <Integer,Integer> mTree, int sampleCount){
		System.out.println("---------------Gaussian distribution : "+ sampleCount+" sample ---------------");
		
		Iterator<Entry<Integer, Integer>> mIterator = mTree.entrySet().iterator();
		mTree.size();
		while(mIterator.hasNext()){
			 Entry<Integer, Integer> mEntry = mIterator.next();
			 int sample = mEntry.getKey();
			 int occurrence = mEntry.getValue();
			 double percentage = (double) occurrence /(double) sampleCount;
			 percentage *=100;
			 System.out.println("Sample value: "+String.format("%02d", sample)
					 +", Occurrence : "+ String.format("%02d", occurrence)
					 +", Percentage : "+String.format("%.2f", percentage)+"%");
		 }
	}
	public static void main(String[] args){
		DataSetGenerator mG = new DataSetGenerator(true);
		mG.addDataPlan(100, 100, 5, 10 );
		//mG.addDataPlan(200, 300, 20, 15);
		//mG.addDataPlan(100, 200, 2 , 1 );
		while(true){
			int res = mG.getSample();
			//System.out.println(res);
			if(res == -1){break;}
		}
	}
}