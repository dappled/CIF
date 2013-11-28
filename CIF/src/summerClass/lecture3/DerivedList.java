package summerClass.lecture3;

import java.util.LinkedList;

/**
 * This is what we would do if we wanted to extend Java's collection class,
 * LinkedList to implement a few methods that it does not have and are 
 * necessary for our analysis.
 * 
 * @author Lee
 *
 * @param <ValType>
 */
public class DerivedList<ValType> extends LinkedList<ValType> implements QInterface<ValType> {

	private static final long serialVersionUID = 1L;
	
	public boolean isReady() {
		return this.size() == 10000; 
	}
	
	public boolean hasElementsToRemove() {
		return this.size() > 10000;
	}

}
