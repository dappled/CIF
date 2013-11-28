package summerClass.lecture3;

public class Test_DoubleLinkedList extends junit.framework.TestCase {
	
	public void test1() throws Exception {
		DoubleLinkedList<Integer> list = new DoubleLinkedList<Integer>( 10 );
		for( int i =  0; i < 10; i++ ) {
			assertTrue( list.isReady() == false );
			list.addLast( i );
		}
		assertTrue( list.isReady() == true );
		assertTrue( list.hasElementsToRemove() == true );
		assertTrue( list.getFirstValue().compareTo( 0 ) == 0 );
		assertTrue( list.getLastValue().compareTo( 9 ) == 0 );
		int j = list.removeFirst();
		assertTrue( j == 0 );
		assertTrue( list.getFirstValue().compareTo( 1 ) == 0 );
		assertTrue( list.isReady() == false );
		list.addLast( 200 );
		assertTrue( list.isReady() == true );
		list.addLast( 300 );
		assertTrue( list.isReady() == true );
		assertTrue( list.size() == 11 );
	}

}
