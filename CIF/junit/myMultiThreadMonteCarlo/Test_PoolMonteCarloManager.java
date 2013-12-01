package myMultiThreadMonteCarlo;

import static org.junit.Assert.assertTrue;

import java.util.concurrent.BlockingQueue;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Test {@link PooledMonteCarloManager}
 * @author Zhenghong Dong
 */
public class Test_PoolMonteCarloManager {
	private final MonteCarloSimulationProblem	_fakeProblem	= new FakeMCSimulationProblem( null, null );
	private final fakePooledMCManager			_manager		= new fakePooledMCManager( 10 );

	/**
	 * This is the fake pooledMonteCarloManager. We add a spy on taskQueue, so with mockito we can check how many
	 * workers we have assigned problem to each time when we try to solve a monte carlo problem.
	 * @author Zhenghong Dong
	 */
	class fakePooledMCManager extends PooledMonteCarloManager {
		// spy on _taskQueue
		protected BlockingQueue<MonteCarloSimulationProblem>	_taskQueueSpy;

		public fakePooledMCManager(int noOfThreads) {
			super( noOfThreads );
			_taskQueueSpy = Mockito.spy( _taskQueue );
		}

		@Override
		protected synchronized void solveProblem(final MonteCarloSimulationProblem problem) throws InterruptedException {
			if (_isStopped) throw new IllegalStateException( "CalculatorPool is stopped" );

			int ret = 0;
			do {
				_taskQueueSpy.offer( problem );
				this.wait( 1 );
			} while (++ret < 3 && _taskQueueSpy.size() == 0);
			if (ret > 1 && _taskQueueSpy.size() != 0) {
				// seems like _taskQueueSpy can't do remove... idk why
				_taskQueue.remove( problem );
			}
		}
	}

	/**
	 * Test if the problem is assigned to at most 3 threads every time we give manager a problem to solve
	 */
	@Test
	public void testMonteCarloSimulationManager() throws InterruptedException {
		_manager.solveProblem( _fakeProblem );

		// problem did being offered three times
		Mockito.verify( _manager._taskQueueSpy, Mockito.times( 3 ) ).offer( _fakeProblem );

		// occupy another six threads( so there should be only one worker free in the pool after this part )
		_manager.solveProblem( _fakeProblem );
		Mockito.verify( _manager._taskQueueSpy, Mockito.times( 6 ) ).offer( _fakeProblem );
		_manager.solveProblem( _fakeProblem );
		Mockito.verify( _manager._taskQueueSpy, Mockito.times( 9 ) ).offer( _fakeProblem );

		// try to publish another problem, this will use the last free worker
		_manager.solveProblem( _fakeProblem );
		// this time should only has one problem being offered, 2 extra offer(one will be removed)
		Mockito.verify( _manager._taskQueueSpy, Mockito.times( 11 ) ).offer( _fakeProblem );
		// queue should still be empty, even though all workers are busy
		assertTrue( _manager._taskQueueSpy.size() == 0 );

		// try to publis another problem, no free workes now, so problem left in the taskQueue
		_manager.solveProblem( _fakeProblem );
		// this time problem should be put to the queue
		Mockito.verify( _manager._taskQueueSpy, Mockito.times( 12 ) ).offer( _fakeProblem );
		assertTrue( _manager._taskQueueSpy.size() == 1 );

		// try to publis another problem, no free workers, problem left in the taskQueue
		_manager.solveProblem( _fakeProblem );
		Mockito.verify( _manager._taskQueueSpy, Mockito.times( 13 ) ).offer( _fakeProblem );
		assertTrue( _manager._taskQueueSpy.size() == 2 );

		// before shutdown _isStopped should be false
		Assert.assertTrue( _manager._isStopped == false );
		// shut down the pool
		_manager.shutDown();
		// manager did stop working
		Assert.assertTrue( _manager._isStopped == true );
		// we can't solve any problem after shutting down the manager
		IllegalStateException e = null;
		try {
			_manager.solveProblem( _fakeProblem );
		} catch (final IllegalStateException q) {
			e = q;
		}
		Assert.assertTrue( e != null );
	}
}
