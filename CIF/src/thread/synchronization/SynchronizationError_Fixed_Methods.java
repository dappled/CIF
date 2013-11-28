package thread.synchronization;

/**
 * This class is thread safe
 * @author Lee
 *
 */
public class SynchronizationError_Fixed_Methods {
	
	private int size;
	
	public synchronized void adjust() {
		size = size + 1;
		if( size >= 100 ) {
			size = 0;
		}
	}
	
	public synchronized void reset() {
			size = 0;
	}
	
}
