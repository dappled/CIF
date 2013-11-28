package clustersSolution;

import java.util.List;

public class Test_MinValuesMap extends junit.framework.TestCase {

	public void test1() {
		
		MinValuesMap<Double, String> map = new MinValuesMap<Double, String>(2);
		
		Double v1 = Math.PI;
		assertTrue( map.addPoint( v1, "hello" ) );
		assertTrue( map.addPoint( v1, "goodbye" ) );
		assertTrue( map.getLastRemoved() == null );
		assertFalse( map.addPoint( v1, "hello again" ) );

		List<String> strings = map.getValues();
		assertEquals( strings.get( 0 ), "hello" );
		assertEquals( strings.get( 1 ), "goodbye" );

		Double v2 = Math.PI - 1.0;
		assertTrue( map.addPoint( v2, "hello again" ) );
		assertTrue( map.getLastRemoved().compareTo( "hello" ) == 0 ); 
		
		strings = map.getValues();
		assertEquals( strings.get( 0 ), "hello again" );
		assertEquals( strings.get( 1 ), "goodbye" );
		
		Double v3 = 1.0;
		map.addPoint( v3, "should replace goodbye" );
		
		strings = map.getValues();
		assertEquals( strings.get( 0 ), "should replace goodbye" );
		assertEquals( strings.get( 1 ), "hello again" );
		
		
	}
	
	public void test2() {
		MinValuesMap<Integer, Integer> map = new MinValuesMap<>( 3 );
		map.addPoint( 1, 1 );
		map.addPoint( 1, 2 );
		map.addPoint( 1, 3 );
		map.addPoint( 1, 4 );
		System.out.println(map.getLastRemoved());
		for (int i = 0; i < map.getValues().size(); i++) {
			System.out.println(map.getValues().get( i));			
		}
		System.out.println("hi");
		map.addPoint( 2, 1 );
		map.addPoint( 2, 1 );
		map.addPoint( 2, 2 );
		for (int i = 0; i < map.getValues().size(); i++) {
			System.out.println(map.getValues().get( i));			
		}
		System.out.println("hi");
		map.addPoint( 3, 1 );
		for (int i = 0; i < map.getValues().size(); i++) {
			System.out.println(map.getValues().get( i));			
		}
		map.addPoint( 4, 4 );
		map.addPoint( -1, 3 );
		map.addPoint( -2, 5 );
		for (int i = 0; i < map.getValues().size(); i++) {
			System.out.println(map.getValues().get( i));			
		}
		System.out.println("hi");
	}
}
