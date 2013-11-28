package summerClass.lecture3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

public class Test_DataStructures extends junit.framework.TestCase {

	public void testLinkedList() {
		
		LinkedList<String> stringList = new LinkedList<String>(); 
		stringList.add( new String( "first" ) ); // Efficient add
		stringList.add( new String( "second" ) );
		stringList.add( new String( "third" ) );
		stringList.add( new String( "fourth" ) ); // All add calls are efficient in LinkedList
		assertTrue( stringList.size() == 4 );
		assertTrue( stringList.get( 1 ).compareTo( "second" ) == 0 ); // Inefficent call to get method
		assertTrue( stringList.remove( 0 ).equals( "first" ) ); // Efficient remove of first or last element
		assertTrue( stringList.size() == 3 );
		stringList.clear();
		assertTrue( stringList.size() == 0 );
		
	}
	
	public void testHashMap() {
		
		HashMap<Integer, String> teacherMap = new HashMap<Integer, String>();
		Integer teacherId1 = new Integer( 59 );  
		String teacherName1 = new String( "Lee" );
		// No need to implement hashCode, equals, and compare for classes 
		// already in the Java collections framework
		
		teacherMap.put( teacherId1, teacherName1 ); // Put teacher name in map using teacher id as key
		
		Integer teacherId2 = new Integer( 80 );
		String teacherName2 = new String( "Bob" );
		
		teacherMap.put( teacherId2, teacherName2 ); // Put another name in the map
		
		assertTrue( teacherMap.get( 80 ).compareTo( "Bob" ) == 0 ); // Make sure we can retrieve both names
		assertTrue( teacherMap.get( 59 ).compareTo( "Lee" ) == 0 );
		assertTrue( teacherMap.get( 100 ) == null ); // Trying to retrieve something that is not in the map
		
		// Overwrite "Bob" to show that keys in a hash map must be unique
		// We are using the same key as was previously used for Bob
		teacherMap.put( 80, "Steve" );
		assertTrue( teacherMap.get( 80 ).compareTo( "Bob" ) != 0 );

	}
	
	public void testTreeMap() {

		TreeMap<Integer,Integer> tm = new TreeMap<Integer, Integer>();
		
		// Populate tree with some values
		
			tm.put( 10, 100 );
			tm.put( 20, 200 );
			tm.put( 30, 300 );
		
		// The contents of the list are ordered by their keys so we
		// can efficiently search for them in ways that are difficult
		// with structures where the content are unsorted. For example:
			
		// Find the key / value pair where the key is greater than or equal 
		// to 15
			
			Map.Entry<Integer,Integer> me = tm.ceilingEntry( 15 );
			assertTrue( me.getKey().equals( 20 ) );
			assertTrue( me.getValue().equals( 200 ) );
			
		// Find key/value pair where key is greater than or equal 
		// to 40 - Should return null
			
			me = tm.ceilingEntry( 40 );
			assertTrue( me == null );

		// Search for something we know is in the tree
			
			assertTrue( tm.get( 20 ) == 200 );
			assertTrue( tm.get( 30 ) == 300 );
			
		// Search for something we know is not in the map
			
			assertTrue( tm.get( 40 ) == null );
			
	}
	
	public void testArrayList() {
		
		ArrayList<String> stringList = new ArrayList<String>(3); 
		stringList.add( new String( "first" ) );
		stringList.add( new String( "second" ) );
		stringList.add( new String( "third" ) );
		stringList.add( new String( "fourth" ) ); // Inefficient growth from size = 3 to size = 4
		assertTrue( stringList.size() == 4 );
		assertTrue( stringList.get( 1 ).compareTo( "second" ) == 0 ); // get method is efficient random access of array
		assertTrue( stringList.remove( 0 ).equals( "first" ) ); // Inefficient remove operation
		assertTrue( stringList.size() == 3 );
		stringList.clear();
		assertTrue( stringList.size() == 0 );
		
	}
	
	
}
