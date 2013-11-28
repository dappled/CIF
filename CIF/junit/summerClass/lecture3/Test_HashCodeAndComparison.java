package summerClass.lecture3;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Test to demonstrate use of equals, hashCode, use of Comparable<T>, and Comparator<T>
 * 
 * @author Lee
 *
 */
public class Test_HashCodeAndComparison extends junit.framework.TestCase {

	/**
	 * Fail because we haven't implemented an equal/hashCode
	 */
	public void test1() {
		class ExampleClass {
			int _i;
			int _j;
			public ExampleClass( int i, int j ) {
				_i = i;
				_j = j;
			}
		}
		HashMap<ExampleClass,Integer> myHashMap = new HashMap<ExampleClass,Integer>();
		ExampleClass firstInstanceOfExampleClass = new ExampleClass( 5, 6 );
		ExampleClass secondInstanceOfExampleClass = new ExampleClass( 5, 6 );
		myHashMap.put( firstInstanceOfExampleClass, 59 );
		System.out.println(this.getName() + "frist: " + myHashMap.get( firstInstanceOfExampleClass ));
		System.out.println(myHashMap.get(this.getName() + "second: " +  secondInstanceOfExampleClass ));
		assertTrue( myHashMap.get( firstInstanceOfExampleClass ).equals(new Integer( 59 ) ) ); // Success!
		assertTrue( myHashMap.get( secondInstanceOfExampleClass ).equals(new Integer( 59 ) ) ); // Failure!
	}
	
	/**
	 * Fail with good equal but no hashCode
	 */
	public void test2() {
		class ExampleClass {
			int _i;
			int _j;
			public ExampleClass( int i, int j ) {
				_i = i;
				_j = j;
			}
			
			public boolean equals( Object object ) { // Good equals
				if( ! ( object instanceof ExampleClass ) )
					return false;
				if( object == this )
					return true;
				ExampleClass zz = (ExampleClass) object;
				return ( _i == zz._i ) && ( _j == zz._j );
			}
			
			// No hashCode function
			
		}
		HashMap<ExampleClass,Integer> myHashMap = new HashMap<ExampleClass,Integer>();
		ExampleClass firstInstanceOfExampleClass = new ExampleClass( 5, 6 );
		ExampleClass secondInstanceOfExampleClass = new ExampleClass( 5, 6 );
		myHashMap.put( firstInstanceOfExampleClass, 59 );
		System.out.println(this.getName() + "frist: " + myHashMap.get( firstInstanceOfExampleClass ));
		System.out.println(this.getName() + "second: " +  myHashMap.get(secondInstanceOfExampleClass ));
		assertTrue( myHashMap.get( firstInstanceOfExampleClass ).equals(new Integer( 59 ) ) ); // Success!
		assertTrue( myHashMap.get( secondInstanceOfExampleClass ).equals(new Integer( 59 ) ) ); // Failure!
	}

	/**
	 * Fail with good hashCode but no equals
	 */
	public void test3() {
		class ExampleClass {
			int _i;
			int _j;
			public ExampleClass( int i, int j ) {
				_i = i;
				_j = j;
			}
			
			public int hashCode() { 
				int result = 17;
				result = 31 * result + _i;
				result = 31 * result + _j;
				return result;
			}
			
		}
		HashMap<ExampleClass,Integer> myHashMap = new HashMap<ExampleClass,Integer>();
		ExampleClass firstInstanceOfExampleClass = new ExampleClass( 5, 6 );
		ExampleClass secondInstanceOfExampleClass = new ExampleClass( 5, 6 );
		myHashMap.put( firstInstanceOfExampleClass, 59 );
		System.out.println(this.getName() + "frist: " + myHashMap.get( firstInstanceOfExampleClass ));
		System.out.println(this.getName() + "second: " +  myHashMap.get(secondInstanceOfExampleClass ));
		assertTrue( myHashMap.get( firstInstanceOfExampleClass ).equals(new Integer( 59 ) ) ); // Success!
		assertTrue( myHashMap.get( secondInstanceOfExampleClass ).equals(new Integer( 59 ) ) ); // Failure!
	}
	
	/**
	 * Pass with good equal/hashCode combo
	 */
	public void test4() {
		class ExampleClass {
			int _i;
			int _j;
			public ExampleClass( int i, int j ) {
				_i = i;
				_j = j;
			}
			
			public boolean equals( Object object ) { // Good equals
				if( ! ( object instanceof ExampleClass ) )
					return false;
				if( object == this )
					return true;
				ExampleClass zz = (ExampleClass) object;
				return ( _i == zz._i ) && ( _j == zz._j );
			}
			
			public int hashCode() { 
				int result = 17;
				result = 31 * result + _i;
				result = 31 * result + _j;
				return result;
			}
		}
		HashMap<ExampleClass,Integer> myHashMap = new HashMap<ExampleClass,Integer>();
		ExampleClass firstInstanceOfExampleClass = new ExampleClass( 5, 6 );
		ExampleClass secondInstanceOfExampleClass = new ExampleClass( 5, 6 );
		myHashMap.put( firstInstanceOfExampleClass, 59 );
		System.out.println(this.getName() + "frist: " + myHashMap.get( firstInstanceOfExampleClass ));
		System.out.println(this.getName() + "second: " +  myHashMap.get(secondInstanceOfExampleClass ));
		System.out.println(myHashMap.get( firstInstanceOfExampleClass ).equals(new Integer( 59 ) )) ; // Success!
		System.out.println(myHashMap.get( secondInstanceOfExampleClass ).equals(new Integer( 59 ) )); // Success!
	}
	
	/**
	 * Implementing a sort
	 */
	public void test5() {
		
		// We can use the default sort by forcing ZZ to implement the Comparable<T> interface
		class ExampleClass implements Comparable<ExampleClass>{
			int _i;
			int _j;
			public ExampleClass( int i, int j ) {
				_i = i;
				_j = j;
			}
			
			public int getI() { return _i; }
			
			public int getJ() { return _j; }
			
			public boolean equals( Object object ) { // Good equals
				if( ! ( object instanceof ExampleClass ) )
					return false;
				if( object == this )
					return true;
				ExampleClass temp = (ExampleClass) object;
				return ( _i == temp._i ) && ( _j == temp._j );
			}
			
			public int hashCode() { 
				int result = 17;
				result = 31 * result + _i;
				result = 31 * result + _j;
				return result;
			}
			
			// The Comparable<T> interface forces us to implement this method
			public int compareTo( ExampleClass otherInstance ) {
				if( _i > otherInstance.getI() )
					return 1;
				if( _i < otherInstance.getI() )
					return -1;
				if( _j > otherInstance.getJ() )
					return 1;
				if( _j < otherInstance.getJ() )
					return -1;
				return 0;
			}
			
		} // End of inner class ZZ
		
		LinkedList<ExampleClass> myList = new LinkedList<ExampleClass>();
		myList.add( new ExampleClass( 1, 2 ) );
		myList.add( new ExampleClass( 3, 4 ) );
		myList.add( new ExampleClass( 0, 0 ) );
		Collections.sort( myList );
		assertTrue( myList.get( 0 ).equals( new ExampleClass( 0, 0 ) ) );
		assertTrue( myList.get( 1 ).equals( new ExampleClass( 1, 2 ) ) );
		assertTrue( myList.get( 2 ).equals( new ExampleClass( 3, 4 ) ) );
		
		// If we wanted to sort but ZZ did not implement the Comparable<T> interface
		// Or if we want to sort in reverse order - as we are about to do - we can
		// call the version of Collections.sort(...) that requires us to provide a
		// new Comparator object as follows.
		
		Collections.sort(
				myList,
				new Comparator<ExampleClass>() {
					public int compare( ExampleClass firstInstance, ExampleClass secondInstance ) {
						return secondInstance.compareTo( firstInstance );
					}
				}
		);
		assertTrue( myList.get( 0 ).equals( new ExampleClass( 3, 4 ) ) );
		assertTrue( myList.get( 1 ).equals( new ExampleClass( 1, 2 ) ) );
		assertTrue( myList.get( 2 ).equals( new ExampleClass( 0, 0 ) ) );
		
	}

}
