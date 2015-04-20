package myMultiThreadMonteCarlo;

import myMonteCarlo.DataWrapper.OptionType;
import myMonteCarlo.Path.GBMPath;
import myMonteCarlo.Path.StockPath;
import myMonteCarlo.PayOut.AsianCall;
import myMonteCarlo.PayOut.EuropeanCall;
import myMonteCarlo.PayOut.PayOut;

import org.joda.time.DateTime;

/**
 * This solves a single MC pricing problem using monte carlo. Every time it generate a new simulation, it sends this
 * simulation's result to the given solution collector untill the global solution is achieved.
 * @author Zhenghong Dong
 */
public class MonteCarloSimulationProblem implements Runnable {
	private final double						_currentPrice;
	private final double						_dailyVol;
	private final double						_dailyRate;
	private final int							_TTM;
	private final double						_strike;
	private final OptionType					_type;
	private final I_SolutionCollector<Double>	_collector;

	
	
	
	/***********************************************************************
	 * Constructor
	 ***********************************************************************/
	/**
	 * @param spec contains detail about this pricing problem
	 * @param solutionCollector where should we send the result to
	 */
	public MonteCarloSimulationProblem(final I_PricingProblemSpecification spec,
			final I_SolutionCollector<Double> solutionCollector) {
		_currentPrice = spec.getCurrentPrice();
		_dailyRate = spec.getDailyRate();
		_dailyVol = spec.getDailyVol();
		_type = spec.getType();
		_TTM = spec.getTTM();
		_strike = spec.getStrike();
		_collector = solutionCollector;
	}

	/***********************************************************************
	 * Utilities
	 ***********************************************************************/
	@Override
	public void run() {
		// set up the simulation (set up here instead of in constructor so different threads have different RVG)
		final StockPath path = new GBMPath( DateTime.now(), _currentPrice, _dailyRate, _dailyVol, _TTM );
		PayOut po = null;
		if (_type == OptionType.EuropeanCall) {
			po = new EuropeanCall( _strike );
		} else if (_type == OptionType.AsianCall) {
			po = new AsianCall( _strike );
		}
		try {
			// do a new simulation while the collector isn't finished (which means the simulation result is not accurate enough)
			while (!_collector.isFinished()) {
				_collector.receiveSolution( po.getPayout( path ) );
			}
		}
		// when po is null, which will arise when type is neither EuropeanCall nor AsianCall
		catch (final NullPointerException e) {
			System.err.println( 
			"MonteCarloSingleSimulation: Call: Inapproporate type - shoule be AsianCall/EuropeanCall but " + _type );
		}
	}
	
	// for test only
	protected double getStrike() { return _strike; }
}
