package myMerge.DBReader;

import java.io.IOException;
import java.util.LinkedList;

import myMerge.ReadGZippedTAQTradesFile;
import myMerge.DataWrapper.TradeData;

/**
 * This reads a single day's single trade data
 * @author Zhenghong Dong
 */
public class DBTradeReaderFirstPass extends ReadGZippedTAQTradesFile implements I_DBReader,
Comparable<DBTradeReaderFirstPass> {
	private final long					_id;
	/**
	 * SequenceNumber is the MillisecondsFromMidnight + SecsFromEpoch, so we can compare two sequenceNumber even they
	 * have different SecsFromEpoch(day)
	 */
	private long						_sequenceNumber;
	private long						_lastSequeneNumberRead;
	private boolean						_isFinished;
	private int							_curRecs;
	private final String				_ticker;

	/** Fields of last record read and parsed into fields */
	private TradeData					_fields;
	/** All of the records read in previous chunk */
	private final LinkedList<TradeData>	_recordsBuffer;

	/***********************************************************************
	 * Constructor
	 ***********************************************************************/
	protected DBTradeReaderFirstPass(final long id, final String filePathName, final String ticker) throws IOException {
		super( filePathName );
		_id = id;
		_isFinished = false;
		_curRecs = -1;
		_lastSequeneNumberRead = -1;
		_recordsBuffer = new LinkedList<TradeData>();
		_ticker = ticker;

		// Set first sequenceNumber
		readOneRecord();
	}

	/***********************************************************************
	 * {@link  I_DBReader} methods
	 ***********************************************************************/
	/**
	 * Parse next record of data into fields. Save sequence number.
	 * @return Null if failed to read (due to end of file), and {@link TradeData} fields otherwise
	 */
	private TradeData readOneRecord() {
		if (++_curRecs >= _nRecs) return null;
		_sequenceNumber = getSecsFromEpochLong() * 1000 + _millisecondsFromMidnight[ _curRecs ];
		_fields = new TradeData( _secsFromEpoch, _millisecondsFromMidnight[ _curRecs ], _size[ _curRecs ],
				_price[ _curRecs ], _ticker );
		return _fields;
	}

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
			_lastSequeneNumberRead = _secsFromEpoch;
			if (readOneRecord() == null) {
				stop();
				return _recordsBuffer.size();
			}
		} while (_sequenceNumber <= targetSequenceNum);

		// Return number of records in our records buffer

		return _recordsBuffer.size();
	}

	@Override
	public long getSequenceNumber() 		{ return _sequenceNumber; }
	@Override
	public void stop() 						{ _isFinished = true; }
	@Override
	public boolean isFinished() 			{ return _isFinished; }
	@Override
	public long getLastSequenceNumberRead() { return _lastSequeneNumberRead; }
	
	/***********************************************************************
	 * Comparator methods
	 ***********************************************************************/
	@Override
	public int compareTo(final DBTradeReaderFirstPass otherReader) {
		return Long.compare( getSequenceNumber(), otherReader.getSequenceNumber() );
	}

	/***********************************************************************
	 * Getter and Setter
	 ***********************************************************************/
	public LinkedList<TradeData> getRecordsBuffer() { return _recordsBuffer;}
	public long getId() 							{ return _id; }
	// secsFromEpoch in ReadGZippedTAQQuotesFile is int, we want long version of it
	public long getSecsFromEpochLong() 				{ return _secsFromEpoch; }

}
