package myMonteCarlo.PayOut;

import java.util.List;

import myMonteCarlo.DataWrapper.PriceRecord;
import myMonteCarlo.Path.StockPath;

/**
 * AsianOption payoff, a concrete implementation of PayOut
 * 
 * @author Zhenghong Dong
 * 
 */
public class AsianCall implements PayOut {
	private final double	_strike;

	/**
	 * @param strike: the strike of this option
	 */
	public AsianCall(final double strike) {
		_strike = strike;
	}

	@Override
	public double getPayout(final StockPath path) {
		final List<PriceRecord> record = path.getPrices();
		double sum = 0;
		for (int j = 0; j < record.size(); j++) {
			sum += record.get( j ).getPrice();
		}
		return Math.max( sum / record.size() - _strike, 0 );
	}
}
