package se.ltu.d7013e.rabbitMQ;

public class Main {

	public static void main(String[] args) {	
		
		Mockup mMockup     = new Mockup();
		RabbitMQSend mSend = new RabbitMQSend(Consts.LOCALHOST, Consts.EXCHANGE_NAME_EVENTS);
		
		mMockup.setRunning(true);
		mSend  .setRunning(true);
		
		mSend.setmInQ(mMockup.getmOutQ());
		
		mSend   .start();
		mMockup .start();
	}
}
