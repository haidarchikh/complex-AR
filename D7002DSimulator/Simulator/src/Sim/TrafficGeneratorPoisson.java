package Sim;

public class TrafficGeneratorPoisson extends TrafficGenerator{

	public TrafficGeneratorPoisson(int network, int node) {
		super(network, node);
	}
	protected int getTimeInterval(){
		// Knuth algorithm
		double ePowerMean = Math.exp(-_expectedDelay);
		  double p = 1.0;
		  int k = 0;

		  do {
		    k++;
		    p *= Math.random();
		  } while (p > ePowerMean);

		  return k - 1;
	}
}