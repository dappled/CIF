package summerSolution;

import java.io.File;
import java.io.FileWriter;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.TreeMap;

import utils1309.GenericBubbleSort;
import utils1309.GenericQuicksort;
import utils1309.I_MovingAverage;
import utils1309.I_SortAlgorithm;
import utils1309.MovingAverage;
import utils1309.SortField;
import utils1309.SortOrder;
import utils1309.AlgoType;
import utils1309.TextFileReader;

public class DataHandler {
	protected boolean       _isReady; // True if records have been loaded and sorted
	protected PriceRecord[] _records;
	
	/**
	 * Constructor - sets the ready flag to false
	 */
	public DataHandler() { _isReady = false; }
	
	/**
	 * @return True if we have sorted records
	 */
	public boolean isReady() { return _isReady; }
	
	/**
	 * Reads text file and converts it to an array of PriceRecord1 objects then sorts this
	 * array according to the specifications provided.
	 * 
	 * @param filePathName 
	 * @param sortField
	 * @param sortOrder
	 * @param sortType
	 * @throws Exception
	 */
	public void loadPriceData( 
		String    filePathName, 
		SortField sortField, 
		SortOrder sortOrder, 
		AlgoType  sortType
	) throws Exception {
		
		// Read all price records from file into memory
			
			_records = getPriceRecords( filePathName ).toArray( new PriceRecord[0] );
			// Note that the 'toArray' call with a zero length array is the way we
			// convert a list to an array
		
		// Based on parameters, build the kind of comparator we want
		
			Comparator<PriceRecord> comparator;
			if( sortField == SortField.DATE )
				comparator = PriceRecord.getDateComparator( sortOrder );
			else {
				double tolerance = 0.001;
				comparator = PriceRecord.getAdjustedCloseComparator( sortOrder, tolerance );
			}
		
		// Based on parameters, create the kind of sort algorithm we want
		
			I_SortAlgorithm<PriceRecord> sortAlgorithm;
			if( sortType == AlgoType.BUBBLESORT )
				sortAlgorithm = new GenericBubbleSort<PriceRecord>();
			else
				sortAlgorithm = new GenericQuicksort<PriceRecord>();
		
		// Perform sort
		
			sortAlgorithm.sort( _records, comparator );
			
		// Tell other methods that we're ready
			
			_isReady = true;
		
	}
	
	/**
	 * Reads in price records from text file and converts them to objects
	 * 
	 * @param filePathName Name of file in which records are text representation of PriceRecord1 objects
	 * @return Linked list of PriceRecord1 objects
	 * @throws Exception If file cannot be read or the data doesn't make sense
	 */
	protected LinkedList<PriceRecord> getPriceRecords( String filePathName ) throws Exception {
		LinkedList<String> lines = TextFileReader.readLineLinkedList( filePathName );
		lines.removeFirst(); // Header line
		return PriceRecord.parseRecords( lines );
	}

	/**
	 * Retrieve prices between start and end dates (inclusive)
	 * @param startDate First date to include
	 * @param endDate Last date to include
	 * @return List of prices
	 * @throws Exception Thrown if data has not been loaded and sorted
	 */
	public Double[] getPrices( long startDate, long endDate ) throws Exception {
		if( ! isReady() )
			throw new Exception( "Data has not been loaded and sorted" );
		LinkedList<Double> results = new LinkedList<Double>();
		for( PriceRecord rec : _records )
			if( ( rec.getDate() >= startDate ) && ( rec.getDate() <= endDate ) )
				results.add( rec.getAdjustedClose() );
		return results.toArray( new Double[ 0 ] );
	}
	
	/**
	 * Compute average of prices between start and end dates (inclusive)
	 * 
	 * @param startDate Start date of prices
	 * @param endDate End date of prices
	 * @return Average of prices between the above dates (inclusive)
	 * @throws Exception Thrown if data has not been loaded or there are zero prices between the two dates
	 */
	public double computeAverage( long startDate, long endDate ) throws Exception {
		if( ! isReady() )
			throw new Exception( "Data has not been loaded and sorted" );
		Double[] prices = getPrices( startDate, endDate );
		if( prices.length == 0 )
			throw new Exception( "Can't calculate average of zero prices" );
		double sum = 0;
		for( Double price : prices )
			sum += price;
		return sum / prices.length;
	}

	/**
	 * Compute the max price between the start date and end date inclusive
	 * 
	 * @param startDate Date - in format yyyymmdd - on which to start analysis
	 * @param endDate Date on which to end analysis
	 * @return Maximum price during specified time period
	 * @throws Exception Thrown where there are no price in the specified dates or when data has not loaded into memory
	 */
	public double computeMax( long startDate, long endDate ) throws Exception {
		if( ! isReady() )
			throw new Exception( "Data has not been loaded and sorted" );
		Double[] prices = getPrices(startDate, endDate);
		if( prices.length == 0 )
			throw new Exception( "Can't compute max price of zero length array" );
		double max = Double.MIN_VALUE;
		for( double price : prices )
			if( price > max )
				max = price;
		return max;
	}
	
	/**
	 * Method to manage reading and inserting corrections records
	 * 
	 * @param correctionRecords Array of records used to update or insert into current list of price records
	 * @throws Exception 
	 */
	public void correctPrices( PriceRecord[] correctionRecords ) throws Exception {
		if( ! isReady() )
			throw new Exception( "Attempting to correct prices before prices have been loaded" );
		TreeMap<Long,PriceRecord> dateOrderRecsMap = new TreeMap<Long,PriceRecord>();
		for( PriceRecord p2 : _records )
			dateOrderRecsMap.put( p2.getDate(), p2 );
		for( PriceRecord correctionRecord : correctionRecords )
			dateOrderRecsMap.put( correctionRecord.getDate(), correctionRecord ); 
			// ...or: insert( dateOrderRecsMap, correctionRecord ) <-- This is what was specified in homework
		
		_records = dateOrderRecsMap.values().toArray( new PriceRecord[ 0 ] );
	}
	
	/**
	 * Method to manage reading and inserting corrections records
	 * 
	 * @param filePathName File containing price records in appropriate format
	 * @throws Exception Thrown if file can't be read or records can't be parsed 
	 */
	public void correctPrices( String filePathName ) throws Exception {
		LinkedList<PriceRecord> records = getPriceRecords(filePathName);
		correctPrices( records.toArray( new PriceRecord[ 0 ] ) );
	}
	
	/**
	 * Appends moving average data to file -- Summer assignment task #7
	 * 
	 * @param movingAverageSize Size of moving average in days
	 * @param startDate Day to start outputting moving average
	 * @param endDate Day to stop outputting moving average
	 * @param fileWriter Class responsible for appending to the results file 
	 * @throws Exception Thrown by the file writer or the moving average class
	 */
	public void writeMovinaAverageDataToFile( int movingAverageSize, long startDate, long endDate, FileWriter fileWriter ) throws Exception {
		if( ! isReady() )
			throw new Exception( "Data has not been loaded and sorted" );
		I_MovingAverage ma = MovingAverage.newMovingAverage( movingAverageSize );
		fileWriter.write( 
			String.format( 
				"The moving average of SPY between %s and %s is:\n", 
				PriceRecord.dateToString( startDate ),
				PriceRecord.dateToString( endDate )
			)
		);
		for( PriceRecord priceRec1 : _records ) {
			if( priceRec1.getDate() > endDate )
				break;
			ma.add( priceRec1.getAdjustedClose() );
			if( priceRec1.getDate() >= startDate ) {
				if( ma.isReady() )
					fileWriter.write( String.format( "\t%.2f\n", ma.getAverage() ) );
				else
					fileWriter.write( "\tNA\n" );
			}
		}
		
	}
	
	public static void main( String[] args ) throws Exception {
		
		String priceFilePathName = "/Users/minddrill/Dropbox/Courant/2013/CIF2013/Summer assignment/prices.csv";
		String correctionsFilePathName = "/Users/minddrill/Dropbox/Courant/2013/CIF2013/Summer assignment/corrections.csv";

		String resultsFile = "/Users/minddrill/Dropbox/Courant/2013/CIF2013/Summer assignment/results.txt";
		File file = new File( resultsFile );
		FileWriter fileWriter = new FileWriter( file );
		
		// #1 - Creating DataHandler object
		DataHandler dh = new DataHandler();
		
		// #2 - Loading prices
		dh.loadPriceData( priceFilePathName, SortField.DATE, SortOrder.ASCENDING, AlgoType.QUICKSORT );
		
		// #3 - Price corrections
		dh.correctPrices( correctionsFilePathName );
		
		// #4 - Retrieving price
		fileWriter.write( "The prices of SPY between 08/15/2004 and 08/20/2004 are:\n" );
		long startDate = 20040815;
		long endDate = 20040820;
		Double[] prices = dh.getPrices( startDate, endDate );
		for( double price : prices )
			fileWriter.write( String.format( "\t%.2f\n", price ) );
		
		// #5 - Average price
		startDate = 20040815;
		endDate = 20040915;
		double averagePrice = dh.computeAverage( startDate, endDate );
		fileWriter.write( String.format( "The average price of SPY between 08/15/2004 and 09/15/2004 was: %.2f\n", averagePrice ) );
		
		// #6 - Max price
		startDate = 20040415;
		endDate = 20040615;
		double maxPrice = dh.computeMax( startDate, endDate );
		fileWriter.write( String.format( "The maximum price of SPY between 04/15/2004 and 06/15/2004 was: %.2f\n", maxPrice ) );
		
		// #7 - Moving average
		int movingAverageSize = 10;
		startDate = 20040815;
		endDate = 20040915;
		dh.writeMovinaAverageDataToFile( movingAverageSize, startDate, endDate, fileWriter );

		// Flush and close results file
		fileWriter.flush();
		fileWriter.close();

	}
	
}
