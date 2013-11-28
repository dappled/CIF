package utils1309;

public class AlgoType {
	
	public static final AlgoType QUICKSORT = new AlgoType( "Quicksort" );
	public static final AlgoType BUBBLESORT = new AlgoType( "BubbleSort" );
	
	private String _sortType;
	
	private AlgoType( String sortType ) { _sortType = sortType; }
	
	public String toString() { return ( String.format( "SortType(%s)", _sortType ) ); }
	
}
