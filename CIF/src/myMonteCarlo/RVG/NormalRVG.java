package myMonteCarlo.RVG;

import org.apache.commons.math3.random.GaussianRandomGenerator;
import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.UncorrelatedRandomVectorGenerator;

/**
 * Generate normally distributed vector, a concrete implementation of RandomVectorGenerator
 * 
 * @author Zhenghong Dong
 * 
 */
public class NormalRVG implements RandomVectorGenerator {
	private final UncorrelatedRandomVectorGenerator	_rvg;
	private final GaussianRandomGenerator			_rawGenerator	= new GaussianRandomGenerator(
																			new JDKRandomGenerator() );

	/**
	 * Constructor for general normal r.v.
	 * 
	 * @param mean: mean of underlying normal distribution
	 * @param sd: standard deviation of underlying normal distribution
	 * @param n: length of the random vector
	 */
	public NormalRVG(final double mean, final double sd, final int n) {
		_rvg = new UncorrelatedRandomVectorGenerator( getArray( n, mean ), getArray( n, sd ),
				_rawGenerator );
	}

	/** Constructor for standard normal */
	public NormalRVG(final int n) {
		_rvg = new UncorrelatedRandomVectorGenerator( getArray( n, 0 ), getArray( n, 1 ),
				_rawGenerator );
	}

	@Override
	public double[] getVector() {
		return _rvg.nextVector();
	}

	private double[] getArray(final int n, final double num) {
		final double[] ret = new double[ n ];
		for (int i = 0; i < ret.length; i++) { ret[ i ] = num; }
		return ret;
	}
}
