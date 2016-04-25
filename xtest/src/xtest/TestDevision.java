package xtest;

public class TestDevision {

	public static enum Cloud {
		C1,C2,C3
	}
	public static void main(String[] args) {
		int x = 3;
		int y = 2;
		
		double z  =  x/(double) y;
		System.out.println(z);
		System.out.println(Cloud.C1);

	}

}
