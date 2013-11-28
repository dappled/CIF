package myMiddleWare;

import myMonteCarlo.DataWrapper.OptionType;

import org.junit.Test;

/**
 * This class test using middleware to solve the two monte carlo problems we faced before.
 * @author Zhenghong Dong
 */
public class Test_MonteCarlo {

	@Test
	public void test() throws Exception {
		// option parameters
		final double p = 152.35, sigma = 0.01, r = 0.0001, stop = 0.96, err = 0.01;
		final int ttm = 252, maxSimulation = 10000000;
		double price;

		// initialize server and client
		final Server sm = new Server( "MonteCarlo" );
		final Client calculator1 = new Client();

		long startTime = System.currentTimeMillis();

		// 1st question: European Call option, with strike at $165,
		String queueName = "Euro1";
		// client listen to this queue
		calculator1.listenTo( queueName );
		// publish quest on this queue
		price = sm.publishQuest( queueName, OptionType.EuropeanCall, p, sigma, r, ttm, 165, err, stop, maxSimulation );
		// the result is about $6.21
		System.out.printf( "<Call Option> Average profit: %.2f\n", price );

		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println( totalTime );

		startTime = System.currentTimeMillis();
		// 2nd question: Asian Call option, with strike at $164,
		queueName = "Asian1";
		// client listen to this queue
		calculator1.listenTo( queueName );
		// publish quest on this queue
		price = sm.publishQuest( queueName, OptionType.AsianCall, p, sigma, r, ttm, 164, err, stop, maxSimulation );
		// the result is about $2.20
		System.out.printf( "<Asian Option> Average profit: %.2f\n", price );
		endTime = System.currentTimeMillis();
		totalTime = endTime - startTime;
		System.out.println( totalTime );
	}

}
