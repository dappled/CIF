package myMerge.DBReader;

import java.io.IOException;
import java.util.LinkedList;

import myMerge.ReadGZippedTAQQuotesFile;
import myMerge.DataWrapper.QuoteData;

/**
 * @author Zhenghong Dong
 */
public class DBQuoteReaderFirstPass extends ReadGZippedTAQQuotesFile implements I_DBReader,
		Comparable<DBQuoteReaderFirstPass> {
	private final long					_id;
	/**
	 * SequenceNumber is the MillisecondsFromMidnight + SecsFromEpoch*1000, so we can compare two sequenceNumber even
	 * they have different SecsFromEpoch(day)
	 */
	private long						_sequenceNumber;
	private long						_lastSequeneNumberRead;
	private boolean						_isFinished;
	private int							_curRecs;
	private final String				_ticker;

	/** Fields of last record read and parsed into fields */
	private QuoteData					_fields;
	/** All of the records read in previous chunk */
	private final LinkedList<QuoteData>	_recordsBuffer;

	/***********************************************************************
	 * Constructor
	 ***********************************************************************/
	protected DBQuoteReaderFirstPass(final long id, final String filePathName, final String ticker) throws IOException {
		super( filePathName );
		_id = id;
		_isFinished = false;
		_curRecs = -1;
		_lastSequeneNumberRead = -1;
		_recordsBuffer = new LinkedList<QuoteData>();
		_ticker = ticker;

		// get first sequenceNumber and field
		readOneRecord();
	}

	/**
	 * Parse next record of data into fields. Save sequence number.
	 * @return Null if failed to read (due to end of file), and {@link QuoteData} fields otherwise
	 */
	private QuoteData readOneRecord() {
		if (++_curRecs >= _nRecs) return null;
		_sequenceNumber = getSecsFromEpochLong() * 1000 + _millisecondsFromMidnight[ _curRecs ];
		_fields = new QuoteData( _secsFromEpoch, _millisecondsFromMidnight[ _curRecs ], _bidSize[ _curRecs ],
				_bidPrice[ _curRecs ], _askSize[ _curRecs ], _askPrice[ _curRecs ], _ticker );
		return _fields;
	}
	
	/***********************************************************************
	 * {@link I_DBReader} methods
	 ***********************************************************************/
	/**
	 * Read all of the records that have the same or less sequence number as the sequence number as the first sequence
	 * number of the chunk (which was read in a previous iteration). Save fields for all of the records read.
	 * 
	 * @return Number of records read
	 */
	@Override
	public int readChunk(final long targetSequenceNum) {

		// Check for finished in a previous read

		if (isFinished()) return 0;

		// Clear list of records to prepare for new records
		/**
		 * Note that I move this line up because we want to clear the buffer even later we find out we have nothing to
		 * read (targetSequenceNum < _sequenceNumber), so process will get [] if he called getRecordsBuffer
		 */
		_recordsBuffer.clear();

		// Check for sequence number that is ahead of
		// specified sequence number - Nothing to read

		if (targetSequenceNum < _sequenceNumber) return 0;

		// Record reading loop - Exit when we encounter a sequence number
		// that is above the one we want

		do {
			_recordsBuffer.add( _fields );
			_lastSequeneNumberRead = _sequenceNumber;
			if (readOneRecord() == null) {
				stop();
				return _recordsBuffer.size();
			}
		} while (_sequenceNumber <= targetSequenceNum);

		// Return number of records in our records buffer

		return _recordsBuffer.size();
	}

	@Override
	public long getSequenceNumber() 				{ return _sequenceNumber; }
	@Override
	public void stop() 								{ _isFinished = true; }
	@Override
	public boolean isFinished() 					{ return _isFinished; }
	@Override
	public long getLastSequenceNumberRead()			{ return _lastSequeneNumberRead; }
	
	/***********************************************************************
	 * Comparator methods
	 ***********************************************************************/
	@Override
	public int compareTo(final DBQuoteReaderFirstPass otherReader) {
		return Long.compare( getSequenceNumber(), otherReader.getSequenceNumber() );
	}

	/***********************************************************************
	 * Getter and Setter
	 ***********************************************************************/
	public long getId() 							{ return _id; }
	// secsFromEpoch in ReadGZippedTAQQuotesFile is int, we want long version of it
	public long getSecsFromEpochLong() 				{ return _secsFromEpoch; }
	public LinkedList<QuoteData> getRecordsBuffer() { return _recordsBuffer;}
		
}
