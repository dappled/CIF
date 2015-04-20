package myMultiThreadMonteCarlo;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import myMonteCarlo.DataWrapper.OptionType;

/**
 * PooledMCManager holds a pool of 10 {@link PooledWorker}. Any time we want to solve a pricing problem using monte
 * carlo, we can give the problem to this manager, it will assign up to 3 threads to the problem.
 * @author Zhenghong Dong
 */
public class PooledMonteCarloManager {
	protected final BlockingQueue<MonteCarloSimulationProblem>	_taskQueue;
	protected final PooledWorker<MonteCarloSimulationProblem>[]	_threads;
	protected boolean											_isStopped	= false;

	/***********************************************************************
	 * Constructor
	 ***********************************************************************/
	@SuppressWarnings("unchecked")
	public PooledMonteCarloManager(final int noOfThreads) {
		// initialize queue
		_taskQueue = new LinkedBlockingQueue<>();
		// initialize workers
		_threads = new PooledWorker[ noOfThreads ];
		for (int i = 0; i < noOfThreads; i++) {
			_threads[ i ] = new PooledWorker<MonteCarloSimulationProblem>( _taskQueue );
		}

		// start workers
		for (final PooledWorker<MonteCarloSimulationProblem> thread : _threads) {
			thread.start();
		}
	}

	/***********************************************************************
	 * Utilities
	 ***********************************************************************/
	/**
	 * PooledMonteCarloManager will assign the problem to up to three workers by offering this problem to the
	 * blocking queue connecting {@link PooledWorkers} up to three times. If none of the workers is free, we will put
	 * the problem on the blockingQueue, so later the problem will be picked up and solved when a worker is free.
	 * @param spec specifies the pricing problem we want to solve using monte carlo
	 * @param solutionCollector the sollution collector to collect monte carlo problem
	 * @throws InterruptedException
	 * @throws Exception
	 */
	public synchronized void solve(final I_PricingProblemSpecification spec,
			final I_SolutionCollector<Double> solutionCollector) throws InterruptedException {
		if (_isStopped) throw new IllegalStateException( "CalculatorPool is stopped" );

		MonteCarloSimulationProblem problem = new MonteCarloSimulationProblem( spec, solutionCollector );
		solveProblem( problem );

	}

	/**
	 * Isolated solve problem part so we can test on fake problem.
	 * @param problem
	 * @throws InterruptedException
	 */
	protected synchronized void solveProblem(final MonteCarloSimulationProblem problem) throws InterruptedException {
		int ret = 0;
		// Offer task at most three times while there are free workers, note that we always offer a problem even all
		// workers are busy (then this problem will stay in the queue waiting for someone to be free to pick it up),
		// otherwise if there are more free workers we will assign up to three of them on this problem (so they will
		// corporate to solve this problem together).
		do {
			_taskQueue.offer( problem );
			// wait for context switch, so free workers have chance to pick up this problem and then queue will be empty
			// again
			this.wait( 1 );
		} while (++ret < 3 && _taskQueue.size() == 0);
		// If there are less than 3 workers in the queue, we will first assign this problems to the remaining one or two
		// free workers (they will corporate to solve this problem together), then the above loop will offer the same
		// problem one extra time before leaving the loop. That extra time is redundant and should be removed.
		if (ret > 1 && _taskQueue.size() != 0) {
			_taskQueue.remove( problem );
		}
	}

	/**
	 * Stop this pool, and all workers swimming in this pool
	 */
	public synchronized void shutDown() {
		_isStopped = true;
		for (final PooledWorker<MonteCarloSimulationProblem> thread : _threads) {
			thread.callStop();
		}
	}

	public static void main(final String[] args) throws Exception {
		final PooledMonteCarloManager manager = new PooledMonteCarloManager( 10 );
		final double p = 152.35, sigma = 0.01, r = 0.0001, stop = 0.96;
		final int ttm = 252, maxSimulation = 10000000;

		final long startTime = System.currentTimeMillis();
		// 1st question: European Call option, with strike at $165,
		final I_PricingProblemSpecification problem1 = new MonteCarloPricingProblemSpecification(
				OptionType.EuropeanCall, p, sigma, r, ttm, 165 );
		final I_SolutionCollector<Double> solution1 = new MonteCarloSolutionCollector( r, ttm, 0.01, stop,
				maxSimulation );
		manager.solve( problem1, solution1 );
		// 2nd question: Asian Call option, with strike at $164,
		final I_PricingProblemSpecification problem2 = new MonteCarloPricingProblemSpecification( OptionType.AsianCall,
				p, sigma, r, ttm, 164 );
		final I_SolutionCollector<Double> solution2 = new MonteCarloSolutionCollector( r, ttm, 0.01, stop,
				maxSimulation );
		manager.solve( problem2, solution2 );

		// wait untill we get both solutions
		while (!(solution1.isFinished() && solution2.isFinished()))
		{
			Thread.sleep( 300 );
		}
		// the 1st result is about $6.21
		System.out.printf( "<Call Option> Average profit: %.2f\n", solution1.getSolution() );
		// the 2nd result is about $2.19
		System.out.printf( "<Asian Option> Average profit: %.2f\n", solution2.getSolution() );
		final long endTime = System.currentTimeMillis();
		final long totalTime = endTime - startTime;
		System.out.println( totalTime );

		// shut down the pool
		manager.shutDown();
	}
}
