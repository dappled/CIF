package dBReader.Example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

import dBReader.DBManager;
import dBReader.I_DBProcessor;
import dBReader.I_DBReader;


public class Test_ExampleDBReader extends junit.framework.TestCase {
	
	public void test1() throws IOException {

		// Create a new dummy file - It won't actually get used, but BufferedReader
		// requires that it exists
		
			File tempFile = File.createTempFile("dummyFile", "tmp", new File( "C:\\users\\Zhenghong Dong" ) );
			tempFile.deleteOnExit();
			
			
			File outTempFile = File.createTempFile("outFile", "tmp", new File( "C:\\users\\Zhenghong Dong" ) );
			outTempFile.deleteOnExit();
		// Create a fake BufferedReader so we can get the db reader to think
		// it's reading from a file while its actually reading from strings
		// stored in an array. (This technique for testing was discussed in
		// the lecture on unit testing.)
		
			BufferedReader br = new BufferedReader( new FileReader( tempFile ) ) {
				int i = 0;
				String[] lines = {
					"1,53",
					"1,54",
					"3,69",
					"4,72",
					"4,80"
				};
				public String readLine() {
					if( i == lines.length )
						return null;
					String line = lines[ i ];
					i++;
					return line;
				}
			};
			
			BufferedReader br2 = new BufferedReader( new FileReader( tempFile ) ) {
				int i = 0;
				String[] lines = {
					"0,53",
					"1,54",
					"4,80"
				};
				public String readLine() {
					if( i == lines.length )
						return null;
					String line = lines[ i ];
					i++;
					return line;
				}
			};
			
		// Instantiate a db reader with the fake buffered reader as input
			
			long id = 263;
			ExampleDBReader rtddbr = new ExampleDBReader( id, br);

			long id2 = 131;
			ExampleDBReader rtddbr2 = new ExampleDBReader( id2, br2 );
			
			LinkedList<I_DBReader> list = new LinkedList<>();
			list.add( rtddbr ); list.add( rtddbr2 );
			ExampleDBClock clock = new ExampleDBClock( list );
			
			ExampleDBProcessor processor = new ExampleDBProcessor( outTempFile );
			LinkedList<I_DBProcessor> processors  = new LinkedList<>();
			processors.add( processor );
			
			DBManager man = new DBManager( list, processors, clock );
			man.launch();
			
		// Make sure that it read one record on open and has a sequence
		// number
		
			assertTrue( rtddbr.getSequenceNumber() == 1 );
			
		// Read all of the records for target sequence number = 1
			
			long targetSequenceNum = 3;
			int numRecsRead = rtddbr.readChunk( targetSequenceNum );
			assertTrue( numRecsRead == 3 );
			assertTrue( rtddbr.getLastSequenceNumberRead() == 3 );
			
		
		// Make sure that the reader is now looking at the first
		// record of the new sequence number, which is 3
			
			assertTrue( rtddbr.getSequenceNumber() == 4 );
			assertTrue( rtddbr.readChunk( 4 ) == 1 );
		
		// Test for isFinished, read the last chunk, and test again
			
			assertTrue( rtddbr.isFinished() == false );
			assertTrue( rtddbr.readChunk( 4 ) == 2 );
		
		// Make sure the fields are correct for first record
		
			assertEquals( rtddbr.getRecordsBuffer().get(0)[0], "4" );
			assertEquals( rtddbr.getRecordsBuffer().get(0)[1], "72" );
			
		// Make sure the fields are correct for second record
			
			assertEquals( rtddbr.getRecordsBuffer().get(1)[0], "4" );
			assertEquals( rtddbr.getRecordsBuffer().get(1)[1], "80" );
		
		// Confirm that db reader reports it is finished
		
			assertTrue( rtddbr.isFinished() == true );
			
	}

}
