package se.ltu.d7031e.CAR;
import se.ltu.d7013e.rabbitMQ.*;

public class Main {
	
	public static String mRabbit_IP = Consts.LOCALHOST;
	
	public static void main(String[] args) {

		RabbitMQReceive mRecv = new RabbitMQReceive(mRabbit_IP, Consts.EXCHANGE_NAME_EVENTS);
		CAR mAlgorithm = new CAR();
		
		mAlgorithm.setRunning(true);
		mRecv     .setRunning(true);

		mAlgorithm.setmInQ(mRecv.getmOutQ());
		
		mAlgorithm.start();
		mRecv     .start();
	}
}