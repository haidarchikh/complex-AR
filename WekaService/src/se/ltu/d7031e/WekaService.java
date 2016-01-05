package se.ltu.d7031e;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.json.JSONObject;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.NominalPrediction;
import weka.core.FastVector;
import weka.core.Instances;
import weka.core.converters.ArffLoader.ArffReader;


public class WekaService extends Thread{

	private String mClassifirePath;
	private String mResultKey;
	private BlockingQueue<JSONObject> mInQ;
	private BlockingQueue<JSONObject> mOutQ = new ArrayBlockingQueue<>(100);
	private boolean running = false;
	private Classifier mClassifier;
	private int totalInstances;
	@Override
	public void run() {
		GetClassifier();
	    totalInstances = 0;
		while(running){
			JSONObject mJSON;
			try {
				mJSON = mInQ.take();
				
				String mName = JSONObject.getNames(mJSON)[0];
				String stringData = mJSON.getString(mName);
				
				StringReader reader = new StringReader(stringData);
				ArffReader arff = new ArffReader(reader);
				Instances data = arff.getData();
				data.setClassIndex(data.numAttributes()-1);
				int numInstances = data.numInstances();
				//System.out.println(numInstances);
				for(int i = 0 ; i < numInstances ; i++){
					totalInstances++;
					double value=mClassifier.classifyInstance(data.instance(i));
		            String prediction = data.classAttribute().value((int)value); 
		            
		            mOutQ.put(new JSONObject().put(mResultKey, prediction));
		            
		            //PrintPredeiction(prediction);
				}
			} catch (InterruptedException e) {
				System.out.println("-----------------INTERRUP-------------------");} 
			catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}
	}
	private void GetClassifier(){
		mClassifier = null;
	    try {
	    	mClassifier = (Classifier) weka.core.SerializationHelper.read(mClassifirePath);
	    } catch (Exception e1) {
	        e1.printStackTrace();
	    }
	}
	public WekaService(String mClassifirePath ,String mResultKey){
		this.mClassifirePath = mClassifirePath;
		this.mResultKey      = mResultKey;
	}
	@SuppressWarnings("unused")
	private void PrintPredeiction(String prediction){
		System.out.println("The predicted value of instance "+
                Integer.toString(totalInstances)+
                ": " + prediction);
	}
	public static BufferedReader readDataFile(String filename) {
		BufferedReader inputReader = null;
		try {
			inputReader = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException ex) {
			System.err.println("File not found: " + filename);
		}
		return inputReader;
	}
	public static Evaluation classify(Classifier model,
			Instances trainingSet, Instances testingSet) throws Exception {
		
		Evaluation evaluation = new Evaluation(trainingSet);
		model.buildClassifier(trainingSet);
		evaluation.evaluateModel(model, testingSet);
		return evaluation;
	}
	public static double calculateAccuracy(FastVector predictions) {
		double correct = 0;
		for (int i = 0; i < predictions.size(); i++) {
			NominalPrediction np = (NominalPrediction) predictions.elementAt(i);
			if (np.predicted() == np.actual()) {
				correct++;
			}
		}
		return 100 * correct / predictions.size();
	}
	public static Instances[][] crossValidationSplit(Instances data, int numberOfFolds) {
		Instances[][] split = new Instances[2][numberOfFolds];
 
		for (int i = 0; i < numberOfFolds; i++) {
			split[0][i] = data.trainCV(numberOfFolds, i);
			split[1][i] = data.testCV(numberOfFolds, i);
		}
		return split;
	}
	public void setRunning(boolean running){
		this.running = running;
		if(!running){this.interrupt();}
	}
	public BlockingQueue<JSONObject> getmOutQ() {
		return mOutQ;
	}
	public void setmInQ(BlockingQueue<JSONObject> mInQ) {
		this.mInQ = mInQ;
	}
}