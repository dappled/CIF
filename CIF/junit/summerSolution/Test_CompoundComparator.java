package summerSolution;

import java.util.Collections;
import java.util.LinkedList;

import utils1309.CompoundComparator;
import utils1309.DoubleComparator;
import utils1309.SortOrder;

public class Test_CompoundComparator extends junit.framework.TestCase {
	
	public void test1() throws Exception {
		
		LinkedList<PriceRecord> priceRecords = new LinkedList<PriceRecord>();
		priceRecords.add( new PriceRecord( "01/01/2001,100,100,100,100,2000000,101" ) );
		priceRecords.add( new PriceRecord( "01/01/2001,100,100,100,100,2000000,100" ) );
		priceRecords.add( new PriceRecord( "01/01/2001,100,100,100,100,2000000,99" ) );
		priceRecords.add( new PriceRecord( "01/01/2001,100,100,100,100,2000000,98" ) );
		priceRecords.add( new PriceRecord( "01/02/2001,100,100,100,100,2000000,97" ) );
		priceRecords.add( new PriceRecord( "01/02/2001,100,100,100,100,2000000,96" ) );
		priceRecords.add( new PriceRecord( "01/02/2001,100,100,100,100,2000000,95" ) );
		priceRecords.add( new PriceRecord( "01/02/2001,100,100,100,100,2000000,94" ) );
		
		CompoundComparator<PriceRecord> cc = new CompoundComparator<PriceRecord>();
		cc.addComparator( PriceRecord.getDateComparator( SortOrder.DESCENDING ) );
		cc.addComparator( PriceRecord.getAdjustedCloseComparator( SortOrder.ASCENDING, 0.000001 ) );
		
		Collections.sort( priceRecords, cc );
		
		// Make sure everything sorted properly
		PriceRecord prevPriceRecord = null;
		do {
			PriceRecord priceRecord = priceRecords.remove();
			System.out.println( priceRecord.toString() );
			if( prevPriceRecord != null )
				assertTrue(
					( priceRecord.getDate() <= prevPriceRecord.getDate() )
					&&
					( 
						DoubleComparator.compare (
							priceRecord.getAdjustedClose(),
							prevPriceRecord.getAdjustedClose(),
							0.0001
						) > 0
					)
				);
			prevPriceRecord = priceRecord;
		} while( priceRecords.size() != 0 );
		
	}

}
