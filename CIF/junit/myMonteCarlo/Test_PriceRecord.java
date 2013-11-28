package myMonteCarlo;

import myMonteCarlo.DataWrapper.PriceRecord;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test PriceRecord
 * 
 * @author Zhenghong Dong
 * 
 */
public class Test_PriceRecord {

	@Test
	public void test1() {
		PriceRecord record = null;
		// test constructor
		Exception error = null;
		try {
			record = new PriceRecord( DateTime.now(), 0 );
		} catch (final Exception e) {
			error = e;
		}
		Assert.assertTrue( error == null );

		// test getDate, getPrice
		Assert.assertTrue( record.getDate().toLocalDate().equals( LocalDate.now() ) );
		Assert.assertTrue( record.getPrice() == 0 );

		// print record
		System.out.println( record.toString() );

	}

}
