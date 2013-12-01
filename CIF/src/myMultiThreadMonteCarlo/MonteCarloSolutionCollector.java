package myMultiThreadMonteCarlo;

import org.apache.commons.math3.distribution.NormalDistribution;

import myMonteCarlo.Stats.I_PathStats;
import myMonteCarlo.Stats.PathStat;

/**
 * This is an implementation of {@link I_SolutionCollector}. A monte carlo solution collector holds a I_PathStats that
 * stores all partial solutions. When certain criteria is satisfied, it set isFnished to true so we can get the complete
 * solution.
 * @author Zhenghong Dong
 */
public class MonteCarloSolutionCollector implements I_SolutionCollector<Double> {
	private final I_PathStats<Double>	_stats;
	private final double				_dailyRate;
	private final double				_TTM;
	private final double				_err;
	private final double				_tol;
	private final int					_maxSimulation;
	private boolean						_isFinished	= false;

	/***********************************************************************
	 * Constructor
	 ***********************************************************************/
	/**
	 * @param dailyRate daily rfr rate
	 * @param TTM time to maturity
	 * @param err the percentage of error range we can bear
	 * @param stoppingCriteria by how much probability we are confident that the option price has converged
	 * @param maxSimulation max number of simulation we can run
	 */
	public MonteCarloSolutionCollector(final double dailyRate, final int TTM, final double err,
			final double stoppingCriteria, final int maxSimulation) {
		_stats = PathStat.<Double> newPathStat();
		_dailyRate = dailyRate;
		_TTM = TTM;
		_err = err;
		_tol = new NormalDistribution().inverseCumulativeProbability( 1 - (1 - stoppingCriteria) / 2 );
		_maxSimulation = maxSimulation;
	}

	/***********************************************************************
	 * Utilities
	 ***********************************************************************/
	/**
	 * Receive a partial solution and set isFinish flag to true if we have fulfilled the stopping criteria.
	 */
	@Override
	public synchronized void receiveSolution(final Double solution) {
		_stats.addPathStat( solution );
		if ((_stats.sampleVariance() != 0 && _err * _stats.sampleMean() * Math.sqrt( _stats.sampleSize() )
				/ Math.sqrt( _stats.sampleVariance() ) > _tol) || _stats.sampleSize() >= _maxSimulation) {
			_isFinished = true;
		}
	}

	/**
	 * @return null if fail to converge to the answer with specific stopping criteria, else return the discounted value
	 *         of the option
	 */
	@Override
	public Double getSolution() {
		if (_stats.sampleSize() >= _maxSimulation) return null;
		return _stats.sampleMean() * Math.exp( -_dailyRate * _TTM );
	}

	@Override
	public boolean isFinished() {
		return _isFinished;
	}
}
