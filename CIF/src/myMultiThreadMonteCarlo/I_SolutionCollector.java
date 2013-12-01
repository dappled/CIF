package myMultiThreadMonteCarlo;

/**
 * @author Zhenghong Dong
 */
public interface I_SolutionCollector<T extends Number> {
	/**
	 * Receive one solution ( might be a partial solution of the whole problem )
	 * @param solution one solution. When it's partial solution, it might be hold in some inner data structure and some
	 *            analysis will be done to determine if we have solve the whole problem
	 */
	void receiveSolution(T solution);

	/**
	 * @return the global solution of this problem
	 */
	T getSolution();

	/**
	 * @return True if the solution for the whole problem has been collected, so we can use
	 *         {@link I_SolutionCollector#getSolution()} to get the global solution
	 */
	boolean isFinished();
}
