package lecture1;

/**
 * This class demonstrate the possible traps in Java.
 * @author eran
 *
 */
public class Traps {
	
	public static void main(String[] args){
		// == test for equality
		System.out.println(new Double(6) == new Double(6) );
		System.out.println((new Double(6)).equals(new Double(6) ));
	}

}
