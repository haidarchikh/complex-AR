package xtest;

import java.util.Random;

public class test {
	public static final double GAUSSIAN_LOWER_LIMIT = -3.0;
	public static final double GAUSSIAN_UPPER_LIMIT = 3.0;
	public static final int MAX_DAMPER = 1000000;
	private static double calcDelayReward(int delay, int min_D, int max_D){
		// if the delay is smaller than the minimum
		if(delay <= min_D){
			return 1;
		}
		// if the delay is bigger than the maximum allowed delay
		if(delay >= max_D){
			return 0;
		}
		// if the delay is between minimum and maximum
		if(min_D < delay && delay < max_D){
			return (double)(max_D - delay)/(max_D - min_D);
		}
		return 0;
	}
	
	private static double calcThroughputReward(int throughput, int min_T, int max_T){
		// if the throughput is smaller than the minimum
		if(throughput <= min_T){
			return 0;
		}
		// if the throughput is bigger than the maximum 
		if(throughput >= max_T){
			return 1;
		}
		// if the delay is between minimum and maximum
		if(min_T < throughput && throughput < max_T){
			return (double)(throughput - min_T )/(max_T - min_T);
		}
		return 0;
	}

	public static void main(String[] args) {
	
		double weight = 0.9;
		double temp = (1.0 - weight)/4.0;
		double thWieght = temp * 3.0;
		double dampWight = temp/2.0;
		
		for (int i = 0; i <1000; i++){
			
			double delayReward 		= calcDelayReward(		19	, 20, 200);
			double throughputReward = calcThroughputReward(	300	, 20, 200);
			
			double delayDamper		= calcDelayReward(		i	, 0, MAX_DAMPER);
			double throughputDamper = calcThroughputReward(	i	, 0, MAX_DAMPER);
			
			double reward = (weight) * delayReward + thWieght * throughputReward
					+dampWight * delayDamper +dampWight * throughputDamper;
			
			System.out.println(reward);
			//System.out.println("i : "+i + ", res : "+calcThroughputReward(i, 50, 200));
		}
		


		
	
	}
}