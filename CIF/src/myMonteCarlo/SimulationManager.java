package myMonteCarlo;

import myMonteCarlo.DataWrapper.OptionType;
import myMonteCarlo.Path.GBMPath;
import myMonteCarlo.Path.StockPath;
import myMonteCarlo.PayOut.AsianCall;
import myMonteCarlo.PayOut.EuropeanCall;
import myMonteCarlo.PayOut.PayOut;
import myMonteCarlo.Stats.I_PathStats;
import myMonteCarlo.Stats.PathStat;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.joda.time.DateTime;

/**
 * Simulate Manager which can simulate European Call and Asian Call using monte carlo
 * 
 * @author Zhenghong Dong
 * 
 */
public class SimulationManager {
	private final OptionType	_type;
	private final double		_currentPrice;
	private final double		_dailyVol;
	private final double		_dailyRate;
	private final int			_ttm;
	private final double		_strike;
	private final DateTime		_start;
	private final double		_err;
	private final double		_tol;
	private final int			_maxSimulation;

	/**
	 * Simulation Manager that can calculate an option's price given certain inputs.
	 * 
	 * @param type
	 *            : type of option, in this homework we only have european call and asian call.
	 * @param currentPrice
	 *            : current price of underlying
	 * @param dailyVol
	 *            : daily volatility
	 * @param dailyRate
	 *            : overnight spot rate
	 * @param TTM
	 *            : time to maturity
	 * @param strike
	 *            : strike price of this option
	 * @param startDate
	 *            : starting date of this option
	 * @param estimationErr
	 *            : how much error we can bear in price, 1% means 1% of "true mean"
	 * @param stoppingCriteria
	 *            : by how much probability we believe the option price has converged
	 * @param maxSimulation
	 *            : maximum number of simulations we allowed
	 */
	public SimulationManager(final OptionType type, final double currentPrice, final double dailyVol,
			final double dailyRate, final int TTM,
			final double strike, final DateTime startDate, final double estimationErr, final double stoppingCriteria,
			final int maxSimulation) {
		_type = type;
		_currentPrice = currentPrice;
		_dailyVol = dailyVol;
		_dailyRate = dailyRate;
		_ttm = TTM;
		_strike = strike;
		_start = startDate;
		_err = estimationErr;
		_tol = new NormalDistribution().inverseCumulativeProbability( 1 - (1 - stoppingCriteria) / 2 );
		_maxSimulation = maxSimulation;
	}

	public double getPrice() throws Exception {
		final StockPath path = new GBMPath( _start, _currentPrice, _dailyRate, _dailyVol, _ttm );
		final PayOut po;
		if (_type == OptionType.EuropeanCall) {
			po = new EuropeanCall( _strike );
		} else {
			po = new AsianCall( _strike );
		}

		final I_PathStats<Double> stat = PathStat.newPathStat();
		int n = 0;
		while ((stat.sampleVariance() == 0 || _err * stat.sampleMean() * Math.sqrt( n ) / Math.sqrt( stat.sampleVariance() ) < _tol)
				&& n < _maxSimulation) {
			stat.addPathStat( po.getPayout( path ) );
			n++;
		}
		if (n >= _maxSimulation) throw new Exception( "Fail to converge." );
		System.out.println( "n: " + n );
		return stat.sampleMean() * Math.exp( -_dailyRate * _ttm ); // return discounted price
	}


	public static void main(final String[] args) throws Exception {
		SimulationManager sm;
		final double p = 152.35, sigma = 0.01, r = 0.0001, stop = 0.96;
		final int ttm = 252, maxSimulation = 10000000;

		long startTime = System.currentTimeMillis();
		// 1st question: European Call option, with strike at $165,
		sm = new SimulationManager( OptionType.EuropeanCall, p, sigma, r, ttm, 165, DateTime.now(), 0.01, stop,
				maxSimulation );
		// the result is about $6.21
		System.out.printf( "<Call Option> Average profit: %.2f\n", sm.getPrice() );

		/*// 2nd question: Asian Call option, with strike at $164,
		sm = new SimulationManager( OptionType.AsianCall, p, sigma, r, ttm, 164, DateTime.now(), 0.01, stop,
				maxSimulation );
		// the result is about $2.18
		System.out.printf( "<Asian Option> Average profit: %.2f\n", sm.getPrice() );*/
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println( totalTime );

	}
}
