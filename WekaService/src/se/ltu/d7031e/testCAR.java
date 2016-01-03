package se.ltu.d7031e;

import se.ltu.d7013e.rabbitMQ.RabbitMQSend;

public class testCAR {
	
	public static String mRabbit_IP = Consts.LOCALHOST ;
	
	public static void main(String[] args) {	
		
		Mockup mMockup     = new Mockup();
		RabbitMQSend mSend = new RabbitMQSend(mRabbit_IP, Consts.EXCHANGE_NAME_EVENTS);
		
		mMockup.setRunning(true);
		mSend  .setRunning(true);
		
		mSend.setmInQ(mMockup.getmOutQ());
		
		mSend   .start();
		mMockup .start();
	}
	
}
