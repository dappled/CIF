package lecture1;

/**
 * This program demonstrates the need to use tolerance for comparing
 * floats and demonstarte finite precision pitfalls. 
 * @author eran
 *
 */
public class HelloWorldTolerance {
	
	public static void main(String[] args){
		double a = 0.1;
		double b = 0.1;
		// testing a and b for equality.
		if ( a == b){
			System.out.println("a==b");
		}
		double c = 0.3;
		// comparing a+b to c
		if ( a+b+a == c){
			System.out.println("a+b=c");
		}else{
			System.out.println(c-a-b-a);
		}
		
	}

}
