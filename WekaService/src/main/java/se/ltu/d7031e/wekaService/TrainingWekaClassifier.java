package se.ltu.d7031e.wekaService;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import org.json.JSONObject;

import se.ltu.d7031e.rabbitMQ.RabbitMQReceive;

public class TrainingWekaClassifier extends Thread{
	public static final String outDir = "/home/haidar/Desktop/newTraining";
	public static String mRabbit_IP = Consts.LOCALHOST ;
	
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
	
	private BufferedWriter mWriter;
	private boolean running = false;
	private int mCount = 0;
	private long mNumberOfRecordsInFile;
	public void run(){
		while(running){
			JSONObject mJSON;
			try {
				mJSON = mInQ.take();
				if(mCount == 0){
					mWriter.append(GetHeader(mJSON));
				}
				if(mCount < mNumberOfRecordsInFile){
					String line = ProcessLine(mJSON);
					mWriter.append(line);
					mCount++;
					System.out.print("\r"+mCount);
				}
				if(mCount%1000 == 0){
					mWriter.flush();
				}
			} catch (InterruptedException e) {
				System.out.println("-----------------INTERRUP-------------------");
				} catch (IOException e) {
				e.printStackTrace();
			} 
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
	public TrainingWekaClassifier(){
		this.mNumberOfRecordsInFile = 100000000000L;
		String time = String.valueOf(System.currentTimeMillis());
		try {
			mWriter = new BufferedWriter(new FileWriter(outDir+time+".arff"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void setRunning(boolean running){
		this.running = running;
		if(!running){this.interrupt();}
	}
	public void setmInQ(BlockingQueue<JSONObject> mInQ) {
		this.mInQ = mInQ;
	}
	public static void main(String args[]){
		RabbitMQReceive  mReceiver  = new RabbitMQReceive(mRabbit_IP, Consts.EXCHANGE_NAME_ACCELEROMETER);
		TrainingWekaClassifier mT = new TrainingWekaClassifier();
		
		mT.setmInQ(mReceiver.getmOutQ());
		mReceiver.setRunning(true);
		mT.setRunning(true);
		mReceiver.start();
		mT.start();
	}
}
