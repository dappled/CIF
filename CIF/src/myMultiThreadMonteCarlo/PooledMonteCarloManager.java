package myMultiThreadMonteCarlo;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import myMonteCarlo.DataWrapper.OptionType;

import org.apache.commons.math3.distribution.NormalDistribution;

/**
 * @author Zhenghong Dong
 */
public class PooledMonteCarloManager {
	private final BlockingQueue<MonteCarloSimulationJob>			_taskQueue;
	private final PooledMonteCarloWorker<MonteCarloSimulationJob>[]	_threads;
	private boolean													_isStopped	= false;

	@SuppressWarnings("unchecked")
	public PooledMonteCarloManager(final int noOfThreads) {
		// initialize queue.
		_taskQueue = new LinkedBlockingQueue<>();
		// initialize workers
		_threads = new PooledMonteCarloWorker[ noOfThreads ];
		for (int i = 0; i < noOfThreads; i++) {
			_threads[ i ] = new PooledMonteCarloWorker<MonteCarloSimulationJob>( _taskQueue );
		}

		// start workers
		for (final PooledMonteCarloWorker<MonteCarloSimulationJob> thread : _threads) {
			thread.start();
		}
	}

	public synchronized void solve(final I_PricingProblemSpecification spec, final I_SolutionCollector solutionCollector) {
		if (_isStopped) throw new IllegalStateException( "CalculatorPool is stopped" );

		try {
			int ret = 0;
			// while _taskQueue has available threads, offer task to it
			while (_taskQueue.offer( new MonteCarloSimulationJob( spec, solutionCollector ) )) {
				// if task fully filled, return
				if (++ret == 3) return;
			}
			// if partially filled, return
			if (ret > 0) return;
			else {
				_taskQueue.put( new MonteCarloSimulationJob( spec, solutionCollector ) );
			}
		} catch (final InterruptedException e) {
			System.err.println( "CalculatorPool: " + e.getMessage() );
			e.printStackTrace();
		}

	}

	/**
	 * Stop this pool, and all workers swimming in this pool
	 */
	public synchronized void shutDown() {
		_isStopped = true;
		for (final PooledMonteCarloWorker<MonteCarloSimulationJob> thread : _threads) {
			thread.callStop();
		}
	}

	public static void main(final String[] args) throws InterruptedException {
		final PooledMonteCarloManager manager = new PooledMonteCarloManager( 10 );
		final double p = 152.35, sigma = 0.01, r = 0.0001, stop = 0.96;
		final int ttm = 252, maxSimulation = 10000000;
		final double tol = new NormalDistribution().inverseCumulativeProbability( 1 - (1 - stop) / 2 );

		final long startTime = System.currentTimeMillis();
		// 1st question: European Call option, with strike at $165,
		final I_PricingProblemSpecification problem1 = new MonteCarloPricingProblemSpecification( OptionType.EuropeanCall, p,
				sigma, r, ttm, 165 );
		final I_SolutionCollector solution1 = new MonteCarloStatSolutionCollector( r, ttm, 0.01, tol, maxSimulation );
		manager.solve( problem1, solution1 );
		// 2nd question: Asian Call option, with strike at $164,
		final I_PricingProblemSpecification problem2 = new MonteCarloPricingProblemSpecification( OptionType.AsianCall, p,
				sigma, r, ttm, 164 );
		final I_SolutionCollector solution2 = new MonteCarloStatSolutionCollector( r, ttm, 0.01, tol, maxSimulation );
		manager.solve( problem2, solution2 );

		// the result is about $6.21
		while (!(solution1.isFinished() && solution2.isFinished()))
		{
			Thread.sleep( 300 );
		}
		System.out.printf( "<Call Option> Average profit: %.2f\n", solution1.getSolution() );
		// the result is about $2.18
		System.out.printf( "<Asian Option> Average profit: %.2f\n", solution2.getSolution() );
		final long endTime = System.currentTimeMillis();
		final long totalTime = endTime - startTime;
		System.out.println( totalTime );
		manager.shutDown();
	}
}
