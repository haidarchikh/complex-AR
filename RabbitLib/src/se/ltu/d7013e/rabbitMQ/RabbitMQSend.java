package se.ltu.d7013e.rabbitMQ;


import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

import org.json.JSONObject;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

public class RabbitMQSend extends Thread{
	
	private String mHostName;
	private String mExchangeName;
	private String mRoutingKey;
	private boolean running = false;
	private ConnectionFactory mFactory;
	private Connection mConnection;
	private Channel mChannel;
	private BlockingQueue<JSONObject> mInQ;
	public void setRunning(boolean running){
		this.running = running;
		if(!running){this.interrupt();}
	}
	@Override
	public void run(){
		try{
		Connet();
		while(running){
			JSONObject mJSON = mInQ.take();
			SendMessage(mJSON);
		}
		Disconnect();
		}catch (TimeoutException | IOException | InterruptedException e){e.printStackTrace();}
	}
	private void Connet() throws IOException, TimeoutException{
		mFactory = new ConnectionFactory();
	    mFactory.setHost(mHostName);
	    mConnection = mFactory.newConnection();
	    mChannel = mConnection.createChannel();
	    mChannel.exchangeDeclare(mExchangeName , "direct");
	}
	private void SendMessage(JSONObject mJSON) throws IOException{
		mChannel.basicPublish(mExchangeName , mRoutingKey , null, mJSON.toString().getBytes());
	}
	private void Disconnect() throws IOException, TimeoutException{
	    mChannel.close();
	    mConnection.close();
	}
	public RabbitMQSend(String mHostName , String mExchangeName , String mRoutingKey){
		this.mHostName     = mHostName;
		this.mExchangeName = mExchangeName;
		this.mRoutingKey   = mRoutingKey;
	}
	public RabbitMQSend(String mHostName , String mExchange){
		this(mHostName , mExchange, "");
	}
	public void setmInQ(BlockingQueue<JSONObject> mInQ) {
		this.mInQ = mInQ;
	}
}