package lecture2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HashSetExample {
	public static void main(String args[]) {
		Set<String> s = new HashSet<String>();
		for (int i = 0; i < args.length; i++) {
			if (!s.add(args[i]))
				System.out.println("Duplicate detected: " + args[i]);
		}
		System.out.println(s.size() + " distinct words detected: " + s);
		List <Integer> test = new ArrayList<>();
		test.add(3);
		test.add(5);
		test.add(3);
		for (java.util.ListIterator<Integer> it = test.listIterator(); it.hasNext();){
			System.out.println(it.next());
			System.out.println(it.nextIndex());
			System.out.println(it.previousIndex());
		}
	}
}
