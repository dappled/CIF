package myMonteCarlo;

import myMonteCarlo.Stats.I_PathStats;
import myMonteCarlo.Stats.PathStat;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test I_PathStat interface with PathStat implementation
 * 
 * @author Zhenghong Dong
 * 
 */
public class Test_PathStat {
	private static final double	_tol	= 0.01;

	@Test
	public void test1() {
		final I_PathStats<Integer> stat = PathStat.newPathStat();
		// PathStat should hold nothing after initialization
		Assert.assertTrue( stat.sampleSize() == 0 );

		// test sample mean and sample variance
		stat.addPathStat( 1 );
		stat.addPathStat( 2 );
		stat.addPathStat( 3 );
		Assert.assertTrue( stat.sampleSize() == 3 );
		Assert.assertTrue( stat.sampleMean() == 2 );
		Assert.assertEquals( stat.sampleVariance(), 0.66, Test_PathStat._tol );

	}
}
