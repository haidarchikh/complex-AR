package se.ltu.d7013e.rabbitMQ;

import java.util.concurrent.BlockingQueue;

import org.json.JSONObject;

public class test {

	public static void main(String[] args) {
		RabbitMQReceive mReceive = new RabbitMQReceive(Consts.LOCALHOST ,Consts.EXCHANGE_NAME_ACCELEROMETER);
		mReceive.setRunning(true);
		BlockingQueue<JSONObject> mQ = mReceive.getmOutQ();
		mReceive.start();
		while(true){
			try {
				JSONObject mJSON = mQ.take();
				System.out.println(mJSON.get("Waist"));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
