package myMultiThreadMonteCarlo;

import java.util.concurrent.BlockingQueue;

/**
 * A pooled worker that can work for any thread pool. It block waiting on the blockingQueue connecting it to the pool
 * manager till a task is assigned, then it works on it by calling the task's run. After finishing the job, it returns
 * to the pool waiting for next job. Such a hard working guy!
 * @author Zhenghong Dong
 */
public class PooledWorker<E extends Runnable> extends Thread {
	private BlockingQueue<E>				_taskQueue;
	private boolean							_isStopped	= false;

	/***********************************************************************
	 * Constructor
	 * @param pooledMonteCarloManager
	 ***********************************************************************/
	public PooledWorker(BlockingQueue<E> queue) {
		_taskQueue = queue;
	}

	/***********************************************************************
	 * Utilities
	 ***********************************************************************/
	@Override
	public void run() {
		while (!isStopped()) {
			try {
				// block waiting until a task is assigned
				E callable = (E) (_taskQueue).take();
				// solve the task
				callable.run();
			} catch (InterruptedException e) {
				// get here when the manager is shut down
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
