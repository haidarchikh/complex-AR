package xtest;




public class Main {
	
	
	public static final double round(double sample, double roundTo) {
		if(roundTo % 1 == 0){
			return Math.floor((sample + roundTo / 2) / roundTo) * roundTo;
		} else{
			return roundTo * Math.floor(sample / roundTo);
		}
	}
	
	public static void main(String[] args) {
	
		double x = 1.8888;
		System.out.println(round(x, 0.5));
	}

}
