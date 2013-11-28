package summerClass.lecture3;

import summerClass.lecture3.RelativePriceChange;
import summerClass.lecture3.Stats2;

public class ReturnsAnalysis implements AnalyticsInterface<RelativePriceChange>{
	
	protected QInterface<RelativePriceChange> _statsQ;
	protected Stats2                          _stats;
	
	public ReturnsAnalysis( QInterface<RelativePriceChange> statsQ ) {
		_stats = new Stats2();
		_statsQ = statsQ;
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

	@Override
	public void add( RelativePriceChange rpc ) {
		_statsQ.addLast( rpc );
		_stats.add( rpc.getReturn( rpc.getTimeScaleInMilliseconds() ) 	);
		while( _statsQ.hasElementsToRemove() ) {
			RelativePriceChange removedValue;
			try {
				removedValue = _statsQ.removeFirst();
				_stats.remove( removedValue.getReturn( removedValue.getTimeScaleInMilliseconds() ) );
			} catch (Exception e1) {
				e1.printStackTrace(); // Should never happen because we're managing the queue
			}
		}
		
		// Use this new information about standard deviation to set some flags
		// or initiate some process in the system... or do nothing, and wait
		// for some other process in the system to access the getMean and 
		// getSampleStd methods in this object.
		
	}

}
