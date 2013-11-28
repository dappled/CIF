package summerClass.lecture1;

public class ArgumentsToMethods {
	
	public static void modifyInt( int i ) {
		i = 7; // Does not change the contents of the original variable
	}
	
	public static void modifyString( String stringArg ) {
		stringArg = stringArg + "...wow!";
	}
	
	public static void modifyStringList1( String[] stringList ) {
		stringList[ 0 ] = "Lee";
	}

	public static void modifyStringList2( String[] stringList ) {
		stringList = new String[ 2 ];
		stringList[ 0 ] = "more";
		stringList[ 1 ] = "strings";
	}

	public static void main( String[] args ) {
		
		int i = 5;
		modifyInt( i ); 
		// 'i' was changed to 7 inside 'exA' but remains 5 here
		
		String s = "hello";
		modifyString( s ); 
		// 's' was changed to "hello...wow!" in 'modifyString' but remains "hello" here.
		
		String[] stringList = { "one", "two" };
		modifyStringList1( stringList );
		// The 0th element of 'stringList' was changed to "Lee" in 'modifyStringList'
		
		modifyStringList2( stringList );
		// stringList is unchanged
		
	}
	
}
