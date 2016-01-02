import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

public class Client implements Runnable {
	
	private Socket 			mSocket;
    private PrintStream 	ps;
    private BufferedReader 	br;	
    private String 			mHostname;
    private int 			mPort;
    private String 			mCommand;
    private boolean 		isRunning = false;
    private BlockingQueue<String> mQueue;
    private Logger mLog = LoggerTest.getLogInstance();
    
    public void setRunning(boolean isRunning){
    	this.isRunning = isRunning;
    }
    
    public Client( String mHostname , int mPort , BlockingQueue <String> mQueue) {
		this.mHostname	=	mHostname;
		this.mPort		=	mPort;
		this.mQueue		= 	mQueue;
	}
    
    public void setupStreams(){
    	try {
    	mSocket = new Socket( mHostname , mPort );
        ps 		= new PrintStream(mSocket.getOutputStream());
        br 		= new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
        
        mLog.info("Setup stream to : "+mHostname+":"+mPort);
    	} catch (IOException e) {
            mLog.severe(e.getStackTrace().toString());
            setRunning(false);
            GUI.mClient = null;
      	}
    }
    
	@Override
	public void run() {
			setupStreams();
			while(isRunning){
				try {
				mCommand = mQueue.take();
				ps.println(mCommand);
				ps.flush();
				String temp;
				temp = br.readLine();
				GUI.getInstance().update(temp);
				} catch (IOException |InterruptedException e){
					mLog.severe(e.getStackTrace().toString());
					} 
				}
			}
				
	public void closeStreams() throws IOException{
			ps.close();
			br.close();
			mSocket.close();
	}
}
