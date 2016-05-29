package xtest;

import java.math.BigDecimal;


public class Main {

	public static final double round(double sample, double roundTo) {
		if(roundTo % 1 == 0){
			// round to an integer
			return Math.floor((sample + roundTo / 2) / roundTo) * roundTo;
		} else{
			// round to a double
			return roundTo * Math.round(sample / roundTo);
		}
	}

	public static void main(String[] args) {
		for (double i = 0.1 ; i < 1000 ; i = i += 0.1){
			double roundedI = round(i, 1);
			int x = 0;
		}
		
	}
}
