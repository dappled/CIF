package myMultiThreadMonteCarlo;

import static org.junit.Assert.assertTrue;
import myMonteCarlo.DataWrapper.OptionType;

import org.junit.Test;

/**
 * @author Zhenghong Dong
 */
public class Test_PricingProblemSpecification {

	@Test
	public void test() {
		I_PricingProblemSpecification spec = new MonteCarloPricingProblemSpecification( OptionType.AsianCall, 97, 0.2,
				0.01, 360, 100 );

		assertTrue( spec.getType() == OptionType.AsianCall );
		assertTrue( spec.getCurrentPrice() == 97 );
		assertTrue( spec.getDailyRate() == 0.01 );
		assertTrue( spec.getDailyVol() == 0.2 );
		assertTrue( spec.getStrike() == 100 );
		assertTrue( spec.getTTM() == 360 );
	}

}
