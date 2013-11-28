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
public class Test_DBManagerQuote {
	/**
	 * Make 3 temporary test Quote files to be merged by db reader framework
	 * 
	 * @param dir Output directory for temporary files
	 * @return List of files that were created and filled with fake data
	 * @throws IOException
	 */
	public File[] makeTestQuoteFiles(final File dir) throws IOException {

		// Create 3 temporary files

		final File[] files = new File[ 3 ];
		for (int i = 0; i < 3; i++) {
			files[ i ] = File.createTempFile( "testDBManagerQuote", ".binRQ", dir );
			files[ i ].deleteOnExit();
		}

		// We want to fill these temporary files with some records
		// 1st
		DataOutputStream dos = GZFileUtils.getGZippedFileOutputStream( files[ 0 ].getAbsolutePath() );
		dos.writeInt( 1182312000 );
		dos.writeInt( 1 );
		dos.writeInt( 57596000 );
		dos.writeInt( 48 );
		dos.writeFloat( (float) 106.0 );
		dos.writeInt( 39 );
		dos.writeFloat( (float) 106.03 );
		dos.flush();
		dos.close();
		// 2nd
		dos = GZFileUtils.getGZippedFileOutputStream( files[ 1 ].getAbsolutePath() );
		dos.writeInt( 1182398400 );
		dos.writeInt( 1 );
		dos.writeInt( 34200000 );
		dos.writeInt( 23 );
		dos.writeFloat( (float) 29.95 );
		dos.writeInt( 6 );
		dos.writeFloat( 30 );
		dos.flush();
		dos.close();
		// 3rd
		dos = GZFileUtils.getGZippedFileOutputStream( files[ 2 ].getAbsolutePath() );
		dos.writeInt( 1182398400 );
		dos.writeInt( 1 );
		dos.writeInt( 34243000 );
		dos.writeInt( 20 );
		dos.writeFloat( (float) 105.75 );
		dos.writeInt( 14 );
		dos.writeFloat( (float) 105.78 );
		dos.flush();
		dos.close();

		return files;
	}

	@Test
	/**
	 * Test db reader framework by reading and merging three test quote files
	 * 
	 * @throws IOException
	 */
	public void testQuote() throws IOException {

		// Make test files

		final File[] testFiles = makeTestQuoteFiles( null );

		// Open db readers for each of test files

		final long id1 = 1000;
		final long id2 = 2000;
		final long id3 = 3500;
		final LinkedList<I_DBReader> readers = new LinkedList<I_DBReader>();
		readers.add( new DBQuoteReaderFirstPass( id1, testFiles[ 0 ].getAbsolutePath(), "IBM" ) );
		readers.add( new DBQuoteReaderFirstPass( id2, testFiles[ 1 ].getAbsolutePath(), "MSFT" ) );
		readers.add( new DBQuoteReaderFirstPass( id3, testFiles[ 2 ].getAbsolutePath(), "IBM" ) );

		// Make a db processor - This is the object that will actually write out
		// the merged data

		final File tempFile = File.createTempFile( "testDBManagerOutQuote", ".binRQ", null ); // Where the processor will
		// write merged data
		tempFile.deleteOnExit();
		final LinkedList<I_DBProcessor> processors = new LinkedList<I_DBProcessor>();
		processors.add( new DBQuoteProcessor( tempFile.getAbsolutePath() ) ); // In this case only one processor to put
		// in list

		// Make a db clock

		final DBMergeClock clock = new DBMergeClock( readers );

		// Make a new db manager and hand off db readers, db processors, and clock

		final DBManager dbManager = new DBManager(
				readers, // List of readers
				processors, // List of processors
				clock // Clock
				);

		// Launch db manager to merge the three db readers

		dbManager.launch();

		// End of example. Now we test to see if the files were properly merged.
		// Read the file back
		QuoteData data;
		LinkedList<QuoteData> recordBuffer = new LinkedList<>();

		final DBQuoteReader reader = new DBQuoteReader( 0, tempFile.getAbsolutePath() );
		Assert.assertEquals( reader.getSequenceNumber(), (long) 1182312000 * 1000 + 57596000, 0.0 );
		// read first record
		Assert.assertTrue( reader.readChunk( (long) 1182312000 * 1000 + 57596000 ) == 1 );
		recordBuffer = reader.getRecordsBuffer();
		Assert.assertTrue( recordBuffer.size() == 1 );
		// test first record
		data = recordBuffer.getFirst();
		Assert.assertTrue( data.getTicker().equals( "IBM" ) );
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
		Assert.assertTrue( data.getTicker().equals( "MSFT" ) );
		Assert.assertTrue( data.getMillisecondsFromMidnight() == 34200000 );
		Assert.assertTrue( data.getBidSize() == 23 );
		Assert.assertEquals( data.getBidPrice(), (float) 29.95, 0.0 );
		Assert.assertTrue( data.getAskSize() == 6 );
		Assert.assertEquals( data.getAskPrice(), 30, 0.0 );
		Assert.assertEquals( data.getSecsFromEpoch(), 1182398400, 0.0 );
		
		// read third record
		Assert.assertTrue( reader.readChunk( (long) 1182398400 * 1000 + 34243000 ) == 1 );
		recordBuffer = reader.getRecordsBuffer();
		Assert.assertTrue( recordBuffer.size() == 1 );
		data = recordBuffer.getFirst();
		Assert.assertTrue( data.getTicker().equals( "IBM" ) );
		Assert.assertTrue( data.getMillisecondsFromMidnight() == 34243000 );
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