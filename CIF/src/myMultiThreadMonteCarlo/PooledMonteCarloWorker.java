package myMultiThreadMonteCarlo;

import java.util.concurrent.BlockingQueue;

/**
 * @author Zhenghong Dong
 */
public class PooledMonteCarloWorker<E extends Runnable> extends Thread {
	private BlockingQueue<E>	_taskQueue;
	private boolean				_isStopped	= false;

	public PooledMonteCarloWorker(BlockingQueue<E> queue) {
		_taskQueue = queue;
	}

	public void run() {
		while (!isStopped()) {
			try {
				E callable = (E) (_taskQueue).take();
				//System.out.printf( "PooledMonteCarloWorker %s. start running job with strike %s\n",currentThread().getName(), ((MonteCarloSimulationJob)callable).getStrike());
				callable.run();
				//System.out.printf( "PooledMonteCarloWorker %s. end running job with strike %s\n",currentThread().getName(),((MonteCarloSimulationJob)callable).getStrike() );
			} catch (InterruptedException e) {
				// log or otherwise report exception,
				// but keep pool thread alive.
			}
		}
	}

	public synchronized void callStop() {
		_isStopped = true;
		this.interrupt();
	}

	public boolean isStopped() {
		return _isStopped;
	}
}
