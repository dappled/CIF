package summerClass.lecture3;

public class Stats2 {
	
	protected double _sumX;
	protected double _sumXX;
	protected int    _count;
	
	public Stats2() {
		_sumX = 0;
		_sumXX = 0;
		_count = 0;
	}
	
	public void add( double value ) {
		_sumXX += value * value;
		_sumX += value;
		_count++;
	}
	
	public void remove( double value ) throws Exception {
		if( _count < 1 )
			throw new Exception( 
				"Remove operation failed in " 
				+ "stats class - Count < 1 before operation" 
			);
		_sumX -= value; // Round off
		_sumXX -= value * value; // Round off
		_count--;
	}
	
	public double getMean() throws Exception {
		if( _count < 1 )
			throw new Exception( 
				"Invalid count in stats mean "
				+ "calculation = (" + _count + ")" 
			);
		return _sumX / _count; 
	}
	
	public double getSampleStd() throws Exception {
		if( _count < 2 )
			throw new Exception( 
				"Invalid count in stats sample " 
				+ "std calculation = (" + _count + ")" 
			);
		double diffOfSums = 
			( _sumXX / _count ) 
			- ( ( _sumX / _count )*( _sumX / _count ) ); 
		return Math.sqrt(  
			diffOfSums 
			* ( (double)_count / ( _count - 1 ) ) 
		);
	}

}
