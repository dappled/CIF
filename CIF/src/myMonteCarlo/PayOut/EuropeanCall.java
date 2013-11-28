package myMonteCarlo.PayOut;

import java.util.List;

import myMonteCarlo.DataWrapper.PriceRecord;
import myMonteCarlo.Path.StockPath;

/**
 * European call option payout, a concrete implementation of PayOut
 * 
 * @author Zhenghong Dong
 * 
 */
public class EuropeanCall implements PayOut {
	private final double	_strike;

	/**
	 * @param strike: the strike of this option
	 */
	public EuropeanCall(final double strike) {
		_strike = strike;
	}

	@Override
	public double getPayout(final StockPath path) {
		final List<PriceRecord> record = path.getPrices();
		return Math.max( record.get( record.size() - 1 ).getPrice() - _strike, 0 );
	}

}
