package myMonteCarlo.Path;

import java.util.ArrayList;
import java.util.List;

import myMonteCarlo.DataWrapper.PriceRecord;
import myMonteCarlo.RVG.AntitheticRVG;
import myMonteCarlo.RVG.NormalRVG;

import org.joda.time.DateTime;

/**
 * Geometric brownian motion stock price, a concrete implementation of StockPath
 * 
 * @author Zhenghong Dong
 * 
 */
public class GBMPath implements StockPath {
	private final AntitheticRVG			_rvg;
	private final DateTime				_startDate;
	private final double				_startPrice;
	private final double				_r, _sigma;

	/**
	 * Constructor
	 * @param startDate: start date
	 * @param startPrice: start price of underlying
	 * @param r: interest rate of each time interval
	 * @param sig: volatility of each time interval
	 * @param n: total number of time interval
	 */
	public GBMPath(final DateTime startDate, final double startPrice, final double r, final double sig, final int n) {
		_rvg = new AntitheticRVG( new NormalRVG( n ) );
		_startDate = startDate;
		_startPrice = startPrice;
		_r = r;
		_sigma = sig;
	}

	@Override
	public List<PriceRecord> getPrices(){
		List<PriceRecord> path = new ArrayList<>();
		path.add( new PriceRecord(_startDate, _startPrice) );
		DateTime date = _startDate;
		final double[] z = _rvg.getVector();
		double price = _startPrice;
		for (final double stdNm : z) {
			price = price * Math.exp( (_r - Math.pow( _sigma, 2 ) / 2) + _sigma * stdNm );
			date = date.plusDays( 1 );
			path.add( new PriceRecord( date, price ) );
		}
		return path;
	}

}
