package dBReader.Example;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import dBReader.I_DBProcessor;
import dBReader.I_DBReader;



public class ExampleDBProcessor implements I_DBProcessor {
	
	private BufferedWriter _bufferedWriter;
	private boolean _isFinished;
	
	public ExampleDBProcessor( File outputFile ) throws IOException {
		_bufferedWriter = new BufferedWriter( new FileWriter( outputFile ) );
		_isFinished = false;
	}

	/** This method gets called by db manager when a chunk of records has
	 * been read by all of the db readers. This is where this db processor
	 * writes all records to one merged output file.
	 */
	@Override
	public boolean processReaders(
			long sequenceNumber,
			int numReadersWithNewData, 
			LinkedList<I_DBReader> readers
	) {
		if( _isFinished )
			return false;
		Iterator<I_DBReader> readerIterator = readers.iterator();
		for( int i = 0; i < numReadersWithNewData; i++ ) {
			ExampleDBReader reader = (ExampleDBReader) readerIterator.next();
			for( String[] fields : reader.getRecordsBuffer() )
				try {
					_bufferedWriter.write( String.format( "%s,%s,%s\n", fields[0], reader.getId(), fields[ 1 ] ) );
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return true; // Not finished
	}
	
	/** Called by db manager to give this processor a chance to tie up loose ends */
	@Override
	public void stop() {
		try {
			_bufferedWriter.flush();
			_bufferedWriter.close();
			_isFinished = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
