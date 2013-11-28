package summerSolution;

import java.util.Comparator;
import java.util.Random;

import utils1309.GenericBubbleSort;
import utils1309.GenericQuicksort;

/**
 * Junit test for sorting methods to be used in summer assignment
 * 
 * @author minddrill
 *
 */
public class Test_Sorting extends junit.framework.TestCase {
	
	private Comparator<Integer> intComparator = new Comparator<Integer>() {

		@Override
		public int compare(Integer i1, Integer i2 ) {
			return i1.compareTo( i2 );
		}
		
	};

	
	/**
	 * Make sure our validation method works
	 */
	public void testTheTester() {
		Integer[] values = { 1, 0, 2, 3, 4, 6, 5 };
		assertFalse( validate( values ) );
		Integer[] values1 = { 0, 1, 2, 3, 4, 5, 6 };
		assertTrue( validate( values1 ) );
	}
	
	/**
	 * Test sorting of empty array - Should just leave it alone
	 * @throws Exception If the array of values passed to the sort is null
	 */
	public void testEmpty() throws Exception {
		(new GenericBubbleSort<Integer>()).sort( new Integer[0], intComparator );
		(new GenericQuicksort<Integer>()).sort(new Integer[0], intComparator );
	}

	/**
	 * Test sorting of array with one element
	 * @throws Exception If the array of values passed to the sort is null
	 */
	public void testSimpleElement() throws Exception {
		Integer[] values = new Integer[ 1 ];
		values[ 0 ] = 5;
		(new GenericBubbleSort<Integer>()).sort( values, intComparator );
		(new GenericQuicksort<Integer>()).sort( values, intComparator );
	}

	/**
	 * Test sorting of array with duplicates
	 * @throws Exception Exception is thrown if array passed to sorts is null
	 */
	public void testSpecial() throws Exception {
		
		// Test bubble sort
		Integer[] test = { 5, 5, 6, 6, 4, 4, 5, 5, 4, 4, 6, 6, 5, 5 };
		(new GenericBubbleSort<Integer>()).sort( test, intComparator );
		assertTrue( validate( test ) );
		
		// Test quick sort
		Integer[] test1 = { 5, 5, 6, 6, 4, 4, 5, 5, 4, 4, 6, 6, 5, 5 };
		(new GenericQuicksort<Integer>()).sort( test1, intComparator );
		assertTrue( validate( test1 ) );
	}

	/**
	 * Test sorting of random arrays
	 * @throws Exception Exception is thrown if array passed to sorts is null 
	 */
	public void testBubbleSort() throws Exception {
		
		// Test bubble sort
		Random generator = new Random();
		for( int iNumRuns = 0; iNumRuns < 100; iNumRuns++ ) {
			int arraySize = ( int )( 20 * generator.nextDouble() );
			Integer[] values = new Integer[ arraySize ];
			for (int i = 0; i < values.length; i++)
				values[i] = generator.nextInt( 5000 ) - 2500;
			(new GenericBubbleSort<Integer>()).sort( values, intComparator );
			assertTrue( validate(values) );
		}
		
		// Test quick sort
		for( int iNumRuns = 0; iNumRuns < 100; iNumRuns++ ) {
			int arraySize = ( int )( 20 * generator.nextDouble() );
			Integer[] values = new Integer[ arraySize ];
			for (int i = 0; i < values.length; i++)
				values[i] = generator.nextInt( 5000 ) - 2500;
			(new GenericQuicksort<Integer>()).sort( values, intComparator );
			assertTrue( validate(values) );
		}
		
	}
	
	/**
	 * Method to validate that an array is in order
	 * @param values Array that is supposed to be in ascending order
	 * @return True if array is in order or false if it is not
	 */
	private <ValType extends Comparable<ValType>> boolean validate( ValType[] values) {
		for (int i = 0; i < values.length - 1; i++)
			if( values[ i ].compareTo( values[ i + 1 ] ) > 0 )
				return false;
		return true;
	}

}
