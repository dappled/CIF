package myMultiThreadMonteCarlo;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author Zhenghong Dong
 */
public class Test_SolutionCollector {

	@Test
	public void test() {
		I_SolutionCollector<Double> collector = new MonteCarloSolutionCollector( 0.00, 1, 0.1, 0.97, 1000 );
		assertTrue(collector.isFinished() == false);
		
		collector.receiveSolution( 10.0 );
		assertTrue(collector.isFinished() == false);
		
		// from my setup of stopping creteria, it should satisfy the stopping rule after receiving this solution
		collector.receiveSolution( 11.0 );
		assertTrue(collector.isFinished() == true);
		
		// since discount rate is 0, the return value should be the mean: 10.5
		assertTrue(collector.getSolution() == 10.5);
	}

}
