package se.ltu.netprog.javaprog.sockets;


import java.net.*;// need this for InetAddress, Socket, ServerSocket 
import java.io.*;// need this for I/O stuff

public class UDPEchoServer { 
	static final int BUFSIZE=1024;
	
	static public void main(String args[]) throws SocketException 
	{ 
		
		if (args.length != 1) {
			throw new IllegalArgumentException("Must specify a port!"); 
						
		}
		
		int port = Integer.parseInt(args[0]);
		DatagramSocket s = new DatagramSocket(port);
		DatagramPacket dp = new DatagramPacket(new byte[BUFSIZE], BUFSIZE);
		String name = "Haidar\n";
		byte[] namebyte = name.getBytes();
		int byteread = 0;
		byte[] buff=new byte[BUFSIZE];
		try { 
			while (true) {
				s.receive(dp);
				byteread= dp.getLength();
				// print out client's address 
				System.out.println("Message from " + dp.getAddress().getHostAddress());
				// add my name 
				buff = dp.getData();
				System.arraycopy(namebyte, 0, buff,byteread,namebyte.length);
				dp.setData(buff);
				// Send it right back  
				s.send(dp); 
				dp.setLength(BUFSIZE);// avoid shrinking the packet buffer
			} 
		} catch (IOException e) {
			System.out.println("Fatal I/O Error !"); 
			System.exit(0);
		} 
	}
}
// echo -n "hello" | nc -4u 127.0.0.1 2499