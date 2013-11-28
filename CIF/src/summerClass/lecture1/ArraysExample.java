package summerClass.lecture1;

public class ArraysExample {

	public static void main(String[] args) {
		int[][] i;
		i = new int[5][];
		i[0] = new int[7];
		for (int j = 0; j < 7; j++) {
			i[0][j] = 3;
		}
		int k = 0;
		try {
			k = i[1][0];
		} catch (Exception e) {
			System.out.println("We caught an error!"+e.getMessage());
		}
		i = new int[5][7];
		k = i[1][0];
	}
}
