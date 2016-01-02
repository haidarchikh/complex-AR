import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.logging.Logger;

class ClientHandler implements Runnable {
 
	private BufferedReader br;
    private PrintStream pr;
    private Socket mSocket;
    private Logger mLog = LoggerTest.getLogInstance();
    
    public ClientHandler(Socket mSocket) {
	this.mSocket = mSocket;
	}
    
    public void setupStreams(){
    	try {
    	    br = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
    	    pr = new PrintStream(mSocket.getOutputStream());
            mLog.info( "Connection established with: " + mSocket.getInetAddress() );
    	} catch (IOException e) {
    		mLog.severe(e.getStackTrace().toString());
    		}
    	}
    
    public void run() {
    	setupStreams();
        String mCommand;
        while(!mSocket.isClosed()){
        	StringBuilder response = new StringBuilder();
            StringBuilder error = new StringBuilder();
        	try { 
        		if(br.ready()){
        			mCommand = br.readLine(); 
        			mLog.finest("Got \""+ mCommand + "\" to excute from "+ mSocket.getInetAddress());
        			
        		//	Setup streams to execute the command and get the results
        			Process mProcess = Runtime.getRuntime().exec(new String[]{"bash","-c",mCommand});
                    BufferedReader mCommandResult = new BufferedReader(new InputStreamReader(mProcess.getInputStream()));
                    BufferedReader mCommandError = new BufferedReader(new InputStreamReader(mProcess.getErrorStream()));
                    mProcess.waitFor();
                
                //  Read execution process streams  
                    String temp;
                    while((temp = mCommandResult.readLine())!=null) {response.append(temp);}
                    while((temp = mCommandError.readLine())!=null) {error.append(temp);}
                    
                //	Determine replaying with an error or result
                    if(error.length() !=0 ){
                    	pr.println(error.toString());
                    	mLog.warning("Error executing \""+ mCommand + "\"" + error.toString());
                    }
                    if(response.length() != 0){
                    	pr.println(response.toString());
                    	mLog.info(" executing \""+ mCommand + "\"" + response.toString());
                    }
                    mCommandResult.close();
                    
        		}
            }
            catch (IOException | InterruptedException e) {
            	mLog.severe(e.getStackTrace().toString());
    		}
    	}
         closeStreams();
	}
    public void closeStreams(){
    	try {
			br.close();
		} catch (IOException e) {
			mLog.severe(e.getStackTrace().toString());
		}
    	pr.close();
    }
}