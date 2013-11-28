package summerClass.lecture3;

import summerClass.lecture3.ArrayBasedList;

public class Test_ArrayBasedList extends junit.framework.TestCase {

	public void test1() throws Exception {
		ArrayBasedList<Integer> cl = new ArrayBasedList<Integer>( 3 );

		// Add 4 elements - Should cause the list to grow
		for( int i = 0; i < 4; i++ ) {
			cl.addLast( i + 1 );
		}
		for( int i = 0; i < 4; i++ ) {
			assertTrue( cl.getValue( i ) == ( i + 1 ) );
		}
		assertTrue( cl.removeFirst() == 1 );
		assertTrue( cl.size() == 3 );
		assertTrue( cl.getFirstValue() == 2 );
		assertTrue( cl.getLastValue() == 4 );
		cl.addLast( 30 );
		assertTrue( cl.getLastValue() == 30 );
		assertTrue( cl.getValue( cl.size() - 2 ) == 4 );
		assertTrue( cl.isReady() );
		assertTrue( cl.hasElementsToRemove() );
		assertTrue( cl.removeFirst() == 2 );
		cl.addLast( 40 );
		assertTrue( cl.getValue( 0 ) == 3  );
		assertTrue( cl.getValue( 1 ) == 4  );
		assertTrue( cl.getValue( 2 ) == 30 );
		assertTrue( cl.getValue( 3 ) == 40 );

	}
}
