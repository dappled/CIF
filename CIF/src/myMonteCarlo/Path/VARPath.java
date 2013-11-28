package myMonteCarlo.Path;

import java.util.ArrayList;
import java.util.List;

import myMonteCarlo.DataWrapper.PriceRecord;
import myMonteCarlo.RVG.MultiNormal;
import myMonteCarlo.RVG.RandomVectorGenerator;

import org.joda.time.DateTime;

/**
 * @author Zhenghong Dong
 */
public class VARPath implements StockPath {
	private final RandomVectorGenerator	_rvg;
	private final DateTime		_startDate;
	private final double[]		_price, _vol, _shares;
	private final int			_n;
	private final double		_t;

	/**
	 * Constructor
	 * @param startDate: start date
	 * @param price: price vector
	 * @param vol: annual volatility vector
	 * @param shares: shares vector
	 * @param cov: covariance matrix
	 * @param time: holding period
	 * @param n: total number of simulations
	 */
	public VARPath(final DateTime startDate, final double[] price, final double[] vol, final double[] shares,
			final double[][] cov, final double time, final int n) {
		_rvg = new MultiNormal( cov );
		_startDate = startDate;
		_price = price;
		_vol = vol;
		_shares = shares;
		_n = n;
		_t = time;
	}

	@Override
	public List<PriceRecord> getPrices() {
		final List<PriceRecord> path = new ArrayList<>();
		DateTime date = _startDate;
		double price = 0;

		double[] delta = new double[ _price.length ];
		double[] z;
		for (int i = 0; i < _n; i++) {
			z = _rvg.getVector();
			price = 0;
			for (int j = 0; j < z.length; j++) {
				delta[ j ] = _vol[ j ] * _price[ j ] * Math.sqrt( _t ) * z[ j ];
				price += _shares[ j ] * delta[ j ];
			}
			date = date.plusDays( 1 );
			path.add( new PriceRecord( date, price ) );
		}
		return path;
	}
}
