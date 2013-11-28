package myMonteCarlo.RVG;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.random.CorrelatedRandomVectorGenerator;
import org.apache.commons.math3.random.GaussianRandomGenerator;
import org.apache.commons.math3.random.JDKRandomGenerator;

/**
 * @author Zhenghong Dong
 */
public class MultiNormal implements RandomVectorGenerator {
	private final CorrelatedRandomVectorGenerator	_rvg;
	private final GaussianRandomGenerator			_rawGenerator	= new GaussianRandomGenerator(
			new JDKRandomGenerator() );
	
	/**
	 * Constructor for general multivariate normal r.v.
	 * 
	 * @param mean: mean matrix
	 * @param cov: covariance matrix 
	 */
	public MultiNormal(final double[] mean, final double[][] cov) {
		final Array2DRowRealMatrix covariance = new Array2DRowRealMatrix( cov );
		_rvg = new CorrelatedRandomVectorGenerator( mean, covariance, 1.0e-12 * covariance.getNorm(),
				_rawGenerator );
	}

	/** Constructor for unit variance multivariate normal */
	public MultiNormal(final double[][] cov) {
		final Array2DRowRealMatrix covariance = new Array2DRowRealMatrix( cov );
		_rvg = new CorrelatedRandomVectorGenerator( covariance, 1.0e-12 * covariance.getNorm(),
				_rawGenerator );
	}

	@Override
	public double[] getVector() {
		return _rvg.nextVector();
	}
}
