package xtest;

public class TestDevision {

	public static enum Cloud {
		C1,C2,C3
	}
	public static void main(String[] args) {
		
		int count = 0;
		
		for(int i = 0; i<=10 ; i++){
			for(int y = 0; y<=10 ; y++){
				for(int z = 0; z<=10 ; z++){
					count++;
					System.out.println(count);
				}
			}
		}

	}

}
