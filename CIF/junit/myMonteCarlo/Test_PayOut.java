package myMonteCarlo;

import java.util.ArrayList;
import java.util.List;

import myMonteCarlo.DataWrapper.PriceRecord;
import myMonteCarlo.Path.StockPath;
import myMonteCarlo.PayOut.AsianCall;
import myMonteCarlo.PayOut.EuropeanCall;
import myMonteCarlo.PayOut.PayOut;
import myMonteCarlo.PayOut.VARPayout;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test European Call payout and Asian Call payoutpayout.
 * 
 * @author Zhenghong Dong
 * 
 */
public class Test_PayOut {
	/**
	 * An artificial implementation of StockPath for testing PayOut Its path is 1,2,...,n
	 */
	private class mockPath implements StockPath {
		private final int	_n;

		public mockPath(final int n) { _n = n; }

		@Override
		public List<PriceRecord> getPrices() {
			final List<PriceRecord> list = new ArrayList<>();
			for (int i = 0; i < _n; i++) {
				list.add( new PriceRecord( DateTime.now(), i + 1 ) );
			}
			return list;
		}

	}

	@Test
	public void EuropeanCall() {
		final StockPath path = new mockPath( 10 );
		final PayOut po = new EuropeanCall( 5 );
		Assert.assertTrue( po.getPayout( path ) == 5 );
	}

	@Test
	public void AsianCall() {
		final StockPath path = new mockPath( 10 );
		final PayOut po = new AsianCall( 5 );
		Assert.assertTrue( po.getPayout( path ) == 0.5 );
	}
	
	@Test
	public void VarPayout(){
		final StockPath path = new mockPath( 10000 );
		final PayOut po = new VARPayout( 0.001 );
		Assert.assertTrue( po.getPayout( path ) == 10 );
	}
}
