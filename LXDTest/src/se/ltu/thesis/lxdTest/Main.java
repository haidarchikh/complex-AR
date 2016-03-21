package se.ltu.thesis.lxdTest;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Main {
	
	public static void main(String[] args) {
		int i = 0;
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("./loop.txt", "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (true){
			writer.println(i);
			writer.flush();
			//System.out.println(i);
			i++;
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}