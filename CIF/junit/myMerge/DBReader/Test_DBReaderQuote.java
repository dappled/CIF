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
public class Test_DBReaderQuote {

	@Test
	public void testQuote() throws IOException {
		// Create a temporary file

		final File tempFile = File.createTempFile( "testDBQuoteReader", ".binRQ", null );
		tempFile.deleteOnExit();

		// Write a file of general daily quote data

		final DataOutputStream dos = GZFileUtils.getGZippedFileOutputStream( tempFile.getAbsolutePath() );
		// 1st day
		// 1st record
		dos.writeLong( 1182312000 ); // secsFromEpoch 
		dos.writeLong( 57596000 ); // millisecondsFromMidnight
		dos.writeInt( 48 ); // bidSize
		dos.writeFloat( (float) 106.0 ); // bidPrice
		dos.writeInt( 39 ); // askSize
		dos.writeFloat( (float) 106.03 ); // askPrice
		dos.writeUTF( "IBM" ); // ticker
		
		// 2nd day
		// 1st record
		dos.writeLong( 1182398400 ); // secsFromEpoch
		dos.writeLong( 34200000 ); // millisecondsFromMidnight
		dos.writeInt( 23 ); // bidSize
		dos.writeFloat( (float) 29.95 ); // bidPrice
		dos.writeInt( 6 ); // askSize
		dos.writeFloat( (float) 30.0 ); // askPrice
		dos.writeUTF( "MSFT" ); // ticker
		// 2nd record
		dos.writeLong( 1182398400 ); // secsFromEpoch
		dos.writeLong( 34243000 ); // millisecondsFromMidnight
		dos.writeInt( 20 ); // bidSize
		dos.writeFloat( (float) 105.75 ); // bidPrice
		dos.writeInt( 14 ); // askSize
		dos.writeFloat( (float) 105.78 ); // askPrice
		dos.writeUTF( "IBM" ); // ticker

		dos.flush();
		dos.close();

		// Read the file back
		QuoteData data;
		LinkedList<QuoteData> recordBuffer = new LinkedList<>();

		final DBQuoteReader reader = new DBQuoteReader( 0, tempFile.getAbsolutePath());
		Assert.assertEquals( reader.getSequenceNumber(), (long)1182312000 * 1000 + 57596000, 0.0 );
		// read first record
		Assert.assertTrue( reader.readChunk( (long) 1182312000 * 1000 + 57596000 ) == 1 );
		recordBuffer = reader.getRecordsBuffer();
		Assert.assertTrue( recordBuffer.size() == 1 );
		// test first record
		data = recordBuffer.getFirst();
		Assert.assertTrue( data.getTicker().equals( "IBM") );
		Assert.assertTrue( data.getMillisecondsFromMidnight() == 57596000 );
		Assert.assertTrue( data.getBidSize() == 48 );
		Assert.assertEquals( data.getBidPrice(), (float) 106.0, 0.0 );
		Assert.assertTrue( data.getAskSize() == 39 );
		Assert.assertEquals( data.getAskPrice(), (float) 106.03, 0.0 );
		Assert.assertEquals( data.getSecsFromEpoch(), 1182312000, 0.0 );

		// read empty record (1st SequenceNumber<targetSequenceNumber<2nd SequenceNumber)
		Assert.assertTrue( reader.readChunk( (long) 1182398400 * 1000 + 34100000 ) == 0 );

		// test second record
		Assert.assertTrue( reader.readChunk( (long) 1182398400 * 1000 + 34200000 ) == 1 );
		recordBuffer = reader.getRecordsBuffer();
		Assert.assertTrue( recordBuffer.size() == 1 );
		data = recordBuffer.getFirst();
		Assert.assertTrue( data.getTicker().equals( "MSFT") );
		Assert.assertTrue( data.getMillisecondsFromMidnight() == 34200000 );
		Assert.assertTrue( data.getBidSize() == 23 );
		Assert.assertEquals( data.getBidPrice(), (float) 29.95, 0.0 );
		Assert.assertTrue( data.getAskSize() == 6 );
		Assert.assertEquals( data.getAskPrice(), (float) 30, 0.0 );
		Assert.assertEquals( data.getSecsFromEpoch(), 1182398400, 0.0 );

		
		// read third record
		Assert.assertTrue( reader.readChunk( (long) 1182398400 * 1000 + 34243000 ) == 1 );
		recordBuffer = reader.getRecordsBuffer();
		Assert.assertTrue( recordBuffer.size() == 1 );
		data = recordBuffer.getFirst();
		Assert.assertTrue( data.getTicker().equals( "IBM") );
		Assert.assertTrue( data.getMillisecondsFromMidnight() == 34243000);
		Assert.assertTrue( data.getBidSize() == 20 );
		Assert.assertEquals( data.getBidPrice(), (float) 105.75, 0.0 );
		Assert.assertTrue( data.getAskSize() == 14 );
		Assert.assertEquals( data.getAskPrice(), (float) 105.78, 0.0 );
		Assert.assertEquals( data.getSecsFromEpoch(), 1182398400, 0.0 );
		
		// should has finished
		Assert.assertTrue( reader.isFinished() );
		
		// Delete this file

		tempFile.delete();
	}
}
