package myMerge.DBReader;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import myMerge.GZFileUtils;
import myMerge.DataWrapper.QuoteData;

/**
 * This is an implementation of {@link I_DBProcessor}, it merges several quote files to a new GZfile 
 * @author Zhenghong Dong
 */
public class DBQuoteProcessor implements I_DBProcessor {
	private boolean				_isFinished;
	private final DataOutputStream	_out;

	/***********************************************************************
	 * Constructor
	 ***********************************************************************/
	public DBQuoteProcessor(final String filePathName) throws IOException {
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
			final Iterator<I_DBReader> readerIterator = readers.iterator();
			for (int i = 0; i < numReadersWithNewData; i++) {
				final I_DBReader reader = readerIterator.next();
				for (final QuoteData fields : ((DBQuoteReaderFirstPass)reader).getRecordsBuffer()) {
					_out.writeLong( fields.getSecsFromEpoch() );
					_out.writeLong( fields.getMillisecondsFromMidnight() );
					_out.writeInt( fields.getBidSize() );
					_out.writeFloat( fields.getBidPrice() );
					_out.writeInt( fields.getAskSize() );
					_out.writeFloat( fields.getAskPrice() );
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
