package summerClass.lecture2;

import java.sql.Timestamp;

public class RelativePriceChange {
	
	protected Timestamp _timeStamp1;
	protected Timestamp _timeStamp2;
	protected double    _value;

	public RelativePriceChange( 
		Timestamp timeStamp1, 
		Timestamp timeStamp2, 
		double    value 
	) throws Exception {
		if( ( timeStamp1 == null ) || ( timeStamp2 == null ) )
			throw new Exception( "Null timestamp in Price constructor" );
		if( timeStamp2.getTime() <= timeStamp1.getTime() )
			throw new Exception( "Invalid time difference in time (" + timeStamp1.getTime() + ") (" + timeStamp2.getTime() + ")" );
		_timeStamp1 = timeStamp1;
		_timeStamp2 = timeStamp2;
		_value      = value;
		
	}
	
	public double getReturn( long timeScaleInMilliseconds ) {
		return _value * (double)timeScaleInMilliseconds / (double)getTimeScaleInMilliseconds();
	}
	
	public long getTimeScaleInMilliseconds() {
		return _timeStamp2.getTime() - _timeStamp1.getTime();
	}
}
