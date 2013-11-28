package summerClass.lecture2;

public class Stats4 {
	
	protected StatsQInterface _statsQ;
	protected Stats2          _stats;
	
	public Stats4( StatsQInterface statsQ ) {
		_stats = new Stats2();
		_statsQ = statsQ;
	}
	
	public void add( double value ) {
		_statsQ.addLast( value );
		_stats.add(value);
		while( _statsQ.hasElementsToRemove() ) {
			double removedValue = _statsQ.removeFirst();
			try {
				_stats.remove( removedValue );
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public double getMean() throws Exception {
		if( ! _statsQ.isReady() )
			throw new Exception( "Stats queue is not ready" );
		return _stats.getMean();
	}
	
	public double getSampleStd() throws Exception {
		if( ! _statsQ.isReady() )
			throw new Exception( "Stats queue is not ready" );
		return _stats.getSampleStd();
	}

}
