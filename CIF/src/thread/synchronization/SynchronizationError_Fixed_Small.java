package thread.synchronization;

/**
 * This class is thread safe
 * @author Lees
 *
 */
public class SynchronizationError_Fixed_Small {
	
	private Integer size;
	
	public void adjust() {
		synchronized( size ) { // Only synchronize on objects
			size = size + 1;
			if( size >= 100 ) {
				size = 0;
			}
		}
	}
	
	public void reset() {
		synchronized( size ) {
			size = 0;
		}
	}
	
}
