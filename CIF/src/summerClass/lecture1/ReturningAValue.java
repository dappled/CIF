package summerClass.lecture1;

public class ReturningAValue {
	
	public static String someMethod( String stringArgument ) {
		String newString = "(" + stringArgument + ")";
		return newString;
	}
	
	public static void modifyTwoStrings( String[] arrayOfStrings ) {
		for( int i = 0; i < arrayOfStrings.length; i++ ) {
			arrayOfStrings[ i ] = "random string " + i;
		}
		
	}

	public static void main( String[] args ) {
		
		String s = "Hello";
		s = someMethod( s );
		
		String [] twoStrings = { "hello", "goodbye" };
		modifyTwoStrings( twoStrings );
		
		System.out.println( "Finished" );
		
	}
	
}
