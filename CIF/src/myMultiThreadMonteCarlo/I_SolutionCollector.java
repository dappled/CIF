package myMultiThreadMonteCarlo;

/**
 * @author Zhenghong Dong
 */
public interface I_SolutionCollector {
	/**
	 * Receive one solution
	 * @param solution one solution, might be add to some inner data structure 
	 */
	void receiveSolution(double solution);

	/**
	 * @return the solution of this problem
	 */
	double getSolution();

	/**
	 * @return True if the solution for the whole problem has been collected
	 */
	boolean isFinished();
}
