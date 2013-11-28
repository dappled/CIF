package summerSolution;

import java.io.File;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.TreeMap;

import utils1309.SortField;
import utils1309.SortOrder;
import utils1309.AlgoType;
import utils1309.TextFileReader;

public class Test_DataHandler extends junit.framework.TestCase {

	/**
	 * Test constructor and loadPriceData method with an ascending quicksort on the date
	 * @throws Exception
	 */
	public void testQuicksort() throws Exception {
		
		LinkedList<PriceRecord> priceRecords = new LinkedList<PriceRecord>();
		priceRecords.add( new PriceRecord("01/02/2001,100,100,100,100,10000,201") );
		priceRecords.add( new PriceRecord("01/03/2001,100,100,100,100,10000,202") );
		priceRecords.add( new PriceRecord("01/01/2001,100,100,100,100,10000,200") );

		class DataHandler1 extends DataHandler {
			public LinkedList<PriceRecord> _testPriceRecords;
			protected LinkedList<PriceRecord> getPriceRecords( String filePathName ) throws Exception {
				return _testPriceRecords;
			}
		};
		
		DataHandler1 dh = new DataHandler1();
		dh._testPriceRecords = priceRecords;
		String priceFilePathName = "";
		dh.loadPriceData( priceFilePathName, SortField.DATE, SortOrder.ASCENDING, AlgoType.QUICKSORT );
		assertTrue( dh._records[ 0 ].getDate() == 20010101 );
		assertTrue( dh._records[ 1 ].getDate() == 20010102 );
		assertTrue( dh._records[ 2 ].getDate() == 20010103 );
		
	}
	
	/**
	 * Test descending quicksort
	 * 
	 * @throws Exception
	 */
	public void testDescendingPrices() throws Exception {
		
		LinkedList<PriceRecord> priceRecords = new LinkedList<PriceRecord>();
		priceRecords.add( new PriceRecord("01/02/2001,100,100,100,100,10000,201") );
		priceRecords.add( new PriceRecord("01/03/2001,100,100,100,100,10000,202") );
		priceRecords.add( new PriceRecord("01/01/2001,100,100,100,100,10000,200") );

		class DataHandler1 extends DataHandler {
			public LinkedList<PriceRecord> _testPriceRecords;
			protected LinkedList<PriceRecord> getPriceRecords( String filePathName ) throws Exception {
				return _testPriceRecords;
			}
		};
		
		DataHandler1 dh = new DataHandler1();
		dh._testPriceRecords = priceRecords;
		String priceFilePathName = "";
		dh.loadPriceData( priceFilePathName, SortField.PRICE, SortOrder.DESCENDING, AlgoType.QUICKSORT );
		assertEquals( dh._records[ 0 ].getAdjustedClose(), 202, 0.001 );
		assertEquals( dh._records[ 1 ].getAdjustedClose(), 201, 0.001 );
		assertEquals( dh._records[ 2 ].getAdjustedClose(), 200, 0.001 );
		
	}
	
	/**
	 * Test bubble sort
	 * 
	 * @throws Exception
	 */
	public void testBubbleSort() throws Exception {
		
		LinkedList<PriceRecord> priceRecords = new LinkedList<PriceRecord>();
		priceRecords.add( new PriceRecord("01/02/2001,100,100,100,100,10000,201") );
		priceRecords.add( new PriceRecord("01/03/2001,100,100,100,100,10000,202") );
		priceRecords.add( new PriceRecord("01/01/2001,100,100,100,100,10000,200") );

		class DataHandler1 extends DataHandler {
			public LinkedList<PriceRecord> _testPriceRecords;
			protected LinkedList<PriceRecord> getPriceRecords( String filePathName ) throws Exception {
				return _testPriceRecords;
			}
		};
		
		DataHandler1 dh = new DataHandler1();
		dh._testPriceRecords = priceRecords;
		String priceFilePathName = "";
		dh.loadPriceData( priceFilePathName, SortField.DATE, SortOrder.ASCENDING, AlgoType.BUBBLESORT );
		assertTrue( dh._records[ 0 ].getDate() == 20010101 );
		assertTrue( dh._records[ 1 ].getDate() == 20010102 );
		assertTrue( dh._records[ 2 ].getDate() == 20010103 );
		
	}
	
	/**
	 * Test corrections and calculations
	 * 
	 * @throws Exception
	 */
	public void testCorrections() throws Exception {
		
		LinkedList<PriceRecord> priceRecords = new LinkedList<PriceRecord>();
		priceRecords.add( new PriceRecord("01/20/2001,100,100,100,100,10000,201") );
		priceRecords.add( new PriceRecord("01/30/2001,100,100,100,100,10000,202") );
		priceRecords.add( new PriceRecord("01/10/2001,100,100,100,100,10000,200") );

		class DataHandler1 extends DataHandler {
			public LinkedList<PriceRecord> _testPriceRecords;
			protected LinkedList<PriceRecord> getPriceRecords( String filePathName ) throws Exception {
				return _testPriceRecords;
			}
		};
		
		DataHandler1 dh = new DataHandler1();
		dh._testPriceRecords = priceRecords;
		String priceFilePathName = "";
		dh.loadPriceData( priceFilePathName, SortField.DATE, SortOrder.ASCENDING, AlgoType.BUBBLESORT ); // Bubble sort variant
		
		assertTrue( dh._records[ 0 ].getDate() == 20010110 );
		assertTrue( dh._records[ 1 ].getDate() == 20010120 );
		assertTrue( dh._records[ 2 ].getDate() == 20010130 );
		
		LinkedList<PriceRecord> correctionsRecords = new LinkedList<PriceRecord>();
		correctionsRecords.add( new PriceRecord("01/31/2001,100,100,100,100,10000,201") ); // Insert
		correctionsRecords.add( new PriceRecord("01/15/2001,100,100,100,100,10000,202") ); // Insert
		correctionsRecords.add( new PriceRecord("01/01/2001,100,100,100,100,10000,200") ); // Insert
		correctionsRecords.add( new PriceRecord("01/10/2001,100,100,100,100,20000,300") ); // Overwrite
		dh._testPriceRecords = correctionsRecords;
		
		String correctionsFilePathName = "";
		dh.correctPrices( correctionsFilePathName );
		
		assertTrue( dh._records[ 0 ].getDate() == 20010101 ); // 200
		assertTrue( dh._records[ 1 ].getDate() == 20010110 ); // 300
		assertTrue( dh._records[ 2 ].getDate() == 20010115 ); // 202
		assertTrue( dh._records[ 3 ].getDate() == 20010120 ); // 201
		assertTrue( dh._records[ 4 ].getDate() == 20010130 ); // 202
		assertTrue( dh._records[ 5 ].getDate() == 20010131 ); // 201
		
		assertEquals( dh._records[ 0 ].getAdjustedClose(), 200, 0.001 ); // 200 - Successful insertion
		assertEquals( dh._records[ 1 ].getAdjustedClose(), 300, 0.001 ); // 300 - Successful overwrite
		assertEquals( dh._records[ 2 ].getAdjustedClose(), 202, 0.001 ); // 202 - Successful insertion
		
		Double[] prices = dh.getPrices( 20010101, 20010115 );
		assertTrue( prices.length == 3 );
		
		// Test getPrices
		assertEquals( prices[0], 200, 0.001 );
		assertEquals( prices[1], 300, 0.001 );
		assertEquals( prices[2], 202, 0.001 );

		// Test average computation
		double avg = dh.computeAverage( 20010115, 20010215 );
		assertEquals( avg, ( 202 + 201 + 202 + 201 ) / 4.0, 0.001 );

		// Test compute max price
		double maxPrice = dh.computeMax(20001215, 20010120 );
		assertEquals( maxPrice, 300, 0.001 );
		assertFalse( dh.computeMax( 20010120, 20050101 ) > 299.0 ); // Make sure it doesn't pick up 300
		
		// Test moving average calculation
		
			// Create and open temp file
			File tempFile = File.createTempFile( "zzz1","zzzz2" );
			tempFile.deleteOnExit();
			FileWriter fw = new FileWriter( tempFile );
				
			// Call method to write moving average data to this file
			int movingAverageSize = 2;
			long startDate = 20010101;
			long endDate = 20010130;
			dh.writeMovinaAverageDataToFile( movingAverageSize, startDate, endDate, fw );
			
			// Flush and close file
			fw.flush();
			fw.close();
			
			// Read file back in and compare results to the calculation we want to see
			LinkedList<String> lines = TextFileReader.readLineLinkedList( tempFile.getAbsolutePath() );
			String exptectedOutput = String.format(
				"The moving average of SPY between %s and %s is:", 
				PriceRecord.dateToString( startDate ),
				PriceRecord.dateToString( endDate )
			);
			assertTrue( lines.get( 0 ).compareTo( exptectedOutput ) == 0 );
			assertTrue( lines.get( 1 ).compareTo("\tNA") == 0 ); // Not enough data to compute ma
			assertEquals( Double.parseDouble( lines.getLast() ), 201.5, 0.001 ); // The last line - average of last 2 prices
			
	}
	
	/**
	 * Demonstrate use of a tree map for storage of price records
	 * 
	 * @throws Exception Thrown by price record parser 
	 */
	public void testTreeMapVersion() throws Exception {
		
		LinkedList<PriceRecord> recordsList = new LinkedList<PriceRecord>();
		recordsList.add( new PriceRecord( "01/01/2001,100,103,99,102,1000000,104" ) );
		recordsList.add( new PriceRecord( "01/02/2001,100,103,99,102,1000000,105" ) );
		recordsList.add( new PriceRecord( "01/03/2001,100,103,99,102,1000000,106" ) );
		recordsList.add( new PriceRecord( "01/04/2001,100,103,99,102,1000000,107" ) );
		
		TreeMap<Long, PriceRecord> tm = new TreeMap<Long, PriceRecord>();
		for( PriceRecord priceRec : recordsList )
			tm.put( priceRec.getDate(), priceRec );
		
		// Compute max
		// With tree map representation, searching is more efficient and requires less code
		
		double maxPrice = Double.MIN_VALUE;
		for( PriceRecord priceRec : tm.subMap( 20010102L, true, 20010103L, true ).values() )
			if( priceRec.getAdjustedClose() > maxPrice )
				maxPrice = priceRec.getAdjustedClose();

	}
	
}
