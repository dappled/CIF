package myMultiThreadMonteCarlo;

import myMonteCarlo.DataWrapper.OptionType;

/**
 * This is a fake monte carlo simulation problem, it just run forever. For test usage only. See
 * {@link Test_PoolMonteCarloManager}.
 * @author Zhenghong Dong
 */
public class FakeMCSimulationProblem extends MonteCarloSimulationProblem {

	public FakeMCSimulationProblem(final I_PricingProblemSpecification spec,
			final I_SolutionCollector<Double> solutionCollector) {
		super( new MonteCarloPricingProblemSpecification( OptionType.AsianCall, 1, 0, 0, 0, 1 ),
				new MonteCarloSolutionCollector( 0, 0, 0, 0, 1 ) );
	}

	@Override
	public void run() {
		// infinite loop
		while (true) {
			try {
				Thread.sleep( 5000 );
			} catch (final InterruptedException e) {
				// when manager is shut down
			}
		}
	}
}
