package thread.synchronization;

/**
 * This class is not thread safe
 * @author Lees
 *
 */
public class SynchronizationError {
	
	private int size;
	
	public void adjust() {
		size = size + 1;
		if( size >= 100 ) {
			size = 0;
		}
	}
	
	public void reset() {
		size = 0;
	}
	
}
