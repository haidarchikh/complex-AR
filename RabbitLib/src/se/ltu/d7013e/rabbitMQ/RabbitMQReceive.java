package se.ltu.d7013e.rabbitMQ;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

import org.json.JSONObject;

import com.rabbitmq.client.*;

public class RabbitMQReceive extends Thread{
	
	private String mHostName;
	private String mExchangeName;
	private String mRoutingKey;
	private boolean running = false;
	private ConnectionFactory mFactory;
	private Connection mConnection;
	private Channel mChannel;
	private String mRabbitQueue;
	private BlockingQueue<JSONObject> mOutQ      = new ArrayBlockingQueue<>(100);  
	private BlockingQueue<JSONObject> mInternalQ = new ArrayBlockingQueue<>(100);
	@Override
	public void run(){
		Connect();
		ReciveMessage();
		while(running){
			JSONObject mJSON = new JSONObject();
			try {
				mJSON = mInternalQ.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			mOutQ.add(mJSON);
		}
		Disconnect();
	}
	public void setRunning(boolean running){
		this.running = running;
		if(!running){this.interrupt();}
	}
	private void Connect(){
		mFactory = new ConnectionFactory();
	    mFactory.setHost(mHostName);
	    try {
			mConnection  = mFactory.newConnection();
		    mChannel     = mConnection.createChannel();
		    mRabbitQueue = mChannel.queueDeclare().getQueue();
		    mChannel.exchangeDeclare(mExchangeName , "direct", false);
		    mChannel.queueBind(mRabbitQueue , mExchangeName , mRoutingKey);
		} catch (IOException | TimeoutException e) {
			System.out.println("Error while connecting to RabbitMQ" +e.getCause());
			e.printStackTrace();
		}
	    System.out.println(" [*] Waiting for events.");
	}
	private void ReciveMessage() {
	    Consumer consumer = new DefaultConsumer(mChannel) {
	      @Override
	      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
	          throws IOException {
	        String message = new String(body, "UTF-8");
	        JSONObject mJSON = new JSONObject(message);
	        mInternalQ.add(mJSON);
	      }
	    };
	    try {
			mChannel.basicConsume(mRabbitQueue, true , consumer);
		} catch (IOException e) {
			System.out.println("Error while binding to a queue"+ e.getCause());
			e.printStackTrace();
		}
	}
	private void Disconnect(){
	    try {
			mChannel.close();
			mConnection.close();
		} catch (IOException | TimeoutException e) {
			System.out.println("Error while disconnecting from RabbitMQ");
			e.printStackTrace();
		}
	}
	public RabbitMQReceive(String mHostName , String mExchange){
		this(mHostName , mExchange, "");
	}
	public RabbitMQReceive(String mHostName, String mExchangeName, String mRoutingKey){
		this.mHostName     = mHostName;
		this.mExchangeName = mExchangeName;
		this.mRoutingKey   = mRoutingKey;
	}
	public BlockingQueue<JSONObject> getmOutQ() {
		return mOutQ;
	}
}