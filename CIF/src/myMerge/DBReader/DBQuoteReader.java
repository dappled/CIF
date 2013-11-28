package myMerge.DBReader;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.LinkedList;

import myMerge.GZFileUtils;
import myMerge.DataWrapper.QuoteData;

/**
 * @author Zhenghong Dong
 */
public class DBQuoteReader implements I_DBReader, Comparable<DBQuoteReader> {
	private final long					_id;
	private long						_sequenceNumber;
	private long						_lastSequeneNumberRead;
	private boolean						_isFinished;
	private final DataInputStream		_in;

	/** Fields of last record read and parsed into fields */
	private QuoteData					_fields;
	/** All of the records read in previous chunk */
	private final LinkedList<QuoteData>	_recordsBuffer;

	/***********************************************************************
	 * Constructor
	 ***********************************************************************/
	protected DBQuoteReader(final long id, final String filePathName) throws IOException {
		_id = id;
		_isFinished = false;
		_lastSequeneNumberRead = -1;

		_recordsBuffer = new LinkedList<QuoteData>();

		_in = GZFileUtils.getGZippedFileInputStream( filePathName );

		if (readOneRecord() == -1) {
			stop();
		}
	}
	
	/***********************************************************************
	 * {@link I_DBReader} methods
	 ***********************************************************************/
	/**
	 * Parse next record of data into fields. Save sequence number.
	 * @return -1 if failed to read (due to end of file); 0 if end of one day (but not end of file), which means we
	 *         should stop this loop of readChunk; 1 otherwise.
	 * @throws IOException
	 */
	private int readOneRecord() throws IOException {
		try {
			final long secsFromEpoch = _in.readLong();
			final long millisecondsFromMidnight = _in.readLong();
			_sequenceNumber = secsFromEpoch * 1000 + millisecondsFromMidnight;
			_fields = new QuoteData( secsFromEpoch, millisecondsFromMidnight, _in.readInt(), _in.readFloat(),
					_in.readInt(), _in.readFloat(), _in.readUTF() );
		} catch (final EOFException e) {
			throw new EOFException( "BinaryQuoteReader: Input file corrupted: less records than there should be." );
		}
		return 0;
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
		 * read (targetSequenceNum < _sequenceNumber)
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
			try {
				if (readOneRecord() == -1) {
					stop();
					return _recordsBuffer.size();
				}
			} catch (final IOException e) {
				stop();
				return _recordsBuffer.size();
			}
		} while (_sequenceNumber <= targetSequenceNum);

		// Return number of records in our records buffer

		return _recordsBuffer.size();
	}
	
	@Override
	public void stop() {
		_isFinished = true;
		try {
			_in.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isFinished() { return _isFinished; }
	@Override
	public long getLastSequenceNumberRead() { return _lastSequeneNumberRead; }
	@Override
	public long getSequenceNumber() { return _sequenceNumber; }

	/***********************************************************************
	 * Comparator methods
	 ***********************************************************************/
	@Override
	public int compareTo(final DBQuoteReader otherReader) {
		return Long.compare( getSequenceNumber(), otherReader.getSequenceNumber() );
	}

	/***********************************************************************
	 * Getter and Setter
	 ***********************************************************************/
	public LinkedList<QuoteData> getRecordsBuffer() { return _recordsBuffer; }
	public long getId() { return _id; }
}
