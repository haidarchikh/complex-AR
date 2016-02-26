package xtest;

import java.util.Random;

public class test {
	public static final double GAUSSIAN_LOWER_LIMIT = -3.0;
	public static final double GAUSSIAN_UPPER_LIMIT = 3.0;
	public static int getPoisson(double lambda) {
		  double L = Math.exp(-lambda);
		  double p = 1.0;
		  int k = 0;

		  do {
		    k++;
		    p *= Math.random();
		  } while (p > L);

		  return k - 1;
		}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int x = 3;
		int y = 5;
		double res = (double)5/(double)3;
		//System.out.println(res);
		Random rand = new Random();
		while (true){
			getPoisson(10);
			
				}
		}
	}