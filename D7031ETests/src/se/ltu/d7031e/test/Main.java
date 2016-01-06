package se.ltu.d7031e.test;

import java.util.concurrent.BlockingQueue;

import org.json.JSONObject;

import se.ltu.d7013e.rabbitMQ.RabbitMQReceive;
import se.ltu.d7013e.rabbitMQ.RabbitMQSend;
import se.ltu.d7031e.Consts;
import se.ltu.d7031e.RawToArff;
import se.ltu.d7031e.WekaService;
import se.ltu.d7031e.CAR.CAR;
import se.ltu.d7031e.acceFeatures.FeaturesRealTime;

public class Main {
	public static final int windowSize  = 16;
	public static final int windowShift = 8;
	
	public static String mRabbit_IP = Consts.LOCALHOST ;
	public static BlockingQueue<JSONObject> mQ;
	
	public static void main(String[] args) {
		
		RabbitMQReceive  mRecvAcce  = new RabbitMQReceive(mRabbit_IP, Consts.EXCHANGE_NAME_ACCELEROMETER);
		RawToArff        mRawToArff = new RawToArff(32);
		FeaturesRealTime mFeatures  = new FeaturesRealTime(windowSize, windowShift);
		WekaService      mWeka      = new WekaService    (Consts.CLASSIFIER_PATH, Consts.ACTIVITY);
		RabbitMQSend     mSender    = new RabbitMQSend   (mRabbit_IP, Consts.EXCHANGE_NAME_EVENTS);
		RabbitMQReceive  mRecvEvent = new RabbitMQReceive(mRabbit_IP, Consts.EXCHANGE_NAME_EVENTS);
		CAR              mAlgorithm = new CAR();
		
		mRecvAcce .setRunning(true);
		mRawToArff.setRunning(true);
		mFeatures .setRunning(true);
		mWeka     .setRunning(true);
		mSender   .setRunning(true);
		mRecvEvent.setRunning(true);
		mAlgorithm.setRunning(true);
		
		
		mRawToArff.setmInQ(mRecvAcce .getmOutQ());
		mFeatures .setmInQ(mRawToArff.getmOutQ());
		mWeka     .setmInQ(mFeatures .getmOutQ());
		mSender   .setmInQ(mWeka     .getmOutQ());
		
		mAlgorithm.setmInQ(mRecvEvent.getmOutQ());
		
		mAlgorithm.start();
		mRecvEvent.start();
		mSender   .start();
		mWeka     .start();
		//mFeatures .start();
		mRawToArff.start();
		mRecvAcce .start();
		
		mQ = mRawToArff.getmOutQ();
		
		
		
		boolean x = false;
		while(true){
			try {
				JSONObject mJSON = mQ.take();
				System.out.println(mJSON.get("waist").toString());				
				/*
				mSamplingRate++;
                mReceivedRecords++;
                if(System.currentTimeMillis() > mStartTime + 1000 ){
                    mStartTime = System.currentTimeMillis();
                   System.out.println("Receiving rate : "  + mSamplingRate);
                   System.out.println("Received records : "+ mReceivedRecords);
                   System.out.println(mJSON.toString());
                   double mString = mJSON.getDouble("x");
                   System.out.println(mString);
                    mSamplingRate = 0 ;
                    }
                  */  
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
