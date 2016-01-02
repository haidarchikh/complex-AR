import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;



public class TCPServer {
	
    private ServerSocket mServer;
    private Socket mClientSocket;
    private int  mPort;
	
    public TCPServer( int mPort ) {
	this.mPort = mPort;
    }
    
    public void startServer() {
    	try {
    	//	Create a TCP server and create a new thread to handle new connections 
    		mServer = new ServerSocket( mPort );
    		System.out.println("Server started on port : "+ mPort);
    		
    		while ( true ) {
    			mClientSocket = null;
    			mClientSocket = mServer.accept();
    			ClientHandler mClient = new ClientHandler(mClientSocket);
    			new Thread(mClient).start();
    			System.out.println("Starts a new thread to handle : "+ mClientSocket.getInetAddress()+"-"+mClientSocket.getPort());
    		}
    		}catch (IOException e) {
    			System.out.println(e.getStackTrace().toString());
    		}
    	}
    
    public static void main(String args[]) {
    	TCPServer mServer = new TCPServer( Integer.valueOf(args[0]));
    	mServer.startServer();
    	}
}