package Sim;

import java.util.Random;

public class TrafficGeneratorNormal extends TrafficGenerator {
	private Random rand = new Random();
	public static final double GAUSSIAN_LOWER_LIMIT = -3.0;
	public static final double GAUSSIAN_UPPER_LIMIT = 3.0;
	public TrafficGeneratorNormal(int network, int node) {
		super(network, node);
	}
	protected int getTimeInterval(){
		double gaussian = rand.nextGaussian();
		// Around 99 % of the random numbers are going to be {-3 ,3}
		if(gaussian <= GAUSSIAN_LOWER_LIMIT ){gaussian = GAUSSIAN_LOWER_LIMIT ;}
		if(gaussian >= GAUSSIAN_UPPER_LIMIT ){gaussian = GAUSSIAN_UPPER_LIMIT ;}
		// Make the range {-0.5,0.5}
		gaussian /= GAUSSIAN_UPPER_LIMIT*2;
		// Make the expected range the mean and extend the range
		gaussian = (_expectedDelay*gaussian)+_expectedDelay ;
		return (int)Math.round(gaussian);
	}
}