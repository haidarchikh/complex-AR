import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Hashtable;


class ClientHandler implements Runnable {
 
	private PrintStream pr;
    private Socket mSocket;
    public static final String DONE = "DONE!";
    public static Hashtable<String, Socket> mHashStorage = new Hashtable<>();
    public static int NumConn = 0 ;
    public static String mIP ;
    public static int mPort ; 
    
    public ClientHandler(Socket mSocket) {
	this.mSocket = mSocket;
	
//  Insert the pointer to the Hashtable
	String Ip= mSocket.getInetAddress().toString();
	int Port = mSocket.getPort();
	String mIndex = Ip+ "-"+Port ;
	mHashStorage.put(mIndex , mSocket);
	NumConn++;
	}
    
    public void run() {
    	
    	
        while(!mSocket.isClosed()){
        	long mSleep = (long) (NumConn * 1.5);
        	writeToShared();
        	try {
				Thread.sleep(mSleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        	SendDone();
        	
    	}
	}
    public synchronized void writeToShared(){
    	String ip= mSocket.getInetAddress().toString();
    	
    	int port = mSocket.getPort();
    	System.out.println("Thread is writing to shared variables" + ip +"-"+port);
		ClientHandler.mIP = ip;
		ClientHandler.mPort = port;
    }
    public synchronized String readFromShared(){
    	String temp = ClientHandler.mIP +"-"+ ClientHandler.mPort;
    	System.out.println("Thread is reading from the shared variables" + temp);
		return temp;
		}
    
    public void SendDone (){
    	Socket mTempSocket ;
    	mTempSocket = mHashStorage.get(readFromShared());
    	try {
    	    pr = new PrintStream(mTempSocket.getOutputStream());
    	    pr.print(DONE);
    	    System.out.println( "DONE! is sent to: " + mTempSocket.getInetAddress() + ":" + mTempSocket.getPort());
    	} catch (IOException e) { e.printStackTrace();}
    } 
}