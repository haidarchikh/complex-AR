package se.ltu.d7031e;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.json.JSONObject;

public class RawToArff extends Thread{
	private static final String COMMA            = ",";
	private static final String QuestionMark     = "?";
	private static final String RELATION         = "@RELATION Accelerometer\n";
	private static final String TIMESTAMP        = "@ATTRIBUTE Timestamp NUMERIC\n";
	private static final String accX             = "@ATTRIBUTE accX NUMERIC\n";
	private static final String accY             = "@ATTRIBUTE accY NUMERIC\n";
	private static final String accZ             = "@ATTRIBUTE accZ NUMERIC\n";
	private static final String accTotal         = "@ATTRIBUTE accTotal NUMERIC\n";
	private static final String LABEL            = "@ATTRIBUTE label {lying,running,sitting,standing,walking}\n";
	private static final String DATA             = "@DATA\n";
	private static final String NEWLINE          = "\n";
	
	private static final String HEADER = RELATION + NEWLINE + TIMESTAMP + accX + accY + accZ + accTotal +
			LABEL + NEWLINE + DATA + NEWLINE;
	
	private BlockingQueue<JSONObject> mInQ;
	private BlockingQueue<JSONObject> mOutQ = new ArrayBlockingQueue<>(100);
	private boolean running = false;
	private String mSensorName;
	private StringBuilder mBuilder = new StringBuilder();
	private int count = 0;
	private long mNumberOfRecordsInFile;
	public void run(){
		while(running){
			JSONObject mJSON;
			try {
				mJSON = mInQ.take();
				
				if(count == 0){
					mBuilder.append(HEADER);
				}
				if(count < mNumberOfRecordsInFile){
					String line = ProcessLine(mJSON);
					mBuilder.append(line);
					count++;
				}  
				if(count == mNumberOfRecordsInFile){
					mOutQ.add(new JSONObject().put(mSensorName, mBuilder.toString()));
					mBuilder = new StringBuilder();
					count = 0;
				}
			} catch (InterruptedException e) {
				System.out.println("-----------------INTERRUP-------------------");} 
		}
	}
	private String ProcessLine(JSONObject mJSON){
		long   timestamp = mJSON.getLong("timestamp");
	    double accX = mJSON.getDouble("x");
	    double accY = mJSON.getDouble("y");
	    double accZ = mJSON.getDouble("z");
	    double accTotal = Math.sqrt(accX * accX + accY * accY + accZ * accZ);
	    StringBuilder mLine = new StringBuilder();
	    
	    if(mJSON.has("label")){
	    	String label = mJSON.getString("label");
	    	/* 
			 * 900426296 ,-1.236,9.297,3.785,10.113759439496276,sitting
			 * [timestamp,accX  ,accY ,accZ ,accToral          ,lable   ]			
			 * */
	    	mLine.append(timestamp)
				.append(COMMA)
				.append(accX)
				.append(COMMA)
				.append(accY)
				.append(COMMA)
				.append(accZ)
				.append(COMMA)
				.append(accTotal)
				.append(COMMA)
				.append(label)
				.append(NEWLINE);			
		} else {
			/*
			 * 900426296 ,-1.236,9.297,3.785,10.113759439496276,?
			 * [timestamp,accX  ,accY ,accZ ,accToral          ,lable]
			 * */
			mLine.append(timestamp)
				.append(COMMA)
				.append(accX)
				.append(COMMA)
				.append(accY)
				.append(COMMA)
				.append(accZ)
				.append(COMMA)
				.append(accTotal)
				.append(COMMA)
				.append(QuestionMark)
				.append(NEWLINE);
		}
		return mLine.toString();
	}
	public RawToArff(String mSensorName , long mNumberOfRecordsInFile){
		this.mSensorName = mSensorName;
		this.mNumberOfRecordsInFile = mNumberOfRecordsInFile;
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