package summerClass.lecture2;

public class Stats1 {

	protected double _sum;
	protected double _mean;
	protected int    _count;
	protected double _sampleStd;
	
	public Stats1( double[] values ) throws Exception {
		if( values == null || values.length < 2 ) {
			throw new Exception( 
				"Need at least two values to compute sample standard deviation" 
			);
		}
		_count = values.length;
		_sum = 0;
		for( int i = 0; i < _count; i++ ) {
			_sum += values[ i ];
		}
		_mean = _sum / values.length;
		double diffSqrdSum = 0;
		for( int i = 0; i < _count; i++ ) {
			double diff = values[ i ] - _mean;
			diffSqrdSum += ( diff * diff );
		}
		_sampleStd = Math.sqrt( diffSqrdSum / ( _count - 1 ) );
	}
	
	public double getSampleStd() {
		return _sampleStd;
	}
	
}
