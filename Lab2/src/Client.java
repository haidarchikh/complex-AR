import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Client {
  
	void met_gui()
	{
	JFrame f =new JFrame();

	final JTextField t1 = new JTextField(25);
	final JTextField t2 = new JTextField(25);
	final JButton b1 = new JButton("Display Result");
	
	f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	JPanel panel = new JPanel();
    f.setPreferredSize(new Dimension(900,400));
	f.add(panel);

    panel.add(t1);
	panel.add(t2);
	panel.add(b1);
	b1.setPreferredSize(new Dimension(100,30));
	t1.setSize(100,100);
	t2.setSize(100,100);
	
	panel.setVisible(true);
	f.setLocationRelativeTo(null);
	f.pack();
	f.setVisible(true);
	
b1.addActionListener(new ActionListener()
{
	public void actionPerformed(ActionEvent e)
	{
		String hostname = "localhost";
		int port = 6789;
		Socket clientSocket = null;  
        PrintStream os = null;
        BufferedReader is = null;		
		try
		{
			clientSocket = new Socket(hostname, port);
            os = new PrintStream(clientSocket.getOutputStream());
            is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));						
		}
		
		catch (UnknownHostException e2) 
		{
            System.err.println("Don't know about host: " + hostname);
        } catch (IOException e5) {
			// TODO Auto-generated catch block
			e5.printStackTrace();
		} 		
		try
		{		
		os.println(t1.getText());
		String response;
        //System.out.println(response);
        while ((response = is.readLine())!=null) 
        	{
        		t2.setText(response);
        	}
		}
   
		catch (IOException e3) 
		{
			System.out.println("Exception in Process");
			e3.printStackTrace();
		}	        
	
		os.close();
		try {
			is.close();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			clientSocket.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		
	}
		
		
		
}
);

	}
	
		



	
public static void main(String args[])
{
Client a = new Client();
a.met_gui();	
}


}
