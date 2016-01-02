import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class GUI implements ActionListener {
	
	private static GUI mGUI = null;
	// ugly fix ("static")
	public static Client mClient = null;
	private Thread mWorker;  
	private int mPort = 2500;
	private String mHostname = "52.11.95.241";
	
	private JFrame 		mFrame;
	private JTextField 	mTextInput;
	private JTextField 	mTextOutput;
	private JTextField 	mPortText;
	private JTextField 	mHostText;
	private JButton 	mButton;
	private JPanel 		mPanel;
	
	private BlockingQueue<String> mQueue = new ArrayBlockingQueue<String>(10);
	
	public static GUI getInstance() {
	      if(mGUI == null) {
	         mGUI = new GUI();
	      }
	      return mGUI;
	   }
	public GUI(){
	//	Initialize a frame , 4 text fields ,a panel and a button 
		mFrame		= new JFrame();
		mTextInput  = new JTextField(25);	
		mTextOutput = new JTextField(25);
		mPortText	= new JTextField(25);
		mHostText	= new JTextField(25);
		mButton		= new JButton("Execute");
		mPanel		= new JPanel();
		
		mButton.setPreferredSize(new Dimension(110,25));
		mTextInput.setSize(110,110);
		mTextOutput.setSize(110,110);
		mPortText.setSize(110, 110);
		mHostText.setSize(110, 110);
	
		mPortText.setText(String.valueOf(mPort));
		mHostText.setText(mHostname);
		mTextInput.setText("pwd");
		mPanel.add(mTextInput);
		mPanel.add(mTextOutput);
		mPanel.add(mHostText);
		mPanel.add(mPortText);
		mPanel.add(mButton);
		mPanel.setVisible(true);
		
		mFrame.add(mPanel);
		mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mFrame.setSize(new Dimension(300,150));
		mFrame.setVisible(true);
		mFrame.setLocationRelativeTo(null);
		mButton.addActionListener(this);
	}
	@Override
	public void actionPerformed(ActionEvent event) {
		String mCommand = mTextInput.getText();
		mQueue.add(mCommand);
		if(mClient == null){
			mHostname 	= mHostText.getText();
			mPort		= Integer.valueOf(mPortText.getText());
			mClient 	= new Client( mHostname , mPort ,  mQueue );
			mWorker 	= new Thread(mClient);
			mClient.setRunning(true);
			mWorker.start();
		}
	}
	
	public void update(final String res){
		SwingUtilities.invokeLater(new Runnable() {
		      public void run() {
		      mTextOutput.setText(res);
		      }
		      }
		);
	}
	
	public static void main(String args[]){
		GUI.getInstance();
	}
}