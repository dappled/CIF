package summerClass.lecture1;

public class Conditionals {

	public static void main( String[] args ) {
		
		int i = 5;
		int j = 6;
		int k = i + j;
		
		if( k == 11 ) {
			System.out.println( "k = 11" );
		}
		
		if( k == 12 ) {
			System.out.println( "k = 12" );
		} else {
			System.out.println( "k = 11" );
		}

		if( k == 12 ) {
		} else if( ( i == 5 ) && ( j == 6 ) ) {
			System.out.println( "i = 5 and j = 6" );
		}
		
		if( ( i == 7 ) || ( j == 6 ) ) {
			System.out.println( "Either i = 7 or j = 6" );
		}
		
		System.out.println( "Finished" );
		
	}
}
