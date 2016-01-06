package se.ltu.d7031e;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.json.JSONObject;

/**
 * This is a helper class used to transform accelerometer raw data to 
 * Arff format. Gets an input as a Input queue, transforms to Arff and puts
 * the output to an output queue. The class take an Integer argument to specify the number
 * of instance in each output file.
 * <p><b><code>RawToArff mRawToArff = new RawToArff(numberOfInstances);</code></b>
 * <p><b>Input example :</b><br>
 * <p> {"timestamp":"1167163271","position":"waist","label":"?","z":"10.238","y":"-0.157","x":"0.118"}<br>
 * {"timestamp":"1167163279","position":"waist","label":"?","z":"10.199","y":"-0.157","x":"0.098"}<br>
 * {"timestamp":"1167163288","position":"waist","label":"?","z":"10.199","y":"-0.157","x":"0.098"}<br>
 * </p>
 * <p><b>Output example :</b><br>
 * <p>
 * {@literal @}RELATION waist<br>
 * {@literal @}ATTRIBUTE Timestamp NUMERIC<br>
 * {@literal @}ATTRIBUTE accX NUMERIC<br>
 * {@literal @}ATTRIBUTE accY NUMERIC<br>
 * {@literal @}ATTRIBUTE accZ NUMERIC<br>
 * {@literal @}ATTRIBUTE accTotal NUMERIC<br>
 * {@literal @}ATTRIBUTE label {lying,running,sitting,standing,walking}<br>
 * <br>
 * {@literal @}DATA<br>
 * <br>
 * 1167388110,0.137,-0.196,10.199,10.201803075927314,?<br>
 * 1167388118,0.137,-0.177,10.219,10.22145092440403,?<br>
 * 1167388127,0.157,-0.196,10.199,10.20209125620821,?</p><br>
 * */

public class RawToArff extends Thread{
	
	private static final String RELATION   = "@RELATION ";
	private static final String TIMESTAMP  = "@ATTRIBUTE Timestamp NUMERIC\n";
	private static final String accX       = "@ATTRIBUTE accX NUMERIC\n";
	private static final String accY       = "@ATTRIBUTE accY NUMERIC\n";
	private static final String accZ       = "@ATTRIBUTE accZ NUMERIC\n";
	private static final String accTotal   = "@ATTRIBUTE accTotal NUMERIC\n";
	private static final String LABEL      = "@ATTRIBUTE label {lying,running,sitting,standing,walking}\n";
	private static final String DATA       = "@DATA\n";
	private static final String NEWLINE    = "\n";
	private static final String COMMA      = ",";
	private static final String mTIMESTAMP = "timestamp";
	private static final String mPOSITION  = "position";
	private static final String mLABEL     = "label";
	private static final String X          = "x";
	private static final String Y          = "y";
	private static final String Z          = "z";
	
	private BlockingQueue<JSONObject> mInQ;
	private BlockingQueue<JSONObject> mOutQ = new ArrayBlockingQueue<>(100);
	private boolean running = false;
	private StringBuilder mBuilder = new StringBuilder();
	private String mSensorPosition;
	private int mCount = 0;
	private long mNumberOfRecordsInFile;
	public void run(){
		while(running){
			JSONObject mJSON;
			try {
				mJSON = mInQ.take();
				if(mCount == 0){
					mBuilder.append(GetHeader(mJSON));
				}
				if(mCount < mNumberOfRecordsInFile){
					String line = ProcessLine(mJSON);
					mBuilder.append(line);
					mCount++;
				}  
				if(mCount == mNumberOfRecordsInFile){
					mSensorPosition = mJSON.getString(mPOSITION);
					mOutQ.add(new JSONObject().put(mSensorPosition , mBuilder.toString()));
					mBuilder = new StringBuilder();
					mCount = 0;
				}
			} catch (InterruptedException e) {
				System.out.println("-----------------INTERRUP-------------------");} 
		}
	}
	private String ProcessLine(JSONObject mJSON){
		
		long timestamp  = mJSON.getLong(mTIMESTAMP);
	    double accX     = mJSON.getDouble(X);
	    double accY     = mJSON.getDouble(Y);
	    double accZ     = mJSON.getDouble(Z);
	    String label    = mJSON.getString(mLABEL);
	    double accTotal = Math.sqrt(accX * accX + accY * accY + accZ * accZ);
	    
	    StringBuilder mLine = new StringBuilder();
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
	return mLine.toString();
	}
	private String GetHeader(JSONObject mJSON){
		String mRelattion = RELATION + mJSON.getString(mPOSITION) + NEWLINE;
		StringBuilder mHeader = new StringBuilder();
		mHeader.append(mRelattion)
		       .append(NEWLINE)
		       .append(TIMESTAMP)
		       .append(accX)
		       .append(accY)
		       .append(accZ)
		       .append(accTotal)
		       .append(LABEL)
		       .append(NEWLINE)
		       .append(DATA)
		       .append(NEWLINE);
	return mHeader.toString();
	}
	public RawToArff(long mNumberOfRecordsInFile){
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