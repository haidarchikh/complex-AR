package se.ltu.d7031e.rabbitMQ;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

import org.json.JSONObject;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
/**
 * This class is a rabbitMQ send client. It connects to a rabbitMQ broker, creates an exchange 
 * and push messages to it.<br> It's customized to hide the complexity and 
 * be very simple to use (no security or authentication) for rapid prototyping.
 * <br>To instantiate :
 * <p><b><code>RabbitMQSend mSender = new RabbitMQSend (mRabbitIP, mExchangeName);</code></b>
 * <p>To send messages we provide the object with a queue and push messages to that queue.<br>
 * <p><b><code>BlockingQueue<JSONObject> mQ = new ArrayBlockingQueue<>(mCapacity);<br>
 * mSender.setmInQ(mQ);
 * </code></b>
 * */
public class RabbitMQSend extends Thread{
	private static final String EXCHANGE_TYPE_DIRECT = "direct";
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
		connet();
		while(running){
			JSONObject mJSON = mInQ.take();
			sendMessage(mJSON);
		}
		disconnect();
		}catch (InterruptedException e) {
			System.out.println("-----------------INTERRUP-------------------");}
		catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			System.out.println("Couldn't connect to RabbitMQ broker");
			e.printStackTrace();
		}
	}
	/**
	 * The method connects to the broker and creates an exchange
	 * */
	private void connet() throws IOException, TimeoutException{
		mFactory = new ConnectionFactory();
	    mFactory.setHost(mHostName);
	    mConnection = mFactory.newConnection();
	    mChannel = mConnection.createChannel();
	    mChannel.exchangeDeclare(mExchangeName, EXCHANGE_TYPE_DIRECT);
		System.out.println("Connectd to :"
		+mHostName +" and sending messages from the "
		+mExchangeName +" exchange");
	}
	/**
	 * After connecting to the broker and creating an exchange. This method will push <br>
	 * JSONObjects to the exchange.
	 * @param mJSON A JSON object 
	 * */
	private void sendMessage(JSONObject mJSON) throws IOException{
		mChannel.basicPublish(mExchangeName , mRoutingKey , null, mJSON.toString().getBytes());
	}
	private void disconnect() throws IOException, TimeoutException{
	    mChannel.close();
	    mConnection.close();
	}
	// Not used, maybe will be in future projects
	public RabbitMQSend(String mHostName , String mExchangeName , String mRoutingKey){
		this.mHostName     = mHostName;
		this.mExchangeName = mExchangeName;
		this.mRoutingKey   = mRoutingKey;
	}
	/**
	 * Creates a RabbitMQSend Object.
	 * @param mHostName RappitMQ broker IP address
	 * @param mExchange Name of the exchange to push messages to.
	 * */
	public RabbitMQSend(String mHostName , String mExchange){
		this(mHostName , mExchange, "");
	}
	public void setmInQ(BlockingQueue<JSONObject> mInQ) {
		this.mInQ = mInQ;
	}
}