package summerClass.lecture1;

public class HelloWorld {
	public static void main( String[] args ) {
		System.out.println( "Hello! Here is what you told me to say:" );
		if( args.length == 0 ) {
			System.out.println( "Nothing." );
		} else { 
			for( int i = 0; i < args.length; i++ ) {
				System.out.println( args[ i ]);
			}
		}
	}
}