package summerSolution;

import utils1309.I_MovingAverage;
import utils1309.MovingAverage;

public class Test_MovingAverage extends junit.framework.TestCase {
	
	public void test1() {
		
		// Instantiate a moving average using the class's static factor method
		
			I_MovingAverage ma = MovingAverage.newMovingAverage( 3 );
			
		// Make sure it's not reporting that it's ready	
		
			assertFalse( ma.isReady() );
			
		// Make sure that it throws an exception if we try to retrieve the average
			
			Exception error = null;
			try {
				ma.getAverage();
			} catch (Exception e) {
				error = e;
			}
			assertTrue( error != null );
			
		// Add two elements and make sure it still reports that it's not ready
		
			ma.add( 5 );
			ma.add( 6 );
			assertFalse( ma.isReady() );
			
		// Add one more element and make sure that it reports that it is now ready
		
			ma.add( 7 );
			assertTrue( ma.isReady() );
			
		// Check calculation of moving average

			error = null;
			try {
				assertEquals( ma.getAverage(), 6.0, 0.001 );
			} catch (Exception e) {
				error = e;
			}
			assertTrue( error == null );
			
		// Add another element and check to make sure it dropped the first element
		// from its calculations
			
			ma.add( 8 );
			error = null;
			try {
				assertEquals( ma.getAverage(), 7.0, 0.001 );
			} catch (Exception e) {
				error = e;
			}
			assertTrue( error == null );

	}

}
