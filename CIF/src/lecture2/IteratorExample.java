package lecture2;

import java.util.ArrayList;
import java.util.Iterator;

public class IteratorExample {
	
	public static class Car{
		
	}
	
	// an example
	public static void main (String[] args){
		ArrayList<Car> cars = new ArrayList<Car>();
	for (int i = 0; i < 12; i++)
		cars.add (new Car());
	Iterator<Car> it = cars.iterator();
	while (it.hasNext())
		System.out.println (it.next());
	}

}
