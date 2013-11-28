package myMultiThreadMonteCarlo;

import myMonteCarlo.DataWrapper.OptionType;
import myMonteCarlo.Path.GBMPath;
import myMonteCarlo.Path.StockPath;
import myMonteCarlo.PayOut.AsianCall;
import myMonteCarlo.PayOut.EuropeanCall;
import myMonteCarlo.PayOut.PayOut;

import org.joda.time.DateTime;

/**
 * @author Zhenghong Dong
 */
public class MonteCarloSimulationJob implements Runnable {
	private final double				_currentPrice;
	private final double				_dailyVol;
	private final double				_dailyRate;
	private final int					_TTM;
	private final double				_strike;
	private final OptionType			_type;
	private final I_SolutionCollector	_collector;

	/**
	 * MonteCarloSimulationJob will solve a pricing problem using monte carlo, and sends the result to
	 * soluctionClollector
	 * @param spec contains detail about this pricing problem
	 * @param solutionCollector where should we send the result to
	 */
	public MonteCarloSimulationJob(final I_PricingProblemSpecification spec, final I_SolutionCollector solutionCollector) {
		_currentPrice = spec.getCurrentPrice();
		_dailyRate = spec.getDailyRate();
		_dailyVol = spec.getDailyVol();
		_type = spec.getType();
		_TTM = spec.getTTM();
		_strike = spec.getStrike();
		_collector = solutionCollector;
	}

	/** Everytime it calculated a simulation result, send to solutionCollector, until the solutionCollector is finished */
	@Override
	public void run() {	
		// get simulation result given data
		final StockPath path = new GBMPath( DateTime.now(), _currentPrice, _dailyRate, _dailyVol, _TTM );
		final PayOut po;
		if (_type == OptionType.EuropeanCall) {
			po = new EuropeanCall( _strike );
		} else if (_type == OptionType.AsianCall) {
			po = new AsianCall( _strike );
		} else {
			po = null;
		}
		try {
			while (!_collector.isFinished()) {
				_collector.receiveSolution( po.getPayout( path ) );
			}
		} catch (final Exception e) {
			System.err
			.println( "MonteCarloSingleSimulation: Call: Inapproporate type - shoule be AsianCall/EuropeanCall but "
					+ _type );
		}
	}

	protected double getStrike() {
		return _strike;
	}
}
