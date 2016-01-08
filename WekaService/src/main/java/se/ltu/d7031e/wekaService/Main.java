package se.ltu.d7031e.wekaService;

import se.ltu.d7031e.rabbitMQ.*;
import se.ltu.d7031e.acceFeatures.FeaturesRealTime;

public class Main {
	
	public static String mRabbit_IP = Consts.LOCALHOST ; 
	public static final int windowSize  = 64;
	public static final int windowShift = 16;
	
	public static void main(String[] args) {
		
		if(args.length != 0){
			mRabbit_IP = args[0];
			RawToArff        mRawToArff = new RawToArff(64);
			FeaturesRealTime mFeatures  = new FeaturesRealTime(windowSize , windowShift);
			WekaService      mWeka      = new WekaService    (Consts.CLASSIFIER_PATH_DOCKER, Consts.ACTIVITY);
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
			mSender   .start();
			mReceiver .start();
		}
		else{System.out.println("Please enter rabbitMQ broker IP");}
//		/*
		RawToArff        mRawToArff = new RawToArff(64);
		FeaturesRealTime mFeatures  = new FeaturesRealTime(windowSize , windowShift);
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
		mSender   .start();
		mReceiver .start();
	//	*/
		}
	}
