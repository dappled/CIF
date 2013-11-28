package myMonteCarlo.RVG;

/**
 * Antithetic random vector generator. Performs antithetic method on a random vector generator.
 * 
 * @author Zhenghong Dong
 * 
 */
public class AntitheticRVG implements RandomVectorGenerator {
	private double[]				_vec	= null;
	private RandomVectorGenerator	_rvg	= null;

	/**
	 * @param rvg: the underlying random vector generator
	 */
	public AntitheticRVG(final RandomVectorGenerator rvg) {
		_rvg = rvg;
	}

	/**
	 * @return the antitheticed random vector
	 */
	public double[] getVector() {
		if (_vec == null) {
			_vec = _rvg.getVector();
			return _vec;
		} else {
			final double[] tmp = new double[ _vec.length ];
			for (int i = 0; i < _vec.length; i++) {
				tmp[ i ] = -1 * _vec[ i ];
			}
			_vec = null;
			return tmp;
		}
	}
}
