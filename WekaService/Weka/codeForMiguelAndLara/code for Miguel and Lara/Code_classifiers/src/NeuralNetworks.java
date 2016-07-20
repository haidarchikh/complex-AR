import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;


public class NeuralNetworks {

	static final String fname = "C:\\Andrey\\Tools\\Weka-3-7\\allResultNN.bat";
	
	static final String cmdStart = "java -cp \"weka.jar\" ";
	
	static String classifier = "weka.classifiers.functions.MultilayerPerceptron";

	static String parameterList[] = {"L","M","N","V","S","E","H"};	
	
	static String parameters[][] = {{"0.3"},{"0.2"},{"500"},{"0"},{"0"},{"20"},
		{"a","10","20","30","40","50","60","70","80","90","100","i","t"}};
	static String prefix = "C:\\Users\\andboy\\Desktop\\ActRec\\";
		
	static String inputSuffix = "FeaturesNoTimestamp.arff";
	
	static String inputnames[] = 
		{"Chest","LowerBack","RightFoot","RightThigh","RightWrist","RightHip"};
	
	/*static String inputnames[] = {"Chest","LowerBack"};*/
	
	static String outputname = 
		"C:\\Users\\andboy\\Desktop\\ActRec\\DecTreeResults.csv";	

	static String logname = 
			"C:\\Users\\andboy\\Desktop\\ActRec\\NNResults.log";	
	
	static final String SEP = ", ";
	static final String PAR = "-";	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try{
			
		    FileWriter fstreamlog = new FileWriter(logname, false);
		    BufferedWriter outlog = new BufferedWriter(fstreamlog);			
			
			//Step 1. Compose the bat file
		    FileWriter fstream = new FileWriter(fname, false);
		    BufferedWriter out = new BufferedWriter(fstream);
		    out.write("cd C:\\Andrey\\Tools\\Weka-3-7\\\n"); //Just in case
		    //Now composing Weka parameters.
		    int totalPars = 1;
		    int multipliers[] = new int[parameterList.length];
		    for (int j=parameterList.length-1;j>=0;j--){
		    	multipliers[j] = totalPars;
		    	totalPars *= parameters[j].length;
			    //log(multipliers[j],outlog);
		    }
		    //log("   "+totalPars,outlog);		    
		    for (int i=0;i<inputnames.length;i++){
		    	String inputString = prefix+inputnames[i]+inputSuffix;
		    	for (int j=0;j<totalPars;j++){
		    		//Generate all possible parameters' strings
			    	String parameterString = " ";
			    	int remainingParNum = j;
			    	for (int k=0;k<parameterList.length;k++){
			    		int parNum = remainingParNum/multipliers[k];
			    		remainingParNum = remainingParNum % multipliers[k];
			    		//log(j+" "+k+" "+parNum+" "+remainingParNum,outlog);			    		
			    		parameterString += PAR+parameterList[k]+" "+
			    					" "+parameters[k][parNum]+" ";
			    	}

		    		//log(parameterString,outlog);			    	
			    	
				    out.write(cmdStart + classifier+parameterString+
				    		" -t "+inputString+"\n");			    	
		    	}
		    }
		    out.flush();
		    out.close();
		    log("Bat file composed",outlog);
			
			//Step 2. Run it.
			
		    double accuracyList[][] = new double[totalPars][inputnames.length];
		    double accuracyAvg[] = new double[totalPars];
		    
			String command = "cmd /c "+fname;			
			Process tr = Runtime.getRuntime().exec(command);
			BufferedReader rd = new BufferedReader( new InputStreamReader( tr.getInputStream() ) );
			String s = rd.readLine();
		    log("Finished running bat.",outlog);			
				
			//Step 3. Process the results
		    FileWriter fstreamres = new FileWriter(outputname, false);
		    BufferedWriter outres = new BufferedWriter(fstreamres);
		    outres.write("Position");
		    for (int k=0;k<parameterList.length;k++){
			    outres.write(SEP+parameterList[k]);
		    }
		    outres.write(SEP+"Value");		    
		    outres.write("\n");		    
			//Writing the header
		    log("Wrote header.",outlog);
		    
		    double bestAccuracy = 0;
		    Vector<String> bestPars = null;
		    
			//Writing the rest
		    for (int i=0;i<inputnames.length;i++){
			    bestPars = new Vector<String>();
			    bestPars.add("None");
			    bestAccuracy = 0;		    	
		    	log("Position: "+inputnames[i],outlog);	    	
		    	for (int j=0;j<totalPars;j++){//For every parameter combination...
			    	String outString = inputnames[i];		    		
			    	String parameterString = " ";
			    	int remainingParNum = j;
			    	for (int k=0;k<parameterList.length;k++){
			    		int parNum = remainingParNum/multipliers[k];
			    		remainingParNum = remainingParNum % multipliers[k];		    		
			    		parameterString += SEP+parameters[k][parNum];
			    	}
				    log("    Parameter string:"+parameterString,outlog);			    	
			    	outString += parameterString;
			    	//Now writing the final value
			    	while ((!s.contains("Correctly Classified Instances"))
			    			&&(s!=null)){
			    		s = rd.readLine();
			    	}
			    	if (s==null){
			    		log("NO CCI! (case 1)",outlog);
			    		throw new Exception("Bad parsing (case 1)");
			    	}
		    		//log("      Found string (training data).",outlog);			    	
		    		s = rd.readLine();				    	
			    	//Skip first - that was for training set
			    	while ((!s.contains("Correctly Classified Instances"))
			    			&&(s!=null)){
			    		s = rd.readLine();
			    	}
			    	if (s==null){
			    		log("NO CCI! (case 2)",outlog);
			    		throw new Exception("Bad parsing (case 2)");
			    	}
		    		//log("      Found string (test data).",outlog);			    	
			    	String[] spl = s.split("\\s");				    	
			    	boolean exitCase = false;
			    	int l = 1;
			    	while (!exitCase){
			     	 try{
					    s = spl[spl.length-l];
			    		//log("      S (before): "+s,outlog);			    		
					    s=s.replaceAll("\\s","");
					    s=s.replaceAll("%","");
			    		//log("      S (after): "+s,outlog);					    
			    		double acc = Double.parseDouble(s);
		    			accuracyList[j][i] = acc;			    		
			    		if (acc >= bestAccuracy){
			    			if (acc > bestAccuracy){
				    			bestPars = new Vector<String>();			    			
				    		}
			    			bestAccuracy = acc;
			    			bestPars.add(parameterString);
			    		}
			    		exitCase = true;
			    	 }catch(Exception e){
			    		//e.printStackTrace();
			    		exitCase = false;
			    	 }
			     	 l++;
			    	}
			    	outString += SEP+s;			    	
					log("    Out string:"+outString,outlog);
					outres.write(outString+"\n");			    	
		    	}
		    	log("Best parameters for "+inputnames[i]+
		    			" ( "+ bestPars.size()+" combination(s) )"+": ",outlog);
		    	for (int m=0;m<bestPars.size();m++){
			    	log("   "+bestPars.elementAt(m),outlog);		    		
		    	}
		    	log("Best accuracy: for "+inputnames[i]+": "+bestAccuracy,outlog);		    	
		    }
		    outres.flush();
		    outres.close();
		    
		    log("\n\nProceeding to best combination of parameters:",outlog);
		    double bestAvgAcc = 0;
		    Vector<Integer> bestParsNum = new Vector<Integer>();
		    Vector<String> bestParsString = new Vector<String>();		    
		    for (int i=0;i<accuracyList.length;i++){
		    	accuracyAvg[i] = 0;
		    	for (int j=0;j<accuracyList[i].length;j++){
			    	accuracyAvg[i] += accuracyList[i][j];
		    	}
		    	accuracyAvg[i] /= accuracyList[i].length;
		    	
		    	String parString = "";
		    	int remainingParNum = i;
		    	for (int k=0;k<parameterList.length;k++){
		    		int parNum = remainingParNum/multipliers[k];
		    		remainingParNum = remainingParNum % multipliers[k];		    		
		    		parString += SEP+parameters[k][parNum];
		    	}		    	
		    	log("Parameters: "+parString,outlog);
		    	log("   Avg accuracy: "+accuracyAvg[i],outlog);
		    	if (accuracyAvg[i]>=bestAvgAcc){
	    			if (accuracyAvg[i]>bestAvgAcc){
		    			bestParsNum = new Vector<Integer>();
		    			bestParsString = new Vector<String>();
		    		}
	    			bestAvgAcc = accuracyAvg[i];
	    			bestParsNum.add(i);
	    			bestParsString.add(parString);
		    	}
		    }

	    	log("Best average parameters "+
	    			" ( "+ bestParsString.size()+" combination(s) )"+": ",outlog);
	    	for (int m=0;m<bestParsString.size();m++){
		    	log("   "+bestParsString.elementAt(m),outlog);		    		
	    	}
	    	log("Best average accuracy: "+bestAvgAcc,outlog);		    

	    	log("Accuracies for parameter combinations: ",outlog);
	    	for (int m=0;m<bestParsString.size();m++){	    		
			    for (int i=0;i<inputnames.length;i++){
			    	log(inputnames[i]+bestParsString.elementAt(m)+SEP+
			    			accuracyList[bestParsNum.elementAt(m)][i],outlog);			    	
			    }	    		
	    	}
		    
		    log("\n\nComplete",outlog);
		    
		    outlog.flush();
		    outlog.close();		    
		}catch(Throwable e){
			e.printStackTrace();
		}
		
	}

	private static void log(String message, BufferedWriter outlog) throws IOException {
	    System.out.println(message);
	    outlog.write(message+"\n");
	    outlog.flush();
	}

}