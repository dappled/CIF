package dBReader.Example;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import dBReader.I_DBClock;
import dBReader.I_DBReader;

public class ExampleDBClock implements I_DBClock {
	
	private List<I_DBReader> _readers;
	
	public ExampleDBClock( List<I_DBReader> readers ) {
		
		_readers = readers;
		
		// Sort readers to make sure that the first time clock's getSequenceNumber is called
		// the first reader in the list is the reader with the lowest sequence number
		
			Collections.sort( 
				_readers,
				new Comparator<I_DBReader>() {
					@Override
					public int compare(I_DBReader arg0, I_DBReader arg1) {
						return Double.compare( arg0.getSequenceNumber(), arg1.getSequenceNumber() );
					}
				}
			);
	}

	/** In this version of db clock, the clock simply uses the next lowest sequence
	 * number from the list of db readers as the target sequence number. This means
	 * that all records will be read from all readers.
	 */
	@Override
	public long getNextSequenceNumber() {
		return _readers.get( 0 ).getSequenceNumber();
	}

	@Override
	public boolean isFinished() {
		if( _readers.size() == 0 ) // Check to see if all readers were removed
			return true;
		return _readers.get( 0 ).isFinished(); // Check to see if next reader is finished
	}
	
}
