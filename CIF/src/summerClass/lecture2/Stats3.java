package summerClass.lecture2;

public class Stats3 extends Stats2 {

	protected StatsQInterface _statsQ;
	
	public Stats3( StatsQInterface statsQ ) {
		super();
		_statsQ = statsQ;
	}
	
	public void add( double value ) {
		_statsQ.addLast(value);
		while( _statsQ.hasElementsToRemove() ) {
			double valueToRemove = _statsQ.removeFirst();
			try {
				super.remove(valueToRemove);
			} catch ( Exception e ) {
				// Do nothing because it's never going to happen
			}
		}
	}
	
	public double getMean() throws Exception {
		if( !_statsQ.isReady() ) {
			throw new Exception( "Queue is not ready" );
		}
		return super.getMean();
	}
	
	public double getSampleStd() throws Exception {
		if( !_statsQ.isReady() ) {
			throw new Exception( "Queue is not ready" );
		}
		return super.getSampleStd();
	}
	
	public void remove( double value ) throws Exception {
		throw new Exception( 
			"Can't remove a value from this object"
			+ " - values are removed automatically" );
	}
	
}
