package se.ltu.d7031e.acceFeatures;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.json.JSONObject;

import se.ltu.d7031e.acceFeatures.Complex;
import se.ltu.d7031e.acceFeatures.FFT;


public class FeaturesRealTime extends Thread {
	//GLOBAL VARS CHANGE HERE
	public static final String outDir = "/home/haidar/Desktop/Features/";
	public static final String inDir  = "/home/haidar/Desktop/Features/";
	
	// Files names in the inDir <LowerBack.arff>
	private String mName;
	
	public static final int windowSize  = 32;
	public static final int windowShift = 16;
	//GLOBAL VARS END	

	private boolean running = false;
	private BlockingQueue<JSONObject> mInQ;
	private BlockingQueue<JSONObject> mOutQ;
	@Override
	public void run(){
		try{
			while(running){
				
				JSONObject mJSON = mInQ.take();
				mName = JSONObject.getNames(mJSON)[0];
				
				String mFeatureData = GetFeatures(mJSON.getString(mName));

 				mOutQ.put(new JSONObject().put(mName, mFeatureData));
			}
		}catch(InterruptedException e){e.printStackTrace();}
	}
	
	public void setRunning(boolean running){
		this.running = running;
	}
	public FeaturesRealTime(){
		mInQ  = new ArrayBlockingQueue<>(100);
		mOutQ = new ArrayBlockingQueue<>(100);
	}
	private String GetFeatures(String mRawData){
		String mResult="";
		try{
			Vector<String> bannedNames = new Vector<String>();
			bannedNames.add("Timestamp");
			
				StringReader reader   = new StringReader(mRawData);
				StringWriter outFile  = new StringWriter();
				BufferedReader inFile = new BufferedReader(reader);
			
				//Reading until data lines
				Vector<String> attrs = new Vector<String>();
				Vector<Integer> corresp = new Vector<Integer>();
				String str = inFile.readLine();

				@SuppressWarnings("unused")
				int startLineNum=0;
				int attrNum = 0;
				String labelString = "";
				while(!"@DATA".equals(str)){
					if (str.contains("ATTRIBUTE")){
						if (str.contains("NUMERIC")){
							str = str.replaceAll("@ATTRIBUTE","");
							str = str.replaceAll("NUMERIC","");
							str = str.trim();
							if (!bannedNames.contains(str)){
								attrs.add(str);
								corresp.add(new Integer(attrNum));
							}
						}else if(str.contains("{")&&str.contains("}")){
							labelString = str;
						}
						attrNum++;
					}					
					str = inFile.readLine();
					startLineNum++;
				};
				//System.out.println(startLineNum);
				
				outFile.write("@RELATION "+mName+"Features\n\n");				
				outFile.write("@ATTRIBUTE Timeframe NUMERIC\n");
				for (int i=0;i<attrs.size();i++){
					outFile.write("@ATTRIBUTE mean"+attrs.elementAt(i)+" NUMERIC\n");
				}
				for (int i=0;i<attrs.size();i++){
					outFile.write("@ATTRIBUTE stdev"+attrs.elementAt(i)+" NUMERIC\n");
				}
				for (int i=0;i<attrs.size();i++){
					outFile.write("@ATTRIBUTE skew"+attrs.elementAt(i)+" NUMERIC\n");
				}
				for (int i=0;i<attrs.size();i++){
					outFile.write("@ATTRIBUTE curt"+attrs.elementAt(i)+" NUMERIC\n");
				}
				for (int i=0;i<attrs.size();i++){
					outFile.write("@ATTRIBUTE energy"+attrs.elementAt(i)+" NUMERIC\n");
				}
				for (int i=0;i<attrs.size()-1;i++){
					for (int j=i+1;j<attrs.size();j++){
						outFile.write("@ATTRIBUTE cor"+attrs.elementAt(i)+attrs.elementAt(j)+" NUMERIC\n");	
					}
				}				
				//TODO Add features
				outFile.write(labelString+"\n");
				outFile.write("\n@DATA\n");
				outFile.flush();
				
				String allLines[] = new String[windowSize];
				allLines = readWindow(allLines, inFile, true, windowShift);
				while (allLines!=null){
					//Calc timeframe
					double startTime = Double.parseDouble(allLines[0].split(",")[0]);
					double endTime = Double.parseDouble(allLines[allLines.length-1].split(",")[0]);
					double timeFrame = endTime-startTime;
					outFile.write(""+timeFrame);
					
					//Calc averages
					double attrAvg[] = new double[attrs.size()];
					for(int i=0;i<attrs.size();i++){
						int numSplit = corresp.elementAt(i);
						attrAvg[i] = 0;
						for (int j=0;j<allLines.length;j++){
							attrAvg[i] += Double.parseDouble(allLines[j].split(",")[numSplit]);
						}
						attrAvg[i] = attrAvg[i]/allLines.length;
						outFile.write(","+attrAvg[i]);
					}
					
					//Calc stdevs
					double stdev[] = new double[attrs.size()];
					for(int i=0;i<attrs.size();i++){
						int numSplit = corresp.elementAt(i);
						stdev[i] = 0;
						for (int j=0;j<allLines.length;j++){
							stdev[i] += 
								(Double.parseDouble(allLines[j].split(",")[numSplit]) - attrAvg[i])*
								(Double.parseDouble(allLines[j].split(",")[numSplit]) - attrAvg[i]);
						}
						stdev[i] = Math.sqrt(stdev[i]/allLines.length);
						outFile.write(","+stdev[i]);						
					}
					
					//3rd central momentum (need for skewness)
					double thirdCentralMoment[] = new double[attrs.size()];
					double fourthCentralMoment[] = new double[attrs.size()];
					for(int i=0;i<attrs.size();i++){
						int numSplit = corresp.elementAt(i);
						thirdCentralMoment[i] = 0;
						fourthCentralMoment[i] = 0;
						for (int j=0;j<allLines.length;j++){
							thirdCentralMoment[i] += 
								(Double.parseDouble(allLines[j].split(",")[numSplit]) - attrAvg[i])*
								(Double.parseDouble(allLines[j].split(",")[numSplit]) - attrAvg[i])*
								(Double.parseDouble(allLines[j].split(",")[numSplit]) - attrAvg[i]);
							fourthCentralMoment[i] +=
									(Double.parseDouble(allLines[j].split(",")[numSplit]) - attrAvg[i])*
									(Double.parseDouble(allLines[j].split(",")[numSplit]) - attrAvg[i])*
									(Double.parseDouble(allLines[j].split(",")[numSplit]) - attrAvg[i])*
									(Double.parseDouble(allLines[j].split(",")[numSplit]) - attrAvg[i]);
						}
						thirdCentralMoment[i] = thirdCentralMoment[i]/allLines.length;
						fourthCentralMoment[i] = fourthCentralMoment[i]/allLines.length;
					}
					
					//Skewness
					double skew[] = new double[attrs.size()];
					for(int i=0;i<attrs.size();i++){
						skew[i] = thirdCentralMoment[i]/(stdev[i]*stdev[i]*stdev[i]);
						outFile.write(","+skew[i]);						
					}					
					
					//Curtosis
					double curtosis[] = new double[attrs.size()];
					for(int i=0;i<attrs.size();i++){
						curtosis[i] = 
						  (fourthCentralMoment[i]/(stdev[i]*stdev[i]*stdev[i]*stdev[i])) - 3;						
						outFile.write(","+curtosis[i]);						
					}
					
					//Energy
					double energy[] = new double[attrs.size()];
					Complex complexVal[][] = new Complex[attrs.size()][allLines.length];
					Complex transformResults[][] = new Complex[attrs.size()][];					
					for(int i=0;i<attrs.size();i++){
						int numSplit = corresp.elementAt(i);						
						for (int j=0;j<allLines.length;j++){
							complexVal[i][j] = 
								new Complex(Double.parseDouble(allLines[j].split(",")[numSplit]),0); 
						}
						transformResults[i] = FFT.fft(complexVal[i]);
						double res = 0;
						for (int j=0;j<transformResults.length;j++){
							res = res+(transformResults[i][j].abs()*transformResults[i][j].abs());
						}
						energy[i] = res/transformResults[i].length;
						outFile.write(","+energy[i]);						
					}
					
					//Correlations
					for (int i=0;i<attrs.size()-1;i++){
						for (int j=i+1;j<attrs.size();j++){
							double mulExp = 0;
							int numSpliti = corresp.elementAt(i);
							int numSplitj = corresp.elementAt(j);
							for (int k=0;k<allLines.length;k++){
								mulExp += 
									(Double.parseDouble(allLines[k].split(",")[numSpliti])-attrAvg[i])*
									(Double.parseDouble(allLines[k].split(",")[numSplitj])-attrAvg[j]);
							}
							mulExp = mulExp/allLines.length;
							mulExp = mulExp/stdev[i];
							mulExp = mulExp/stdev[j];
							outFile.write(","+mulExp);
						}
					}				

					
					outFile.write(","+allLines[0].split(",")[allLines[0].split(",").length-1]+"\n");
					outFile.flush();					
					allLines = readWindow(allLines, inFile, false, windowShift);
				}
				outFile.flush();
				mResult = outFile.toString();
				outFile.close();
		}catch(Throwable e){
			e.printStackTrace();
		}
		return mResult;
	}
	public static String[] readWindow
			(String[] oldWindow, BufferedReader inFile,
			 boolean firstWindow, int shift) throws IOException{

		String res[] = new String[windowSize];
		int ctr = 0;
		
		String oldLabel = null;
		if (!firstWindow){
			while(ctr+shift < oldWindow.length){
				res[ctr] = oldWindow[ctr+shift];
				ctr++;
			}
			String split[] = res[0].split(",");
			oldLabel = split[split.length-1];			
		}
		
		//Check whether we have one more window
		boolean windowBroken = false;
		boolean changedLabel = false;
		String newLabelLine[] = new String[1]; 
		while ((ctr<windowSize)&&(!windowBroken)){
			res[ctr] = inFile.readLine();
			if (null==res[ctr]){
				if (ctr<windowSize-1){
				  windowBroken = true;
				}
			}else{
				String split[] = res[ctr].split(",");
				String label = split[split.length-1];
				if (oldLabel==null){
					oldLabel = label;
				}else if(!oldLabel.equals(label)){
					changedLabel = true;
					windowBroken = true;
					newLabelLine[0] = res[ctr];
				}
			}
			ctr++;
		}
		
		if (changedLabel){
			return readWindow(newLabelLine, inFile, false,0);
		}else if (windowBroken){
			return null;
		}else{
			return res;
		}
	}
	public BlockingQueue<JSONObject> getmInQ() {
		return mInQ;
	}
	public void setmInQ(BlockingQueue<JSONObject> mInQ) {
		this.mInQ = mInQ;
	}
	public BlockingQueue<JSONObject> getmOutQ() {
		return mOutQ;
	}
	public void setmOutQ(BlockingQueue<JSONObject> mOutQ) {
		this.mOutQ = mOutQ;
	}
}
