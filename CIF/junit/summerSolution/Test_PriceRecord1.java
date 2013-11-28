package summerSolution;

import utils1309.DoubleComparator;

public class Test_PriceRecord1 extends junit.framework.TestCase {

	public void testConstruction() throws Exception {
		String dataLine = "9/10/1964,29.3,30.1,28.9,29.9,1000000,50.00";
		PriceRecord pr1 = new PriceRecord( dataLine );
		assertTrue( pr1.getDate() == 19640910 );
		assertTrue( DoubleComparator.equal( pr1.getOpen(), 29.3, 0.0001 ) );
		assertTrue( DoubleComparator.equal( pr1.getHigh(), 30.1, 0.0001 ) );
		assertTrue( DoubleComparator.equal( pr1.getLow(), 28.9, 0.0001 ) );
		assertTrue( DoubleComparator.equal( pr1.getClose(), 29.9, 0.0001 ) );
		assertTrue( pr1.getVolume() == 1000000 );
		assertTrue( DoubleComparator.equal( pr1.getAdjustedClose(), 50.00, 0.0001 ) );
	}
	
}
