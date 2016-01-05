package se.ltu.d7031e.test;

public class WekaExtra {
	 /*
		public static void main(String[] args) throws Exception {
			BufferedReader datafile = readDataFile("/home/haidar/Desktop/Training/0010/walkingandstandingsittingnotime.arff");
	 
			Instances data = new Instances(datafile);
			data.setClassIndex(data.numAttributes() - 1);
	 
			// Do 10-split cross validation
			Instances[][] split = crossValidationSplit(data, 10);
	 
			// Separate split into training and testing arrays
			Instances[] trainingSplits = split[0];
			Instances[] testingSplits = split[1];
	 
			// Use a set of classifiers
			Classifier[] models = { 
					new J48(), // a decision tree
					new PART(), 
					new DecisionTable(),//decision table majority classifier
					new DecisionStump() //one-level decision tree
			};
	 
			// Run for each model
			for (int j = 0; j < models.length; j++) {
	 
				// Collect every group of predictions for current model in a FastVector
				FastVector predictions = new FastVector();
	 
				// For each training-testing split pair, train and test the classifier
				for (int i = 0; i < trainingSplits.length; i++) {
					Evaluation validation = classify(models[j], trainingSplits[i], testingSplits[i]);
	 
					predictions.appendElements(validation.predictions());
	 
					// Uncomment to see the summary for each training-testing pair.
					System.out.println(models[j].toString());
				}
	 
				// Calculate overall accuracy of current classifier on all splits
				double accuracy = calculateAccuracy(predictions);
	 
				// Print current classifier's name and accuracy in a complicated,
				// but nice-looking way.
				System.out.println("Accuracy of " + models[j].getClass().getSimpleName() + ": "
						+ String.format("%.2f%%", accuracy)
						+ "\n---------------------------------");
			}
		}
	*/
}
