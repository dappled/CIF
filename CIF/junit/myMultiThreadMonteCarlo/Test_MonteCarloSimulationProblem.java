package myMultiThreadMonteCarlo;

import static org.junit.Assert.*;
import myMonteCarlo.DataWrapper.OptionType;

import org.junit.Test;

/**
 * @author Zhenghong Dong
 */
public class Test_MonteCarloSimulationProblem {

	/**
	 * Here we test the two Monte Carlo problems, we can see the answer is quite accurate. (92%(96%^2) pass the test, 
	 * hope you are lucky!)
	 * @throws InterruptedException 
	 */
	@Test
	public void test() throws InterruptedException{
		final PooledMonteCarloManager manager = new PooledMonteCarloManager( 10 );
		final double p = 152.35, sigma = 0.01, r = 0.0001, stop = 0.96;
		final int ttm = 252, maxSimulation = 10000000;
		
		// 1st question: European Call option, with strike at $165,
		final I_PricingProblemSpecification problem1 = new MonteCarloPricingProblemSpecification(
				OptionType.EuropeanCall, p,	sigma, r, ttm, 165 );
		final I_SolutionCollector<Double> solution1 = new MonteCarloSolutionCollector( r, ttm, 0.01, stop, maxSimulation );
		manager.solve( problem1, solution1 );
		// 2nd question: Asian Call option, with strike at $164,
		final I_PricingProblemSpecification problem2 = new MonteCarloPricingProblemSpecification( OptionType.AsianCall,
				p, sigma, r, ttm, 164 );
		final I_SolutionCollector<Double> solution2 = new MonteCarloSolutionCollector( r, ttm, 0.01, stop, maxSimulation );
		manager.solve( problem2, solution2 );

		// wait untill get both solutions
		while (!(solution1.isFinished() && solution2.isFinished()))
		{
			Thread.sleep( 300 );
		}
		
		// the 1st result is about $6.21 with 1% error, 96% confidence
		assertEquals(solution1.getSolution(), 6.21, 0.0621 );
		// the 2nd result is about $2.19 with 1% error, 96% confidence
		assertEquals( solution2.getSolution(), 2.19, 0.0218 );
		
		// shut down the pool
		manager.shutDown();
	}

}
