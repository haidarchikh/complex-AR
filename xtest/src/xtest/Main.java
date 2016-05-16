package xtest;

import java.math.BigDecimal;


public class Main {

	public static final double round(double sample, double roundTo) {
		if (roundTo % 1 == 0) {
			return Math.floor((sample + roundTo / 2) / roundTo) * roundTo;
		} else {
			return roundTo * Math.floor(sample / roundTo);
		}
	}

	public static void main(String[] args) {

		BigDecimal mStep = new BigDecimal("0.5");
		int pointNum = 1;

		for (BigDecimal mLearningRate = BigDecimal.ZERO; mLearningRate
				.compareTo(BigDecimal.ONE) <= 0; mLearningRate = mLearningRate
				.add(mStep)) {

			for (BigDecimal mDiscountFactor = BigDecimal.ZERO; mDiscountFactor
					.compareTo(BigDecimal.ONE) <= 0; mDiscountFactor = mDiscountFactor
					.add(mStep)) {
				
				System.out.println(pointNum+ ", learning : "+ mLearningRate.doubleValue()+ 
						" discount : "+ mDiscountFactor.doubleValue());
				pointNum++;

			}
		}
	}

}
