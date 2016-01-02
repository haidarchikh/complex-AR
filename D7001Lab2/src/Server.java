
import java.net.*;
import java.util.logging.Logger;
import java.io.IOException;

public class Server {
    
    private ServerSocket mServer;
    private Socket mClientSocket;
    private int  mPort;
    private Logger mLog = LoggerTest.getLogInstance();
	
    public Server( int mPort ) {
	this.mPort = mPort;
    }
    
    public void startServer() {
    	try {
    	//	Create a TCP server and create a new thread to handle new connections 
    		mServer = new ServerSocket( mPort );   
    		mLog.info("Server started on port : "+ mPort);
    		while ( true ) {
    			mClientSocket = null;
    			mClientSocket = mServer.accept();
    			ClientHandler mClient = new ClientHandler(mClientSocket);
    			new Thread(mClient).start();
    			mLog.finest("Start a new thread to handle : "+ mClientSocket.getInetAddress());
    		}
    		}catch (IOException e) {
    			mLog.severe(e.getStackTrace().toString());
    		}
    	}
    
    public static void main(String args[]) {
    	Server mServer = new Server( Integer.valueOf(args[0]));
    	LoggerTest.setLogLevel(args[1]);
    	mServer.startServer();
    	}
    }
//scp -i AWS_Key.pem /home/haidar/workspace/D7001Lab2/src/ClientHandler.java  ubuntu@52.11.95.241:/home/ubuntu/workspace
