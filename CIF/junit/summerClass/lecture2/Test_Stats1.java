package summerClass.lecture2;

import summerClass.lecture2.Stats1;


public class Test_Stats1 extends junit.framework.TestCase {
	
	public void testNullValues() {
		double[] values = null;
		Exception expectedError = null;
		try {
			new Stats1( values );
		} catch (Exception e) {
			expectedError = e;
		}
		assertTrue( expectedError != null );
	}
	
	public void testLessThan2Values() {
		double[] values = { 1 };
		Exception expectedError = null;
		try {
			new Stats1( values );
		} catch (Exception e) {
			expectedError = e;
		}
		assertTrue( expectedError != null );
	}
	
	public void test2Values() {
		double[] values = { 1, 2 };
		Exception expectedError = null;
		try {
			new Stats1( values );
		} catch (Exception e) {
			expectedError = e;
		}
		assertTrue( expectedError == null );
	}
	
	public void test30Values() {
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
		/* Note - This is R code that was used to check the calculation
			> sd(1:30)
			[1] 8.803408
			>
		 */
		double tolerance = 0.0001;
		double valueObtainedFromRCalculation = 8.803408;
		junit.framework.TestCase.assertEquals(  s, valueObtainedFromRCalculation, tolerance );
	}

}
