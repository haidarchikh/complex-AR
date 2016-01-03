package se.ltu.d7031e;

import java.util.concurrent.BlockingQueue;

import org.json.JSONObject;

import se.ltu.d7013e.rabbitMQ.RabbitMQReceive;
import se.ltu.d7013e.rabbitMQ.RabbitMQSend;
import se.ltu.d7031e.acceFeatures.FeaturesRealTime;

public class testAndroid {
	public static final int windowSize  = 512;
	public static final int windowShift = 256;
	
	public static String mRabbit_IP = Consts.LOCALHOST ;
	public static BlockingQueue<JSONObject> mQ;
	
	public static void main(String[] args) {
		
		RawToArff        mRawToArff = new RawToArff("waist", 2048);
		FeaturesRealTime mFeatures  = new FeaturesRealTime(windowSize, windowShift);
		WekaService      mWeka      = new WekaService    (Consts.CLASSIFIER_PATH, Consts.ACTIVITY);
		RabbitMQSend     mSender    = new RabbitMQSend   (mRabbit_IP, Consts.EXCHANGE_NAME_EVENTS);
		RabbitMQReceive  mReceiver  = new RabbitMQReceive(mRabbit_IP, Consts.EXCHANGE_NAME_ACCELEROMETER);
		
		mRawToArff.setRunning(true);
		mFeatures .setRunning(true);
		mWeka     .setRunning(true);
		mSender   .setRunning(true);
		mReceiver .setRunning(true);
		
		mRawToArff.setmInQ(mReceiver .getmOutQ());
		mFeatures .setmInQ(mRawToArff.getmOutQ());
		mWeka     .setmInQ(mFeatures .getmOutQ());
		mSender   .setmInQ(mWeka     .getmOutQ());
		
		
		mRawToArff.start();
		mFeatures .start();
		mWeka     .start();
		//mSender   .start();
		mQ = mWeka.getmOutQ();
		
		
		
		
		mReceiver .start();
		
		
		while(true){
			try {
				JSONObject mJSON = mQ.take();
				System.out.println(mJSON.toString());				
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