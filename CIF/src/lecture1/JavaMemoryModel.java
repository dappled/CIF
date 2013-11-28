package lecture1;

import java.util.LinkedList;
import java.util.List;


/**
 * This class demonstrate Java's memory model. It uses various examples
 * in conjunction with some illustration to demonstrate how Java's Heap
 * works.
 * 
 * @author eran
 *
 */
public class JavaMemoryModel {

	public static void main(String[] args){
		Object a = new Object();
		// a is now pointer to a location in memory.
		System.out.println(a);
		// a is now pointing to a new object
		a = new Object();
		System.out.println(a);
		// b will point to a
		Object b = a;
		System.out.println(b);
		// a will now point to a new object
		a = new Object();
		System.out.println(a);
		System.out.println(b);
		// Java pass by value example.
		List<String> list = new LinkedList<String>();
		System.out.println("Stage 1: " + list);
		methodExample1(list);
		System.out.println("Stage 2: " + list);
		// now using the second example
		methodExample2(list);
		System.out.println("Stage 3: " + list);
		
	}
	
	/**
	 * This method will add the word computing to a list
	 * @param list The list to which we need to add the word computing.
	 */
	public static void methodExample1(List<String> list){
		list.add("computing");
	}
	
	/**
	 * This method will add the word computing to a list
	 * @param list The list to which we need to add the word computing.
	 */
	public static void methodExample2(List<String> list){
		list.add( "more stuff" );
		System.out.println( list );
		list = new LinkedList<String>();
		list.add( "even more stuff" );
		System.out.println( list );
	}
	
}
