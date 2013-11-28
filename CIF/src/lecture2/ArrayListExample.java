package lecture2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ArrayListExample {

	public static void main(String args[]) {
		List<String> l = new ArrayList<String>();
		for (int i = 0; i < args.length; i++)
			l.add(args[i]);
		Collections.shuffle(l, new Random());
		System.out.println(l);
	}

}
