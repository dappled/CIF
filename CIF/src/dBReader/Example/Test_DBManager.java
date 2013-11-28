package dBReader.Example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import dBReader.DBManager;
import dBReader.I_DBProcessor;
import dBReader.I_DBReader;

public class Test_DBManager extends junit.framework.TestCase {

	/**
	 * Make 3 temporary test files to be merged by db reader framework
	 * 
	 * @param dir Output directory for temporary files
	 * @return List of files that were created and filled with fake data
	 * @throws IOException
	 */
	public File[] makeTestFiles( File dir ) throws IOException {
		
		// Create 3 temporary files
		
			File[] files = new File[ 3 ];
			for( int i = 0; i < 3; i++ ) {
				files[ i ] = File.createTempFile( "DBManagerTest", "tmp", dir );
				System.out.println( files[ i ].getAbsolutePath() );
			}
			
		// We want to fill these temporary files with some records, something
		// that looks like a sequence number and a price. The sequence numbers
		// will be in order but will have gaps in them.
			
			for( int i = 0; i < 3; i++ ) {
				BufferedWriter bw = new BufferedWriter( new FileWriter( files[ i ]) );
				int sequenceNumber = 0;
				for( int iNumLines = 0; iNumLines < 500; iNumLines++ ) {
					bw.write( String.format("%d,%.2f\n", sequenceNumber, Math.random() * 200 ) );
					sequenceNumber += (int)(Math.random() * 3 ); // This will skip some sequence numbers and repeat others
				}
				bw.flush();
				bw.close();
			}
			
			
		return files;
		
	}
	
	/**
	 * Test db reader framework by reading and merging three test files
	 * 
	 * @throws IOException
	 */
	public void test1() throws IOException {
		
		// Make test files
		
			File[] testFiles = makeTestFiles( new File( "/Users/minddrill/" ) );
			
		// Open db readers for each of test files
			
			long id1 = 1000;
			long id2 = 2000;
			long id3 = 3500;
			LinkedList<I_DBReader> readers = new LinkedList<I_DBReader>();
			readers.add(  new ExampleDBReader( id1, testFiles[ 0 ] ) );
			readers.add( new ExampleDBReader( id2, testFiles[ 1 ] ) );
			readers.add(  new ExampleDBReader( id3, testFiles[ 2 ] ) );			
			
		// Make a db processor - This is the object that will actually write out
		// the merged data
			
			File outputFile = new File( "/Users/minddrill/testOut.csv" ); // Where the processor will write merged data
			LinkedList<I_DBProcessor> processors = new LinkedList<I_DBProcessor>();
			processors.add( new ExampleDBProcessor( outputFile ) ); // In this case only one processor to put in list
			
		// Make a db clock 
			
			ExampleDBClock clock = new ExampleDBClock( readers );
			
		// Make a new db manager and hand off db readers, db processors, and clock
			
			DBManager dbManager = new DBManager( 
				readers,     // List of readers 
				processors,  // List of processors
				clock        // Clock
			);
			
		// Launch db manager to merge the three db readers
		
			dbManager.launch();
			

		// End of example. Now we test to see if the files were properly merged.
			
			// Read data from individual data files into memory
				
				String[] ids = { "1000", "2000", "3500" };
				HashMap<String,Iterator<String[]>> iterators = new HashMap<String,Iterator<String[]>>();
				ArrayList<LinkedList<String[]>> data = new ArrayList<LinkedList<String[]>>();
				for( int i = 0; i < 3; i++ ) {
					data.add( new LinkedList<String[]>() );
					BufferedReader reader = new BufferedReader( new FileReader( testFiles[ i ] ) );
					String line = null;
					while( ( line = reader.readLine() ) != null ) {
						String[] fields = line.split( "," );
						data.get(i).add( fields );
					}
					reader.close();
					iterators.put( ids[ i ], data.get( i ).iterator() ); // Save iterator in map by individual file ids
				}
			
			// Iterate over merged file data to see if we get all the same contents as
			// individual file data and in the right sequence
				
				@SuppressWarnings("resource")
				BufferedReader reader = new BufferedReader( new FileReader( outputFile ) );
				String line = null;
				Long sequenceNum = null; 
				while( ( line = reader.readLine() ) != null ) {
					String[] mergedFileFields = line.split( "," );
					if( sequenceNum != null )
						assertTrue( Long.parseLong( mergedFileFields[ 0 ] ) >= sequenceNum );
					sequenceNum = Long.parseLong( mergedFileFields[ 0 ] );
					String[] individualFileFields = iterators.get( mergedFileFields[ 1 ] ).next();
					assertTrue( 
						( individualFileFields[ 0 ].compareTo( mergedFileFields[ 0 ] ) == 0 ) // Sequence number
						&&
						( individualFileFields[ 1 ].compareTo( mergedFileFields[ 2 ] ) == 0 ) // Price	
					);
				}
				
			// Make sure that all of the data in the individual files has been
			// matched to data in merged file - data iterators should have no
			// data left
			
				assertTrue( iterators.get( "1000" ).hasNext() == false );
				assertTrue( iterators.get( "2000" ).hasNext() == false );
				assertTrue( iterators.get( "3500" ).hasNext() == false );
				
			// Delete all files
				
				outputFile.delete();
				for( File file : testFiles ) 
					file.delete();
		
	}
	
}
