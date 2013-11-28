package lecture1;

/**
 * This class displays various control flow examples
 * 
 * @author eran
 * @version 1.1
 * 
 */
public class HelloWorldControlFlow {

	public static void main(String[] args) {
		// Print hello world to the screen
		System.out.println("Hello World");

		// if block where the condition is true
		if (true) {
			System.out.println("Hello World if block is true");
		}
		// if block where the condition is false
		if (false) {
			System.out.println("Hello World if block is false");
		}

		// if else block. if block is true
		if (true) {
			System.out.println("Hello World if-else block if true");
		} else {
			System.out.println("Hello World if-else block is false");
		}

		// if else block. if block is true
		if (false) {
			System.out.println("Hello World if-else block if is true example 2");
		} else {
			System.out.println("Hello World if-else block if is false example 2");
		}

		// if else-if block
		if ( true ){
			System.out.println("Hello World if else-if if block is true");
		} else if ( true ){
			System.out.println("Hello World if else-if if block is false");
		}
		
		// if else-if block
		if ( false ){
			System.out.println("Hello World if else-if if block is false");
		} else if ( true ){
			System.out.println("Hello World if else-if if block is false example 2");
		}
		
		// if else-if block
		if ( false ){
			System.out.println("Hello World if else-if if block is false else-if is false");
		} else if ( true ){
			System.out.println("Hello World if else-if if block is false, else-if is false");
		}
		
	}
}
