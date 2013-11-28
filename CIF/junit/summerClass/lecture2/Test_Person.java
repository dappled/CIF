package summerClass.lecture2;

import summerClass.lecture2.Person;

public class Test_Person extends junit.framework.TestCase {

	public void test1() {
		Person p = new Person( "Lee", 48 );
		junit.framework.TestCase.assertTrue( p.getName().compareTo("Lee" ) == 0 );
		junit.framework.TestCase.assertEquals( p.getAge(), 48 );
	}
	
	public void test2() {
		Person[] persons = new Person[ 5 ];
		persons[ 0 ] = new Person( "Bob", 32 );
		persons[ 1 ] = new Person( "Lee", 48 );
		junit.framework.TestCase.assertFalse( persons[ 0 ].equals( persons[ 1 ] ) );
	}
	
}
