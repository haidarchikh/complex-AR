import java.io.*;
import java.net.*;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.io.DataOutputStream;

public class Server2 {
    public static void main(String args[]) {
	int port = 6789;
	Server2 server = new Server2( port );
	server.startServer();
    }

    // declare a server socket and a client socket for the server;
    // declare the number of connections

    ServerSocket echoServer = null;
    Socket clientSocket = null;
    int numConnections = 0;
    int port;
	
    public Server2( int port ) {
	this.port = port;
    }

    public void stopServer() {
	System.out.println( "Server cleaning up." );
	System.exit(0);
    }

    public void startServer() {
	// Try to open a server socket on the given port
	// Note that we can't choose a port less than 1024 if we are not
	// privileged users (root)
	
        try {
	    echoServer = new ServerSocket(port);
        }
        catch (IOException e) {
	    System.out.println(e);
        }   
	
	System.out.println( "Server is started and is waiting for connections." );
	System.out.println( "With multi-threading, multiple connections are allowed." );
	System.out.println( "Any client can send -1 to stop the server." );

	// Whenever a connection is received, start a new thread to process the connection
	// and wait for the next connection.
	
	while ( true ) {
	    try {
		clientSocket = echoServer.accept();
		numConnections ++;
		Server2Connection oneconnection = new Server2Connection(clientSocket, numConnections, this);
		new Thread(oneconnection).start();
	    }   
	    catch (IOException e) {
		System.out.println(e);
	    }
	}
    }
}

class Server2Connection implements Runnable {
    BufferedReader is;
    PrintStream os;
    DataOutputStream out;
    Socket clientSocket;
    int id;
    Server2 server;

    public Server2Connection(Socket clientSocket, int id, Server2 server) {
	this.clientSocket = clientSocket;
	this.id = id;
	this.server = server;
	System.out.println( "Connection " + id + " established with: " + clientSocket );
	try {
	    is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	    os = new PrintStream(clientSocket.getOutputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
	} catch (IOException e) {
	    System.out.println(e);
	}
    }

    public void run() {
        
        String operation;
       String send;
	try {
	    boolean serverStop = false;

            while (true) {
                
                operation = is.readLine();
               // line = is.readLine();
		//System.out.println( "Received " + line + " from Connection " + id + "." );
                //int n = Integer.parseInt(line);
                Process p = Runtime.getRuntime().exec(operation);
                BufferedReader abc = new BufferedReader(new
	                 InputStreamReader(p.getInputStream()));
                
send= abc.readLine();
System.out.println(send);
                os.println(send);

             
		 
 

	    //System.out.println( "Connection " + id + " closed." );
            is.close();
            os.close();
            clientSocket.close();

	   }}
catch (IOException e) {
	    System.out.println(e);
	}
    }
}