package se.ltu.d7031e.rabbitMQ;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

import org.json.JSONObject;


import com.rabbitmq.client.*;
/**
 * This class is a rabbitMQ receive client. It connects to rabbitMQ broker and 
 * receives individual messages.<br> It's customized to hide the complexity and 
 * be very simple to use (no security or authentication) for rapid prototyping.<br>
 * There is no real need to make it run as a separate thread since the provided call back is not called from <br>
 * the main thread and has it's own working thread, but for consistency reasons it has its own thread<br>
 * To instantiate :
 * <p><b><code>RabbitMQReceive  mReceiver = new RabbitMQReceive(mRabbitIP, mExchangeName);</code></b>
 * <p>The object will connect to the broker, create a temporary queue, bind the exchange to<br>
 * this queue and start receiving. All messages between different parts of the system are JSON objects,<br>
 * so the object will receive the messages and put them in a queue ready to be used by a consumer <br>
 * To get the queue
 * <p><b><code>BlockingQueue {@literal <JSONObject>} mQ = mReceiver.getmOutQ(); </code></b>
 * */
public class RabbitMQReceive extends Thread{
	private static final String EXCHANGE_TYPE_DIRECT = "direct";
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
		connect();
		reciveMessage();
		while(running){
			JSONObject mJSON = new JSONObject();
			try {
				mJSON = mInternalQ.take();
				mOutQ.add(mJSON);
			} catch (InterruptedException e) {
				System.out.println("-----------------INTERRUP-------------------");
			}
		}
		disconnect();
	}
	public void setRunning(boolean running){
		this.running = running;
		if(!running){this.interrupt();}
	}
	/**
	 * The method connects to the broker, creates a temporary <br>
	 * queue, creates a exchange and bind the exchange to the temprory queue.
	 * */
	private void connect(){
		mFactory = new ConnectionFactory();
	    mFactory.setHost(mHostName);
	    try {
			mConnection  = mFactory.newConnection();
		    mChannel     = mConnection.createChannel();
		    mRabbitQueue = mChannel.queueDeclare().getQueue();
		    mChannel.exchangeDeclare(mExchangeName, EXCHANGE_TYPE_DIRECT, false);
		    mChannel.queueBind(mRabbitQueue , mExchangeName , mRoutingKey);
		} catch (IOException | TimeoutException e) {
			System.out.println("Error while connecting to RabbitMQ" +e.getCause());
			e.printStackTrace();
		}
	    //System.out.println(" [*] Waiting for events.");
	} 
	/**
	 *This method creates a consumer and overrides a callback provided by rappitMQ library
	 *<br> the received messages go throw unnecessary stage which is from this callback to the internal
	 *queue.
	 */
	private void reciveMessage() {
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
	/**
	 * This method will disconnect form the broker and close the channel.
	 * */
	private void disconnect(){
	    try {
			mChannel.close();
			mConnection.close();
		} catch (IOException | TimeoutException e) {
			System.out.println("Error while disconnecting from RabbitMQ");
			e.printStackTrace();
		}
	}
	// Not used, maybe will be in future projects
	public RabbitMQReceive(String mHostName, String mExchangeName, String mRoutingKey){
		this.mHostName     = mHostName;
		this.mExchangeName = mExchangeName;
		this.mRoutingKey   = mRoutingKey;
	}
	/**
	 * Creates a RabbitMQReceive Object.
	 * @param mHostName RappitMQ broker IP address
	 * @param mExchange Name of the exchange to receive messages from.
	 * */
	public RabbitMQReceive(String mHostName , String mExchange){
		this(mHostName , mExchange, "");
	}
	public BlockingQueue<JSONObject> getmOutQ() {
		return mOutQ;
	}
}