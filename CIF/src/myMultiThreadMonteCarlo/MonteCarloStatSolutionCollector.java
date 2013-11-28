package myMultiThreadMonteCarlo;

import myMonteCarlo.Stats.I_PathStats;
import myMonteCarlo.Stats.PathStat;

/**
 * @author Zhenghong Dong
 */
public class MonteCarloStatSolutionCollector implements I_SolutionCollector {
	private final I_PathStats<Double>	_stats;
	private final double				_dailyRate;
	private final double				_TTM;
	private final double				_err;
	private final double				_tol;
	private final int					_maxSimulation;
	private boolean						_isFinished	= false;

	public MonteCarloStatSolutionCollector(final double dailyRate, final double TTM, final double err,
			final double tol, final int maxSimulation) {
		_stats = PathStat.<Double> newPathStat();
		_dailyRate = dailyRate;
		_TTM = TTM;
		_err = err;
		_tol = tol;
		_maxSimulation = maxSimulation;
	}

	@Override
	public synchronized void receiveSolution(final double solution) {
		if ((_stats.sampleVariance() == 0 || _err * _stats.sampleMean() * Math.sqrt( _stats.sampleSize() )
				/ Math.sqrt( _stats.sampleVariance() ) < _tol) && _stats.sampleSize() < _maxSimulation) {
			_stats.addPathStat( solution );
		}
		else {
			_isFinished = true;
		}
	}

	/**
	 * @return -1 if fail to converge to the answer with specific stopping criteria, else return the discounted value of
	 *         the option
	 */
	@Override
	public double getSolution() {
		if (_stats.sampleSize() >= _maxSimulation) return -1;
		return _stats.sampleMean() * Math.exp( -_dailyRate * _TTM );
	}

	@Override
	public boolean isFinished() {
		return _isFinished;
	}
}
