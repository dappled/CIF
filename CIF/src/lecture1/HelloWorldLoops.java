package lecture1;

/**
 * This class demonstrate various usage of loops
 * @author eran
 *
 */
public class HelloWorldLoops {
	
	public static void main(String[] args){
		// printing hellow world
		System.out.println("Hello World");
		// regular for loop
		for ( int  i = 0; i < 5; ++i){
			System.out.println("Hello World for loop " + i);
		}
		// regular while loop
		int  i = 0;
		while (i < 5 ){
			System.out.println("Hello World while loop " + i);
			++i;
		}
		// regular do-while loop
		int j = 0;
		do{
			System.out.println("Hello World do-while loop " + j);
			++j;
		} while ( j < 5);
		// missing for loop
		for ( int k = 0; k < 5 ; ){
			System.out.println("Hello World for loop missing increment " + k);
			++k;
		}
		// use of continue
		for ( int k = 0; k < 5 ; ++k){
			if ( k <= 3 ){ continue;}
			System.out.println("Hello World for loop continue " + k);
			
		}
		// use of break
		for ( int k = 0; k < 5 ; ++k){
			System.out.println("Hello World for loop break " + k);
			if ( k > 3 ){ break;}
		}
		// shorthand java structure.
		String[] strings = {"a","b","c"};
		for (String string : strings){
			System.out.println("An iterator style for " + string);
		}
	}

}
