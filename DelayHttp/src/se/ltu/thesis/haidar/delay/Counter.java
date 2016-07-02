package se.ltu.thesis.haidar.delay;

public class Counter extends Thread{
	private int mCounter;
	private boolean running;
	public void run(){
		while(running){
			mCounter++;
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void setRunning(boolean running){
		this.running = running;
	}
	public int getCount(){
		return mCounter;
	}
}
