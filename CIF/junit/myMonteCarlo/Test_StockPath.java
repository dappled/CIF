package myMonteCarlo;

import java.util.List;

import myMonteCarlo.DataWrapper.PriceRecord;
import myMonteCarlo.Path.GBMPath;
import myMonteCarlo.Path.StockPath;
import myMonteCarlo.Path.VARPath;
import myMonteCarlo.PayOut.PayOut;
import myMonteCarlo.PayOut.VARPayout;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test StockPath interface with geometric brownian motion stock path implementation
 * 
 * @author Zhenghong Dong
 * 
 */
public class Test_StockPath {

	/**
	 * test if one geometric brownian motion stock path is valid
	 */
	@Test
	public void testGBMPath() {
		final StockPath path = new GBMPath( DateTime.now(), 152.35, 0.0001, 0.01, 252 );
		final List<PriceRecord> tm = path.getPrices();
		Assert.assertTrue( validateDate( tm ) ); // test date are sorted
		Assert.assertTrue( tm.size() == 253 ); // test record length
		Assert.assertFalse( isDifferent( tm, tm ) ); // test isDifferent

		// test if we get a new path every time
		final List<PriceRecord> tm2 = path.getPrices();
		Assert.assertTrue( tm2.size() == 253 );
		Assert.assertTrue( isDifferent( tm, tm2 ) );
	}
	
	@Test
	public void testVARPath() {
		double[] price = {47, 144, 31};
		double[] vol = {0.4, 0.45, 0.3};
		double[] shares = {100000, -40000, 10000};
		double[][] cov = {{ 1,   0.6, 0.2 },
						  { 0.6, 1,   0.4 },
						  { 0.2, 0.4, 1   }};
		final StockPath path = new VARPath( DateTime.now(), price, vol, shares, cov, 1/252.0, 100000 );
		PayOut po = new VARPayout( 0.01 );
		System.out.println(po.getPayout( path ));
	}

	/**
	 * helper function test if given PriceRecord is sorted by date
	 * 
	 * @param date: a List of PriceRecord to be validated
	 */
	private boolean validateDate(final List<PriceRecord> record) {
		DateTime prev = null;
		for (final PriceRecord tmp : record) {
			if (prev == null) ;
			else if (prev.isAfter( tmp.getDate() )) return false;
			prev = tmp.getDate();
		}
		return true;
	}
	
	/**
	 * helper function test if two PriceRecord contains different price path
	 * 
	 * @param rec1
	 * @param rec2
	 * @return false if two PriceRecord has same price path
	 */
	private boolean isDifferent(final List<PriceRecord> rec1, final List<PriceRecord> rec2) {
		for (int i = 0; i < rec1.size(); i++) {
			if (rec1.get( i ).getPrice() != rec2.get( i ).getPrice()) return true;
		}
		return false;
	}
	
}
