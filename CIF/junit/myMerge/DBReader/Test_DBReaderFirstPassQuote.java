package myMerge.DBReader;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import myMerge.GZFileUtils;
import myMerge.DataWrapper.QuoteData;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Zhenghong Dong
 */
public class Test_DBReaderFirstPassQuote {

	@Test
	public void testQuote() throws IOException {
		// Create a temporary file

		final File tempFile = File.createTempFile( "testDBQuoteReaderFirstPass", ".binRQ", null );
		tempFile.deleteOnExit();

		// Write a file of general daily quote data

		final DataOutputStream dos = GZFileUtils.getGZippedFileOutputStream( tempFile.getAbsolutePath() );
		dos.writeInt( 1182312000 ); // secsFromEpoch
		dos.writeInt( 3 ); // nRecs
		// millisecondsFromMidnight
		dos.writeInt( 57596000 ); // 1st record
		dos.writeInt( 57596000 ); // 2nd record
		dos.writeInt( 57608000 ); // 3rd record
		// bidSize
		dos.writeInt( 48 );
		dos.writeInt( 49 );
		dos.writeInt( 43 );
		// bidPirce
		dos.writeFloat( (float) 106.0 );
		dos.writeFloat( (float) 106.01 );
		dos.writeFloat( (float) 106.02 );
		// askSize
		dos.writeInt( 39 );
		dos.writeInt( 41 );
		dos.writeInt( 52 );
		// askPrice
		dos.writeFloat( (float) 106.03 );
		dos.writeFloat( (float) 106.02 );
		dos.writeFloat( (float) 106.07 );
		dos.flush();
		dos.close();

		// Read the file back
		QuoteData data;
		LinkedList<QuoteData> recordBuffer = new LinkedList<>();

		final DBQuoteReaderFirstPass reader = new DBQuoteReaderFirstPass( 0, tempFile.getAbsolutePath(), "IBM" );
		Assert.assertTrue( reader.getSecsFromEpochLong() == 1182312000 );
		Assert.assertTrue( reader.getNRecs() == 3 );
		Assert.assertEquals( reader.getSequenceNumber(), (long) 1182312000 * 1000 + 57596000, 0.0 );

		// read first and second record
		Assert.assertTrue( reader.readChunk( (long) 1182312000 * 1000 + 57596000 ) == 2 );
		recordBuffer = reader.getRecordsBuffer();
		Assert.assertTrue( recordBuffer.size() == 2 );
		// test first record
		data = recordBuffer.getFirst();
		Assert.assertTrue( data.getTicker() == "IBM" );
		Assert.assertTrue( data.getMillisecondsFromMidnight() == 57596000 );
		Assert.assertTrue( data.getBidSize() == 48 );
		Assert.assertEquals( data.getBidPrice(), (float) 106.0, 0.0 );
		Assert.assertTrue( data.getAskSize() == 39 );
		Assert.assertEquals( data.getAskPrice(), (float) 106.03, 0.0 );
		// test second record
		data = recordBuffer.getLast();
		Assert.assertTrue( data.getTicker() == "IBM" );
		Assert.assertTrue( data.getMillisecondsFromMidnight() == 57596000 );
		Assert.assertTrue( data.getBidSize() == 49 );
		Assert.assertEquals( data.getBidPrice(), (float) 106.01, 0.0 );
		Assert.assertTrue( data.getAskSize() == 41 );
		Assert.assertEquals( data.getAskPrice(), (float) 106.02, 0.0 );

		// read empty record (secondSequenceNumber<targetSequenceNumber<thirdSequenceNumber)
		Assert.assertTrue( reader.readChunk( (long) 1182312000 * 1000 + 57606000 ) == 0 );

		// read third record
		Assert.assertTrue( reader.readChunk( (long) 1182312000 * 1000 + 57608000 ) == 1 );
		recordBuffer = reader.getRecordsBuffer();
		Assert.assertTrue( recordBuffer.size() == 1 );
		// test first record
		data = recordBuffer.getFirst();
		Assert.assertTrue( data.getTicker() == "IBM" );
		Assert.assertTrue( data.getMillisecondsFromMidnight() == 57608000 );
		Assert.assertTrue( data.getBidSize() == 43 );
		Assert.assertEquals( data.getBidPrice(), (float) 106.02, 0.0 );
		Assert.assertTrue( data.getAskSize() == 52 );
		Assert.assertEquals( data.getAskPrice(), (float) 106.07, 0.0 );
		
		// should has finished
		Assert.assertTrue( reader.isFinished() );
		
		// Delete this file

		tempFile.delete();
	}
}
