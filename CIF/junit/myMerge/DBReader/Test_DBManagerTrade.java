package myMerge.DBReader;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import myMerge.GZFileUtils;
import myMerge.DataWrapper.TradeData;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Zhenghong Dong
 */
public class Test_DBManagerTrade {

	/**
	 * Make 3 temporary test trade files to be merged by db reader framework
	 * 
	 * @param dir Output directory for temporary files
	 * @return List of files that were created and filled with fake data
	 * @throws IOException
	 */
	public File[] makeTestTradeFiles(final File dir) throws IOException {

		// Create 4 temporary files

		final File[] files = new File[ 4 ];
		for (int i = 0; i < 4; i++) {
			files[ i ] = File.createTempFile( "testDBManagerTrade", ".binRQ", dir );
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
		dos.flush();
		dos.close();
		// 2nd
		dos = GZFileUtils.getGZippedFileOutputStream( files[ 1 ].getAbsolutePath() );
		dos.writeInt( 1182398400 );
		dos.writeInt( 1 );
		dos.writeInt( 34200000 );
		dos.writeInt( 23 );
		dos.writeFloat( (float) 29.95 );
		dos.flush();
		dos.close();
		// 3rd
		dos = GZFileUtils.getGZippedFileOutputStream( files[ 2 ].getAbsolutePath() );
		dos.writeInt( 1182398400 );
		dos.writeInt( 1 );
		dos.writeInt( 34243000 );
		dos.writeInt( 20 );
		dos.writeFloat( (float) 105.75 );
		dos.flush();
		dos.close();
		// 4th
		dos = GZFileUtils.getGZippedFileOutputStream( files[ 3 ].getAbsolutePath() );
		dos.writeInt( 1182312000 );
		dos.writeInt( 1 );
		dos.writeInt( 34243000 );
		dos.writeInt( 76 );
		dos.writeFloat( (float) 22.0 );
		dos.flush();
		dos.close();

		return files;
	}

	@Test
	public void testTradeMerge() throws IOException {
		// Make test files

		final File[] testFiles = makeTestTradeFiles( null );

		// Open db readers for each of test files
		// need two lists of readers to merge f1& f2, f3&f4 seperately
		final long id1 = 1000;
		final long id2 = 2000;
		final long id3 = 3500;
		final long id4 = 1500;
		final LinkedList<I_DBReader> readers = new LinkedList<I_DBReader>();
		readers.add( new DBTradeReaderFirstPass( id1, testFiles[ 0 ].getAbsolutePath(), "IBM" ) );
		readers.add( new DBTradeReaderFirstPass( id2, testFiles[ 1 ].getAbsolutePath(), "MSFT" ) );
		readers.add( new DBTradeReaderFirstPass( id3, testFiles[ 2 ].getAbsolutePath(), "IBM" ) );
		readers.add( new DBTradeReaderFirstPass( id4, testFiles[ 3 ].getAbsolutePath(), "MSFT" ) );
		// Make db processor - This is the object that will actually write out the merged data
		final File tempFile = File.createTempFile( "testDBManagerOutTrade", ".binRQ", null );
		tempFile.deleteOnExit();
		final LinkedList<I_DBProcessor> processors = new LinkedList<I_DBProcessor>();
		processors.add( new DBTradeProcessor( tempFile.getAbsolutePath() ) ); 
		// Make db clock
		final DBMergeClock clock = new DBMergeClock( readers );
		// merge 
		DBManager dbManager = new DBManager(
				readers, // List of readers
				processors, // List of processors
				clock // Clock
		);
		dbManager.launch();
		
		
		// End of example. Now we test to see if the files were properly merged.
		// Read the file back
		TradeData data;
		LinkedList<TradeData> recordBuffer = new LinkedList<>();

		final DBTradeReader reader = new DBTradeReader( 0, tempFile.getAbsolutePath() );
		Assert.assertEquals( reader.getSequenceNumber(), (long) 1182312000 * 1000 + 34243000, 0.0 );
		// test first record
		Assert.assertTrue( reader.readChunk( (long) 1182312000 * 1000 + 34243000 ) == 1 );
		recordBuffer = reader.getRecordsBuffer();
		Assert.assertTrue( recordBuffer.size() == 1 );
		data = recordBuffer.getFirst();
		Assert.assertTrue( data.getTicker().equals( "MSFT" ) );
		Assert.assertTrue( data.getMillisecondsFromMidnight() == 34243000 );
		Assert.assertTrue( data.getSize() == 76 );
		Assert.assertEquals( data.getPrice(), (float) 22, 0.0 );
		Assert.assertEquals( data.getSecsFromEpoch(), 1182312000, 0.0 );
		
		// test second record
		Assert.assertTrue( reader.readChunk( (long) 1182312000 * 1000 + 57596000 ) == 1 );
		recordBuffer = reader.getRecordsBuffer();
		Assert.assertTrue( recordBuffer.size() == 1 );
		data = recordBuffer.getFirst();
		Assert.assertTrue( data.getTicker().equals( "IBM" ) );
		Assert.assertTrue( data.getMillisecondsFromMidnight() == 57596000 );
		Assert.assertTrue( data.getSize() == 48 );
		Assert.assertEquals( data.getPrice(), (float) 106.0, 0.0 );
		Assert.assertEquals( data.getSecsFromEpoch(), 1182312000, 0.0 );

		// read empty record (1st SequenceNumber<targetSequenceNumber<2nd SequenceNumber)
		Assert.assertTrue( reader.readChunk( (long) 1182398400 * 1000 + 34100000 ) == 0 );

		// test third record
		Assert.assertTrue( reader.readChunk( (long) 1182398400 * 1000 + 34200000 ) == 1 );
		recordBuffer = reader.getRecordsBuffer();
		Assert.assertTrue( recordBuffer.size() == 1 );
		data = recordBuffer.getFirst();
		Assert.assertTrue( data.getTicker().equals( "MSFT" ) );
		Assert.assertTrue( data.getMillisecondsFromMidnight() == 34200000 );
		Assert.assertTrue( data.getSize() == 23 );
		Assert.assertEquals( data.getPrice(), (float) 29.95, 0.0 );
		Assert.assertEquals( data.getSecsFromEpoch(), 1182398400, 0.0 );

		// read fourth record
		Assert.assertTrue( reader.readChunk( (long) 1182398400 * 1000 + 34243000 ) == 1 );
		recordBuffer = reader.getRecordsBuffer();
		Assert.assertTrue( recordBuffer.size() == 1 );
		data = recordBuffer.getFirst();
		Assert.assertTrue( data.getTicker().equals( "IBM" ) );
		Assert.assertTrue( data.getMillisecondsFromMidnight() == 34243000 );
		Assert.assertTrue( data.getSize() == 20 );
		Assert.assertEquals( data.getPrice(), (float) 105.75, 0.0 );
		Assert.assertEquals( data.getSecsFromEpoch(), 1182398400, 0.0 );

		// should has finished
		Assert.assertTrue( reader.isFinished() );

		// Delete this file

		tempFile.delete();
	}

}
