package dBReader.Example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import dBReader.I_DBReader;

public class ExampleDBReader implements I_DBReader, Comparable<ExampleDBReader> {
	
	/** Unique identification for this reader */
	private long _id;

	/** Text file input reader */
	private BufferedReader _bufferedReader;
	
	/** Sequence number of first record of chunk that has not been fully read */
	private Long _sequenceNumber;
	
	/** Sequence number of records of previously (fully read) chunk */
	private Long _lastSequeneNumberRead;
	
	/** Fields of last record read and parsed into fields */
	private String[] _fields;
	
	/** Set to true when an end of file is encountered. */
	private boolean _isFinished;
	
	/** All of the records read in previous chunk */
	private LinkedList<String[]> _recordsBuffer;
	
	/** If we're calling this constructor directly, we're probably passing in a fake
	 * buffered reader so we can do some testing. Instantiate a db reader object.
	 * Initialize an empty list for buffering records of a type that this
	 * object understands. Read the first line of the file so we can get a sequence
	 * number for the next chunk - a chunk is a group of records that all have the
	 * same sequence number.
	 * 
	 * @param br A buffered reader with a readLine and a flush method
	 * @throws IOException
	 */
	protected ExampleDBReader( long id, BufferedReader br ) throws IOException {
		_id = id;
		_isFinished = false;
		_lastSequeneNumberRead = null;
		_recordsBuffer = new LinkedList<String[]>();
		_bufferedReader = br;
		readOneLine();
	}
	
	/** This constructor allows us to start with a File object. The other
	 * constructor does all of the work. (See above.) Why two constructors?
	 * So we can fake the data input for testing.
	 * 
	 * @param textFile Text file from which we will read data
	 * @throws IOExcepon
	 */
	public ExampleDBReader( long id, File textFile ) throws IOException {
		this( id, new BufferedReader( new FileReader( textFile ) ) );
	}
	
	/**
	 * Read one line of data and parse it into fields. Save sequence number.
	 * @return Null if failed to read (due to end of file), and String[] fields
	 *         otherwise
	 * @throws IOException
	 */
	private String[] readOneLine() throws IOException {
		String line = _bufferedReader.readLine();
		if( line == null )
			return null;
		_fields = line.split( "," );
		_sequenceNumber = Long.parseLong( _fields[ 0 ] );
		return _fields;
	}

	/**
	 * Read all of the records that have the same sequence number
	 * as the sequence number as the first sequence number of the
	 * chunk (which was read in a previous iteration). Save fields
	 * for all of the records read.
	 * 
	 * @return Number of records read
	 */
	@Override
	public int readChunk(long targetSequenceNum ) {
		
		// Check for finished in a previous read
		
			if( isFinished() )
				return 0;
			
		// Check for sequence number that is ahead of
		// specified sequence number - Nothing to read
			
			if( targetSequenceNum < _sequenceNumber )
				return 0;
		
		// Record reading loop - Exit when we encounter a sequence number
		// that is above the one we want
			
			do {
				_recordsBuffer.add( _fields );
				_lastSequeneNumberRead = _sequenceNumber;
				try {
					if( readOneLine() == null ) {
						stop();
						return _recordsBuffer.size();
					}
				} catch (IOException e) {
					stop();
					return _recordsBuffer.size();
				}
				_sequenceNumber = Long.parseLong( _fields[ 0 ] );
			} while( _sequenceNumber<= targetSequenceNum );
			
		// Return number of records in our records buffer
		
			return _recordsBuffer.size();
	}

	/** Get the sequence number of the next chunk of records (not the
	 * last chunk that was read and buffered).
	 */
	@Override
	public long getSequenceNumber() { return _sequenceNumber; }

	/** Stop all reading, set _isFinished flag, and close all files */ 
	@Override
	public void stop() {
		_isFinished = true;
		try {
			_bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** Return true if this db reader finished reading and false otherwise. */
	@Override
	public boolean isFinished() { return _isFinished; }
	
	/**
	 * Return sequence number of last chunk of records read. Remember,
	 * we stopped because we encountered one record with a new sequence
	 * number.
	 */
	@Override
	public long getLastSequenceNumberRead() { return _lastSequeneNumberRead; }

	/**
	 * @return List of records read in last readChunk call
	 */
	public List<String[]> getRecordsBuffer() { return _recordsBuffer; }
	
	/** @return Unique id of this reader.
	 */
	public long getId() { return _id; }

	@Override
	public int compareTo( ExampleDBReader otherReader ) {
		return Double.compare( getSequenceNumber(), otherReader.getSequenceNumber() );
	}
	
}
