package summerClass.lecture1;

public class Loops {

	public static void main(String[] args) {

		// For loop
		for (int i = 0; i < 5; ++i) {
			System.out.println("i = " + i);
		}

		// While loop
		int i = 0;
		while (i < 5) {
			System.out.println("i = " + i);
			++i;
		}

		// Do-While loop
		int j = 0;
		do {
			System.out.println("j = " + j);
			++j;
		} while (j < 5);

		// Missing for loop
		for (int k = 0; k < 5;) {
			System.out.println("k = " + k);
			++k;
		}

		// Use of continue
		for (int k = 0; k < 5; ++k) {
			if (k <= 3) {
				continue;
			}
			System.out.println("k = " + k);
		}

		// Use of break
		for (int k = 0; k < 5; ++k) {
			System.out.println("k = " + k);
			if (k > 3) {
				break;
			}
		}

		// Shorthand for looping over Java structures
		String[] strings = { "a", "b", "c" };
		for (String string : strings) {
			System.out.println(string);
		}

	}

}
