package myMonteCarlo.PayOut;

import java.util.Collections;
import java.util.List;

import myMonteCarlo.DataWrapper.PriceRecord;
import myMonteCarlo.Path.StockPath;

/**
 * @author Zhenghong Dong
 */
public class VARPayout implements PayOut {
	private final double _per;

	/**
	 * @param strike: the strike of this option
	 */
	public VARPayout(final double percent) {
		_per = percent;
	}

	@Override
	public double getPayout(final StockPath path) {
		final List<PriceRecord> record = path.getPrices();
		Collections.sort( record );
		return record.get( Math.max( ((int) (_per*record.size())) - 1, 0) ).getPrice();
	}
}
