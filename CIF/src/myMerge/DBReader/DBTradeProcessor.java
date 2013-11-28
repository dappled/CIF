package myMerge.DBReader;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import myMerge.GZFileUtils;
import myMerge.DataWrapper.TradeData;

/**
 *
 * @author Zhenghong Dong
 */
public class DBTradeProcessor implements I_DBProcessor {
	private boolean					_isFinished;
	private final DataOutputStream	_out;

	/***********************************************************************
	 * Constructor
	 ***********************************************************************/
	public DBTradeProcessor(final String filePathName) throws IOException {
		_isFinished = false;
		_out = GZFileUtils.getGZippedFileOutputStream( filePathName );
	}

	/***********************************************************************
	 * {@link I_DBProcessor} methods
	 ***********************************************************************/
	/**
	 * This method gets called by db manager when a chunk of records has
	 * been read by all of the db readers. This is where this db processor
	 * writes all records to one merged output file.
	 */
	@Override
	public boolean processReaders(
			final long sequenceNumber,
			final int numReadersWithNewData,
			final LinkedList<I_DBReader> readers
			) {
		if (_isFinished) return false;

		try {
			// write header for every new day, record its secsFromEpoch and total records in this day

			final Iterator<I_DBReader> readerIterator = readers.iterator();
			for (int i = 0; i < numReadersWithNewData; i++) {
				final I_DBReader reader = readerIterator.next();
				for (final TradeData fields : ((DBTradeReaderFirstPass) reader).getRecordsBuffer()) {
					_out.writeLong( fields.getSecsFromEpoch() );
					_out.writeLong( fields.getMillisecondsFromMidnight() );
					_out.writeInt( fields.getSize() );
					_out.writeFloat( fields.getPrice() );
					_out.writeUTF( fields.getTicker() );
				}
			}
		} catch (final IOException e) {
			stop();
		}
		return true; // Not finished
	}
	
	/** Called by db manager to give this processor a chance to tie up loose ends */
	@Override
	public void stop() {
		try {
			_out.flush();
			_out.close();
			_isFinished = true;
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
}
