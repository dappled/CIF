package summerClass.lecture2;

import summerClass.lecture2.DoubleComparator;
import summerClass.lecture2.Stats1;
import summerClass.lecture2.Stats2;


public class Test_Stats2 extends junit.framework.TestCase {

	public void test1() throws Exception {
		Stats2 stats2 = new Stats2();
		for( int i = 0; i < 3; i++ )
			stats2.add( i );
		double std = stats2.getSampleStd();
		assertTrue( DoubleComparator.equal( std, 1.0, 0.00001 ) );
	}
	
	public void test2() throws Exception {
		double[] values = {};
		Exception expectedError = null;
		try {
			new Stats1( values );
		} catch (Exception e) {
			expectedError = e;
		}
		assertTrue( expectedError != null );
	}
	
	public void test3() {
		double[] values = new double[ 30 ];
		for( int i = 0; i < 30; i++ ) {
			values[ i ] = i;
		}
		boolean noError = true;
		Stats1 stats1 = null;
		try {
			stats1 = new Stats1( values );
		} catch (Exception e) {
			noError = false;
		}
		junit.framework.TestCase.assertTrue( noError );
		double s = stats1.getSampleStd();
		/*
			> sd(1:30)
			[1] 8.803408
			>
		 */
		junit.framework.TestCase.assertTrue( DoubleComparator.equal( s, 8.803408, 0.0001 ) );
	}

}
