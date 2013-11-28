package myMonteCarlo;

import myMonteCarlo.RVG.AntitheticRVG;
import myMonteCarlo.RVG.MultiNormal;
import myMonteCarlo.RVG.NormalRVG;
import myMonteCarlo.RVG.RandomVectorGenerator;

import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.descriptive.MultivariateSummaryStatistics;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test normal random vector generator as well as antithetic RVG.
 * 
 * @author Zhenghong Dong
 * 
 */
public class Test_RandomVectorGenerator {

	/**
	 * test Normal random vector generator
	 * THIS TEST MIGHT FAIL!!!! It will pass only about 99.7% times, so good luck :)
	 */
	@Test
	public void testNormal() {
		final int n = 90000; // P(|error| < 1%) =99.7% => n = (3*sigma/0.01)^2 = 300^2
		final RandomVectorGenerator rv = new NormalRVG( 0, 1, n );
		Assert.assertNotNull( rv );
		final double[] vec = rv.getVector();
		// 99.7% probability that error will be less than 1%. THIS TEST MIGHT BE FALSE!!!!!
		Assert.assertEquals( Math.abs( StatUtils.mean( vec ) ), 0, 0.01 );
		// System.out.println( StatUtils.mean( vec ) );
		// System.out.println( Math.sqrt( StatUtils.variance( vec ) ) );
	}

	@Test
	public void testMultiNormal() {
		final int n = 90000;
		final double[][] cov = {{ 1,   0.6, 0.4 },
								{ 0.6, 1,   0.3 },
								{ 0.4, 0.3, 1   }};
		final RandomVectorGenerator rv = new MultiNormal( cov );
		Assert.assertNotNull( rv );
		MultivariateSummaryStatistics stats = new MultivariateSummaryStatistics( 3, true );
		double[] vec1 = new double[n];
		double[] vec2 = new double[n];
		double[] vec3 = new double[n];
		for (int i = 0; i < n; i++) {
			double[] vec= rv.getVector();
			stats.addValue( vec );
			vec1[i] = vec[0];
			vec2[i] = vec[1];
			vec3[i] = vec[2];
		}
		System.out.println(stats.getCovariance());
		Assert.assertEquals( Math.abs( StatUtils.mean( vec1 ) ), 0, 0.01 );
		Assert.assertEquals( Math.abs( StatUtils.mean( vec2 ) ), 0, 0.01 );
		Assert.assertEquals( Math.abs( StatUtils.mean( vec3 ) ), 0, 0.01 );
		System.out.println( StatUtils.variance( vec1 ));
		System.out.println( StatUtils.variance( vec2 ));
		System.out.println( StatUtils.variance( vec3 ));
	}

	/** test antithetic random vector generator with a normal underlying distribution */
	@Test
	public void testAntitethicRVG() {
		final RandomVectorGenerator rv = new NormalRVG( 0, 1, 5 );
		final RandomVectorGenerator anti = new AntitheticRVG( rv );
		double[] vec1, vec2, vec3;
		vec1 = anti.getVector();
		vec2 = anti.getVector(); // should be the antithetic of vec1
		vec3 = anti.getVector();
		// vec2 should be the antithetic of vec1
		Assert.assertTrue( equalInverse( vec1, vec2 ) );
		// vec3 should not be the antithetic of vec1
		Assert.assertFalse( equalInverse( vec1, vec3 ) );
		// vec3 should not be the antithetic of vec2
		Assert.assertFalse( equalInverse( vec2, vec3 ) );
	}

	/**
	 * helper function
	 * compare two vector to see if one is the antithetic of the other
	 * 
	 * @param vec1: vector 1
	 * @param vec2: vector 2
	 * @return true if vec2 is antithetic of vec1
	 */
	private boolean equalInverse(final double[] vec1, final double[] vec2) {
		for (int i = 0; i < vec1.length; i++) {
			if (vec2[ i ] != -vec1[ i ]) return false;
		}
		return true;
	}
}
