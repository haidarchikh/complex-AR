package se.ltu.d7031e;

import se.ltu.d7013e.rabbitMQ.*;
import se.ltu.d7031e.acceFeatures.FeaturesRealTime;

public class Main {
	public static final String EXCHANGE_NAME_ACCELEROMETER = "accelerometer";
	public static final String EXCHANGE_NAME_EVENTS        = "events";
	public static final String ACTIVITY                    = "activity";
	public static final String CONTEXT                     = "context";
	public static final String CLASSIFIER_PATH = "/home/haidar/Desktop/Features/featuresModel.model";
	
	public static String mRabbit_IP = "localhost"; 

	public static void main(String[] args) {
		
		FeaturesRealTime mFeatures = new FeaturesRealTime();
		WekaService      mWeka     = new WekaService(CLASSIFIER_PATH, ACTIVITY);
		RabbitMQSend     mSender   = new RabbitMQSend   (mRabbit_IP, EXCHANGE_NAME_EVENTS);
		RabbitMQReceive  mReceiver = new RabbitMQReceive(mRabbit_IP, EXCHANGE_NAME_ACCELEROMETER);
		
		mFeatures.setRunning(true);
		mWeka    .setRunning(true);
		mSender  .setRunning(true);
		mReceiver.setRunning(true);
		
		mFeatures.setmInQ(mReceiver.getmOutQ());
		mWeka    .setmInQ(mFeatures.getmOutQ());
		mSender  .setmInQ(mWeka    .getmOutQ());
		
		mFeatures.start();
		mWeka    .start();
		mSender  .start();
		mReceiver.start();
		}	  
	}