package summerSolution;

import java.util.Random;

import utils1309.Quicksort;

/**
 * This is a test of the original quicksort that I got off the Internet
 * 
 * @author minddrill
 *
 */
public class Test_Quicksort extends junit.framework.TestCase {
	
	  public void testNull() {
	    Quicksort sorter = new Quicksort();
	    sorter.sort(null);
	  }

	  public void testEmpty() {
	    Quicksort sorter = new Quicksort();
	    sorter.sort(new int[0]);
	  }

	  public void testSimpleElement() {
	    Quicksort sorter = new Quicksort();
	    int[] test = new int[1];
	    test[0] = 5;
	    sorter.sort(test);
	  }

	  public void testSpecial() {
	    Quicksort sorter = new Quicksort();
	    //int[] test = { 5, 5, 6, 6, 4, 4, 5, 5, 4, 4, 6, 6, 5, 5 };
	    int[] test = { 1,2,3,8,9,10,4,5,6,7,11,12,13 };
	    sorter.sort(test);
	    assertTrue( validate(test) );
	    System.out.print( "Special case test: " );
	    printResult(test);
	  }

	  public void testQuickSort() {
		  
		int SIZE = 7;
		int MAX = 20;
		int[] numbers = new int[SIZE];
	    Random generator = new Random();
	    for (int i = 0; i < numbers.length; i++)
	      numbers[i] = generator.nextInt(MAX);

		System.out.print( "Before quicksort: " );
	    printResult( numbers );

	    Quicksort sorter = new Quicksort();
	    sorter.sort(numbers);

	    System.out.print( "After quicksort: " );
	    printResult( numbers );

	    assertTrue( validate(numbers) );
	  }

	  private boolean validate(int[] numbers) {
	    for (int i = 0; i < numbers.length - 1; i++) {
	      if (numbers[i] > numbers[i + 1]) {
	        return false;
	      }
	    }
	    return true;
	  }

	  private void printResult(int[] numbers) {
	    for (int i = 0; i < numbers.length; i++) {
	      System.out.print(numbers[i]);
	      if( i < ( numbers.length - 1 ) )
	    	  System.out.print( ", " );
	    }
	    System.out.println();
	  }
}
